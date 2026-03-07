package de.tzblockbuster.cauldronrevamped.mixin;

import de.tzblockbuster.cauldronrevamped.registry.CRItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.UseItemGoal;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(WanderingTrader.class)
public abstract class WanderingTraderMixin extends AbstractVillager {


    public WanderingTraderMixin(EntityType<? extends AbstractVillager> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyArg(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/UseItemGoal;<init>(Lnet/minecraft/world/entity/Mob;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/sounds/SoundEvent;Ljava/util/function/Predicate;)V", ordinal = 1), index = 3)
    private static Predicate<? super WanderingTrader> cauldronrevamped$wanderingTrader$useItemGoal$useMilkPredicate(Predicate<? super WanderingTrader> predicate) {
        return wanderingTrader -> ((wanderingTrader.level().isClientSide() || ((ServerLevel)wanderingTrader.level()).getDayCount() % 2 == 0)) && predicate.test(wanderingTrader);
    }

    @Inject(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V", ordinal = 2, shift = At.Shift.AFTER))
    private void cauldronrevamped$wanderingTrader$registerGoals$inject(CallbackInfo ci) {
        this.goalSelector.addGoal(0, new UseItemGoal<>(
                this,
                new ItemStack(CRItems.MILK_BOTTLE),
                SoundEvents.WANDERING_TRADER_REAPPEARED,
                wanderingTrader -> ((wanderingTrader.level().isClientSide() || ((ServerLevel)wanderingTrader.level()).getDayCount() % 2 == 1)) && this.level().isBrightOutside() && wanderingTrader.isInvisible()
        ));
    }
}
