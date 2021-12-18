package markisha.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

	public static Map<Player, Boolean> playerEnabled = new HashMap<>();
	public static boolean enabled = false;
	
	public void setEnabledDisabled(Player player) {	
		if (playerEnabled.containsKey(player)) {
			playerEnabled.put(player, !playerEnabled.get(player).booleanValue());
		} else {
			playerEnabled.put(player, true);
		}
	}

	public boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {

			Player player = (Player) sender;

			if (cmd.getName().equalsIgnoreCase("tc")) {

				setEnabledDisabled(player);

				if (playerEnabled.get(player).booleanValue())
					player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "(!)" + ChatColor.RESET + "" + ChatColor.YELLOW + " TreeCutter enabled.");
				else
					player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "(!)" + ChatColor.RESET + "" + ChatColor.YELLOW + " TreeCutter disabled.");
			}
		}

		return true;
	}

}
