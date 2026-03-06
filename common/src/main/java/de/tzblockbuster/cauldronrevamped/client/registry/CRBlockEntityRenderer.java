package de.tzblockbuster.cauldronrevamped.client.registry;

import de.tzblockbuster.cauldronrevamped.client.renderer.blockentity.BrewingCauldronBlockEntityRenderer;
import de.tzblockbuster.cauldronrevamped.registry.CRBlockEntities;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.function.Supplier;

public class CRBlockEntityRenderer {

    public static final ArrayList<BlockEntityRendererEntry<?, ?, ?>> BLOCK_ENTITY_RENDERERS = new ArrayList<>();

    static {
        BLOCK_ENTITY_RENDERERS.add(new BlockEntityRendererEntry<>(CRBlockEntities.brewingCauldronBlockEntity, BrewingCauldronBlockEntityRenderer::new));
    }


    public record BlockEntityRendererEntry<T extends BlockEntityType<S>, S extends BlockEntity, RS extends BlockEntityRenderState>(
            Supplier<T> blockEntity,
            BlockEntityRendererProvider<S, RS> blockEntityRenderer
    ) {
    }
}
