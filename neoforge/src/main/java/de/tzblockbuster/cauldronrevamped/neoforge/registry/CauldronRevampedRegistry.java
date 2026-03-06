package de.tzblockbuster.cauldronrevamped.neoforge.registry;

import de.tzblockbuster.cauldronrevamped.CauldronRevamped;
import de.tzblockbuster.cauldronrevamped.registry.CRBlocks;
import de.tzblockbuster.cauldronrevamped.registry.CRItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

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
    }
}
