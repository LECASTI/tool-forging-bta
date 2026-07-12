package toolforging.inventory;

import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.tool.ItemTool;
import toolforging.mixin.ItemStackMixin;
import java.util.Random;

public class SlotReforgingOutput extends Slot {

    private final Container forgeInventory;
    private final Container outputInventory;
    private final Random random = new Random();

    public SlotReforgingOutput(Container forgeInventory, Container outputInventory, int slot, int x, int y) {
        // We pass outputInventory to super because this slot belongs to the output container!
        // But the slot index in outputInventory is 0, not 2.
        super(outputInventory, 0, x, y);
        this.forgeInventory = forgeInventory;
        this.outputInventory = outputInventory;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public void onTake(ItemStack stack) {
        // The player just took the item from the output slot.
        // We apply the random tier to the stack they took.
        
        // Random tier between -3 and +3
        // We have tiers: Dreadful(-3), Bad(-2), Poor(-1), Normal(0), Good(1), Great(2), Perfect(3)
        // Let's weight it so 0 is most common? For now, just purely random from -3 to 3.
        int tier = random.nextInt(7) - 3;
        
        // If it somehow rolled 0, we could make it 1 just to ensure it actually changed,
        // or just let it be 0. Let's just let it be random.
        if (stack.getData().containsKey("Tier")) {
            stack.getData().putInt("Tier", tier);
        } else {
            stack.getData().putInt("Tier", tier);
        }
        
        // Consume input and material
        this.forgeInventory.removeItem(0, 1);
        this.forgeInventory.removeItem(1, 1);
        
        super.onTake(stack);
    }
}
