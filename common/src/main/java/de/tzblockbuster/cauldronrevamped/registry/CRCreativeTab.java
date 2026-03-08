package de.tzblockbuster.cauldronrevamped.registry;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Arrays;

public class CRCreativeTab {

    public static ArrayList<Tuple<String, CreativeTab>> creativeTabs = new ArrayList<>();

    static {
        creativeTabs.add(new Tuple<>("cauldronrevamped", new CreativeTab(new ItemStack(Items.CAULDRON), Component.translatable("itemGroup.cauldronrevamped"))
                .addItems(CRItems.SLIME_BUCKET, CRItems.HONEY_BUCKET, CRItems.SLIME_BOTTLE, CRItems.LAVA_BOTTLE, CRItems.MILK_BOTTLE)
        ));
    }


    public record CreativeTab(ItemStack icon, Component title, ArrayList<ItemStack> displayedItems) {

        public CreativeTab(ItemStack icon, Component title) {
            this(icon, title, new ArrayList<>());
        }

        public CreativeTab addItem(ItemStack item) {
            displayedItems.add(item);
            return this;
        }

        public CreativeTab addItems(Item... items) {
            return addItems(Arrays.stream(items).map(ItemStack::new).toArray(ItemStack[]::new));
        }

        public CreativeTab addItems(ItemStack... items) {
            displayedItems.addAll(Arrays.stream(items).map(ItemStack::copy).toList());
            return this;
        }
    }
}
