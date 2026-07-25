package toolforging.recipe;

import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeNamespace;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import com.mojang.nbt.tags.CompoundTag;
import toolforging.ToolForgingMod;

import java.util.ArrayList;
import java.util.List;

/**
 * Registers Reforging Anvil recipe entries and group in BTA 8.0's native Recipe Booklet.
 * Ordered strictly by tier potency (Wood -> Stone -> Iron -> Gold -> Diamond -> Steel -> Misc)
 * and within tiers by tool type (Sword -> Pickaxe -> Axe -> Shovel -> Hoe).
 * Uses %03d key formatting so numerical and string ordering match 100%.
 */
public class ReforgingRecipeRegistry {

    public static void registerRecipes() {
        RecipeNamespace modNamespace = Registries.RECIPES.getItem(ToolForgingMod.MOD_ID);
        if (modNamespace == null) {
            modNamespace = new RecipeNamespace();
            Registries.RECIPES.register(ToolForgingMod.MOD_ID, modNamespace);
        }

        // Machine Symbol for Reforging Anvil category tab icon
        RecipeSymbol machineSymbol = new RecipeSymbol(new ItemStack(ToolForgingMod.reforgingAnvil));
        RecipeGroup reforgingGroup = new RecipeGroup(machineSymbol);
        modNamespace.register("reforging", reforgingGroup);

        int index = 0;

        // 1. Wood Tier (Planks) -> Sword, Pickaxe, Axe, Shovel, Hoe
        index = registerReforgeEntries(reforgingGroup, index, new Item[]{
            Items.TOOL_SWORD_WOOD, Items.TOOL_PICKAXE_WOOD, Items.TOOL_AXE_WOOD, Items.TOOL_SHOVEL_WOOD, Items.TOOL_HOE_WOOD
        }, new RecipeSymbol("minecraft:planks"));

        // 2. Stone Tier (Cobblestone) -> Sword, Pickaxe, Axe, Shovel, Hoe
        index = registerReforgeEntries(reforgingGroup, index, new Item[]{
            Items.TOOL_SWORD_STONE, Items.TOOL_PICKAXE_STONE, Items.TOOL_AXE_STONE, Items.TOOL_SHOVEL_STONE, Items.TOOL_HOE_STONE
        }, new RecipeSymbol("minecraft:cobblestones"));

        // 3. Iron Tier (Iron Ingot) -> Sword, Pickaxe, Axe, Shovel, Hoe
        index = registerReforgeEntries(reforgingGroup, index, new Item[]{
            Items.TOOL_SWORD_IRON, Items.TOOL_PICKAXE_IRON, Items.TOOL_AXE_IRON, Items.TOOL_SHOVEL_IRON, Items.TOOL_HOE_IRON
        }, new RecipeSymbol(new ItemStack(Items.INGOT_IRON)));

        // 4. Gold Tier (Gold Ingot) -> Sword, Pickaxe, Axe, Shovel, Hoe
        index = registerReforgeEntries(reforgingGroup, index, new Item[]{
            Items.TOOL_SWORD_GOLD, Items.TOOL_PICKAXE_GOLD, Items.TOOL_AXE_GOLD, Items.TOOL_SHOVEL_GOLD, Items.TOOL_HOE_GOLD
        }, new RecipeSymbol(new ItemStack(Items.INGOT_GOLD)));

        // 5. Diamond Tier (Diamond) -> Sword, Pickaxe, Axe, Shovel, Hoe
        index = registerReforgeEntries(reforgingGroup, index, new Item[]{
            Items.TOOL_SWORD_DIAMOND, Items.TOOL_PICKAXE_DIAMOND, Items.TOOL_AXE_DIAMOND, Items.TOOL_SHOVEL_DIAMOND, Items.TOOL_HOE_DIAMOND
        }, new RecipeSymbol(new ItemStack(Items.DIAMOND)));

        // 6. Steel Tier (Steel Ingot) -> Sword, Pickaxe, Axe, Shovel, Hoe
        index = registerReforgeEntries(reforgingGroup, index, new Item[]{
            Items.TOOL_SWORD_STEEL, Items.TOOL_PICKAXE_STEEL, Items.TOOL_AXE_STEEL, Items.TOOL_SHOVEL_STEEL, Items.TOOL_HOE_STEEL
        }, new RecipeSymbol(new ItemStack(Items.INGOT_STEEL)));

        // 7. Miscellaneous Tools Tier
        index = registerReforgeEntries(reforgingGroup, index, new Item[]{
            Items.HANDCANNON_UNLOADED
        }, new RecipeSymbol(new ItemStack(Items.INGOT_STEEL)));

        index = registerReforgeEntries(reforgingGroup, index, new Item[]{
            Items.TOOL_FISHINGROD, Items.TOOL_BOW
        }, new RecipeSymbol(new ItemStack(Items.STRING)));

        index = registerReforgeEntries(reforgingGroup, index, new Item[]{
            Items.TOOL_SHEARS, Items.TOOL_FIRESTRIKER_IRON
        }, new RecipeSymbol(new ItemStack(Items.INGOT_IRON)));

        index = registerReforgeEntries(reforgingGroup, index, new Item[]{
            Items.TOOL_SHEARS_STEEL, Items.TOOL_FIRESTRIKER_STEEL
        }, new RecipeSymbol(new ItemStack(Items.INGOT_STEEL)));
    }

    private static int registerReforgeEntries(RecipeGroup group, int startIndex, Item[] tools, RecipeSymbol materialSymbol) {
        for (Item tool : tools) {
            if (tool == null) continue;
            startIndex++;

            // Create tool preview output stack
            ItemStack outputStack = new ItemStack(tool);
            CompoundTag tag = outputStack.getData();
            if (tag == null) {
                tag = new CompoundTag();
                outputStack.setData(tag);
            }
            tag.putByte("toolforging:preview", (byte) 1);

            // 2 Input slots: [Input Tool] + [Input Material] -> [Reforged Tool Preview]
            List<RecipeSymbol> inputs = new ArrayList<>();
            inputs.add(new RecipeSymbol(new ItemStack(tool)));
            inputs.add(materialSymbol);

            group.register(String.format("reforge_%03d", startIndex), new RecipeEntryReforging(inputs, outputStack));
        }
        return startIndex;
    }
}
