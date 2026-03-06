package de.tzblockbuster.cauldronrevamped.fabric.datagen;

import de.tzblockbuster.cauldronrevamped.registry.CRBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class CauldronRevampedBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public CauldronRevampedBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider wrapperLookup) {
        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(CRBlocks.BREWING_CAULDRON)
                .add(CRBlocks.SLIME_CAULDRON)
                .add(CRBlocks.HONEY_CAULDRON);
        valueLookupBuilder(BlockTags.CAULDRONS)
                .add(CRBlocks.BREWING_CAULDRON)
                .add(CRBlocks.SLIME_CAULDRON)
                .add(CRBlocks.HONEY_CAULDRON);
    }
}
