package toolforging;

import turniplabs.halplibe.util.ModelEntrypoint;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.EntityRendererDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;

import net.minecraft.client.gui.guidebook.GuidebookSections;
import toolforging.client.GuidebookSectionReforging;

public class ToolForgingClient implements ModelEntrypoint, GameStartEntrypoint, RecipeEntrypoint {

    public static GuidebookSectionReforging SECTION_REFORGING;

    @Override
    public void beforeGameStart() {
    }

    @Override
    public void afterGameStart() {
        if (SECTION_REFORGING == null) {
            SECTION_REFORGING = GuidebookSections.register(new GuidebookSectionReforging());
        }
    }

    @Override
    public void onRecipesReady() {
    }

    @Override
    public void initNamespaces() {
    }

    @Override
    public void initBlockModels(BlockModelDispatcher dispatcher) {
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
