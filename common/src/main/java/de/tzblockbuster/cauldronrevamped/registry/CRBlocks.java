package de.tzblockbuster.cauldronrevamped.registry;

import de.tzblockbuster.cauldronrevamped.CauldronRevamped;
import de.tzblockbuster.cauldronrevamped.blocks.brewing_cauldron.BrewingCauldron;
import de.tzblockbuster.cauldronrevamped.cauldron.CauldronRevampedInteraction;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Function;

public class CRBlocks {

    public static final ArrayList<Tuple<String, Block>> blocks = new ArrayList<>();
    public static final ArrayList<Tuple<String, Item>> items = new ArrayList<>();


    public static final Block SLIME_CAULDRON = registerBlockWithoutItem("slime_cauldron", properties -> new LayeredCauldronBlock(Biome.Precipitation.NONE, CauldronRevampedInteraction.SLIME, properties), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(2.0F).noOcclusion());
    public static final Block HONEY_CAULDRON = registerBlockWithoutItem("honey_cauldron", properties -> new LayeredCauldronBlock(Biome.Precipitation.NONE, CauldronRevampedInteraction.HONEY, properties), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(2.0F).noOcclusion());
    public static final Block MILK_CAULDRON = registerBlockWithoutItem("milk_cauldron", properties -> new LayeredCauldronBlock(Biome.Precipitation.NONE, CauldronRevampedInteraction.MILK, properties), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(2.0F).noOcclusion());
    public static final Block BREWING_CAULDRON = registerBlockWithoutItem("brewing_cauldron", properties -> new BrewingCauldron(CauldronRevampedInteraction.BREWING, properties), BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(2.0F).noOcclusion());


    private static Block registerBlock(String name, Function<Block.Properties, Block> blockFunction, Block.Properties properties, @Nullable Function<Tuple<Item.Properties, Block>, Item> itemFunction, @Nullable Item.Properties itemProperties) {
        Block block = blockFunction.apply(properties.setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(CauldronRevamped.MOD_ID, name))));
        if (itemFunction != null && itemProperties == null) {
            itemProperties = new Item.Properties();
        }
        if (itemFunction != null) {
            Item item = itemFunction.apply(new Tuple<>(itemProperties.useBlockDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(CauldronRevamped.MOD_ID, name))), block));
            items.add(new Tuple<>(name, item));
        }
        blocks.add(new Tuple<>(name, block));
        return block;
    }

    private static Block registerBlock(String name, Function<Block.Properties, Block> blockFunction, Block.Properties properties) {
        return registerBlock(name, blockFunction, properties, (tuple) -> new BlockItem(tuple.getB(), tuple.getA()), new Item.Properties());
    }

    private static Block registerBlockWithoutItem(String name, Function<Block.Properties, Block> blockFunction, Block.Properties properties) {
        return registerBlock(name, blockFunction, properties, null, null);
    }

    private static Block registerBlock(String name, Block.Properties properties) {
        return registerBlock(name, Block::new, properties);
    }

}
