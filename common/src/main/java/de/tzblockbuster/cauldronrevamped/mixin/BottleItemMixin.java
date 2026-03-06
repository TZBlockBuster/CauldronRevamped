package de.tzblockbuster.cauldronrevamped.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import de.tzblockbuster.cauldronrevamped.registry.CRItems;
import dev.architectury.injectables.annotations.PlatformOnly;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BottleItem.class)
public abstract class BottleItemMixin {

    @Shadow
    protected abstract ItemStack turnBottleIntoItem(ItemStack arg, Player arg2, ItemStack arg3);

    @PlatformOnly({ PlatformOnly.FABRIC})
    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/BlockHitResult;getBlockPos()Lnet/minecraft/core/BlockPos;", shift = At.Shift.AFTER), cancellable = true)
    private void cauldronrevamped$useBottleItemOnLava$fabric(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir, @Local(name = "blockHitResult") BlockHitResult blockHitResult) {
        cauldronrevamped$useBottleItemOnLava(level, player, interactionHand, cir, blockHitResult);
    }

    @PlatformOnly({ PlatformOnly.FORGE})
    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/BlockHitResult;getBlockPos()Lnet/minecraft/core/BlockPos;", shift = At.Shift.AFTER), cancellable = true)
    private void cauldronrevamped$useBottleItemOnLava$forge(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir, @Local(name = "blockhitresult") BlockHitResult blockhitresult) {
        cauldronrevamped$useBottleItemOnLava(level, player, interactionHand, cir, blockhitresult);
    }

    @Unique
    private void cauldronrevamped$useBottleItemOnLava(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir, BlockHitResult blockHitResult) {
        BlockPos blockPos = blockHitResult.getBlockPos();
        if (!level.mayInteract(player, blockPos)) {
            cir.setReturnValue(InteractionResult.PASS);
            cir.cancel();
            return;
        }
        if (level.getFluidState(blockPos).is(FluidTags.LAVA)) {
            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
            level.gameEvent(player, GameEvent.FLUID_PICKUP, blockPos);
            cir.setReturnValue(InteractionResult.SUCCESS.heldItemTransformedTo(this.turnBottleIntoItem(player.getItemInHand(interactionHand), player, new ItemStack(CRItems.LAVA_BOTTLE))));
            cir.cancel();
        }
    }

}
