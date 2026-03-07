package de.tzblockbuster.cauldronrevamped.blocks.brewing_cauldron;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class BrewingCauldron extends AbstractCauldronBlock implements EntityBlock {

    public static final MapCodec<BrewingCauldron> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            CauldronInteraction.CODEC.fieldOf("interactions").forGetter(brewingCauldron -> brewingCauldron.interactions),
                            propertiesCodec()
                    )
                    .apply(instance, BrewingCauldron::new)
    );

    private static final VoxelShape[] FILLED_SHAPES = Util.make(
            () -> Block.boxes(2, i -> Shapes.or(AbstractCauldronBlock.SHAPE, Block.column(12.0, 4.0, getPixelContentHeight(i + 1))))
    );

    public static final BooleanProperty HEATED = BooleanProperty.create("heated");
    public static final BooleanProperty BREWING = BooleanProperty.create("brewing");

    public BrewingCauldron(CauldronInteraction.InteractionMap interactionMap, BlockBehaviour.Properties properties) {
        super(properties, interactionMap);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HEATED, false)
                .setValue(LayeredCauldronBlock.LEVEL, 1)
                .setValue(BREWING, false)
        );
    }

    @Override
    protected double getContentHeight(BlockState blockState) {
        return getPixelContentHeight(blockState.getValue(LayeredCauldronBlock.LEVEL)) / 16.0;
    }

    private static double getPixelContentHeight(int i) {
        return 6.0 + i * 3.0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HEATED);
        builder.add(LayeredCauldronBlock.LEVEL);
        builder.add(BREWING);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NonNull BlockPos blockPos, @NonNull BlockState blockState) {
        return new BrewingCauldronBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NonNull BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState()
                .setValue(HEATED, blockPlaceContext.getLevel().getBlockState(blockPlaceContext.getClickedPos().below()).is(BlockTags.CAMPFIRES));
    }

    @Override
    protected @NonNull BlockState updateShape(@NonNull BlockState blockState, @NonNull LevelReader levelReader, @NonNull ScheduledTickAccess scheduledTickAccess, @NonNull BlockPos blockPos, @NonNull Direction direction, @NonNull BlockPos blockPos2, @NonNull BlockState blockState2, @NonNull RandomSource randomSource) {
        return direction == Direction.DOWN ? blockState.setValue(HEATED, blockState2.is(BlockTags.CAMPFIRES)) :
                super.updateShape(blockState, levelReader, scheduledTickAccess, blockPos, direction, blockPos2, blockState2, randomSource);
    }

    @Override
    protected @NonNull MapCodec<? extends AbstractCauldronBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean isFull(BlockState blockState) {
        return blockState.getValue(LayeredCauldronBlock.LEVEL) == 3;
    }

    @Override
    protected @NonNull VoxelShape getEntityInsideCollisionShape(BlockState blockState, @NonNull BlockGetter blockGetter, @NonNull BlockPos blockPos, @NonNull Entity entity) {
        return FILLED_SHAPES[blockState.getValue(LayeredCauldronBlock.LEVEL) - 1];
    }

    @Override
    protected void entityInside(@NonNull BlockState blockState, @NonNull Level level, @NonNull BlockPos blockPos, @NonNull Entity entity, @NonNull InsideBlockEffectApplier insideBlockEffectApplier, boolean bl) {
        BrewingCauldronBlockEntity blockEntity = (BrewingCauldronBlockEntity) level.getBlockEntity(blockPos);
        if (blockEntity == null) return;
        if (entity instanceof Player player) {
            if (!blockEntity.potionFractions.stream().flatMap(f -> f.potion().value().getEffects().stream()).allMatch(effect -> player.hasEffect(effect.getEffect()))) {
                blockEntity.getEffectsForNextPotion().stream().flatMap(f -> f.potion().value().getEffects().stream()).forEach(effect -> player.addEffect(new MobEffectInstance(effect.getEffect(), effect.getDuration(), effect.getAmplifier())));
                lowerFillLevel(blockState, level, blockPos);
            }
        } else if (entity instanceof ItemEntity itemEntity) {
            if (!level.isClientSide()) {
                ItemStack itemStack = itemEntity.getItem();
                ItemStack clone = itemStack.copyWithCount(1);
                if (blockEntity.isValidIngredient(clone) && blockEntity.setIngredient(clone)) {
                    if (itemStack.getCount() == 1) {
                        itemEntity.discard();
                    } else {
                        itemEntity.setItem(itemStack.copyWithCount(itemStack.getCount() - 1));
                    }
                }
            }
        }
    }

    public static void lowerFillLevel(BlockState blockState, Level level, BlockPos blockPos) {
        int i = blockState.getValue(LayeredCauldronBlock.LEVEL) - 1;
        BlockState blockState2 = i == 0 ? Blocks.CAULDRON.defaultBlockState() : blockState.setValue(LayeredCauldronBlock.LEVEL, i);
        level.setBlockAndUpdate(blockPos, blockState2);
        level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(blockState2));
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NonNull Level level, @NonNull BlockState blockState, @NonNull BlockEntityType<T> blockEntityType) {
        return (level1, blockPos1, blockState1, blockEntity1) -> {
            if (blockEntity1 instanceof BrewingCauldronBlockEntity brewingCauldronBlockEntity) {
                brewingCauldronBlockEntity.tick();
            }
        };
    }
}
