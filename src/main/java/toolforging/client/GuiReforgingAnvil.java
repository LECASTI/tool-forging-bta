package toolforging.client;

import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.client.render.renderer.GLRenderer;
import toolforging.inventory.MenuReforgingAnvil;
import net.minecraft.client.gui.ButtonElement;

public class GuiReforgingAnvil extends ScreenContainerAbstract {

    private Container anvilInventory;

    public GuiReforgingAnvil(ContainerInventory inventoryPlayer, Container anvilInventory) {
        super(new MenuReforgingAnvil(inventoryPlayer, anvilInventory));
        this.anvilInventory = anvilInventory;
    }

    @Override
    public void init() {
        super.init();
        // Add our custom Reforge button
        // buttonId, x, y, width, height, text
        this.buttons.add(new ButtonElement(0, (this.width - this.xSize) / 2 + 10, (this.height - this.ySize) / 2 + 30, 40, 20, "Reforge"));
    }

    @Override
    protected void buttonClicked(ButtonElement button) {
        if (button.id == 0) {
            // Button is just decorative for now, as clicking output slot handles logic
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        this.drawStringNoShadow(this.fontRenderer, "Reforging Anvil", 60, 6, 0x404040);
        this.drawStringNoShadow(this.fontRenderer, "Inventory", 8, this.ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        // Use the furnace texture as a placeholder since it has 3 slots perfectly placed
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int textureId = this.mc.textureManager.loadTexture("/gui/furnace.png").id();
        this.mc.textureManager.bindTexture(textureId);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }
}
