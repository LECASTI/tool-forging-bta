package toolforging.inventory;

import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.InventorySorter;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.entity.player.Player;

public class MenuReforgingAnvil extends MenuAbstract {

    private Container inventory;
    private Container outputInventory;

    public MenuReforgingAnvil(ContainerInventory inventoryPlayer, Container customInventory) {
        this.inventory = customInventory;
        this.outputInventory = new net.minecraft.core.player.inventory.container.ContainerResult();
        
        // Slot 0: Input tool
        this.addSlot(new Slot(inventory, 0, 56, 17));
        
        // Slot 1: Material
        this.addSlot(new Slot(inventory, 1, 56, 53));
        
        // Slot 2: Output/Preview
        this.addSlot(new toolforging.inventory.SlotReforgingOutput(inventory, outputInventory, 2, 116, 35));
        
        // Player inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(inventoryPlayer, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        
        // Player hotbar
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(inventoryPlayer, col, 8 + col * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true; // We don't have a specific block entity to check distance against yet, so returning true is fine for now
    }

    @Override
    public it.unimi.dsi.fastutil.ints.IntList getMoveSlots(net.minecraft.core.InventoryAction action, Slot slot, int target, Player player) {
        if (slot.index >= 0 && slot.index < 3) {
            return this.getSlots(0, 3, false);
        }
        if (action == net.minecraft.core.InventoryAction.MOVE_ALL) {
            if (slot.index >= 3 && slot.index < 30) {
                return this.getSlots(3, 27, false);
            }
            if (slot.index >= 30) {
                return this.getSlots(30, 9, false);
            }
        }
        if (action == net.minecraft.core.InventoryAction.MOVE_SIMILAR && slot.index >= 3) {
            return this.getSlots(3, 36, false);
        }
        return null;
    }

    @Override
    public it.unimi.dsi.fastutil.ints.IntList getTargetSlots(net.minecraft.core.InventoryAction action, Slot slot, int target, Player player) {
        if (slot.index >= 0 && slot.index < 3) {
            return this.getSlots(3, 36, false);
        }
        return this.getSlots(0, 3, false);
    }
    
    @Override
    public void slotsChanged(Container inventory) {
        if (inventory == this.inventory) {
            this.updateReforgeOutput();
        }
        super.slotsChanged(inventory);
    }

    private void updateReforgeOutput() {
        net.minecraft.core.item.ItemStack tool = this.inventory.getItem(0);
        net.minecraft.core.item.ItemStack material = this.inventory.getItem(1);
        
        // Validate tool and material
        if (tool != null && tool.getItem() instanceof net.minecraft.core.item.tool.ItemTool && 
            material != null && material.itemID == net.minecraft.core.item.Items.INGOT_GOLD.id) {
            
            net.minecraft.core.item.ItemStack output = tool.copy();
            output.stackSize = 1;
            
            // Mark it as a preview visually if needed, but for now we just show the output.
            // When taken, the real tier is randomized.
            this.outputInventory.setItem(0, output);
        } else {
            this.outputInventory.setItem(0, null);
        }
    }

    @Override
    public void onCraftGuiClosed(Player player) {
        super.onCraftGuiClosed(player);
        if (!player.world.isClientSide) {
            for (int i = 0; i < this.inventory.getContainerSize(); ++i) {
                net.minecraft.core.item.ItemStack stack = this.inventory.removeItem(i, this.inventory.getItem(i) != null ? this.inventory.getItem(i).stackSize : 0);
                if (stack != null) {
                    player.dropPlayerItem(stack);
                }
            }
        }
    }
}
