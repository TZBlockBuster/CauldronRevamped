package de.tzblockbuster.cauldronrevamped.fabric.client.datagen;

import de.tzblockbuster.cauldronrevamped.fabric.datagen.CauldronRevampedBlockTagProvider;
import de.tzblockbuster.cauldronrevamped.fabric.datagen.CauldronRevampedLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jspecify.annotations.NonNull;

public class CauldronRevampedDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(@NonNull FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(CauldronRevampedModelProvider::new);
        pack.addProvider(CauldronRevampedLootTableProvider::new);
        pack.addProvider(CauldronRevampedBlockTagProvider::new);
    }
}
