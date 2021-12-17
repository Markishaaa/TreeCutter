package markisha.treecutter;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import markisha.events.CutTree;

public class TreeCutter extends JavaPlugin {

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new CutTree(), this);
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[TreeCutter]: Plugin enabled!");
	}
	
	@Override
	public void onDisable() {
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[TreeCutter]: Plugin disabled!");
	}
	
}
