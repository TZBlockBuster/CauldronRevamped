package de.tzblockbuster.cauldronrevamped.registry;

import de.tzblockbuster.cauldronrevamped.CauldronRevamped;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.alchemy.Potion;
import oshi.util.tuples.Triplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CRPotion {

    public static ArrayList<Triplet<String, Potion, Consumer<Holder<Potion>>>> potions = new ArrayList<>();

    public static Holder<Potion> MIXED_POTION;

    static {
        registerPotion("mixed_potion", new Potion("mixed_potion"), potion -> MIXED_POTION = potion);
    }

    private static void registerPotion(String potionName, Potion potion, Consumer<Holder<Potion>> potionConsumer) {
        potions.add(new Triplet<>(potionName, potion, potionConsumer));
    }
}
