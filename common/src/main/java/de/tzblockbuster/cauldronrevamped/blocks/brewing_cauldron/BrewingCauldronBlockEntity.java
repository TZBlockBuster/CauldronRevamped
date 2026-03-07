package de.tzblockbuster.cauldronrevamped.blocks.brewing_cauldron;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.tzblockbuster.cauldronrevamped.registry.CRBlockEntities;
import de.tzblockbuster.cauldronrevamped.registry.CRPotion;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class BrewingCauldronBlockEntity extends BlockEntity {

    public ArrayList<PotionFraction> potionFractions = new ArrayList<>();

    public int brewingTime = 0;
    public Optional<ItemStack> ingredient = Optional.empty();

    public BrewingCauldronBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CRBlockEntities.brewingCauldronBlockEntity.get(), blockPos, blockState);
        if (Platform.getEnvironment() == Env.CLIENT) {
            currentLevel = blockState.getValue(LayeredCauldronBlock.LEVEL);
        }
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput valueInput) {
        super.loadAdditional(valueInput);
        potionFractions = new ArrayList<>(valueInput.read("potionFractions", PotionFraction.CODEC.codec().listOf()).orElse(new ArrayList<>()));
        brewingTime = valueInput.read("brewingTime", Codec.intRange(0, 20 * 20)).orElse(0);
        ingredient = valueInput.read("ingredient", ItemStack.CODEC);
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        valueOutput.store("potionFractions", PotionFraction.CODEC.codec().listOf(), potionFractions);
        valueOutput.store("brewingTime", Codec.intRange(0, 20 * 20), brewingTime);
        ingredient.ifPresent(itemStack -> valueOutput.store("ingredient", ItemStack.CODEC, itemStack));
    }

    @Override
    public @NonNull CompoundTag getUpdateTag(HolderLookup.@NonNull Provider provider) {
        CompoundTag tag;
        try (ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(this.problemPath(), LogUtils.getLogger())) {
            TagValueOutput tagValueOutput = TagValueOutput.createWithContext(scopedCollector, provider);
            tagValueOutput.store("potionFractions", PotionFraction.CODEC.codec().listOf(), potionFractions);
            tagValueOutput.store("brewingTime", Codec.intRange(0, 20 * 20), brewingTime);
            ingredient.ifPresent(itemStack -> tagValueOutput.store("ingredient", ItemStack.CODEC, itemStack));
            tag = tagValueOutput.buildResult();
        }

        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return new ClientboundBlockUpdatePacket(getBlockPos(), getBlockState());
    }

    public boolean addPotion(ItemStack stack) {
        if (getLevel() == null) return false;
        if (getBlockState().getValue(LayeredCauldronBlock.LEVEL) >= 3) return false;
        PotionContents potionContents = stack.get(DataComponents.POTION_CONTENTS);
        if (potionContents == null || potionContents.potion().isEmpty()) {
            return false;
        }
        addFraction(new PotionFraction(potionContents.potion().get(), 1f));

        return true;
    }

    public void addFraction(PotionFraction fraction) {
        if (potionFractions.stream().anyMatch(fraction1 -> fraction1.potion().value() == fraction.potion().value())) {
            potionFractions.stream().filter(fraction1 -> fraction1.potion().value() == fraction.potion().value()).findFirst().ifPresent(fraction1 -> {
                float newFraction = fraction1.fraction() + fraction.fraction();
                potionFractions.remove(fraction1);
                potionFractions.add(new PotionFraction(fraction1.potion(), newFraction));
            });
        } else {
            potionFractions.add(fraction);
        }
    }

    public void removeFraction(PotionFraction fraction) {
        if (potionFractions.stream().anyMatch(fraction1 -> fraction1.potion().value() == fraction.potion().value())) {
            PotionFraction fractionToRemove = potionFractions.stream().filter(fraction1 -> fraction1.potion().value() == fraction.potion().value()).findFirst().orElseThrow();
            float newFraction = fractionToRemove.fraction() - fraction.fraction();
            newFraction = Float.parseFloat(new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US)).format(newFraction));
            potionFractions.remove(fractionToRemove);
            if (newFraction > 0) {
                potionFractions.add(new PotionFraction(fractionToRemove.potion(), newFraction));
            }
        }
    }

    public ItemStack takePotion() {
        ItemStack stack = new ItemStack(Items.POTION);
        stack.set(DataComponents.POTION_CONTENTS, createPotionContents(getEffectsForNextPotion()));
        return stack;
    }

    public ArrayList<PotionFraction> getEffectsForNextPotion() {
        if (getLevel() == null) return new ArrayList<>();

        // Get Current Level
        int level = getBlockState().getValue(LayeredCauldronBlock.LEVEL);
        float factor = 1f / level;


        ArrayList<PotionFraction> takeFractions = new ArrayList<>();
        ArrayList<PotionFraction> potionFractionInstances = this.potionFractions;
        ArrayList<Integer> effectsToRemove = new ArrayList<>();


        for (int i = 0; i < potionFractionInstances.size(); i++) {
            PotionFraction fraction = potionFractionInstances.get(i);
            // Prevent small effects to remain in the cauldron
            if (fraction.fraction() * (1 - factor) < 0.01) {
                effectsToRemove.add(i);
                takeFractions.add(fraction);
                continue;
            }
            potionFractionInstances.set(i, new PotionFraction(fraction.potion(), fraction.fraction() * (1 - factor)));
            takeFractions.add(new PotionFraction(fraction.potion(), fraction.fraction() * factor));
        }

        for (Integer index : effectsToRemove.reversed()) {
            potionFractionInstances.remove((int) index);
        }

        return takeFractions;
    }


    public PotionContents createPotionContents(ArrayList<PotionFraction> potionFractions) {
        if (potionFractions.isEmpty()) return null;
        if (potionFractions.size() == 1) {
            return new PotionContents(potionFractions.getFirst().potion());
        } else {
            ArrayList<MobEffectInstance> effects = new ArrayList<>();
            for (PotionFraction potionFraction : potionFractions) {
                for (MobEffectInstance effect : potionFraction.potion().value().getEffects()) {
                    effects.add(new MobEffectInstance(effect.getEffect(), (int) (effect.getDuration() * potionFraction.fraction()), effect.getAmplifier()));
                }
            }
            return new PotionContents(Optional.of(CRPotion.MIXED_POTION), Optional.of(getColor(potionFractions)), effects, Optional.empty());
        }
    }

    public int getColor(ArrayList<PotionFraction> potionFractions) {
        if (getLevel() == null) return 0x3F76E4;
        if (potionFractions.isEmpty()) return getLevel().getBiome(getBlockPos()).value().getWaterColor();
        List<ColorMix> colors = new ArrayList<>();
        double totalFraction = potionFractions.stream().mapToDouble(PotionFraction::fraction).sum();
        for (PotionFraction potionFraction : potionFractions) {
            PotionContents potionContents = new PotionContents(potionFraction.potion());
            int color = potionContents.getColor();
            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;
            colors.add(new ColorMix(r, g, b, potionFraction.fraction() / (float) totalFraction));
        }
        float r = 0, g = 0, b = 0;
        for (ColorMix color : colors) {
            r += (int) (color.r * color.ratio);
            g += (int) (color.g * color.ratio);
            b += (int) (color.b * color.ratio);
        }

        return (int) (r) << 16 | (int) (g) << 8 | (int) (b);
    }

    record ColorMix(int r, int g, int b, float ratio) {
    }


    private float currentLevel = 0;

    private static final float ANIMATION_SPEED = 1f / 5f;

    public float getFluidAnimationLevel(float partialTicks) {
        if (Platform.getEnvironment() == Env.CLIENT) {
            if (getLevel() == null) return 0;
            int level = getBlockState().getValue(LayeredCauldronBlock.LEVEL);
            int direction = currentLevel < level ? 1 : currentLevel > level ? -1 : 0;
            return currentLevel + (direction * ANIMATION_SPEED) * partialTicks;
        } else {
            return 0;
        }
    }

    public boolean setIngredient(ItemStack ingredient) {
        if (isValidIngredient(ingredient)) {
            this.ingredient = Optional.of(ingredient);
            this.brewingTime = 20 * 20;
            return true;
        }
        return false;
    }

    public boolean isValidIngredient(ItemStack ingredient) {
        if (getLevel() == null) return false;
        if (potionFractions.isEmpty()) {
            ItemStack awkwardPotion = new ItemStack(Items.POTION);
            awkwardPotion.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));
            return getLevel().potionBrewing().hasMix(awkwardPotion, ingredient);
        } else {
            for (Potion potion : potionFractions.stream().filter(fraction -> fraction.fraction() >= 1f).map(PotionFraction::potion).map(Holder::value).toList()) {
                ItemStack potionStack = new ItemStack(Items.POTION);
                potionStack.set(DataComponents.POTION_CONTENTS, new PotionContents(BuiltInRegistries.POTION.wrapAsHolder(potion)));
                if (getLevel().potionBrewing().hasMix(potionStack, ingredient)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void tick() {
        if (getLevel() == null) return;
        if (!getLevel().isClientSide()) {
            serverTick();
        } else {
            if (getLevel() != null) {
                int level = getBlockState().getValue(LayeredCauldronBlock.LEVEL);
                int direction = currentLevel < level ? 1 : currentLevel > level ? -1 : 0;
                currentLevel += direction * ANIMATION_SPEED;
                if (Math.abs(currentLevel - level) < ANIMATION_SPEED) {
                    currentLevel = level;
                }
            }
        }
    }

    public void serverTick() {
        if (getLevel() == null) return;
        if (brewingTime > 0 && getBlockState().getValue(BrewingCauldron.HEATED)) {
            if (ingredient.isEmpty()) {
                brewingTime = 0;
                return;
            }
            if (!isValidIngredient(ingredient.get())) {
                ItemEntity itemEntity = new ItemEntity(getLevel(), getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5, ingredient.get());
                itemEntity.setDeltaMovement(0, 1, 0);
                getLevel().addFreshEntity(itemEntity);
                ingredient = Optional.empty();
                brewingTime = 0;
                return;
            }
            LogUtils.getLogger().info("Brewing potion with ingredient: {}", ingredient.get());
            brewingTime--;
            if (!getLevel().getBlockState(getBlockPos()).getValue(BrewingCauldron.BREWING)) {
                getLevel().setBlockAndUpdate(getBlockPos(), getLevel().getBlockState(getBlockPos()).setValue(BrewingCauldron.BREWING, true));
            }
            if (brewingTime == 0) {
                for (PotionFraction potionFraction : potionFractions.stream().filter(fraction -> fraction.fraction() >= 1f).toList()) {
                    ItemStack ingredientStack = ingredient.get().copy();
                    ItemStack potionStack = new ItemStack(Items.POTION);
                    potionStack.set(DataComponents.POTION_CONTENTS, new PotionContents(potionFraction.potion()));
                    ItemStack resultStack = getLevel().potionBrewing().mix(ingredientStack, potionStack);
                    PotionContents potionContents = resultStack.get(DataComponents.POTION_CONTENTS);
                    if (potionContents == null || potionContents.potion().isEmpty()) continue;
                    for (int i = 0; i < Math.floor(potionFraction.fraction()); i++) {
                        removeFraction(new PotionFraction(potionFraction.potion(), 1f));
                        addFraction(new PotionFraction(potionContents.potion().get(), 1f));
                    }
                }
                ingredient = Optional.empty();
                brewingTime = 0;
                getLevel().levelEvent(1035, getBlockPos(), 0);
            }
        } else if (getLevel().getBlockState(getBlockPos()).getValue(BrewingCauldron.BREWING)) {
            getLevel().setBlockAndUpdate(getBlockPos(), getLevel().getBlockState(getBlockPos()).setValue(BrewingCauldron.BREWING, false));
        }
    }

    public record PotionFraction(Holder<Potion> potion, float fraction) {
        public static MapCodec<PotionFraction> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Potion.CODEC.fieldOf("potion").forGetter(PotionFraction::potion),
                Codec.FLOAT.fieldOf("fraction").forGetter(PotionFraction::fraction)
        ).apply(instance, PotionFraction::new));

        @Override
        public @NonNull String toString() {
            return String.format("PotionFraction{potion=%s, fraction=%.2f}", potion.toString(), fraction);
        }
    }
}
