package toolforging.inventory;

import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.item.ItemStack;
import java.util.concurrent.ThreadLocalRandom;
import toolforging.block.entity.TileEntityReforgingAnvil;

public class SlotReforgingOutput extends Slot {

    private final Container forgeInventory;

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
        int tier = java.util.concurrent.ThreadLocalRandom.current().nextInt(7) - 3;
        com.mojang.nbt.tags.CompoundTag data = stack.getData();
        if (data == null) {
            data = new com.mojang.nbt.tags.CompoundTag();
            stack.setData(data);
        }
        data.putInt("toolforging:tier", tier);
        // Zero the preview flag rather than removing it — CompoundTag's remove API
        // varies across BTA versions; putByte(0) is safe and the mixin checks != 0.
        data.putByte("toolforging:preview", (byte) 0);

        // Consume inputs
        this.forgeInventory.removeItem(0, 1);
        this.forgeInventory.removeItem(1, 1);

        // Play sound at the anvil block position if we can reach the world
        // ponytail: cast to TileEntity to get worldObj — safe because forgeInventory IS
        // always a TileEntityReforgingAnvil on the server side (client dummy never calls onTake).
        if (forgeInventory instanceof TileEntityReforgingAnvil) {
            TileEntityReforgingAnvil te = (TileEntityReforgingAnvil) forgeInventory;
            if (te.worldObj != null) {
                te.worldObj.playSoundEffect(
                    null,
                    net.minecraft.core.sound.SoundCategory.WORLD_SOUNDS,
                    te.tilePos.x() + 0.5,
                    te.tilePos.y() + 0.5,
                    te.tilePos.z() + 0.5,
                    "ui.crafting_made",
                    1.0f,
                    1.0f
                );
            }
        }

        super.onTake(stack);
    }
}
