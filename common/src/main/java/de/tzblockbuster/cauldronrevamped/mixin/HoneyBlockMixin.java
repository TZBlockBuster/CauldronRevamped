package de.tzblockbuster.cauldronrevamped.mixin;

import de.tzblockbuster.cauldronrevamped.registry.CRItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Optional;

@Mixin(HoneyBlock.class)
public class HoneyBlockMixin implements BucketPickup {

    @Override
    public @NonNull ItemStack pickupBlock(@Nullable LivingEntity livingEntity, @NonNull LevelAccessor levelAccessor, @NonNull BlockPos blockPos, @NonNull BlockState blockState) {
        levelAccessor.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
        if (!levelAccessor.isClientSide()) {
            levelAccessor.levelEvent(2001, blockPos, Block.getId(blockState));
        }

        return new ItemStack(CRItems.HONEY_BUCKET);
    }

    @Override
    public @NonNull Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL_POWDER_SNOW);
    }
}
