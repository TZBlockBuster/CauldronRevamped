package de.tzblockbuster.cauldronrevamped;

import de.tzblockbuster.cauldronrevamped.cauldron.CauldronRevampedInteraction;
import de.tzblockbuster.cauldronrevamped.client.registry.CRModelLayerLocation;
import de.tzblockbuster.cauldronrevamped.registry.CRPotion;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;

public final class CauldronRevamped {
    public static final String MOD_ID = "cauldronrevamped";

    public static void init() {
        CRPotion.register();
        CauldronRevampedInteraction.register();

        if (Platform.getEnvironment() == Env.CLIENT) {
            CRModelLayerLocation.register();
        }
    }
}
