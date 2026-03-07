package de.tzblockbuster.cauldronrevamped.fabric.client.datagen;

import de.tzblockbuster.cauldronrevamped.CauldronRevamped;
import de.tzblockbuster.cauldronrevamped.registry.CRBlocks;
import de.tzblockbuster.cauldronrevamped.registry.CRItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.model.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;

public class CauldronRevampedModelProvider extends FabricModelProvider {

    private static final ArrayList<Block> customBlockModels = new ArrayList<>();
    private static final ArrayList<Item> customItemModels = new ArrayList<>();


    public CauldronRevampedModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(@NonNull BlockModelGenerators blockModelGenerators) {
        generateCauldronModels(blockModelGenerators);
        generateBrewingCauldronModels(blockModelGenerators);

        for (Tuple<String, Block> block : CRBlocks.blocks.stream().filter(block -> !customBlockModels.contains(block.getB())).toList()) {
            blockModelGenerators.createTrivialCube(block.getB());
        }
    }

    @Override
    public void generateItemModels(@NonNull ItemModelGenerators itemModelGenerators) {
        for (Tuple<String, Item> item : CRItems.items.stream().filter(item -> !customItemModels.contains(item.getB())).toList()) {
            Identifier itemModel = itemModelGenerators.createFlatItemModel(item.getB(), ModelTemplates.FLAT_ITEM);
            itemModelGenerators.itemModelOutput.accept(item.getB(), ItemModelUtils.plainModel(itemModel));
        }
    }


