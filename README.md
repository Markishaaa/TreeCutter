# TreeCutter

A simple, no-nonsense tree-cutting plugin that does things a little differently.

## What's Different?
Unlike other tree-cutting plugins that instantly remove entire trees, **TreeCutter** takes a more natural approach:

- **Uses a revised flood-fill algorithm** to break logs in all three axes (X, Y, Z) instead of just X and Y like a typical flood-fill algorithm.
- **Leaves decay faster**, but still decay naturally.
- **Prevents accidental axe breakage** by stopping at 1 durability and warning you. If you continue using the axe, it will still break like normal.
- **Auto-disables when you leave the server**, so you don't come back to unexpected behavior.
- **Wart blocks (Nether wood) have a cap of 96 blocks per use** to prevent excessive breaking in dense areas, as Nether forests are often packed with logs that could otherwise lead to massive unintended clearing.

## How It Works

### Log Breaking
- Works in **six directions (along X, Y, and Z axes)** using a revised flood-fill algorithm.
- Stops before breaking your axe (1 durability left) and warns you in chat.
- **Wart blocks are limited to 96 per action** to prevent breaking an entire Nether forest with a single chop.

### Leaf Decay
TreeCutter speeds up leaf decay by breaking adjacent leaves when one naturally decays. This keeps the world looking natural while removing leftover floating leaves faster than vanilla Minecraft.

### Wood Groups
TreeCutter recognizes different wood types and even some **non-traditional** wood blocks for convenience.

#### Standard Woods:
- Birch, Oak, Spruce, Jungle, Acacia, Dark Oak, Cherry, Pale Oak (Log & Wood versions)

#### Nether Woods:
- Crimson: Includes **Crimson Stem, Crimson Hyphae, Nether Wart Block, Shroomlight**
- Warped: Includes **Warped Stem, Warped Hyphae, Warped Wart Block, Shroomlight**

#### Mangrove:
- Mangrove: Includes **Mangrove Log, Mangrove Wood, Mangrove Roots, Muddy Mangrove Roots**

## Commands
TreeCutter is not always on. You **toggle it** using:

- `/treecutter` or `/tc` → Toggle on/off
- `/tc enable` or `/tc e` → Enable
- `/tc disable` or `/tc d` → Disable

## Why Use TreeCutter?
Just a simple, smarter way to cut trees. 🌲✂️