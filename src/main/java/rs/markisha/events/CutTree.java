package main.java.rs.markisha.events;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;

import main.java.rs.markisha.commands.Commands;
import main.java.rs.markisha.util.WoodMaterialUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class CutTree implements Listener {

	private Random random = new Random();

	private List<BlockFace> faces = new ArrayList<>(
			List.of(BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.UP, BlockFace.DOWN));

	private int breakCounter = 0;

	@EventHandler
	public void cutTree(BlockBreakEvent event) {
		Player player = event.getPlayer();
		PlayerInventory inventory = player.getInventory();
		Block block = event.getBlock();

		if (!isTreeCutterEnabled(player))
			return;

		if (WoodMaterialUtil.isWood(block.getType())
				&& WoodMaterialUtil.isAxe(inventory.getItemInMainHand().getType())) {
			Damageable axe = (Damageable) inventory.getItemInMainHand().getItemMeta();

			if (!isAxeAlmostBroken(axe, inventory.getItemInMainHand().getType())) {
				List<Material> materials = WoodMaterialUtil.getMaterials(block.getType());
				breakCounter = 0;
				floodBreakLogs(block, materials, player);
				checkAxeDurability(player, axe);
			}
		}
	}

	private void floodBreakLogs(Block startBlock, List<Material> validMaterials, Player player) {
		Queue<Block> queue = new LinkedList<>();
		Set<Location> visitedBlocks = new HashSet<>();

		queue.add(startBlock);
		visitedBlocks.add(startBlock.getLocation());

		while (!queue.isEmpty()) {
			Block block = queue.poll();

			Damageable axe = (Damageable) player.getInventory().getItemInMainHand().getItemMeta();
			Material axeType = player.getInventory().getItemInMainHand().getType();

			if (!isValidLogBlock(block, validMaterials))
				continue;
			if (isAxeAlmostBroken(axe, axeType)) {
				checkAxeDurability(player, axe);
				break;
			}

			block.breakNaturally();
			breakCounter++;

			if (player.getGameMode() == GameMode.SURVIVAL) {
				applyAxeDurabilityDamage(player);
			}

			for (BlockFace face : faces) {
				Block adjacent = block.getRelative(face);
				Location adjacentLoc = adjacent.getLocation();

				if (!visitedBlocks.contains(adjacentLoc)) {
					queue.add(adjacent);
					visitedBlocks.add(adjacentLoc);
				}
			}
		}
	}

	private void checkAxeDurability(Player player, Damageable axe) {
		PlayerInventory inventory = player.getInventory();
		Material axeType = inventory.getItemInMainHand().getType();
		int maxDurability = axeType.getMaxDurability();

		Bukkit.getLogger().info("Checking axe dura");

		if (isAxeAlmostBroken(axe, axeType)) {
			Bukkit.getLogger().info("OOPS");
			player.sendMessage(
					Component.text("(!) TreeCutter:").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true)
							.append(Component.text(" Warning! Your axe is about to break!").color(NamedTextColor.WHITE)
									.decoration(TextDecoration.BOLD, false)));
		} else if (axe.getDamage() > maxDurability) {
			Bukkit.getLogger().info("brokey");
			inventory.clear(inventory.getHeldItemSlot());
			player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
		}
	}

	private void applyAxeDurabilityDamage(Player player) {
		Damageable axe = (Damageable) player.getInventory().getItemInMainHand().getItemMeta();
		double chance = getBreakingChance(axe);

		if (random.nextDouble() < chance / 100.0) {
			axe.setDamage(axe.getDamage() + 1);
			player.getInventory().getItemInMainHand().setItemMeta(axe);
		}
	}

	private boolean isTreeCutterEnabled(Player player) {
		return Commands.playerEnabled.getOrDefault(player, false);
	}

	private boolean isAxeAlmostBroken(Damageable axe, Material axeType) {
		return axe.getDamage() == axeType.getMaxDurability() - 1;
	}

	private boolean isValidLogBlock(Block block, List<Material> materials) {
		return materials.contains(block.getType()) && !isWartBlockLimitExceeded(block);
	}

	private boolean isWartBlockLimitExceeded(Block block) {
		return (block.getType() == Material.WARPED_WART_BLOCK || block.getType() == Material.NETHER_WART_BLOCK)
				&& breakCounter > 96;
	}

	private double getBreakingChance(Damageable axe) {
		return 100 / (axe.getEnchantLevel(Enchantment.UNBREAKING) + 1);
	}

	@EventHandler
	public void breakLeaves(LeavesDecayEvent l) {
		Block leaf = l.getBlock();

		for (BlockFace face : faces) {
			Block relative = leaf.getRelative(face);

			if (WoodMaterialUtil.isLeaf(relative.getType())) {
				relative.breakNaturally();
			}
		}
	}

	@EventHandler
	public void disableAfterQuiting(PlayerQuitEvent e) {
		Player p = e.getPlayer();

		if (Commands.playerEnabled.containsKey(p) && Commands.playerEnabled.get(p).booleanValue() == true) {
			Commands.playerEnabled.put(p, false);
		}
	}

}
