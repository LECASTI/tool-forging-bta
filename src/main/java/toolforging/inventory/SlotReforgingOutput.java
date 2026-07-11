package toolforging.inventory;

import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.item.ItemStack;
import java.util.Random;

public class SlotReforgingOutput extends Slot {

    private final Container forgeInventory;
    private final Random random = new Random();

    public SlotReforgingOutput(Container forgeInventory, Container outputInventory, int slot, int x, int y) {
        super(outputInventory, slot, x, y);
        this.forgeInventory = forgeInventory;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public void onTake(ItemStack stack) {
        // Roll tier
        int tier = random.nextInt(7) - 3;
        stack.getData().putInt("toolforging:tier", tier);
        
        // Consume input and material
        this.forgeInventory.removeItem(0, 1);
        this.forgeInventory.removeItem(1, 1);
        
        super.onTake(stack);
    }
}
