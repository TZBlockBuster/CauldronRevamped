package de.tzblockbuster.cauldronrevamped.neoforge;

import de.tzblockbuster.cauldronrevamped.CauldronRevamped;
import de.tzblockbuster.cauldronrevamped.neoforge.client.registry.CauldronRevampedEntityRendererRegistry;
import de.tzblockbuster.cauldronrevamped.neoforge.registry.CauldronRevampedRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod(CauldronRevamped.MOD_ID)
public final class CauldronRevampedNeoForge {
    public CauldronRevampedNeoForge(IEventBus modBus) {
        // Run our common setup.
        CauldronRevamped.init();

        modBus.register(new CauldronRevampedEntityRendererRegistry());
        modBus.register(new CauldronRevampedRegistry());

        modBus.addListener(FMLCommonSetupEvent.class, (event) -> CauldronRevamped.lateInit());
    }
}
