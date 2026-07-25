package toolforging.client;

import net.minecraft.client.gui.guidebook.GuidebookPage;
import net.minecraft.client.gui.guidebook.GuidebookSection;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeNamespace;
import net.minecraft.core.item.ItemStack;
import toolforging.ToolForgingMod;
import toolforging.recipe.RecipeEntryReforging;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom section bookmark tab for the Reforging Anvil on the right side of the Recipe Booklet.
 * Placed at the very end of the Recipe Booklet tabs (after Creatures/Mobs).
 */
public class GuidebookSectionReforging extends GuidebookSection {

    private final List<GuidebookPage> pages = new ArrayList<>();
    private final List<GuidebookSection.Index> indices = new ArrayList<>();

    public GuidebookSectionReforging() {
        super("guidebook.section.reforging", null, 0x383838, 0xFFFFFF);
    }

    @Override
    public ItemStack getTabIcon() {
        return new ItemStack(ToolForgingMod.reforgingAnvil);
    }

    private void initPages() {
        pages.clear();
        RecipeNamespace modNs = Registries.RECIPES.getItem(ToolForgingMod.MOD_ID);
        if (modNs == null) return;
        RecipeGroup reforgingGroup = modNs.getItem("reforging");
        if (reforgingGroup == null) return;

        List<RecipeEntryReforging> recipes = new ArrayList<>();
        for (Object entry : reforgingGroup.getAllRecipes()) {
            if (entry instanceof RecipeEntryReforging) {
                recipes.add((RecipeEntryReforging) entry);
            }
        }

        // Add 5 recipes per page side (1 clean column of 5 recipes per page)
        for (int i = 0; i < recipes.size(); i += 5) {
            List<RecipeEntryReforging> pageRecipes = recipes.subList(i, Math.min(i + 5, recipes.size()));
            pages.add(new GuidebookPageReforging(this, pageRecipes));
        }
    }

    @Override
    public List<GuidebookPage> getPages() {
        if (pages.isEmpty()) {
            initPages();
        }
        return pages;
    }

    @Override
    public List<GuidebookSection.Index> getIndices() {
        return indices;
    }
}
