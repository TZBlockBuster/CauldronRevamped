package de.tzblockbuster.cauldronrevamped.fabric.client.registry;

import de.tzblockbuster.cauldronrevamped.client.registry.CRBlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CauldronRevampedBlockEntityRendererRegistry {


    public static void init() {
        for (CRBlockEntityRenderer.BlockEntityRendererEntry blockEntityRenderer : CRBlockEntityRenderer.BLOCK_ENTITY_RENDERERS) {
            BlockEntityRenderers.register((BlockEntityType<? extends BlockEntity>) blockEntityRenderer.blockEntity().get(), context -> blockEntityRenderer.blockEntityRenderer().create(context));
        }
    }

}
