package me.enz0z.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.enz0z.utils.C;

public class Restart implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;

		if (player.hasPermission("fullpvp.restart")) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
		} else {
			C.sendNoPerm(player);
		}
		return false;
	}
}