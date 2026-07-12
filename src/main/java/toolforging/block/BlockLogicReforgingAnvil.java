package toolforging.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import toolforging.ToolForgingMod;

public class BlockLogicReforgingAnvil extends BlockLogic {
    
    public BlockLogicReforgingAnvil(Block<?> block, Material material) {
        super(block, material);
    }
    @Override
    public boolean onInteracted(World world, TilePosc pos, Player player, Side side, double xHit, double yHit) {
        if (!world.isClientSide) {
            // Server side
            toolforging.block.entity.TileEntityReforgingAnvil anvilInventory = (toolforging.block.entity.TileEntityReforgingAnvil) world.getTileEntity(pos);
            if (anvilInventory == null) return true;
            
            ToolForgingMod.LOGGER.info("[ToolForging] Player class: {}", player.getClass().getName());
            ToolForgingMod.LOGGER.info("[ToolForging] TileEntity items: slot0={}, slot1={}", anvilInventory.getItem(0), anvilInventory.getItem(1));
            
            // Close the old container menu first (vanilla pattern)
            player.containerMenu.onCraftGuiClosed(player);
            
            // Create menu backed by the TileEntity
            toolforging.inventory.MenuReforgingAnvil menu = new toolforging.inventory.MenuReforgingAnvil(player.inventory, anvilInventory);
            
            // Get next window ID via reflection to avoid class loading issues on client
            int windowId = 1; // Fallback
            try {
                Class<?> clazz = player.getClass();
                while (clazz != null && !clazz.getName().equals("net.minecraft.server.entity.player.PlayerServer")) {
                    clazz = clazz.getSuperclass();
                }
                if (clazz != null) {
                    java.lang.reflect.Method m = clazz.getDeclaredMethod("getNextWindowId");
                    m.setAccessible(true);
                    m.invoke(player);
                    java.lang.reflect.Field f = clazz.getDeclaredField("currentWindowId");
                    f.setAccessible(true);
                    windowId = f.getInt(player);
                    ToolForgingMod.LOGGER.info("[ToolForging] Got windowId via reflection: {}", windowId);
                } else {
                    ToolForgingMod.LOGGER.warn("[ToolForging] Could not find PlayerServer class, using fallback windowId {}", windowId);
                }
            } catch (Exception e) {
                ToolForgingMod.LOGGER.error("[ToolForging] Reflection failed", e);
            }
            
            // Set the container on the player
            player.containerMenu = menu;
            menu.containerId = windowId; 
            
            // Send packet to client to open the GUI
            turniplabs.halplibe.helper.network.NetworkHandler.sendToPlayer(player, new toolforging.network.OpenReforgingAnvilMessage(windowId));

            // Add the player as a slot listener so server pushes slot updates to client
            if (player instanceof net.minecraft.core.crafting.ContainerListener) {
                menu.addSlotListener((net.minecraft.core.crafting.ContainerListener) player);
            }
        }
        return true;
    }
    
    @Override
    public void onRemoved(World world, TilePosc pos, int data) {
        toolforging.block.entity.TileEntityReforgingAnvil anvil = (toolforging.block.entity.TileEntityReforgingAnvil) world.getTileEntity(pos);
        if (anvil != null) {
            for (int i = 0; i < anvil.getContainerSize(); ++i) {
                net.minecraft.core.item.ItemStack stack = anvil.getItem(i);
                if (stack != null) {
                    world.dropItem(pos, stack);
                }
            }
        }
        super.onRemoved(world, pos, data);
    }
}
