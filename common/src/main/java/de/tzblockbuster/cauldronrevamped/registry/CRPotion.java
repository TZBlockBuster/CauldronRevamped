package de.tzblockbuster.cauldronrevamped.registry;

import de.tzblockbuster.cauldronrevamped.CauldronRevamped;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.alchemy.Potion;

public class CRPotion {

    private static final Registrar<Potion> potionRegistrar = RegistrarManager.get(CauldronRevamped.MOD_ID).get(Registries.POTION);

    public static Holder<Potion> MIXED_POTION;

    public static void register() {
        MIXED_POTION = registerPotion("mixed_potion", new Potion("mixed_potion"));
    }

    private static Holder<Potion> registerPotion(String potionName, Potion potion) {
        Identifier id = Identifier.fromNamespaceAndPath(CauldronRevamped.MOD_ID, potionName);
        potionRegistrar.register(id, () -> potion);
        return potionRegistrar.getHolder(id);
    }
}
