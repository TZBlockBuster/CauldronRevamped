package de.tzblockbuster.cauldronrevamped.fabric.client;

import de.tzblockbuster.cauldronrevamped.fabric.client.registry.CauldronRevampedBlockEntityRendererRegistry;
import net.fabricmc.api.ClientModInitializer;

public final class CauldronRevampedFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        CauldronRevampedBlockEntityRendererRegistry.init();
    }
}
