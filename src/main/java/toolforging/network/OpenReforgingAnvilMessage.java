package toolforging.network;

import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;
import net.minecraft.core.entity.player.Player;
import net.minecraft.client.Minecraft;

public class OpenReforgingAnvilMessage implements NetworkMessage {
    private int windowId;

    public OpenReforgingAnvilMessage() {}
    public OpenReforgingAnvilMessage(int windowId) {
        this.windowId = windowId;
    }

    @Override
    public void encodeToUniversalPacket(UniversalPacket packet) {
        packet.writeInt(this.windowId);
    }

    @Override
    public void decodeFromUniversalPacket(UniversalPacket packet) {
        this.windowId = packet.readInt();
    }

    @Override
    public void handleClientEnv(NetworkContext context) {
        Player player = context.player;
        Minecraft mc = Minecraft.getMinecraft();
        
        // In single-player, onInteracted already set player.containerMenu to a
        // MenuReforgingAnvil backed by the real TileEntity. Reuse it.
        // In multiplayer, the server and client are different players, so we
        // need to create a client-side menu with a dummy container.
        toolforging.inventory.MenuReforgingAnvil menu;
        if (player.containerMenu instanceof toolforging.inventory.MenuReforgingAnvil) {
            menu = (toolforging.inventory.MenuReforgingAnvil) player.containerMenu;
        } else {
            net.minecraft.core.player.inventory.container.ContainerSimple anvilInventory =
                new net.minecraft.core.player.inventory.container.ContainerSimple("Reforging Anvil", 3);
            menu = new toolforging.inventory.MenuReforgingAnvil(player.inventory, anvilInventory);
        }
        menu.containerId = this.windowId;
        
        toolforging.client.GuiReforgingAnvil gui = new toolforging.client.GuiReforgingAnvil(menu);
        mc.displayScreen(gui);
        player.containerMenu = menu;
    }
}
