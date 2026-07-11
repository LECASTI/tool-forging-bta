package toolforging.inventory;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerSimple;
import net.minecraft.core.player.inventory.menu.MenuAbstract;

public class ContainerReforging extends ContainerSimple {
    private MenuAbstract eventHandler;

    public ContainerReforging(String s, int i) {
        super(s, i);
    }
    
    public void setMenu(MenuAbstract menu) {
        this.eventHandler = menu;
    }

    @Override
    public void setItem(int i, ItemStack itemstack) {
        super.setItem(i, itemstack);
        if (this.eventHandler != null) {
            this.eventHandler.slotsChanged(this);
        }
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        ItemStack itemstack = super.removeItem(i, j);
        if (this.eventHandler != null) {
            this.eventHandler.slotsChanged(this);
        }
        return itemstack;
    }
}
