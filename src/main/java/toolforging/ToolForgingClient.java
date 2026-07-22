package toolforging;

import turniplabs.halplibe.util.ModelEntrypoint;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.EntityRendererDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelStandard;

public class ToolForgingClient implements ModelEntrypoint {

    @Override
    public void initBlockModels(BlockModelDispatcher dispatcher) {
        // ponytail: Load model from json file and register as BlockModelGeneric
        dispatcher.addDispatch(
            ToolForgingMod.reforgingAnvil, 
            new net.minecraft.client.render.block.model.generic.BlockModelGeneric<>(
                ToolForgingMod.reforgingAnvil,
                net.minecraft.client.render.block.model.BlockModelDispatcher.loadDataModel("toolforging:block/reforging_anvil_block")
            )
        );
    }

    @Override
    public void initItemModels(ItemModelDispatcher dispatcher) {
    }

    @Override
    public void initEntityModels(EntityRendererDispatcher dispatcher) {
    }

    @Override
    public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {
    }

    @Override
    public void initBlockColors(BlockColorDispatcher dispatcher) {
    }
}
