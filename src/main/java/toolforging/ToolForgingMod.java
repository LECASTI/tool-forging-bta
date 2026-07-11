package toolforging;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

public class ToolForgingMod implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
	public static final String MOD_ID = HalpLibe.registerMod("toolforging", true);
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static net.minecraft.core.block.Block<?> reforgingAnvil;

	@Override
	public void onInitialize() {
		LOGGER.info("ToolForgingMod initialized.");
		turniplabs.halplibe.helper.network.NetworkHandler.registerNetworkMessage(toolforging.network.OpenReforgingAnvilMessage::new);

		int blockId = 2000; // Hardcoded ID for now
		reforgingAnvil = new turniplabs.halplibe.helper.BlockBuilder(MOD_ID)
			.setHardness(5.0f)
			.setResistance(2000.0f)
			.setTags(net.minecraft.core.block.tag.BlockTags.MINEABLE_BY_PICKAXE)
			.setTileEntity(toolforging.block.entity.TileEntityReforgingAnvil::new)
			.build("reforging.anvil", blockId, b -> new toolforging.block.BlockLogicReforgingAnvil(b, net.minecraft.core.block.material.Materials.METAL));
			
		net.minecraft.core.net.command.CommandManager.registerCommand(new toolforging.command.CommandTier());
	}

	@Override
	public void beforeGameStart() {
		turniplabs.halplibe.helper.EntityHelper.addMapping(toolforging.block.entity.TileEntityReforgingAnvil.class, new net.minecraft.core.util.collection.NamespaceID(MOD_ID, "reforging_anvil"));
	}

	@Override
	public void afterGameStart() {

	}

	@Override
	public void onRecipesReady() {
		turniplabs.halplibe.helper.RecipeBuilder.Shaped(MOD_ID)
			.setShape("LL", "LL")
			.addInput('L', "minecraft:logs")
			.create("reforging_anvil", new net.minecraft.core.item.ItemStack(reforgingAnvil));
	}

	@Override
	public void initNamespaces() {

	}
}
