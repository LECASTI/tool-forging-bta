package toolforging.recipe;

import net.minecraft.core.data.registry.recipe.RecipeEntryBase;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.item.ItemStack;

import java.util.List;

/**
 * Custom recipe entry for Reforging Anvil recipes.
 * Extends RecipeEntryBase (NOT RecipeEntryCrafting), preventing BTA's Crafting Guidebook section from grabbing these recipes.
 */
public class RecipeEntryReforging extends RecipeEntryBase<List<RecipeSymbol>, ItemStack, Void> {

    public RecipeEntryReforging(List<RecipeSymbol> input, ItemStack output) {
        super(input, output, null);
    }
}
