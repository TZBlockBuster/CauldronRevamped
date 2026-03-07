package de.tzblockbuster.cauldronrevamped.registry;

import de.tzblockbuster.cauldronrevamped.CauldronRevamped;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CRItemTags {

    public static TagKey<Item> BABY_COW_FOOD = tag("baby_cow_food");

    private static TagKey<Item> tag(String name) {
        return TagKey.create(BuiltInRegistries.ITEM.key(), Identifier.fromNamespaceAndPath(CauldronRevamped.MOD_ID, name));
    }

}
