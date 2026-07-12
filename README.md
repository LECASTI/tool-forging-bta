# Tool Forging Mod - BTA

This is a mod for Minecraft Better Than Adventure 8.0-pre2 that adds a "Reforging Anvil". It allows players to re-roll the stats of their tools by consuming a material corresponding to the tool's tier (e.g. Diamond for Diamond Pickaxe).

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

## Pending Tasks
* Adjust stat multipliers for all tiers (e.g., Dreadful shouldn't be 50% across the board, maybe 50% durability but 20% damage).
* Update GUI textures and add a button for Reforging (instead of using the furnace result slot behavior).

## Contribution Flow
* **`8.0` (Main Branch)**: Stable releases and public builds. Maintained directly by the owner. No direct commits from the public.
* **`review`**: Pull requests from the public should target this branch. All PRs will be reviewed here before merging into `8.0`.
* **`dev`**: Private branch for internal development and testing. Direct commits or PRs are not accepted.
* **Local Branches**: Anyone can create local or custom feature branches for testing or development. Contributions should be submitted as PRs targeting the `review` branch.
