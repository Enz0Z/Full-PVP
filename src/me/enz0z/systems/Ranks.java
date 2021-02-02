package me.enz0z.systems;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Ranks implements Listener {

	public static void setRankTag(Player player) {
		Scoreboard s = player.getScoreboard();
		for (Player o : Bukkit.getOnlinePlayers()) {
			Team t = s.getTeam(getTeamName(o));
			if (t == null)
				t = s.registerNewTeam(getTeamName(o));
			t.setPrefix(getPrefix(o));
			t.addPlayer(o);
		}
	}

	private static String getTeamName(Player player) {
		if (player.hasPermission("rank.owner"))
			return "a";
		else if (player.hasPermission("rank.developer"))
			return "b";
		else if (player.hasPermission("rank.admin"))
			return "c";
		else if (player.hasPermission("rank.gmoderador"))
			return "d";
		else if (player.hasPermission("rank.moderador"))
			return "e";
		else if (player.hasPermission("rank.smanager"))
			return "f";
		else if (player.hasPermission("rank.helper"))
			return "g";
		else if (player.hasPermission("rank.youtuber"))
			return "h";
		else if (player.hasPermission("rank.poseidon"))
			return "i";
		else if (player.hasPermission("rank.aqua"))
			return "j";
		else if (player.hasPermission("rank.vip"))
			return "k";
		else
			return "l";
	}
	//§
	private static String getPrefix(Player player) {
		if (player.hasPermission("rank.owner"))
			return "§4[Creador] ";
		else if (player.hasPermission("rank.developer"))
			return "§3[Dev] ";
		else if (player.hasPermission("rank.admin"))
			return "§c[Admin] ";
		else if (player.hasPermission("rank.gmoderador"))
			return "§5[GMod] ";
		else if (player.hasPermission("rank.moderador"))
			return "§d[Mod] ";
		else if (player.hasPermission("rank.smanager"))
			return "§9[S-Manager] ";
		else if (player.hasPermission("rank.helper"))
			return "§a[Ayudante] ";
		else if (player.hasPermission("rank.youtuber"))
			return "§c[You§fTuber] ";
		else if (player.hasPermission("rank.poseidon"))
			return "§3[Poseidón] ";
		else if (player.hasPermission("rank.aqua"))
			return "§b[Aqua] ";
		else if (player.hasPermission("rank.vip"))
			return "§6[VIP] ";
		else
			return "§8[Usuario] ";
	}
	
	public static String getRank(Player player) {
		return getPrefix(player).replaceAll(" ", "");
	}
}
