package me.enz0z.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.enz0z.files.GuildsYML;
import me.enz0z.utils.C;

public class Test implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
    	if (player.hasPermission("fullpvp.test")) {
    		GuildsYML guildsyml = new GuildsYML();
    		FileConfiguration c = guildsyml.get();
    		for (String guilds : c.getConfigurationSection("guilds").getKeys(false)) {
    			C.sendMessage(player, "Debug", guilds);
    		}
    	} else {
    		C.sendNoPerm(player);
    	}
		return false;
	}
}