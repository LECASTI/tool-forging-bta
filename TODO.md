- `[x]` Subtask 1: Register block & commands (Tier command)
- `[x]` Subtask 2: Base Mixins for ItemStack
- `[x]` Subtask 3: Functional Blocks & GUI (Reforging Anvil GUI and texture working)
- `[x]` Subtask 4: Networking setup (Container syncing)
- `[/]` Subtask 5: Item modifier application & Balancing
    - `[x]` NBT tier system attached to items
    - `[x]` Fix item disappearance when GUI closes
    - `[ ]` Adjust stat multipliers for each tier
    - `[ ]` Fix GUI interaction (currently behaves like furnace instead of button)
    - `[ ]` Dynamic recipe requirement based on tool material
- `[ ]` Subtask 6: Final tests

## Notes for Tomorrow
- **Balancing:** Review stat multipliers. Dreadful should probably be 50% durability, but 20% damage, for example. Perfect should feel truly "perfect" (maybe more than 150%).
- **GUI UX:** Currently, the reforging works by clicking the third (output) slot which randomly assigns a tier, but it uses the furnace texture. We should swap to a custom GUI texture with an actual button that triggers the reforge (and sends a packet to the server to consume the items and apply the tier).
- **Recipes:** We need to figure out how to dynamically fetch a tool's base material (Diamond for Diamond Pickaxe) instead of hardcoding Gold Ingots.
- **Git Repo:** Run `gh auth login` in your terminal to authenticate your GitHub CLI, then we can push this to a private repository!
