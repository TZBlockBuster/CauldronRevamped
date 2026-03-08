package de.tzblockbuster.cauldronrevamped.network;

import de.tzblockbuster.cauldronrevamped.CauldronRevamped;
import de.tzblockbuster.cauldronrevamped.blocks.brewing_cauldron.BrewingCauldronBlockEntity;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;

public record BrewingCauldronSyncPacket(BlockPos pos,
                                        ArrayList<BrewingCauldronBlockEntity.PotionFraction> potionFractions,
                                        int brewingTime,
                                        Optional<ItemStack> ingredient) implements CustomPacketPayload {

    public static Identifier identifier = Identifier.fromNamespaceAndPath(CauldronRevamped.MOD_ID, "brewing_cauldron_sync");

    public BrewingCauldronSyncPacket(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), ((Supplier<ArrayList<BrewingCauldronBlockEntity.PotionFraction>>) () -> {
            int potionFractionCount = buf.readInt();
            ArrayList<BrewingCauldronBlockEntity.PotionFraction> potionFractions = new ArrayList<>();
            for (int i = 0; i < potionFractionCount; i++) {
                Identifier potionId = buf.readIdentifier();
                Holder<Potion> potionHolder = BuiltInRegistries.POTION.wrapAsHolder(BuiltInRegistries.POTION.get(potionId).get().value());
                float fraction = buf.readFloat();
                potionFractions.add(new BrewingCauldronBlockEntity.PotionFraction(potionHolder, fraction));
            }
            return potionFractions;
        }).get(), buf.readInt(), buf.readBoolean() ? Optional.of(new ItemStack(BuiltInRegistries.ITEM.get(buf.readIdentifier()).orElseThrow().value())) : Optional.empty());
    }

    public BrewingCauldronSyncPacket(BlockPos pos, ArrayList<BrewingCauldronBlockEntity.PotionFraction> potionFractions, int brewingTime, Optional<ItemStack> ingredient) {
        this.pos = pos;
        this.potionFractions = potionFractions;
        this.brewingTime = brewingTime;
        this.ingredient = ingredient;
    }

    public FriendlyByteBuf encode() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeBlockPos(this.pos);
        buf.writeInt(potionFractions.size());
        for (BrewingCauldronBlockEntity.PotionFraction potionFraction : potionFractions) {
            buf.writeIdentifier(potionFraction.potion().unwrapKey().orElseThrow().identifier());
            buf.writeFloat(potionFraction.fraction());
        }
        buf.writeInt(brewingTime);
        buf.writeBoolean(ingredient.isPresent());
        ingredient.ifPresent(itemStack -> buf.writeIdentifier(BuiltInRegistries.ITEM.getKey(itemStack.getItem())));
        return buf;
    }

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return new Type<>(identifier);
    }

    public static Type<BrewingCauldronSyncPacket> identifier() {
        return new Type<>(identifier);
    }

    public static StreamCodec<FriendlyByteBuf, BrewingCauldronSyncPacket> STREAM_CODEC =
            StreamCodec.of((object, object2) -> {
                object.writeBytes(object2.encode());
            }, BrewingCauldronSyncPacket::new);
}
