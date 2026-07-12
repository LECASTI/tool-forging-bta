package toolforging.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentTypeInteger;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;

public class CommandTier implements CommandManager.CommandRegistry {

    @Override
    public void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
            ArgumentBuilderLiteral.<CommandSource>literal("tier")
                .then(ArgumentBuilderLiteral.<CommandSource>literal("set")
                    .then(ArgumentBuilderRequired.<CommandSource, Integer>argument("value", ArgumentTypeInteger.integer(-3, 3))
                        .executes(context -> {
                            int tier = ArgumentTypeInteger.getInteger(context, "value");
                            CommandSource source = context.getSource();
                            Player player = source.getSender();
                            
                            if (player == null) {
                                source.sendMessage("This command must be run by a player.");
                                return 0;
                            }
                            
                            ItemStack heldItem = player.getHeldItem();
                            if (heldItem == null) {
                                source.sendMessage("You must be holding an item.");
                                return 0;
                            }
                            
                            CompoundTag data = heldItem.getData();
                            if (data == null) {
                                data = new CompoundTag();
                                heldItem.setData(data);
                            }
                            data.putInt("toolforging:tier", tier);
                            
                            source.sendMessage("Applied tier " + tier + " to held item.");
                            return 1;
                        })
                    )
                )
        );
    }
}
