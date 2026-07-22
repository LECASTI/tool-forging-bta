package toolforging.inventory;

import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;

public class MenuReforgingAnvil extends MenuAbstract {

    private static final RecipeSymbol PLANKS_GROUP = new RecipeSymbol("minecraft:planks");
    private static final RecipeSymbol COBBLESTONES_GROUP = new RecipeSymbol("minecraft:cobblestones");

    private Container inventory;
    private net.minecraft.core.player.inventory.container.ContainerResult outputInventory;
    private net.minecraft.core.item.ItemStack lastTool = null;
    private net.minecraft.core.item.ItemStack lastMaterial = null;

    public MenuReforgingAnvil(net.minecraft.core.player.inventory.container.ContainerInventory inventoryPlayer, Container customInventory) {
        this.inventory = customInventory;
        this.outputInventory = new net.minecraft.core.player.inventory.container.ContainerResult();
        
        // Slot 0: Input tool
        this.addSlot(new net.minecraft.core.player.inventory.slot.Slot(inventory, 0, 56, 17) {
            @Override
            public boolean mayPlace(net.minecraft.core.item.ItemStack stack) {
                return isValidTool(stack);
            }
        });
        
        // Slot 1: Material
        this.addSlot(new net.minecraft.core.player.inventory.slot.Slot(inventory, 1, 56, 53) {
            @Override
            public boolean mayPlace(net.minecraft.core.item.ItemStack stack) {
                if (stack == null || stack.getItem() == null) return false;
                int id = stack.itemID;
                return PLANKS_GROUP.matches(stack) ||
                       COBBLESTONES_GROUP.matches(stack) ||
                       id == net.minecraft.core.item.Items.INGOT_IRON.id ||
                       id == net.minecraft.core.item.Items.INGOT_GOLD.id ||
                       id == net.minecraft.core.item.Items.DIAMOND.id ||
                       id == net.minecraft.core.item.Items.INGOT_STEEL.id ||
                       id == net.minecraft.core.item.Items.STRING.id;
            }
        });
        
        // Slot 2: Output/Preview
        this.addSlot(new toolforging.inventory.SlotReforgingOutput(inventory, outputInventory, 0, 116, 35));
        
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

        // Populate the output slot immediately if valid items are already present
        this.updateReforgeOutput();
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    @Override
    public it.unimi.dsi.fastutil.ints.IntList getMoveSlots(net.minecraft.core.InventoryAction action, Slot slot, int target, Player player) {
        if (slot.index >= 0 && slot.index < 3) {
            return this.getSlots(3, 36, false);
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
        return this.getSlots(0, 2, false);
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

        if (tool != null && material != null) {
            if (isValidMaterialForTool(tool, material)) {
                net.minecraft.core.item.ItemStack output = tool.copy();
                output.stackSize = 1;
                com.mojang.nbt.tags.CompoundTag data = output.getData();
                if (data == null) {
                    data = new com.mojang.nbt.tags.CompoundTag();
                    output.setData(data);
                }
                data.putByte("toolforging:preview", (byte) 1);
                this.outputInventory.setItem(0, output);
                return;
            }
        }
        this.outputInventory.setItem(0, null);
    }

    private boolean isValidTool(net.minecraft.core.item.ItemStack tool) {
        if (tool == null || tool.getItem() == null) return false;
        String name = tool.getItem().getKey().toLowerCase();
        return tool.getItem() instanceof net.minecraft.core.item.tool.ItemTool 
            || tool.getItem() instanceof net.minecraft.core.item.tool.ItemToolSword 
            || name.contains("bow") 
            || name.contains("handcannon") 
            || name.contains("hand_cannon")
            || name.contains("fishing");
    }

    private boolean isValidMaterialForTool(net.minecraft.core.item.ItemStack tool, net.minecraft.core.item.ItemStack material) {
        if (!isValidTool(tool) || material == null || material.getItem() == null) return false;
        
        String toolName = tool.getItem().getKey().toLowerCase();
        String matName = material.getItem().getKey().toLowerCase();
        int matId = material.itemID;

        if (toolName.contains("wood")) {
            return matId == net.minecraft.core.block.Blocks.PLANKS_OAK.id() || PLANKS_GROUP.matches(material);
        }
        if (toolName.contains("stone")) {
            return COBBLESTONES_GROUP.matches(material);
        }
        if (toolName.contains("iron")) return matId == net.minecraft.core.item.Items.INGOT_IRON.id;
        if (toolName.contains("gold")) return matId == net.minecraft.core.item.Items.INGOT_GOLD.id;
        if (toolName.contains("diamond")) return matId == net.minecraft.core.item.Items.DIAMOND.id;
        if (toolName.contains("steel")) return matId == net.minecraft.core.item.Items.INGOT_STEEL.id;
        if (toolName.contains("bow") || toolName.contains("fishing")) return matId == net.minecraft.core.item.Items.STRING.id;
        if (toolName.contains("handcannon") || toolName.contains("hand_cannon")) return matId == net.minecraft.core.item.Items.INGOT_STEEL.id;
        
        return false;
    }



    @Override
    public void broadcastChanges() {
        net.minecraft.core.item.ItemStack currentTool = this.inventory.getItem(0);
        net.minecraft.core.item.ItemStack currentMaterial = this.inventory.getItem(1);

        boolean toolChanged = !net.minecraft.core.item.ItemStack.areItemStacksEqual(currentTool, lastTool);
        boolean materialChanged = !net.minecraft.core.item.ItemStack.areItemStacksEqual(currentMaterial, lastMaterial);

        if (toolChanged || materialChanged) {
            this.lastTool = currentTool == null ? null : currentTool.copy();
            this.lastMaterial = currentMaterial == null ? null : currentMaterial.copy();
            this.updateReforgeOutput();
        }

        super.broadcastChanges();
    }

    @Override
    public void onCraftGuiClosed(Player player) {
        super.onCraftGuiClosed(player);
        if (!player.world.isClientSide) {
            this.outputInventory.setItem(0, null);
        }
    }
}
