package markisha.treecutter;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import markisha.commands.Commands;
import markisha.events.CutTree;
import markisha.util.WoodMaterialUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class TreeCutter extends JavaPlugin {

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new CutTree(), this);
		
		getCommand("tc").setExecutor(new Commands());
		if (WoodMaterialUtil.isWood(Material.OAK_LOG))
			getServer().getConsoleSender().sendMessage(Component.text().content("[TreeCutter]: Initialized!").color(NamedTextColor.GREEN));
		
		getServer().getConsoleSender().sendMessage(Component.text().content("[TreeCutter]: Plugin enabled!").color(NamedTextColor.GREEN));
	}
	
	@Override
	public void onDisable() {
		getServer().getConsoleSender().sendMessage(Component.text().content("[TreeCutter]: Plugin disabled!").color(NamedTextColor.GREEN));
	}
	
}
