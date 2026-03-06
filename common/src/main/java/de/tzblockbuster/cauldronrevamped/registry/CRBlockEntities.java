package de.tzblockbuster.cauldronrevamped.registry;

import de.tzblockbuster.cauldronrevamped.CauldronRevamped;
import de.tzblockbuster.cauldronrevamped.blocks.brewing_cauldron.BrewingCauldronBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CRBlockEntities {

    public static final ArrayList<BlockEntityRegistration<?>> blockEntitySuppliers = new ArrayList<>();

    public static Supplier<BlockEntityType<BrewingCauldronBlockEntity>> brewingCauldronBlockEntity = registerBlockEntity("brewing_cauldron", BrewingCauldronBlockEntity::new, CRBlocks.BREWING_CAULDRON);


    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<T> blockEntitySupplier, Block... blocks) {
        blockEntitySuppliers.add(new BlockEntityRegistration<>(name, blockEntitySupplier, List.of(blocks)));
        return () -> BuiltInRegistries.BLOCK_ENTITY_TYPE.get(Identifier.fromNamespaceAndPath(CauldronRevamped.MOD_ID, name)).map(blockEntityTypeReference -> (BlockEntityType<T>) blockEntityTypeReference.value()).orElse(null);
    }


    public record BlockEntityRegistration<T extends BlockEntity>(String name, BlockEntityType.BlockEntitySupplier<T> blockEntitySupplier, List<Block> blocks) {

    }

}