    public void generateCauldronModels(BlockModelGenerators blockModelGenerators) {
        customBlockModels.add(CRBlocks.SLIME_CAULDRON);
        customBlockModels.add(CRBlocks.HONEY_CAULDRON);
        customBlockModels.add(CRBlocks.MILK_CAULDRON);

        Identifier slime_cauldron_full = ModelTemplates.CAULDRON_FULL.createWithSuffix(CRBlocks.SLIME_CAULDRON, "_full", TextureMapping.cauldron(Identifier.withDefaultNamespace("block/cauldron")).put(TextureSlot.CONTENT, TextureMapping.getBlockTexture(Blocks.SLIME_BLOCK)), blockModelGenerators.modelOutput);
        Identifier slime_cauldron_level1 = ModelTemplates.CAULDRON_LEVEL1.createWithSuffix(CRBlocks.SLIME_CAULDRON, "_level1", TextureMapping.cauldron(Identifier.withDefaultNamespace("block/cauldron")).put(TextureSlot.CONTENT, TextureMapping.getBlockTexture(Blocks.SLIME_BLOCK)), blockModelGenerators.modelOutput);
        Identifier slime_cauldron_level2 = ModelTemplates.CAULDRON_LEVEL2.createWithSuffix(CRBlocks.SLIME_CAULDRON, "_level2", TextureMapping.cauldron(Identifier.withDefaultNamespace("block/cauldron")).put(TextureSlot.CONTENT, TextureMapping.getBlockTexture(Blocks.SLIME_BLOCK)), blockModelGenerators.modelOutput);

        blockModelGenerators.blockStateOutput.accept(MultiPartGenerator.multiPart(CRBlocks.SLIME_CAULDRON)
                .with(BlockModelGenerators.condition().term(LayeredCauldronBlock.LEVEL, 3), BlockModelGenerators.plainVariant(slime_cauldron_full))
                .with(BlockModelGenerators.condition().term(LayeredCauldronBlock.LEVEL, 2), BlockModelGenerators.plainVariant(slime_cauldron_level2))
                .with(BlockModelGenerators.condition().term(LayeredCauldronBlock.LEVEL, 1), BlockModelGenerators.plainVariant(slime_cauldron_level1))
        );

        Identifier honey_cauldron_full = ModelTemplates.CAULDRON_FULL.createWithSuffix(CRBlocks.HONEY_CAULDRON, "_full", TextureMapping.cauldron(Identifier.withDefaultNamespace("block/cauldron")).put(TextureSlot.CONTENT, TextureMapping.getBlockTexture(Blocks.HONEY_BLOCK, "_top")), blockModelGenerators.modelOutput);
        Identifier honey_cauldron_level1 = ModelTemplates.CAULDRON_LEVEL1.createWithSuffix(CRBlocks.HONEY_CAULDRON, "_level1", TextureMapping.cauldron(Identifier.withDefaultNamespace("block/cauldron")).put(TextureSlot.CONTENT, TextureMapping.getBlockTexture(Blocks.HONEY_BLOCK, "_top")), blockModelGenerators.modelOutput);
        Identifier honey_cauldron_level2 = ModelTemplates.CAULDRON_LEVEL2.createWithSuffix(CRBlocks.HONEY_CAULDRON, "_level2", TextureMapping.cauldron(Identifier.withDefaultNamespace("block/cauldron")).put(TextureSlot.CONTENT, TextureMapping.getBlockTexture(Blocks.HONEY_BLOCK, "_top")), blockModelGenerators.modelOutput);

        blockModelGenerators.blockStateOutput.accept(MultiPartGenerator.multiPart(CRBlocks.HONEY_CAULDRON)
                .with(BlockModelGenerators.condition().term(LayeredCauldronBlock.LEVEL, 3), BlockModelGenerators.plainVariant(honey_cauldron_full))
                .with(BlockModelGenerators.condition().term(LayeredCauldronBlock.LEVEL, 2), BlockModelGenerators.plainVariant(honey_cauldron_level2))
                .with(BlockModelGenerators.condition().term(LayeredCauldronBlock.LEVEL, 1), BlockModelGenerators.plainVariant(honey_cauldron_level1))
        );

        Identifier milk_cauldron_full = ModelTemplates.CAULDRON_FULL.createWithSuffix(CRBlocks.MILK_CAULDRON, "_full", TextureMapping.cauldron(Identifier.withDefaultNamespace("block/cauldron")).put(TextureSlot.CONTENT, Identifier.fromNamespaceAndPath(CauldronRevamped.MOD_ID, "block/milk_still")), blockModelGenerators.modelOutput);
        Identifier milk_cauldron_level1 = ModelTemplates.CAULDRON_LEVEL1.createWithSuffix(CRBlocks.MILK_CAULDRON, "_level1", TextureMapping.cauldron(Identifier.withDefaultNamespace("block/cauldron")).put(TextureSlot.CONTENT, Identifier.fromNamespaceAndPath(CauldronRevamped.MOD_ID, "block/milk_still")), blockModelGenerators.modelOutput);
        Identifier milk_cauldron_level2 = ModelTemplates.CAULDRON_LEVEL2.createWithSuffix(CRBlocks.MILK_CAULDRON, "_level2", TextureMapping.cauldron(Identifier.withDefaultNamespace("block/cauldron")).put(TextureSlot.CONTENT, Identifier.fromNamespaceAndPath(CauldronRevamped.MOD_ID, "block/milk_still")), blockModelGenerators.modelOutput);

        blockModelGenerators.blockStateOutput.accept(MultiPartGenerator.multiPart(CRBlocks.MILK_CAULDRON)
                .with(BlockModelGenerators.condition().term(LayeredCauldronBlock.LEVEL, 3), BlockModelGenerators.plainVariant(milk_cauldron_full))
                .with(BlockModelGenerators.condition().term(LayeredCauldronBlock.LEVEL, 2), BlockModelGenerators.plainVariant(milk_cauldron_level2))
                .with(BlockModelGenerators.condition().term(LayeredCauldronBlock.LEVEL, 1), BlockModelGenerators.plainVariant(milk_cauldron_level1))
        );

        Identifier lava_cauldron_full = ModelTemplates.CAULDRON_FULL.createWithSuffix(Blocks.LAVA_CAULDRON, "_full", TextureMapping.cauldron(Identifier.withDefaultNamespace("block/cauldron")).put(TextureSlot.CONTENT, Identifier.withDefaultNamespace("block/lava_still")), blockModelGenerators.modelOutput);
        Identifier lava_cauldron_level1 = ModelTemplates.CAULDRON_LEVEL1.createWithSuffix(Blocks.LAVA_CAULDRON, "_level1", TextureMapping.cauldron(Identifier.withDefaultNamespace("block/cauldron")).put(TextureSlot.CONTENT, Identifier.withDefaultNamespace("block/lava_still")), blockModelGenerators.modelOutput);
        Identifier lava_cauldron_level2 = ModelTemplates.CAULDRON_LEVEL2.createWithSuffix(Blocks.LAVA_CAULDRON, "level_2", TextureMapping.cauldron(Identifier.withDefaultNamespace("block/cauldron")).put(TextureSlot.CONTENT, Identifier.withDefaultNamespace("block/lava_still")), blockModelGenerators.modelOutput);

        blockModelGenerators.blockStateOutput.accept(MultiPartGenerator.multiPart(Blocks.LAVA_CAULDRON)
                .with(BlockModelGenerators.condition().term(LayeredCauldronBlock.LEVEL, 3), BlockModelGenerators.plainVariant(lava_cauldron_full))
                .with(BlockModelGenerators.condition().term(LayeredCauldronBlock.LEVEL, 2), BlockModelGenerators.plainVariant(lava_cauldron_level2))
                .with(BlockModelGenerators.condition().term(LayeredCauldronBlock.LEVEL, 1), BlockModelGenerators.plainVariant(lava_cauldron_level1))
        );
    }


    public void generateBrewingCauldronModels(BlockModelGenerators blockModelGenerators) {
        customBlockModels.add(CRBlocks.BREWING_CAULDRON);

        blockModelGenerators.blockStateOutput.accept(MultiPartGenerator.multiPart(CRBlocks.BREWING_CAULDRON)
                .with(BlockModelGenerators.condition().term(LayeredCauldronBlock.LEVEL, 3), BlockModelGenerators.plainVariant(Identifier.withDefaultNamespace("block/cauldron")))
                .with(BlockModelGenerators.condition().term(LayeredCauldronBlock.LEVEL, 2), BlockModelGenerators.plainVariant(Identifier.withDefaultNamespace("block/cauldron")))
                .with(BlockModelGenerators.condition().term(LayeredCauldronBlock.LEVEL, 1), BlockModelGenerators.plainVariant(Identifier.withDefaultNamespace("block/cauldron")))
        );
    }
}
