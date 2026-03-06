package de.tzblockbuster.cauldronrevamped.client.render_state.blockentity;

import de.tzblockbuster.cauldronrevamped.client.models.BrewingCauldronContentModel;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

public class BrewingCauldronRenderState extends BlockEntityRenderState {

    public BrewingCauldronContentModel.BrewingCauldronContentModelState modelState;

    public BrewingCauldronRenderState() {
        super();
        this.modelState = new BrewingCauldronContentModel.BrewingCauldronContentModelState(0, 0);
    }

}
