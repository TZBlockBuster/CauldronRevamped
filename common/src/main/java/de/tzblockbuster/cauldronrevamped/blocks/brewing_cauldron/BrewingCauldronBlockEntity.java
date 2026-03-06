package de.tzblockbuster.cauldronrevamped.blocks.brewing_cauldron;

import de.tzblockbuster.cauldronrevamped.registry.CRBlockEntities;
import de.tzblockbuster.cauldronrevamped.registry.CRPotion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BrewingCauldronBlockEntity extends BlockEntity {

    public ArrayList<MobEffectInstance> effects = new ArrayList<>();

    public BrewingCauldronBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CRBlockEntities.brewingCauldronBlockEntity.get(), blockPos, blockState);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput valueInput) {
        super.loadAdditional(valueInput);
        effects = new ArrayList<>(valueInput.read("effects", MobEffectInstance.CODEC.listOf()).orElse(new ArrayList<>()));
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        valueOutput.store("effects", MobEffectInstance.CODEC.listOf(), effects);
    }

    @Override
    public @NonNull CompoundTag getUpdateTag(HolderLookup.@NonNull Provider provider) {
        CompoundTag tag = super.getUpdateTag(provider);
        tag.store("effects", MobEffectInstance.CODEC.listOf(), effects);
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
        if (potionContents == null) {
            return false;
        }
        for (MobEffectInstance effect : potionContents.getAllEffects()) {
            if (effects.stream().anyMatch(e -> e.getEffect() == effect.getEffect())) {
                MobEffectInstance existingEffect = effects.stream().filter(e -> e.getEffect() == effect.getEffect()).findFirst().get();
                effects.set(effects.indexOf(existingEffect), new MobEffectInstance(effect.getEffect(), existingEffect.getDuration() + effect.getDuration(), Math.max(existingEffect.getAmplifier(), effect.getAmplifier())));
            } else {
                effects.add(effect);
            }
        }

        // Because when we put in the first potion its still a normal cauldron so the adding happens after the potion is added. So this prevents a correct block update so we do a double block update.
        updateBlock();

        return true;
    }

    public ItemStack takePotion() {
        ItemStack stack = new ItemStack(Items.POTION);
        stack.set(DataComponents.POTION_CONTENTS, createPotionContents(getEffectsForNextPotion()));
        updateBlock();
        return stack;
    }

    public ArrayList<MobEffectInstance> getEffectsForNextPotion() {
        if (getLevel() == null) return new ArrayList<>();

        // Get Current Level
        int level = getBlockState().getValue(LayeredCauldronBlock.LEVEL);
        float factor = 1f / level;


        ArrayList<MobEffectInstance> takeEffects = new ArrayList<>();
        ArrayList<MobEffectInstance> mobEffectInstances = this.effects;
        ArrayList<Integer> effectsToRemove = new ArrayList<>();


        for (int i = 0; i < mobEffectInstances.size(); i++) {
            MobEffectInstance effect = mobEffectInstances.get(i);
            // Prevent small effects to remain in the cauldron
            if (effect.getDuration() * (1 - factor) < 20) {
                effectsToRemove.add(i);
                takeEffects.add(effect);
                continue;
            }
            int duration = (int) (effect.getDuration() * factor);
            effects.set(i, new MobEffectInstance(effect.getEffect(), effect.getDuration() - duration, effect.getAmplifier()));
            takeEffects.add(new MobEffectInstance(effect.getEffect(), duration, effect.getAmplifier()));
        }

        for (Integer index : effectsToRemove.reversed()) {
            effects.remove((int) index);
        }

        return takeEffects;
    }


    public PotionContents createPotionContents(ArrayList<MobEffectInstance> effects) {
        if (effects.isEmpty()) return new PotionContents(Potions.MUNDANE);

        return new PotionContents(Optional.of(CRPotion.MIXED_POTION), Optional.of(getColor()), effects, Optional.empty());
    }

    public int getColor() {
        if (effects.isEmpty()) return 0x0000ff;
        List<Integer> colors = new ArrayList<>();
        for (MobEffectInstance effect : effects) {
            if (effect.getEffect().unwrapKey().isPresent()) {
                Optional<Holder.Reference<MobEffect>> mobEffect = BuiltInRegistries.MOB_EFFECT.get(effect.getEffect().unwrapKey().get());
                mobEffect.ifPresent(mobEffectReference -> colors.add(mobEffectReference.value().getColor()));
            }
        }
        if (colors.isEmpty()) return 0x0000ff;
        return (int) colors.stream().mapToInt(Integer::intValue).average().orElse(colors.getFirst());
    }

    public void updateBlock() {
        if (getLevel() == null) return;
        getLevel().setBlockAndUpdate(getBlockPos(), getBlockState());

        if (getLevel().getBlockState(getBlockPos().below()).is(BlockTags.CAMPFIRES)) {
            getLevel().setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BrewingCauldron.HEATED, true));
        } else {
            getLevel().setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BrewingCauldron.HEATED, false));
        }
    }


}
