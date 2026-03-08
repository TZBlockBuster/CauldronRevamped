package de.tzblockbuster.cauldronrevamped.network;

import de.tzblockbuster.cauldronrevamped.blocks.brewing_cauldron.BrewingCauldronBlockEntity;
import dev.architectury.networking.NetworkManager;
import net.minecraft.server.level.ServerPlayer;

public class CRNetworkManager {


    public static void register() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, BrewingCauldronSyncPacket.identifier(), BrewingCauldronSyncPacket.STREAM_CODEC, CRNetworkManager::handleBrewingCauldronSyncPacket);
    }

    public static void sendBrewingCauldronSyncPacket(BrewingCauldronBlockEntity blockEntity) {
        if (blockEntity.getLevel() != null) {
            if (!blockEntity.getLevel().isClientSide()) {
                blockEntity.getLevel().players().stream().map(player -> (ServerPlayer) player).filter(serverPlayer -> serverPlayer.blockPosition().getCenter().distanceTo(blockEntity.getBlockPos().getCenter()) < 128).forEach(player -> NetworkManager.sendToPlayer(player, new BrewingCauldronSyncPacket(blockEntity.getBlockPos(), blockEntity.potionFractions, blockEntity.brewingTime, blockEntity.ingredient)));
            }
        }
    }

    private static void handleBrewingCauldronSyncPacket(BrewingCauldronSyncPacket brewingCauldronSyncPacket, NetworkManager.PacketContext packetContext) {
        packetContext.queue(() -> {
            // Check if the block entity is still there and of the correct type
            if (packetContext.getPlayer().level().getBlockEntity(brewingCauldronSyncPacket.pos()) instanceof BrewingCauldronBlockEntity blockEntity) {
                blockEntity.potionFractions = brewingCauldronSyncPacket.potionFractions();
                blockEntity.brewingTime = brewingCauldronSyncPacket.brewingTime();
                blockEntity.ingredient = brewingCauldronSyncPacket.ingredient();
            }
        });
    }


}
