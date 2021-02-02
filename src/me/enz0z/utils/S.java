package me.enz0z.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class S {

	public static Long currentTimeMillis() {
		return System.currentTimeMillis();
	}

	public static Long currentTimeSeconds() {
		return System.currentTimeMillis() / 1000;
	}
	
	public static Long elapsed(Long from) {
		return from - (System.currentTimeMillis() / 1000);
	}
	
	public static Player getMarchPlayers(Player player, String playername) {
		List<Player> players = Bukkit.matchPlayer(playername);
		if (players.size() > 1) {
			C.sendMessage(player, "Finder", "Se han encontrado &a" + players.size() + " &7posibilidades para [&a" + playername + "&7].");
			C.sendMessage(player, "Finder", "Quizas estabas buscando a [&e" + players.toString().replaceAll(", ", ChatColor.translateAlternateColorCodes('&', "&7, &e")) + "&7].");
			return null;
		} else if (players.size() == 1) {
			if (players.get(0).getName().equals(player.getName())) {
				C.sendMessage(player, "Finder", "No puedes seleccionarte a ti mismo.");
				return null;
			}
			if (!players.get(0).isOnline()) {
				C.sendMessage(player, "Finder", "No pudimos encontrar a &e" + playername + "&7, quizá no se encuentre conectado.");
				return null;
			}
			return players.get(0);
		} else {
			C.sendMessage(player, "Finder", "No pudimos encontrar a &e" + playername + "&7, quizá no se encuentre conectado.");
			return null;
		}
	}
}
