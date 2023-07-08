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
	
	private void setEnabledDisabled(Player player) {	
		if (playerEnabled.containsKey(player)) {
			playerEnabled.put(player, !playerEnabled.get(player).booleanValue());
		} else {
			playerEnabled.put(player, true);
		}
	}
	
	private void setEnabled(Player player) {
		playerEnabled.put(player, true);
	}
	
	private void setDisabled(Player player) {
		playerEnabled.put(player, false);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {

			Player player = (Player) sender;

			if (cmd.getName().equalsIgnoreCase("tc")) {
				
				if (args.length > 0) {
					if (args[0].equalsIgnoreCase("e") || args[0].equalsIgnoreCase("enable")) {
						setEnabled(player);
					} else if (args[0].equalsIgnoreCase("d") || args[0].equalsIgnoreCase("disable")) {
						setDisabled(player);
					}
				} else {
					setEnabledDisabled(player);
				}
				
				if (playerEnabled.get(player).booleanValue())
					player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "(!)" + ChatColor.RESET + "" + ChatColor.YELLOW + " TreeCutter enabled.");
				else
					player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "(!)" + ChatColor.RESET + "" + ChatColor.YELLOW + " TreeCutter disabled.");
			}
		}

		return true;
	}

}
