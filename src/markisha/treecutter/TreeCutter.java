package markisha.treecutter;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import markisha.commands.Commands;
import markisha.events.CutTree;

public class TreeCutter extends JavaPlugin {

	@Override
	public void onEnable() {
		Commands commands = new Commands();
		getServer().getPluginManager().registerEvents(new CutTree(), this);
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[TreeCutter]: Plugin enabled!");
		getCommand("tc").setExecutor(commands);
	}
	
	@Override
	public void onDisable() {
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[TreeCutter]: Plugin disabled!");
	}
	
}
