package toolforging.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ItemElement;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.gui.guidebook.GuidebookPage;
import net.minecraft.client.gui.guidebook.GuidebookSection;
import net.minecraft.client.gui.guidebook.SlotGuidebook;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.font.FontRenderer;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.input.Keyboard;
import toolforging.recipe.RecipeEntryReforging;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom 3-slot Recipe Booklet page layout for the Reforging Anvil:
 * Renders 5 recipes stacked vertically in a single clean column per page side.
 * Shifted baseX=40, baseY=28, 29px step (11px clear gap between row slots).
 * Item icons, smooth 1.5s cycling, and native mouse-following item tooltips.
 * Supports Ctrl key expansion for item descriptions matching native BTA recipe booklet pages.
 */
public class GuidebookPageReforging extends GuidebookPage {

    public final List<SlotGuidebook> pageSlots = new ArrayList<>();
    private final List<RecipeEntryReforging> recipes;
    private final ItemElement itemElement;
    private final TooltipElement tooltipElement;
    private int ticks = 0;

    public GuidebookPageReforging(GuidebookSection section, List<RecipeEntryReforging> recipes) {
        super(section);
        this.recipes = recipes;
        Minecraft mc = Minecraft.getMinecraft();
        this.itemElement = new ItemElement(mc);
        this.tooltipElement = new TooltipElement(mc);
        initSlots();
    }

    private void initSlots() {
        pageSlots.clear();
        for (int i = 0; i < recipes.size(); i++) {
            RecipeEntryReforging recipe = recipes.get(i);
            List<RecipeSymbol> inputs = recipe.getInput();
            RecipeSymbol toolInput = (inputs != null && inputs.size() > 0) ? inputs.get(0) : null;
            RecipeSymbol matInput = (inputs != null && inputs.size() > 1) ? inputs.get(1) : null;
            RecipeSymbol outputSymbol = new RecipeSymbol(recipe.getOutput());

            int baseX = 40;
            int baseY = 28 + (i * 29); // 29px step (11px clear vertical gap between row slots)

            // Slot 0: Input Tool
            pageSlots.add(new SlotGuidebook(i * 3 + 0, baseX, baseY, toolInput, false, recipe));
            // Slot 1: Input Material
            pageSlots.add(new SlotGuidebook(i * 3 + 1, baseX + 27, baseY, matInput, false, recipe));
            // Slot 2: Reforged Tool Result (+4px padding for arrow)
            pageSlots.add(new SlotGuidebook(i * 3 + 2, baseX + 61, baseY, outputSymbol, true, recipe));
        }
    }

    @Override
    public void onTick() {
        super.onTick();
        ticks++;
        // Cycle multi-item group symbols (e.g. Planks, Cobblestone) every 30 ticks (~1.5s)
        if (ticks % 30 == 0) {
            for (SlotGuidebook slot : pageSlots) {
                if (slot != null) {
                    slot.showRandomItem();
                }
            }
        }
    }

    @Override
    public void renderBackground(TextureManager renderEngine, int x, int y) {
        super.renderBackground(renderEngine, x, y);
        // Draw 18x18 slot background frames using native guidebook slot texture
        for (SlotGuidebook slot : pageSlots) {
            drawGuiIcon(x + slot.x - 1, y + slot.y - 1, 18, 18, TextureRegistry.getTexture("minecraft:gui/screen/guidebook/slot"));
        }
    }

    @Override
    public void renderForeground(TextureManager renderEngine, FontRenderer fontRenderer, int x, int y, int mouseX, int mouseY, float partialTicks) {
        drawStringCenteredNoShadow(fontRenderer, "Reforging Anvil", x + 80, y + 14, 0x404040);

        for (int i = 0; i < recipes.size(); i++) {
            int baseX = 40;
            int baseY = 28 + (i * 29);

            drawStringCenteredNoShadow(fontRenderer, "+", x + baseX + 22, y + baseY + 4, 0x606060);
            drawStringCenteredNoShadow(fontRenderer, "->", x + baseX + 52, y + baseY + 4, 0x606060);
        }

        // Render item icons inside each slot
        for (SlotGuidebook slot : pageSlots) {
            if (slot != null) {
                ItemStack stack = slot.getItemStack();
                if (stack != null) {
                    int slotX = x + slot.x;
                    int slotY = y + slot.y;
                    boolean isMouseOver = (mouseX >= slotX && mouseX < slotX + 18 && mouseY >= slotY && mouseY < slotY + 18);
                    itemElement.render(stack, slotX, slotY, isMouseOver, slot);
                }
            }
        }
    }

    @Override
    public void renderOverlay(TextureManager renderEngine, FontRenderer fontRenderer, int x, int y, int mouseX, int mouseY, float partialTicks) {
        super.renderOverlay(renderEngine, fontRenderer, x, y, mouseX, mouseY, partialTicks);

        SlotGuidebook hoveredSlot = null;
        for (SlotGuidebook slot : pageSlots) {
            if (slot != null && slot.getItemStack() != null) {
                int slotX = x + slot.x;
                int slotY = y + slot.y;
                if (mouseX >= slotX && mouseX < slotX + 18 && mouseY >= slotY && mouseY < slotY + 18) {
                    hoveredSlot = slot;
                    break;
                }
            }
        }

        if (hoveredSlot != null && hoveredSlot.getItemStack() != null) {
            // Check if Left or Right Ctrl key is down to expand item details (matching native BTA recipe booklet)
            boolean showDescription = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
            String tooltipText = tooltipElement.getTooltipText(hoveredSlot.getItemStack(), showDescription, hoveredSlot);
            if (tooltipText != null && !tooltipText.isEmpty()) {
                // Pass (mouseX, mouseY, 8, 8) so the tooltip box renders directly next to the mouse cursor
                tooltipElement.render(tooltipText, mouseX, mouseY, 8, 8);
            }
        }
    }
}
