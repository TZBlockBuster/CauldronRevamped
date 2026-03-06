package de.tzblockbuster.cauldronrevamped.client.registry;

import de.tzblockbuster.cauldronrevamped.client.models.BrewingCauldronContentModel;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;

public class CRModelLayerLocation {

    public static void register() {
        EntityModelLayerRegistry.register(BrewingCauldronContentModel.BrewingCauldronContentModel, BrewingCauldronContentModel::getTexturedModelData);
    }
}
