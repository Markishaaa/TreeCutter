package markisha.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.PlayerInventory;

import markisha.commands.Commands;

public class CutTree implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void cutTree(BlockBreakEvent event) {
		Player p = event.getPlayer();
		PlayerInventory pInv = p.getInventory();
		Block b = event.getBlock();
		Material m = b.getType();

		if (Commands.playerEnabled.get(p) != null && Commands.playerEnabled.get(p).booleanValue()) {
			if (isWood(b.getType()) && isAxe(pInv.getItemInMainHand().getType())
					&& (p.getItemInHand().getDurability() != p.getItemInHand().getType().getMaxDurability() - 1)) {

				floodBreakLogs(b, m, p);

				breakCounter = 0;

				short max = pInv.getItemInMainHand().getType().getMaxDurability();

				if (p.getItemInHand().getDurability() == p.getItemInHand().getType().getMaxDurability() - 1)
					p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "(!) TreeCutter:" + ChatColor.RESET + ""
							+ ChatColor.YELLOW + " Warning! Your axe is about to break!");

				if (p.getItemInHand().getDurability() > max) {
					pInv.clear(pInv.getHeldItemSlot());
					p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
				}
			}
		}
	}

	private int breakCounter = 0;

	@SuppressWarnings("deprecation")
	private void floodBreakLogs(Block b, Material m, Player p) {
		if (!b.getType().equals(m))
			return;
		if ((b.getType().equals(Material.WARPED_WART_BLOCK) || b.getType().equals(Material.NETHER_WART_BLOCK))
				&& breakCounter > 50)
			return;

		b.breakNaturally();
		breakCounter++;

		if (p.getGameMode().equals(GameMode.SURVIVAL))
			p.getInventory().getItemInMainHand()
					.setDurability((short) (p.getInventory().getItemInMainHand().getDurability() + 1));

		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				for (int z = -1; z <= 1; z++) {
					if (p.getItemInHand().getDurability() == p.getItemInHand().getType().getMaxDurability() - 1)
						return;

					floodBreakLogs(b.getLocation().add(x, y, z).getBlock(), m, p);
				}
			}
		}
	}

	private List<BlockFace> faces = new ArrayList<>(
			List.of(BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.UP, BlockFace.DOWN));

	@EventHandler
	public void breakLeaves(LeavesDecayEvent l) {
		Block leaf = l.getBlock();

		for (BlockFace face : faces) {
			Block relative = leaf.getRelative(face);

			if (isLeaf(relative.getType())) {
				relative.breakNaturally();
			}
		}
	}

	private boolean isLeaf(Material material) {
		boolean isLeaf;
		switch (material) {
		case OAK_LEAVES:
		case ACACIA_LEAVES:
		case AZALEA_LEAVES:
		case BIRCH_LEAVES:
		case DARK_OAK_LEAVES:
		case FLOWERING_AZALEA_LEAVES:
		case JUNGLE_LEAVES:
		case SPRUCE_LEAVES:
			isLeaf = true;
			break;
		default:
			isLeaf = false;
			break;
		}
		return isLeaf;
	}

	private boolean isWood(Material material) {
		boolean isWood;
		switch (material) {
		case BIRCH_LOG:
		case DARK_OAK_LOG:
		case SPRUCE_LOG:
		case OAK_LOG:
		case ACACIA_LOG:
		case JUNGLE_LOG:
		case CRIMSON_STEM:
		case NETHER_WART_BLOCK:
		case WARPED_STEM:
		case WARPED_WART_BLOCK:
			isWood = true;
			break;
		default:
			isWood = false;
			break;
		}
		return isWood;
	}

	private boolean isAxe(Material material) {
		boolean isAxe;
		switch (material) {
		case WOODEN_AXE:
		case STONE_AXE:
		case IRON_AXE:
		case GOLDEN_AXE:
		case DIAMOND_AXE:
		case NETHERITE_AXE:
			isAxe = true;
			break;
		default:
			isAxe = false;
			break;
		}
		return isAxe;
	}

}
