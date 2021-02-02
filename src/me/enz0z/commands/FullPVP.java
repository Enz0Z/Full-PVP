package me.enz0z.commands;

import java.util.HashSet;

import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.enz0z.Core;
import me.enz0z.files.ChestsYML;
import me.enz0z.utils.C;

public class FullPVP implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;

		if (player.hasPermission("fullpvp.admin")) {
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("addchest")) {
					if (args.length != 2) {
						C.sendUsageMessage(player, "clan addchest <tiempo de refill> (0 para infinito)");
					} else {
						if (!NumberUtils.isNumber(args[1])) {
							C.sendMessage(player, "FullPVP",
									"El n�mero ingresado debe ir de 0 a infinito, siempre siendo un n�mero.");
							return false;
						}
						Integer refillTime = Integer.valueOf(args[1]);
						Block block = player.getTargetBlock((HashSet<Material>) null, 2);

						if (block != null && block.getType() == Material.CHEST) {
							Location loc = block.getLocation();
							Chest chest = (Chest) loc.getBlock().getState();
							Inventory inv = chest.getInventory();
							ChestsYML chestyml = new ChestsYML();
							String confName = loc.getBlockX() + "-" + loc.getBlockY() + "-" + loc.getBlockZ();
							ConfigurationSection cs = chestyml.get().createSection(confName);

							cs.set("created-by", player.getName());
							if (refillTime == 0) {
								cs.set("unlimited", true);
							} else {
								cs.set("refilltime", refillTime);
							}
							for (int i = 0; i < inv.getSize(); i++) {
								if (inv.getItem(i) != null && inv.getItem(i).getType() != Material.AIR) {
									cs.set("contents." + i, inv.getItem(i));
								}
							}
							chestyml.save();
							C.sendMessage(player, "FullPVP", "El cofre fue guardado exitosamente en la configuraci�n.");
						} else {
							C.sendMessage(player, "FullPVP",
									"El bloque al que est�s mirando no parece ser un cofre, intenta acercarte como una opci�n.");
						}
					}
				} else if (args[0].equalsIgnoreCase("removechest")) {
					Block block = player.getTargetBlock((HashSet<Material>) null, 2);

					if (block != null && block.getType() == Material.CHEST) {
						Location loc = block.getLocation();
						ChestsYML chestyml = new ChestsYML();
						String confName = loc.getBlockX() + "-" + loc.getBlockY() + "-" + loc.getBlockZ();
						ConfigurationSection cs = chestyml.get().getConfigurationSection(confName);

						if (cs == null) {
							C.sendMessage(player, "FullPVP", "Este cofre no estaba registrado en la configuraci�n.");
						} else {
							chestyml.get().set(confName, null);
							chestyml.save();
							C.sendMessage(player, "FullPVP", "Este cofre se ha borrado de la configuraci�n.");
						}
					} else {
						C.sendMessage(player, "FullPVP",
								"El bloque al que est�s mirando no parece ser un cofre, intenta acercarte como una opci�n.");
					}
				} else if (args[0].equalsIgnoreCase("setspawn")) {
					Bukkit.getWorld("Game").setSpawnLocation(player.getLocation().getBlockX(),
							player.getLocation().getBlockY(), player.getLocation().getBlockZ());
					C.sendMessage(player, "FullPVP", "El spawn fue guardado exitosamente en la configuraci�n.");
				} else {
					C.sendUsageMessage(player, "fullpvp <addchest/removechest/setspawn>");
				}
			} else {
				C.sendUsageMessage(player, "fullpvp <addchest/removechest/setspawn>");
			}
		} else {
			player.sendMessage(" ");
			player.sendMessage(C.c("&a&l" + Core.getPluginFullName() + " creado por Enz0Z."));
			player.sendMessage(" ");
		}
		return false;
	}
}