package toolforging.block.entity;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.entity.player.Player;
import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;

public class TileEntityReforgingAnvil extends TileEntity implements Container {
    private ItemStack[] inventory = new ItemStack[2];

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public ItemStack getItem(int i) {
        if (i >= 0 && i < this.inventory.length) {
            return this.inventory[i];
        }
        return null;
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        if (i >= 0 && i < this.inventory.length && this.inventory[i] != null) {
            if (this.inventory[i].stackSize <= j) {
                ItemStack itemstack = this.inventory[i];
                this.inventory[i] = null;
                this.setChanged();
                return itemstack;
            } else {
                ItemStack itemstack1 = this.inventory[i].splitStack(j);
                if (this.inventory[i].stackSize == 0) {
                    this.inventory[i] = null;
                }
                this.setChanged();
                return itemstack1;
            }
        }
        return null;
    }

    @Override
    public void setItem(int i, ItemStack itemstack) {
        if (i >= 0 && i < this.inventory.length) {
            this.inventory[i] = itemstack;
            if (itemstack != null && itemstack.stackSize > this.getMaxStackSize()) {
                itemstack.stackSize = this.getMaxStackSize();
            }
            this.setChanged();
        }
    }

    @Override
    public String getNameTranslationKey() {
        return "Reforging Anvil";
    }

    @Override
    public void readAdditionalData(CompoundTag tag) {
        // super.readAdditionalData(tag); // Abstract in base class
        ListTag list = tag.getList("Items");
        this.inventory = new ItemStack[2];
        for (int i = 0; i < list.tagCount(); ++i) {
            CompoundTag itemTag = (CompoundTag) list.tagAt(i);
            byte slot = itemTag.getByte("Slot");
            if (slot >= 0 && slot < this.inventory.length) {
                this.inventory[slot] = ItemStack.readItemStackFromNbt(itemTag);
            }
        }
    }

    @Override
    public void writeAdditionalData(CompoundTag tag) {
        // super.writeAdditionalData(tag); // Abstract in base class
        ListTag list = new ListTag();
        for (int i = 0; i < this.inventory.length; ++i) {
            if (this.inventory[i] != null) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(itemTag);
                list.addTag(itemTag);
            }
        }
        tag.putList("Items", list);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.worldObj.getTileEntity(this.tilePos) != this) {
            return false;
        }
        return player.distanceToSqr((double) this.tilePos.x() + 0.5, (double) this.tilePos.y() + 0.5, (double) this.tilePos.z() + 0.5) <= 64.0;
    }

    @Override
    public void sort() {
    }
}
