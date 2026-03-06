package de.tzblockbuster.cauldronrevamped.fabric.registry;

import de.tzblockbuster.cauldronrevamped.CauldronRevamped;
import de.tzblockbuster.cauldronrevamped.registry.CRBlocks;
import de.tzblockbuster.cauldronrevamped.registry.CRItems;
import de.tzblockbuster.cauldronrevamped.registry.CRPotion;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.Block;
import oshi.util.tuples.Triplet;

import java.util.function.Consumer;

public class CauldronRevampedRegistry {

    public static void register() {
        for (Tuple<String, Item> item : CRItems.items) {
            Identifier id = Identifier.fromNamespaceAndPath(CauldronRevamped.MOD_ID, item.getA());
            Registry.register(BuiltInRegistries.ITEM, id, item.getB());
        }
        for (Tuple<String, Item> item : CRBlocks.items) {
            Identifier id = Identifier.fromNamespaceAndPath(CauldronRevamped.MOD_ID, item.getA());
            Registry.register(BuiltInRegistries.ITEM, id, item.getB());
        }
        for (Tuple<String, Block> block : CRBlocks.blocks) {
            Identifier id = Identifier.fromNamespaceAndPath(CauldronRevamped.MOD_ID, block.getA());
            Registry.register(BuiltInRegistries.BLOCK, id, block.getB());
        }
        for (Triplet<String, Potion, Consumer<Holder<Potion>>> potion : CRPotion.potions) {
            Identifier id = Identifier.fromNamespaceAndPath(CauldronRevamped.MOD_ID, potion.getA());
            Registry.register(BuiltInRegistries.POTION, id, potion.getB());
            potion.getC().accept(BuiltInRegistries.POTION.wrapAsHolder(potion.getB()));
        }
    }

}
