package de.tzblockbuster.cauldronrevamped.fabric.registry;

import de.tzblockbuster.cauldronrevamped.CauldronRevamped;
import de.tzblockbuster.cauldronrevamped.registry.CRBlockEntities;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

public class CauldronRevampedBlockEntityRegistry {

    public static void register() {
        CRBlockEntities.blockEntitySuppliers.forEach((registration) -> {
            Identifier identifier = Identifier.fromNamespaceAndPath(CauldronRevamped.MOD_ID, registration.name());
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, identifier, FabricBlockEntityTypeBuilder.create((blockPos, blockState) -> registration.blockEntitySupplier().create(blockPos, blockState), registration.blocks().toArray(Block[]::new)).build());
        });
    }
}
