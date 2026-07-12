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

    private float getDamageSpeedMultiplier(int tier) {
        switch (tier) {
            case -3: return 0.25f;
            case -2: return 0.50f;
            case -1: return 0.80f;
            case 1: return 1.25f;
            case 2: return 1.75f;
            case 3: return 3.00f;
            default: return 1.00f;
        }
    }

    private float getDurabilityMultiplier(int tier) {
        switch (tier) {
            case -3: return 0.25f;
            case -2: return 0.50f;
            case -1: return 0.80f;
            case 1: return 1.25f;
            case 2: return 1.75f;
            case 3: return 3.00f;
            default: return 1.00f;
        }
    }

    private float applyTierRounding(float value, int tier, boolean isDurability) {
        if (tier == 0) return value;
        float multiplier = isDurability ? getDurabilityMultiplier(tier) : getDamageSpeedMultiplier(tier);
        float multiplied = value * multiplier;
        float rounded;
        if (tier < 0) {
            rounded = (float) Math.floor(multiplied * 2.0) / 2.0f;
        } else {
            rounded = (float) Math.ceil(multiplied * 2.0) / 2.0f;
        }
        return Math.max(0.5f, rounded);
    }

    private int applyTierRoundingInt(int value, int tier, boolean isDurability) {
        if (tier == 0) return value;
        float multiplier = isDurability ? getDurabilityMultiplier(tier) : getDamageSpeedMultiplier(tier);
        float multiplied = value * multiplier;
        int rounded;
        if (tier < 0) {
            rounded = (int) Math.floor(multiplied);
        } else {
            rounded = (int) Math.ceil(multiplied);
        }
        return Math.max(isDurability ? 1 : 0, rounded);
    }

    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    private void addTierToName(CallbackInfoReturnable<String> cir) {
        ItemStack self = (ItemStack) (Object) this;
        com.mojang.nbt.tags.CompoundTag data = self.getData();

        // Preview slot: show "??? <tool name>" in white to hint at tool type without revealing tier
        if (data != null && data.containsKey("toolforging:preview") && data.getByte("toolforging:preview") != 0) {
            String baseName = self.getItem() != null ? self.getItem().getTranslatedName(self) : cir.getReturnValue();
            cir.setReturnValue(net.minecraft.core.net.command.TextFormatting.WHITE + "??? " + baseName);
            return;
        }

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
            cir.setReturnValue(applyTierRounding(original, tier, false));
        }
    }

    @Inject(method = "getMaxDamage", at = @At("RETURN"), cancellable = true)
    private void modifyMaxDamage(CallbackInfoReturnable<Integer> cir) {
        int tier = getTier();
        if (tier != 0) {
            int original = cir.getReturnValue();
            cir.setReturnValue(applyTierRoundingInt(original, tier, true));
        }
    }

    @Inject(method = "getDamageVsEntity", at = @At("RETURN"), cancellable = true)
    private void modifyEntityDamage(Entity entity, CallbackInfoReturnable<Integer> cir) {
        int tier = getTier();
        if (tier != 0) {
            int original = cir.getReturnValue();
            cir.setReturnValue(applyTierRoundingInt(original, tier, false));
        }
    }
}
