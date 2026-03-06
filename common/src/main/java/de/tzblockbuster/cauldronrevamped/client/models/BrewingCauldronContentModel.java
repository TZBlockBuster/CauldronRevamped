package de.tzblockbuster.cauldronrevamped.client.models;

import de.tzblockbuster.cauldronrevamped.CauldronRevamped;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Set;

public class BrewingCauldronContentModel extends Model<BrewingCauldronContentModel.BrewingCauldronContentModelState> {

    public static ModelLayerLocation BrewingCauldronContentModel = new ModelLayerLocation(Identifier.fromNamespaceAndPath(CauldronRevamped.MOD_ID, "brewing_cauldron_content"), "bb_main");

    public BrewingCauldronContentModel(ModelPart modelPart) {
        super(modelPart, identifier -> RenderTypes.solidMovingBlock());
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("fluid", CubeListBuilder.create().texOffs(-20, 0).addBox(2f, 6f, 2f, 12f, 0f, 12f, Set.of(Direction.UP)), PartPose.ZERO);
        return LayerDefinition.create(meshDefinition, 16, 16);
    }

    public static LayerDefinition getTexturedModelDataDebug() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild("fluid", CubeListBuilder.create().texOffs(0, 0).addBox(2f, 6f, 2f, 12f, 0f, 12f, Set.of(Direction.UP)), PartPose.ZERO);
        return LayerDefinition.create(meshDefinition, 1, 1);
    }

    @Override
    public void setupAnim(BrewingCauldronContentModelState brewingCauldronContentModelState) {
        super.setupAnim(brewingCauldronContentModelState);
        this.root().getChild("fluid").y = brewingCauldronContentModelState.fluidLevel() * 3.0f;
    }

    public record BrewingCauldronContentModelState(
            float fluidLevel,
            int color
    ) {
    }
}
