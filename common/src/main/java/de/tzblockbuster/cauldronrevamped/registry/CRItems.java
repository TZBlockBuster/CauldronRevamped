package de.tzblockbuster.cauldronrevamped.registry;

import de.tzblockbuster.cauldronrevamped.CauldronRevamped;
import dev.architectury.platform.Platform;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SolidBucketItem;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.function.Function;

public class CRItems {

    public static final ArrayList<Tuple<String, Item>> items = new ArrayList<>();


    public static final Item LAVA_BOTTLE = registerItem(
            "lava_bottle",
            new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).usingConvertsTo(Items.GLASS_BOTTLE).stacksTo(16)
    );

    public static final Item SLIME_BOTTLE = registerItem(
            "slime_bottle",
            new Item.Properties().craftRemainder(Items.GLASS_BOTTLE).usingConvertsTo(Items.GLASS_BOTTLE).stacksTo(16)
    );

    public static final Item SLIME_BUCKET = registerItem(
            "slime_bucket",
            properties -> new SolidBucketItem(Blocks.SLIME_BLOCK, SoundEvents.BUCKET_EMPTY_POWDER_SNOW, properties),
            new Item.Properties().stacksTo(1)
    );

    public static final Item HONEY_BUCKET = registerItem(
            "honey_bucket",
            properties -> new SolidBucketItem(Blocks.HONEY_BLOCK, SoundEvents.BUCKET_EMPTY_POWDER_SNOW, properties),
            new Item.Properties().stacksTo(1)
    );

    private static Item registerItem(String name, Function<Item.Properties, Item> itemFunction, Item.Properties properties) {
        Identifier identifier = Identifier.fromNamespaceAndPath(CauldronRevamped.MOD_ID, name);
        Item item;
        if (Platform.isFabric()) {
            item = itemFunction.apply(properties.useItemDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, identifier)));
        } else {
            item = itemFunction.apply(properties);
        }
        items.add(new Tuple<>(name, item));
        return item;
    }

    private static Item registerItem(String name, Item.Properties properties) {
        return registerItem(name, Item::new, properties);
    }

}
