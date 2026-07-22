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
* **`master` (Main Branch)**: Stable releases and public builds. Maintained directly by the owner. No direct commits from the public.
* **`review`**: Pull requests from the public should target this branch. All PRs will be reviewed here before merging into `master`.
* **`dev`**: Private branch for internal development and testing. Direct commits or PRs are not accepted.
* **Local Branches**: Anyone can create local or custom feature branches for testing or development. Contributions should be submitted as PRs targeting the `review` branch.

## Credits & Attributions
* **Turnip Labs**: Creators of the Better Than Adventure (BTA) mod and the **Halplibe** library.
* **BTA Reference Mods**: This mod references `dyables` and `improvedsigns` for structure and API usage.
* **License Compliance**: Turnip Labs' `halplibe`, `dyables`, and `improvedsigns` are licensed under the CC0-1.0 (Public Domain) license. We thank them for their open-source contributions.

## Building the Mod Icon
If you update the placeholder anvil textures and need to regenerate the 3D isometric `icon.png`, you can run the following ImageMagick commands from the `src/main/resources/assets/toolforging/textures/block` directory. *(Note: This process only applies to the current placeholder 2D textures, and will eventually be cycled out when a proper 3D block model is implemented.)*

```bash
convert reforging_anvil_up.png -scale 1024x1024 -virtual-pixel transparent -set option:distort:viewport 1140x1140+0+0 -distort Affine "0,0 570,0 1024,0 1082,256 0,1024 58,256" /tmp/top.png
convert reforging_anvil_north.png -scale 1024x1024 -virtual-pixel transparent -set option:distort:viewport 1140x1140+0+0 -distort Affine "0,0 58,256 1024,0 570,512 0,1024 58,883" /tmp/left.png
convert reforging_anvil_east.png -scale 1024x1024 -virtual-pixel transparent -set option:distort:viewport 1140x1140+0+0 -distort Affine "0,0 570,512 1024,0 1082,256 0,1024 570,1139" /tmp/right.png
convert -size 1140x1140 xc:none /tmp/top.png -geometry +0+0 -composite /tmp/left.png -geometry +0+0 -composite /tmp/right.png -geometry +0+0 -composite ../../../../../icon.png
```
