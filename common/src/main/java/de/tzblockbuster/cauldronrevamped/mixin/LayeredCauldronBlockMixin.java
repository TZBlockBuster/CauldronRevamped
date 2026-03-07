package de.tzblockbuster.cauldronrevamped.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import de.tzblockbuster.cauldronrevamped.registry.CRBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.InsideBlockEffectType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LayeredCauldronBlock.class)
public abstract class LayeredCauldronBlockMixin extends AbstractCauldronBlock {


    @Shadow
    public static void lowerFillLevel(BlockState arg, Level arg2, BlockPos arg3) {
    }

    public LayeredCauldronBlockMixin(Properties properties, CauldronInteraction.InteractionMap interactionMap) {
        super(properties, interactionMap);
    }

    @WrapMethod(method = "canReceiveStalactiteDrip")
    public boolean cauldronrevamped$canReceiveStalactiteDrip(Fluid fluid, Operation<Boolean> original) {
        if (interactions.equals(CauldronInteraction.LAVA)) {
            return fluid == Fluids.LAVA;
        } else {
            return original.call(fluid);
        }
    }

    @WrapMethod(method = "entityInside")
    public void cauldronrevamped$entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity, InsideBlockEffectApplier insideBlockEffectApplier, boolean bl, Operation<Void> original) {
        if (blockState.is(Blocks.LAVA_CAULDRON)) {
            insideBlockEffectApplier.apply(InsideBlockEffectType.CLEAR_FREEZE);
            insideBlockEffectApplier.apply(InsideBlockEffectType.LAVA_IGNITE);
            insideBlockEffectApplier.runAfter(InsideBlockEffectType.LAVA_IGNITE, Entity::lavaHurt);
        } else if (blockState.is(CRBlocks.MILK_CAULDRON)) {
            if (entity instanceof LivingEntity livingEntity) {
                if (!livingEntity.getActiveEffects().isEmpty()) {
                    livingEntity.removeAllEffects();
                    lowerFillLevel(blockState, level, blockPos);
                }
            }
        } else {
            if (blockState.is(Blocks.POWDER_SNOW_CAULDRON)) {
                insideBlockEffectApplier.apply(InsideBlockEffectType.FREEZE);
            }
            original.call(blockState, level, blockPos, entity, insideBlockEffectApplier, bl);
        }
    }


}
