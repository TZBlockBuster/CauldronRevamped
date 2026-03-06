package de.tzblockbuster.cauldronrevamped.neoforge.client.registry;

import de.tzblockbuster.cauldronrevamped.client.registry.CRBlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class CauldronRevampedEntityRendererRegistry {

    @SubscribeEvent
    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        for(CRBlockEntityRenderer.BlockEntityRendererEntry blockEntityRenderer : CRBlockEntityRenderer.BLOCK_ENTITY_RENDERERS) {
            event.registerBlockEntityRenderer((BlockEntityType<? extends BlockEntity>) blockEntityRenderer.blockEntity().get(), context -> blockEntityRenderer.blockEntityRenderer().create(context));
        }
    }
}
