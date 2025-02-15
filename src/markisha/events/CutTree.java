package markisha.events;

import java.util.ArrayList;
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
import markisha.util.WoodMaterialUtil;
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
			if (WoodMaterialUtil.isWood(b.getType()) && WoodMaterialUtil.isAxe(pInv.getItemInMainHand().getType())) {
				Damageable axe = (Damageable) pInv.getItemInMainHand().getItemMeta();

				if (axe.getDamage() != pInv.getItemInMainHand().getType().getMaxDurability() - 1) {

					List<Material> m = WoodMaterialUtil.getMaterials(b.getType());

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
				&& breakCounter > 96)
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
