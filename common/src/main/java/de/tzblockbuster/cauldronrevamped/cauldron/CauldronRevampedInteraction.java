package de.tzblockbuster.cauldronrevamped.cauldron;


import de.tzblockbuster.cauldronrevamped.CauldronRevamped;
import de.tzblockbuster.cauldronrevamped.blocks.brewing_cauldron.BrewingCauldron;
import de.tzblockbuster.cauldronrevamped.blocks.brewing_cauldron.BrewingCauldronBlockEntity;
import de.tzblockbuster.cauldronrevamped.mixin.CauldronInteractionMixin;
import de.tzblockbuster.cauldronrevamped.registry.CRBlocks;
import de.tzblockbuster.cauldronrevamped.registry.CRItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;

import java.util.Map;

public class CauldronRevampedInteraction {
    public static CauldronInteraction.InteractionMap SLIME;
    public static CauldronInteraction.InteractionMap HONEY;
    public static CauldronInteraction.InteractionMap BREWING;


    public static void register() {
        Map<Item, CauldronInteraction> empty = CauldronInteraction.EMPTY.map();
        empty.put(CRItems.LAVA_BOTTLE, (blockState, level, blockPos, player, interactionHand, itemStack) -> {
            if (!level.isClientSide()) {
                Item item = itemStack.getItem();
                player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                level.setBlockAndUpdate(blockPos, Blocks.LAVA_CAULDRON.defaultBlockState());
                level.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PLACE, blockPos);
            }

            return InteractionResult.SUCCESS;
        });
        empty.put(CRItems.SLIME_BOTTLE, (blockState, level, blockPos, player, interactionHand, itemStack) -> {
            if (!level.isClientSide()) {
                Item item = itemStack.getItem();
                player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                level.setBlockAndUpdate(blockPos, CRBlocks.SLIME_CAULDRON.defaultBlockState());
                level.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PLACE, blockPos);
            }

            return InteractionResult.SUCCESS;
        });
        empty.put(Items.HONEY_BOTTLE, (blockState, level, blockPos, player, interactionHand, itemStack) -> {
            if (!level.isClientSide()) {
                Item item = itemStack.getItem();
                player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                level.setBlockAndUpdate(blockPos, CRBlocks.HONEY_CAULDRON.defaultBlockState());
                level.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PLACE, blockPos);
            }

            return InteractionResult.SUCCESS;
        });
        empty.put(Items.SLIME_BALL, (blockState, level, blockPos, player, interactionHand, itemStack) -> {
            if (!level.isClientSide()) {
                Item item = itemStack.getItem();
                player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.AIR)));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                level.setBlockAndUpdate(blockPos, CRBlocks.SLIME_CAULDRON.defaultBlockState());
                level.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PLACE, blockPos);
            }

            return InteractionResult.CONSUME;
        });
        addDefaultInteractions(empty);

        Map<Item, CauldronInteraction> lava = CauldronInteraction.LAVA.map();
        lava.put(Items.GLASS_BOTTLE, (blockState, level, blockPos, player, interactionHand, itemStack) -> {
            if (!level.isClientSide()) {
                Item item = itemStack.getItem();
                player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(CRItems.LAVA_BOTTLE)));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                LayeredCauldronBlock.lowerFillLevel(blockState, level, blockPos);
                level.playSound(null, blockPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
            }

            return InteractionResult.SUCCESS;
        });
        lava.put(CRItems.LAVA_BOTTLE, (blockState, level, blockPos, player, interactionHand, itemStack) -> {
            if (blockState.getValue(LayeredCauldronBlock.LEVEL) == 3) {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            } else {
                if (!level.isClientSide()) {
                    player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    player.awardStat(Stats.USE_CAULDRON);
                    player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                    level.setBlockAndUpdate(blockPos, blockState.cycle(LayeredCauldronBlock.LEVEL));
                    level.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.FLUID_PLACE, blockPos);
                }

                return InteractionResult.SUCCESS;
            }
        });
        addDefaultInteractions(lava);

        Map<Item, CauldronInteraction> slime = CauldronRevampedInteraction.SLIME.map();
        slime.put(
                Items.BUCKET,
                (blockState, level, blockPos, player, interactionHand, itemStack) -> CauldronInteraction.fillBucket(
                        blockState,
                        level,
                        blockPos,
                        player,
                        interactionHand,
                        itemStack,
                        new ItemStack(CRItems.SLIME_BUCKET),
                        blockStatex -> blockStatex.getValue(LayeredCauldronBlock.LEVEL) == 3,
                        SoundEvents.BUCKET_FILL_POWDER_SNOW
                )
        );
        slime.put(Items.GLASS_BOTTLE, (blockState, level, blockPos, player, interactionHand, itemStack) -> {
            if (!level.isClientSide()) {
                Item item = itemStack.getItem();
                player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(CRItems.SLIME_BOTTLE)));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                LayeredCauldronBlock.lowerFillLevel(blockState, level, blockPos);
                level.playSound(null, blockPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
            }

            return InteractionResult.SUCCESS;
        });
        slime.put(CRItems.SLIME_BOTTLE, (blockState, level, blockPos, player, interactionHand, itemStack) -> {
            if (blockState.getValue(LayeredCauldronBlock.LEVEL) == 3) {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            } else {
                if (!level.isClientSide()) {
                    player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    player.awardStat(Stats.USE_CAULDRON);
                    player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                    level.setBlockAndUpdate(blockPos, blockState.cycle(LayeredCauldronBlock.LEVEL));
                    level.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.FLUID_PLACE, blockPos);
                }

                return InteractionResult.SUCCESS;
            }
        });
        slime.put(Items.SLIME_BALL, (blockState, level, blockPos, player, interactionHand, itemStack) -> {
            if (blockState.getValue(LayeredCauldronBlock.LEVEL) == 3) {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            } else {
                if (!level.isClientSide()) {
                    player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.AIR)));
                    player.awardStat(Stats.USE_CAULDRON);
                    player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                    level.setBlockAndUpdate(blockPos, blockState.cycle(LayeredCauldronBlock.LEVEL));
                    level.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.FLUID_PLACE, blockPos);
                }

                return InteractionResult.CONSUME;
            }
        });
        addDefaultInteractions(slime);

        Map<Item, CauldronInteraction> honey = CauldronRevampedInteraction.HONEY.map();
        honey.put(
                Items.BUCKET,
                (blockState, level, blockPos, player, interactionHand, itemStack) -> CauldronInteraction.fillBucket(
                        blockState,
                        level,
                        blockPos,
                        player,
                        interactionHand,
                        itemStack,
                        new ItemStack(CRItems.HONEY_BUCKET),
                        blockStatex -> blockStatex.getValue(LayeredCauldronBlock.LEVEL) == 3,
                        SoundEvents.BUCKET_FILL_POWDER_SNOW
                )
        );
        honey.put(Items.GLASS_BOTTLE, (blockState, level, blockPos, player, interactionHand, itemStack) -> {
            if (!level.isClientSide()) {
                Item item = itemStack.getItem();
                player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.HONEY_BOTTLE)));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                LayeredCauldronBlock.lowerFillLevel(blockState, level, blockPos);
                level.playSound(null, blockPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
            }

            return InteractionResult.SUCCESS;
        });
        honey.put(Items.HONEY_BOTTLE, (blockState, level, blockPos, player, interactionHand, itemStack) -> {
            if (blockState.getValue(LayeredCauldronBlock.LEVEL) == 3) {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            } else {
                if (!level.isClientSide()) {
                    player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    player.awardStat(Stats.USE_CAULDRON);
                    player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                    level.setBlockAndUpdate(blockPos, blockState.cycle(LayeredCauldronBlock.LEVEL));
                    level.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.FLUID_PLACE, blockPos);
                }

                return InteractionResult.SUCCESS;
            }
        });
        addDefaultInteractions(honey);

        Map<Item, CauldronInteraction> brewing = CauldronRevampedInteraction.BREWING.map();
        brewing.put(Items.GLASS_BOTTLE, (blockState, level, blockPos, player, interactionHand, itemStack) -> {
            BrewingCauldronBlockEntity brewingCauldronBlockEntity = (BrewingCauldronBlockEntity) level.getBlockEntity(blockPos);
            if (brewingCauldronBlockEntity == null) return InteractionResult.TRY_WITH_EMPTY_HAND;
            ItemStack potionStack = brewingCauldronBlockEntity.takePotion();
            if (potionStack == null) return InteractionResult.TRY_WITH_EMPTY_HAND;

            if (!level.isClientSide()) {
                Item item = itemStack.getItem();
                player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, potionStack));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                LayeredCauldronBlock.lowerFillLevel(blockState, level, blockPos);
                level.playSound(null, blockPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.gameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
            }

            return InteractionResult.SUCCESS;
        });
        brewing.put(Items.POTION, (blockState, level, blockPos, player, interactionHand, itemStack) -> {
            if (blockState.getValue(LayeredCauldronBlock.LEVEL) == 3) {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            } else {
                BrewingCauldronBlockEntity brewingCauldronBlockEntity = (BrewingCauldronBlockEntity) level.getBlockEntity(blockPos);

                if (brewingCauldronBlockEntity == null) return InteractionResult.TRY_WITH_EMPTY_HAND;
                if (!brewingCauldronBlockEntity.addPotion(itemStack)) return InteractionResult.TRY_WITH_EMPTY_HAND;


                if (!level.isClientSide()) {
                    player.setItemInHand(interactionHand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    player.awardStat(Stats.USE_CAULDRON);
                    player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
                    level.setBlockAndUpdate(blockPos, blockState.cycle(LayeredCauldronBlock.LEVEL).setValue(BrewingCauldron.HEATED, level.getBlockState(blockPos.below()).is(BlockTags.CAMPFIRES)));
                    level.playSound(null, blockPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.gameEvent(null, GameEvent.FLUID_PLACE, blockPos);
                }

                return InteractionResult.SUCCESS;
            }
        });
        addDefaultInteractions(brewing);
    }

    public static void addDefaultInteractions(Map<Item, CauldronInteraction> map) {
        CauldronInteraction.addDefaultInteractions(map);
        map.put(CRItems.SLIME_BUCKET, CauldronRevampedInteraction::fillSlimeInteraction);
        map.put(CRItems.HONEY_BUCKET, CauldronRevampedInteraction::fillHoneyInteraction);
    }

    public static InteractionResult fillSlimeInteraction(
            BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack
    ) {
        return isUnderWater(level, blockPos)
                ? InteractionResult.CONSUME
                : CauldronInteraction.emptyBucket(
                level,
                blockPos,
                player,
                interactionHand,
                itemStack,
                CRBlocks.SLIME_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3),
                SoundEvents.BUCKET_EMPTY_POWDER_SNOW
        );
    }

    public static InteractionResult fillHoneyInteraction(
            BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, ItemStack itemStack
    ) {
        return isUnderWater(level, blockPos)
                ? InteractionResult.CONSUME
                : CauldronInteraction.emptyBucket(
                level,
                blockPos,
                player,
                interactionHand,
                itemStack,
                CRBlocks.HONEY_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3),
                SoundEvents.BUCKET_EMPTY_POWDER_SNOW
        );
    }

    private static boolean isUnderWater(Level level, BlockPos blockPos) {
        FluidState fluidState = level.getFluidState(blockPos.above());
        return fluidState.is(FluidTags.WATER);
    }

}
