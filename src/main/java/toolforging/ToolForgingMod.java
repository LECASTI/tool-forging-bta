package toolforging;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryFurnace;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryCategory;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryPlacement;
import turniplabs.halplibe.helper.creativeInventory.CreativeInventoryRegistry;
import turniplabs.halplibe.helper.CreativeHelper;
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
		CreativeInventoryRegistry.INSTANCE.register(reforgingAnvil, new CreativeInventoryPlacement.Category(CreativeInventoryCategory.WORKBENCHES));
	}

	@Override
	public void onRecipesReady() {
		// 1. Crafting Recipe for Reforging Anvil
		// Shape:
		//   I I I   (iron ingots)
		//   S C S   (any stone, chainlink, any stone)
		//   S D S   (any stone, diamond, any stone)
		turniplabs.halplibe.helper.RecipeBuilder.Shaped(MOD_ID)
			.setShape("III", "SCS", "SDS")
			.addInput('I', net.minecraft.core.item.Items.INGOT_IRON)
			.addInput('C', net.minecraft.core.item.Items.CHAINLINK)
			.addInput('D', net.minecraft.core.item.Items.DIAMOND)
			.addInput('S', "minecraft:stones")
			.create("reforging_anvil", new net.minecraft.core.item.ItemStack(reforgingAnvil));

		// 2. Register Reforging Anvil Recipe Booklet group and entries
		toolforging.recipe.ReforgingRecipeRegistry.registerRecipes();
	}

	@Override
	public void initNamespaces() {
		net.minecraft.core.data.registry.recipe.RecipeNamespace modNamespace = new net.minecraft.core.data.registry.recipe.RecipeNamespace();
		Registries.RECIPES.register(MOD_ID, modNamespace);
	}
}
