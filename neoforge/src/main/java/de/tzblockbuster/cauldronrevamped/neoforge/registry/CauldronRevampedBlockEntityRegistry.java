package de.tzblockbuster.cauldronrevamped.neoforge.registry;

import de.tzblockbuster.cauldronrevamped.CauldronRevamped;
import de.tzblockbuster.cauldronrevamped.registry.CRBlockEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashSet;

public class CauldronRevampedBlockEntityRegistry {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CauldronRevamped.MOD_ID);

    public static void register() {
        CRBlockEntities.blockEntitySuppliers.forEach((registration) -> BLOCK_ENTITY_TYPES.register(registration.name(), identifier1 -> new BlockEntityType<>(registration.blockEntitySupplier(), new HashSet<>(registration.blocks()))));
    }
}
