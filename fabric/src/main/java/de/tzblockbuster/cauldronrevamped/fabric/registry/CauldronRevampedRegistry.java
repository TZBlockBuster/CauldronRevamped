package de.tzblockbuster.cauldronrevamped.fabric.registry;

import de.tzblockbuster.cauldronrevamped.CauldronRevamped;
import de.tzblockbuster.cauldronrevamped.registry.CRBlocks;
import de.tzblockbuster.cauldronrevamped.registry.CRItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

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
        for (Tuple<String, Block> item : CRBlocks.blocks) {
            Identifier id = Identifier.fromNamespaceAndPath(CauldronRevamped.MOD_ID, item.getA());
            Registry.register(BuiltInRegistries.BLOCK, id, item.getB());
        }
    }

}
