package de.tzblockbuster.cauldronrevamped.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import de.tzblockbuster.cauldronrevamped.blocks.brewing_cauldron.BrewingCauldron;
import de.tzblockbuster.cauldronrevamped.blocks.brewing_cauldron.BrewingCauldronBlockEntity;
import de.tzblockbuster.cauldronrevamped.cauldron.CauldronRevampedInteraction;
import de.tzblockbuster.cauldronrevamped.registry.CRBlocks;
import de.tzblockbuster.cauldronrevamped.registry.CRItems;
import de.tzblockbuster.cauldronrevamped.registry.CRPotion;
import dev.architectury.injectables.annotations.PlatformOnly;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(CauldronInteraction.class)
public interface CauldronInteractionMixin {

    @Shadow
    static CauldronInteraction.InteractionMap newInteractionMap(String string) {
        return null;
    }

    /// This method registers the interaction maps for the slime and honey cauldrons, which are used to store the interactions for those cauldrons. We have to do this in a mixin because the original class initializes these maps in a static initializer, which we cannot modify directly.
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void init(CallbackInfo ci) {
        CauldronRevampedInteraction.SLIME = newInteractionMap("SLIME");
        CauldronRevampedInteraction.HONEY = newInteractionMap("HONEY");
        CauldronRevampedInteraction.MILK = newInteractionMap("MILK");
        CauldronRevampedInteraction.BREWING = newInteractionMap("BREWING");
    }

    /// This overrides the lava filling behavior to set the level of the Lava Cauldron to 3 when filled with lava. As the lava cauldron has no level in vanilla we need to add this behavior
    @ModifyArg(method = "fillLavaInteraction", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/cauldron/CauldronInteraction;emptyBucket(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/sounds/SoundEvent;)Lnet/minecraft/world/InteractionResult;"), index = 5)
    private static BlockState cauldronrevamped$emptyBucket(BlockState arg6) {
        return Blocks.LAVA_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3);
    }

    @Inject(method = "bootStrap", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0, shift = At.Shift.AFTER))
    private static void cauldronrevamped$bootStrapEmptyCauldronPotionBehavior(CallbackInfo ci, @Local(name = "map") Map<Item, CauldronInteraction> map) {
        map.remove(Items.POTION);
        map.put(Items.POTION, (blockState, level, blockPos, player, interactionHand, itemStack) -> {
            PotionContents potionContents = itemStack.get(DataComponents.POTION_CONTENTS);
            if (potionContents != null && potionContents.potion().isPresent() && potionContents.potion().get() != CRPotion.MIXED_POTION) {
                level.setBlockAndUpdate(blockPos, CRBlocks.BREWING_CAULDRON.defaultBlockState());

                BrewingCauldronBlockEntity brewingCauldronBlockEntity = (BrewingCauldronBlockEntity) level.getBlockEntity(blockPos);
                if (brewingCauldronBlockEntity == null) return InteractionResult.TRY_WITH_EMPTY_HAND;
                if (!brewingCauldronBlockEntity.addPotion(itemStack)) return InteractionResult.TRY_WITH_EMPTY_HAND;

                if (!level.isClientSide()) {
                    Item item = itemStack.getItem();
                    player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    player.awardStat(Stats.USE_CAULDRON);
                    player.awardStat(Stats.ITEM_USED.get(item));
                    level.setBlockAndUpdate(blockPos, CRBlocks.BREWING_CAULDRON.defaultBlockState().setValue(BrewingCauldron.HEATED, level.getBlockState(blockPos.below()).is(BlockTags.CAMPFIRES)));
                    level.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.FLUID_PLACE, blockPos);
                }

                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            }
        });
    }

    @PlatformOnly({PlatformOnly.FABRIC})
    @Inject(method = "addDefaultInteractions", at = @At("TAIL"))
    private static void cauldronrevamped$addDefaultInteractions(Map<Item, CauldronInteraction> map, CallbackInfo ci) {
        map.put(CRItems.SLIME_BUCKET, CauldronRevampedInteraction::fillSlimeInteraction);
        map.put(CRItems.HONEY_BUCKET, CauldronRevampedInteraction::fillHoneyInteraction);
        map.put(Items.MILK_BUCKET, CauldronRevampedInteraction::fillMilkInteraction);
    }

}
