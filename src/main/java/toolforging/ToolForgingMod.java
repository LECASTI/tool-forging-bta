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
			//.setTextures("reforging_anvil.png") // We can add texture later
			.build("reforging_anvil", blockId, b -> new toolforging.block.BlockLogicReforgingAnvil(b, net.minecraft.core.block.material.Materials.METAL));
			
		net.minecraft.core.net.command.CommandManager.registerCommand(new toolforging.command.CommandTier());
	}

	@Override
	public void beforeGameStart() {

	}

	@Override
	public void afterGameStart() {

	}

	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}
}
