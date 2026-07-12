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
            net.minecraft.core.player.inventory.container.Container anvilInventory = new net.minecraft.core.player.inventory.container.ContainerSimple("Reforging Anvil", 3);
            toolforging.inventory.MenuReforgingAnvil menu = new toolforging.inventory.MenuReforgingAnvil(player.inventory, anvilInventory);
            
            // Set the container on the player
            player.containerMenu = menu;
            // A fake window ID (needs to match what client receives)
            // Wait, does player have getInventoryWindowId or something similar?
            // Actually we can just generate a random ID from 1 to 100 or check if there is an incrementWindowID() method in BTA's PlayerServer / Player class.
            // BTA's Player does not have incrementWindowID(). Wait, let's use a static counter or 70+
            player.containerMenu.containerId = 75; 
            
            // Dispatch packet to client
            turniplabs.halplibe.helper.network.NetworkHandler.sendToPlayer(player, new toolforging.network.OpenReforgingAnvilMessage(75));
        }
        return true;
    }
}
