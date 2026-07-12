package toolforging.network;

import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;
import turniplabs.halplibe.helper.network.NetworkMessage.NetworkContext;
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
        
        // We will create the GUI and set the current window ID so that the client and server match
        toolforging.client.GuiReforgingAnvil gui = new toolforging.client.GuiReforgingAnvil(
            player.inventory, 
            new net.minecraft.core.player.inventory.container.ContainerSimple("Reforging Anvil", 3)
        );
        mc.displayScreen(gui);
        player.containerMenu.containerId = this.windowId;
    }
}
