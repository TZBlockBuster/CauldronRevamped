package de.tzblockbuster.cauldronrevamped.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import de.tzblockbuster.cauldronrevamped.blocks.brewing_cauldron.BrewingCauldron;
import de.tzblockbuster.cauldronrevamped.blocks.brewing_cauldron.BrewingCauldronBlockEntity;
import de.tzblockbuster.cauldronrevamped.client.models.BrewingCauldronContentModel;
import de.tzblockbuster.cauldronrevamped.client.render_state.blockentity.BrewingCauldronRenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;

public class BrewingCauldronBlockEntityRenderer implements BlockEntityRenderer<BrewingCauldronBlockEntity, BrewingCauldronRenderState> {

    private final BrewingCauldronContentModel contentModel;
    private final TextureAtlasSprite waterStill;

    public BrewingCauldronBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        contentModel = new BrewingCauldronContentModel(context.bakeLayer(BrewingCauldronContentModel.BrewingCauldronContentModel));
        waterStill = Minecraft.getInstance().getAtlasManager().get(Sheets.BLOCKS_MAPPER.defaultNamespaceApply("water_still"));
    }

    @Override
    public BrewingCauldronRenderState createRenderState() {
        return new BrewingCauldronRenderState();
    }

    @Override
    public void extractRenderState(BrewingCauldronBlockEntity blockEntity, BrewingCauldronRenderState blockEntityRenderState, float f, @NonNull Vec3 vec3, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, blockEntityRenderState, f, vec3, crumblingOverlay);
        blockEntityRenderState.modelState = new BrewingCauldronContentModel.BrewingCauldronContentModelState(blockEntity.getFluidAnimationLevel(f), blockEntityRenderState.blockState.getValue(BrewingCauldron.BREWING) ? magicEffect() : blockEntity.getColor(blockEntity.potionFractions));
        blockEntityRenderState.brewingTime = blockEntity.brewingTime;
        blockEntityRenderState.potionFractions = blockEntity.potionFractions;
        blockEntityRenderState.isSneaking = Minecraft.getInstance().options.keyShift.isDown();
    }

    @Override
    public void submit(BrewingCauldronRenderState blockEntityRenderState, @NonNull PoseStack poseStack, @NonNull SubmitNodeCollector submitNodeCollector, @NonNull CameraRenderState cameraRenderState) {
        submitNodeCollector.submitModel(contentModel, blockEntityRenderState.modelState, poseStack, Sheets.translucentBlockItemSheet(), blockEntityRenderState.lightCoords, OverlayTexture.NO_OVERLAY, convertColorRgbToArgb(blockEntityRenderState.modelState.color()), waterStill, 0, blockEntityRenderState.breakProgress);
        if (blockEntityRenderState.isSneaking) {
            ArrayList<Component> tooltips = blockEntityRenderState.getTooltips();
            for (int i = 0; i < tooltips.size(); i++) {
                Component component = tooltips.get(i);
                submitNodeCollector.submitNameTag(poseStack, new Vec3(0.5, 1.1 + 0.25 * tooltips.size(), 0.5), (blockEntityRenderState.getTooltips().size() - i) * (Minecraft.getInstance().font.lineHeight + 3), component, true, blockEntityRenderState.lightCoords, cameraRenderState.blockPos.getCenter().distanceToSqr(blockEntityRenderState.blockPos.getCenter()), cameraRenderState);
            }
        }
    }

    public int computeColor(int red, int green, int blue) {
        return (red << 16) | (green << 8) | blue | 0xFF000000;
    }

    public int convertColorRgbToArgb(int colorRGB) {
        return (colorRGB & 0xFFFFFF) | 0xFF000000;
    }

    public int magicEffect() {
        return computeColor((int) (System.currentTimeMillis() % 255), 255 - (int) (System.currentTimeMillis() % 255), 255 - (int) (System.currentTimeMillis() % 128));
    }


}
