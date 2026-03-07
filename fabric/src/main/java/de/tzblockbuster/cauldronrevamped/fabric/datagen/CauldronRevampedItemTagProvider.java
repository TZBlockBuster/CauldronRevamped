package de.tzblockbuster.cauldronrevamped.fabric.datagen;

import de.tzblockbuster.cauldronrevamped.registry.CRBlocks;
import de.tzblockbuster.cauldronrevamped.registry.CRItemTags;
import de.tzblockbuster.cauldronrevamped.registry.CRItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class CauldronRevampedItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public CauldronRevampedItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider wrapperLookup) {
        valueLookupBuilder(CRItemTags.BABY_COW_FOOD)
                .add(CRItems.MILK_BOTTLE);
    }
}
