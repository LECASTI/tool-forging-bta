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
			.setResistance(90.0f) // 3x stronger than cobblestone (30), weaker than obsidian (~2000).
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
		// Shape:
		//   I I I   (iron ingots)
		//   S C S   (any stone, chainlink, any stone)
		//   S D S   (any stone, diamond, any stone)
		// Chainlink confirmed as item ID 16503 via F3+H; using itemsList array since no Items.CHAINLINK exists.
		turniplabs.halplibe.helper.RecipeBuilder.Shaped(MOD_ID)
			.setShape("III", "SCS", "SDS")
			.addInput('I', net.minecraft.core.item.Items.INGOT_IRON)
			.addInput('C', net.minecraft.core.item.Items.CHAINLINK) // chainlink (confirmed ID 16503)
			.addInput('D', net.minecraft.core.item.Items.DIAMOND)
			.addInput('S', "minecraft:stones") // any stone variant (including BTA stone variants)
			.create("reforging_anvil", new net.minecraft.core.item.ItemStack(reforgingAnvil));
	}

	@Override
	public void initNamespaces() {

	}
}
