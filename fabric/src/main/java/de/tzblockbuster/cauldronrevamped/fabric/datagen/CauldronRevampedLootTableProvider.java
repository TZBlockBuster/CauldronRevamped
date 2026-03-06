package de.tzblockbuster.cauldronrevamped.fabric.datagen;

import de.tzblockbuster.cauldronrevamped.registry.CRBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class CauldronRevampedLootTableProvider extends FabricBlockLootTableProvider {

    public CauldronRevampedLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropOther(CRBlocks.BREWING_CAULDRON, Items.CAULDRON);
        dropOther(CRBlocks.HONEY_CAULDRON, Items.CAULDRON);
        dropOther(CRBlocks.SLIME_CAULDRON, Items.CAULDRON);
    }
}
