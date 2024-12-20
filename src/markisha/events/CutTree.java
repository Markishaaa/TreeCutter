package markisha.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.GameMode;
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

import markisha.commands.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class CutTree implements Listener {

	private Random random = new Random();

	@EventHandler
	public void cutTree(BlockBreakEvent event) {
		Player p = event.getPlayer();
		PlayerInventory pInv = p.getInventory();
		Block b = event.getBlock();

		if (Commands.playerEnabled.containsKey(p) && Commands.playerEnabled.get(p).booleanValue()) {
			if (isWood(b.getType()) && isAxe(pInv.getItemInMainHand().getType())) {
				Damageable axe = (Damageable) pInv.getItemInMainHand().getItemMeta();

				if (axe.getDamage() != pInv.getItemInMainHand().getType().getMaxDurability() - 1) {

					List<Material> m = getMaterials(b.getType());

					floodBreakLogs(b, m, p);
					axe = (Damageable) pInv.getItemInMainHand().getItemMeta();

					breakCounter = 0;

					int max = pInv.getItemInMainHand().getType().getMaxDurability();

					if (axe.getDamage() == pInv.getItemInMainHand().getType().getMaxDurability() - 1) {
						p.sendMessage(Component.text().content("(!) TreeCutter:").color(NamedTextColor.YELLOW)
								.decoration(TextDecoration.BOLD, true)
								.append(Component.text().content(" Warning! Your axe is about to break!")
										.color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false)));
						return;
					}

					if (axe.getDamage() > max) {
						pInv.clear(pInv.getHeldItemSlot());
						p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
					}
				}
			}
		}
	}

	private int breakCounter = 0;

	private void floodBreakLogs(Block b, List<Material> m, Player p) {
		Damageable axe = (Damageable) p.getInventory().getItemInMainHand().getItemMeta();

		if (axe.getDamage() == p.getInventory().getItemInMainHand().getType().getMaxDurability() - 1)
			return;

		boolean shouldContinue = false;

		for (Material mat : m) {
			if (mat.equals(b.getType())) {
				shouldContinue = true;
				break;
			}
		}

		if (!shouldContinue)
			return;

		if ((b.getType().equals(Material.WARPED_WART_BLOCK) || b.getType().equals(Material.NETHER_WART_BLOCK))
				&& breakCounter > 50)
			return;

		b.breakNaturally();
		breakCounter++;

		if (p.getGameMode().equals(GameMode.SURVIVAL)) {
			double chance = getBreakingChance(axe);

			if (random.nextDouble() < chance / 100.0) {
				axe.setDamage((axe.getDamage() + 1));
				p.getInventory().getItemInMainHand().setItemMeta(axe);
			}
		}

		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				for (int z = -1; z <= 1; z++) {
					floodBreakLogs(b.getLocation().add(x, y, z).getBlock(), m, p);
				}
			}
		}
	}

	private List<BlockFace> faces = new ArrayList<>(
			List.of(BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.UP, BlockFace.DOWN));

	private double getBreakingChance(Damageable axe) {
		return 100 / (axe.getEnchantLevel(Enchantment.UNBREAKING) + 1);
	}

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

	@EventHandler
	public void disableAfterQuiting(PlayerQuitEvent e) {
		Player p = e.getPlayer();

		if (Commands.playerEnabled.containsKey(p) && Commands.playerEnabled.get(p).booleanValue() == true) {
			Commands.playerEnabled.put(p, false);
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
		case MANGROVE_LEAVES:
		case CHERRY_LEAVES:
		case PALE_OAK_LEAVES:
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
		case MANGROVE_LOG:
		case MANGROVE_ROOTS:
		case MUDDY_MANGROVE_ROOTS:
		case CHERRY_LOG:
		case PALE_OAK_LOG:
			isWood = true;
			break;
		default:
			isWood = false;
			break;
		}
		return isWood;
	}

	private List<Material> getMaterials(Material m) {
		List<Material> materials;

		if (m.equals(Material.MANGROVE_ROOTS) || m.equals(Material.MUDDY_MANGROVE_ROOTS)
				|| m.equals(Material.MANGROVE_LOG)) {
			materials = Arrays.asList(Material.MANGROVE_ROOTS, Material.MUDDY_MANGROVE_ROOTS, Material.MANGROVE_LOG);
		} else {
			materials = Arrays.asList(m);
		}

		return materials;
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
