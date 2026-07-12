package toolforging.client;

import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.client.render.renderer.GLRenderer;
import toolforging.inventory.MenuReforgingAnvil;

public class GuiReforgingAnvil extends ScreenContainerAbstract {

    public GuiReforgingAnvil(MenuReforgingAnvil menu) {
        super(menu);
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        this.drawStringNoShadow(this.fontRenderer, "Reforging Anvil", 60, 6, 0x404040);
        this.drawStringNoShadow(this.fontRenderer, "Inventory", 8, this.ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int textureId = this.mc.textureManager.loadTexture("/assets/toolforging/gui/reforging_anvil_GUI.png").id();
        this.mc.textureManager.bindTexture(textureId);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }
}
