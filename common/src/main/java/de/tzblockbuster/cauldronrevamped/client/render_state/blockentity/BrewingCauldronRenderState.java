package de.tzblockbuster.cauldronrevamped.client.render_state.blockentity;

import de.tzblockbuster.cauldronrevamped.blocks.brewing_cauldron.BrewingCauldronBlockEntity;
import de.tzblockbuster.cauldronrevamped.client.models.BrewingCauldronContentModel;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public class BrewingCauldronRenderState extends BlockEntityRenderState {

    public BrewingCauldronContentModel.BrewingCauldronContentModelState modelState;
    public ArrayList<BrewingCauldronBlockEntity.PotionFraction> potionFractions;
    public int brewingTime;
    public boolean isSneaking;

    public BrewingCauldronRenderState() {
        super();
        this.modelState = new BrewingCauldronContentModel.BrewingCauldronContentModelState(0, 0);
        this.potionFractions = new ArrayList<>();
        this.brewingTime = 0;
    }

    public ArrayList<Component> getTooltips() {
        ArrayList<Component> tooltips = new ArrayList<>();
        for (BrewingCauldronBlockEntity.PotionFraction potionFraction : potionFractions) {
            if (potionFraction.potion().value().getEffects().isEmpty()) {
                ItemStack itemStack = new ItemStack(Items.POTION);
                itemStack.set(DataComponents.POTION_CONTENTS, new PotionContents(potionFraction.potion()));
                tooltips.add(itemStack.getDisplayName().copy().append(" x ").append(String.valueOf(potionFraction.fraction())));
            } else {
                for (MobEffectInstance mobEffectInstance : potionFraction.potion().value().getEffects()) {
                    Component component = PotionContents.getPotionDescription(mobEffectInstance.getEffect(), mobEffectInstance.getAmplifier());
                    if (!mobEffectInstance.endsWithin(20)) {
                        component = Component.translatable("potion.withDuration", component, MobEffectUtil.formatDuration(mobEffectInstance, 1, 20));
                    }
                    tooltips.add(component.copy().append(" x ").append(String.valueOf(potionFraction.fraction())));
                }
            }
        }
        if (brewingTime > 0) {
            int longestTooltipLength = tooltips.stream().mapToInt(value -> Minecraft.getInstance().font.width(value)).max().orElse(0);
            MutableComponent component = Component.empty();
            int bracketWidth = Minecraft.getInstance().font.width("[]");
            int innerWidth = Minecraft.getInstance().font.width("|");
            int innerCount = (longestTooltipLength - bracketWidth) / innerWidth;
            float brewingTimeFraction = 1f - ((float) brewingTime / (20f * 20f));
            component.append(Component.literal("[").withStyle(ChatFormatting.DARK_GRAY));
            for (int i = 0; i < innerCount; i++) {
                float fraction = (float) i / (float) innerCount;
                if (brewingTimeFraction > fraction) {
                    component.append(Component.literal("|").withStyle(ChatFormatting.GREEN));
                } else {
                    component.append(Component.literal("|").withStyle(ChatFormatting.DARK_GRAY));
                }
            }
            component.append(Component.literal("]").withStyle(ChatFormatting.DARK_GRAY));
            tooltips.add(component);
        }
        return tooltips;
    }

}
