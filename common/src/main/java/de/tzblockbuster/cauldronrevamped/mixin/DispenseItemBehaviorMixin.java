package de.tzblockbuster.cauldronrevamped.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import de.tzblockbuster.cauldronrevamped.registry.CRItems;
import dev.architectury.injectables.annotations.PlatformOnly;
import dev.architectury.platform.Platform;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.level.block.DispenserBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DispenseItemBehavior.class)
public interface DispenseItemBehaviorMixin {


    @PlatformOnly({PlatformOnly.FABRIC})
    @Inject(method = "bootStrap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/DispenserBlock;registerBehavior(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/core/dispenser/DispenseItemBehavior;)V", ordinal = 25))
    private static void cauldronrevamped$bootStrap$fabric(CallbackInfo ci, @Local(name = "dispenseItemBehavior") DispenseItemBehavior dispenseItemBehavior) {
        cauldronrevamped$bootStrap(ci, dispenseItemBehavior);
    }


    @PlatformOnly({PlatformOnly.FORGE})
    @Inject(method = "bootStrap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/DispenserBlock;registerBehavior(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/core/dispenser/DispenseItemBehavior;)V", ordinal = 25))
    private static void cauldronrevamped$bootStrap$forge(CallbackInfo ci, @Local(name = "dispenseitembehavior") DispenseItemBehavior dispenseItemBehavior) {
        cauldronrevamped$bootStrap(ci, dispenseItemBehavior);
    }

    @Unique
    private static void cauldronrevamped$bootStrap(CallbackInfo ci, @Local(name = "dispenseItemBehavior") DispenseItemBehavior dispenseItemBehavior) {
        DispenserBlock.registerBehavior(CRItems.HONEY_BUCKET, dispenseItemBehavior);
        DispenserBlock.registerBehavior(CRItems.SLIME_BUCKET, dispenseItemBehavior);
    }

}
