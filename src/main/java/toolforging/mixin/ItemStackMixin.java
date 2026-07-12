package toolforging.mixin;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import com.mojang.nbt.tags.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemStack.class, remap = false)
public class ItemStackMixin {
    
    private int getTier() {
        ItemStack self = (ItemStack) (Object) this;
        CompoundTag data = self.getData();
        if (data != null && data.containsKey("toolforging:tier")) {
            return data.getInteger("toolforging:tier");
        }
        return 0;
    }

    private float getTierMultiplier(int tier) {
        switch (tier) {
            case -3: return 0.50f;
            case -2: return 0.75f;
            case -1: return 0.90f;
            case 1: return 1.10f;
            case 2: return 1.20f;
            case 3: return 1.50f;
            default: return 1.00f;
        }
    }

    private float applyTierRounding(float value, int tier) {
        if (tier == 0) return value;
        float multiplied = value * getTierMultiplier(tier);
        if (tier < 0) {
            return (float) Math.floor(multiplied * 2.0) / 2.0f;
        } else {
            return (float) Math.ceil(multiplied * 2.0) / 2.0f;
        }
    }

    private int applyTierRoundingInt(int value, int tier) {
        if (tier == 0) return value;
        float multiplied = value * getTierMultiplier(tier);
        if (tier < 0) {
            return (int) Math.floor(multiplied);
        } else {
            return (int) Math.ceil(multiplied);
        }
    }

    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    private void addTierToName(CallbackInfoReturnable<String> cir) {
        int tier = getTier();
        if (tier != 0) {
            String prefix = "";
            switch (tier) {
                case -3: prefix = net.minecraft.core.net.command.TextFormatting.RED + "Dreadful "; break;
                case -2: prefix = net.minecraft.core.net.command.TextFormatting.YELLOW + "Poor "; break;
                case -1: prefix = net.minecraft.core.net.command.TextFormatting.BROWN + "Bad "; break;
                case 1: prefix = net.minecraft.core.net.command.TextFormatting.BLUE + "Good "; break;
                case 2: prefix = net.minecraft.core.net.command.TextFormatting.PURPLE + "Great "; break;
                case 3: prefix = net.minecraft.core.net.command.TextFormatting.MAGENTA + "Perfect "; break;
            }
            if (!prefix.isEmpty()) {
                String original = cir.getReturnValue();
                if (original != null && !original.startsWith(prefix)) {
                    cir.setReturnValue(prefix + net.minecraft.core.net.command.TextFormatting.WHITE + original);
                }
            }
        }
    }

    @Inject(method = "getStrVsBlock", at = @At("RETURN"), cancellable = true)
    private void modifyMiningSpeed(Block<?> block, CallbackInfoReturnable<Float> cir) {
        int tier = getTier();
        if (tier != 0) {
            float original = cir.getReturnValue();
            // Only modify if the tool is actually effective (speed > 1.0 usually means it's the right tool)
            // But we'll just apply it universally to base speed.
            cir.setReturnValue(applyTierRounding(original, tier));
        }
    }

    @Inject(method = "getMaxDamage", at = @At("RETURN"), cancellable = true)
    private void modifyMaxDamage(CallbackInfoReturnable<Integer> cir) {
        int tier = getTier();
        if (tier != 0) {
            int original = cir.getReturnValue();
            cir.setReturnValue(applyTierRoundingInt(original, tier));
        }
    }

    @Inject(method = "getDamageVsEntity", at = @At("RETURN"), cancellable = true)
    private void modifyEntityDamage(Entity entity, CallbackInfoReturnable<Integer> cir) {
        int tier = getTier();
        if (tier != 0) {
            int original = cir.getReturnValue();
            cir.setReturnValue(applyTierRoundingInt(original, tier));
        }
    }
}
