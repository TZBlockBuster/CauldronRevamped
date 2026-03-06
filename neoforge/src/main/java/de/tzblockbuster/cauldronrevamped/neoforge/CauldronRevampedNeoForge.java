package de.tzblockbuster.cauldronrevamped.neoforge;

import de.tzblockbuster.cauldronrevamped.CauldronRevamped;
import de.tzblockbuster.cauldronrevamped.neoforge.client.registry.CauldronRevampedEntityRendererRegistry;
import de.tzblockbuster.cauldronrevamped.neoforge.registry.CauldronRevampedBlockEntityRegistry;
import de.tzblockbuster.cauldronrevamped.neoforge.registry.CauldronRevampedRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(CauldronRevamped.MOD_ID)
public final class CauldronRevampedNeoForge {
    public CauldronRevampedNeoForge(IEventBus modBus) {
        // Run our common setup.
        CauldronRevamped.init();

        CauldronRevampedBlockEntityRegistry.register();
        modBus.register(new CauldronRevampedEntityRendererRegistry());
        modBus.register(new CauldronRevampedRegistry());
    }
}
