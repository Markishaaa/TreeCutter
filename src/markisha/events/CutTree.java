package markisha.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.PlayerInventory;

public class CutTree implements Listener {

	private static final BlockFace[] faces = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };

	@SuppressWarnings("deprecation")
	@EventHandler
	public void cutTree(BlockBreakEvent event) {
		Player player = event.getPlayer();
		PlayerInventory pInv = player.getInventory();
		Block b = event.getBlock();
		Material m = b.getType();

		if (player.isSneaking()) {
			if (isWood(b.getType()) && isAxe(pInv.getItemInMainHand().getType())) {
				breakLogs(b, m);

//				pInv.getItemInMainHand().setDurability((short) (pInv.getItemInMainHand().getDurability() + y - 1));
			}
		}
	}

	private List<Block> findConnectedLogsCoord(Block b, int y, Material m) {
		List<Block> logs = new ArrayList<>();

		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {

				Block nextBlock = b.getLocation().add(i, y, j).getBlock();
				
				if (nextBlock.getType().equals(m)) {
					logs.add(nextBlock);
				}

			}
		}

		return logs;
	}

	private void breakLogs(Block b, Material m) {
		int y = 0;
		
		while (true) {
			List<Block> logsAbove = findConnectedLogsCoord(b, y, m);

			if (logsAbove.isEmpty())
				break;
			
			for (Block logAbove : logsAbove) {
				logAbove.breakNaturally();
			}
			
			y++;
		}
	}

	private boolean isWood(Material material) {
		boolean isWood;
		switch (material) {
		case ACACIA_LOG:
		case BIRCH_LOG:
		case DARK_OAK_LOG:
		case JUNGLE_LOG:
		case OAK_LOG:
		case SPRUCE_LOG:
		case CRIMSON_STEM:
		case WARPED_STEM:
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
			isAxe = true;
			break;
		default:
			isAxe = false;
			break;
		}
		return isAxe;
	}

}
