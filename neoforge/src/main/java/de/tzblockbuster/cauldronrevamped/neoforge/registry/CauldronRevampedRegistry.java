package de.tzblockbuster.cauldronrevamped.neoforge.registry;

import de.tzblockbuster.cauldronrevamped.CauldronRevamped;
import de.tzblockbuster.cauldronrevamped.registry.CRBlockEntities;
import de.tzblockbuster.cauldronrevamped.registry.CRBlocks;
import de.tzblockbuster.cauldronrevamped.registry.CRItems;
import de.tzblockbuster.cauldronrevamped.registry.CRPotion;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import oshi.util.tuples.Triplet;

import java.util.HashSet;
import java.util.function.Consumer;

public class CauldronRevampedRegistry {

    @SubscribeEvent
    public void registerItems(RegisterEvent event) {
        event.register(BuiltInRegistries.BLOCK.key(), registry -> {
            for (Tuple<String, Block> block : CRBlocks.blocks) {
                Identifier id = Identifier.fromNamespaceAndPath(CauldronRevamped.MOD_ID, block.getA());
                registry.register(id, block.getB());
            }
        });
        event.register(BuiltInRegistries.ITEM.key(), registry -> {
            for (Tuple<String, Item> item : CRBlocks.items) {
                Identifier id = Identifier.fromNamespaceAndPath(CauldronRevamped.MOD_ID, item.getA());
                registry.register(id, item.getB());
            }
        });
        event.register(BuiltInRegistries.ITEM.key(), registry -> {
            for (Tuple<String, Item> item : CRItems.items) {
                Identifier id = Identifier.fromNamespaceAndPath(CauldronRevamped.MOD_ID, item.getA());
                registry.register(id, item.getB());
            }
        });
        event.register(BuiltInRegistries.BLOCK_ENTITY_TYPE.key(), registry -> {
            for (CRBlockEntities.BlockEntityRegistration<?> blockEntityType : CRBlockEntities.blockEntitySuppliers) {
                Identifier id = Identifier.fromNamespaceAndPath(CauldronRevamped.MOD_ID, blockEntityType.name());
                registry.register(id, new BlockEntityType<>((blockPos, blockState) -> blockEntityType.blockEntitySupplier().create(blockPos, blockState), new HashSet<>(blockEntityType.blocks())));
            }
        });
        event.register(BuiltInRegistries.POTION.key(), registry -> {
            for (Triplet<String, Potion, Consumer<Holder<Potion>>> potion : CRPotion.potions) {
                Identifier id = Identifier.fromNamespaceAndPath(CauldronRevamped.MOD_ID, potion.getA());
                registry.register(id, potion.getB());
                potion.getC().accept(BuiltInRegistries.POTION.wrapAsHolder(potion.getB()));
            }
        });
    }
}
