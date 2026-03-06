package de.tzblockbuster.cauldronrevamped.fabric;

import de.tzblockbuster.cauldronrevamped.CauldronRevamped;
import de.tzblockbuster.cauldronrevamped.fabric.registry.CauldronRevampedBlockEntityRegistry;
import de.tzblockbuster.cauldronrevamped.fabric.registry.CauldronRevampedRegistry;
import net.fabricmc.api.ModInitializer;

public final class CauldronRevampedFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        CauldronRevamped.init();

        CauldronRevampedRegistry.register();

        CauldronRevampedBlockEntityRegistry.register();

        CauldronRevamped.lateInit();
    }
}
