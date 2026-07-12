# Tool Forging Mod - BTA

This is a mod for Minecraft Better Than Adventure 8.0-pre2 that adds a "Reforging Anvil". It allows players to re-roll the stats of their tools by consuming a specific material (currently hardcoded to Gold Ingot for testing, will be updated to specific materials per tool tier).

## Current Features
* Reforging Anvil block
* Custom UI for reforging tools
* `/tier set <value>` command to modify tool tiers directly (for testing)

## Tiers
The tiers offer stat multipliers (Durability, Damage, Mining Speed):
* Dreadful (-3): Red
* Bad (-2): Brown
* Poor (-1): Yellow
* Normal (0): Default
* Good (1): Blue
* Great (2): Bright Purple
* Perfect (3): Magenta / Animated Rainbow (Pending)

## Pending Tasks (Next Session)
* Adjust stat multipliers for all tiers (e.g., Dreadful shouldn't be 50% across the board, maybe 50% durability but 20% damage).
* Make the anvil recipe requirement dynamic based on tool material instead of hardcoded Gold Ingot (e.g. Diamond Pickaxe needs Diamond).
* Update GUI textures and add a button for Reforging (instead of using the furnace result slot behavior).
