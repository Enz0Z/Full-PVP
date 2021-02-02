package me.enz0z.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class C {

	public static String cBold = "" + ChatColor.BOLD;
	public static String cScramble = "" + ChatColor.MAGIC;
	public static String cUnderline = "" + ChatColor.UNDERLINE;
	public static String cStrike = "" + ChatColor.STRIKETHROUGH;
	public static String cItallic = "" + ChatColor.ITALIC;
	public static String cReset = "" + ChatColor.RESET;

	public static String cAqua = "" + ChatColor.AQUA;
	public static String cBlack = "" + ChatColor.BLACK;
	public static String cBlue = "" + ChatColor.BLUE;
	public static String cDarkAqua = "" + ChatColor.DARK_AQUA;
	public static String cDarkBlue = "" + ChatColor.DARK_BLUE;
	public static String cDarkGray = "" + ChatColor.DARK_GRAY;
	public static String cDarkGreen = "" + ChatColor.DARK_GREEN;
	public static String cDarkPurple = "" + ChatColor.DARK_PURPLE;
	public static String cDarkRed = "" + ChatColor.DARK_RED;
	public static String cGold = "" + ChatColor.GOLD;
	public static String cGray = "" + ChatColor.GRAY;
	public static String cGreen = "" + ChatColor.GREEN;
	public static String cPurple = "" + ChatColor.LIGHT_PURPLE;
	public static String cRed = "" + ChatColor.RED;
	public static String cWhite = "" + ChatColor.WHITE;
	public static String cYellow = "" + ChatColor.YELLOW;
	
	public static String r(String text) {
		return text.replaceAll("§a", "").replaceAll("§b", "").replaceAll("§c", "").replaceAll("§d", "")
				.replaceAll("§e", "").replaceAll("§f", "").replaceAll("§0", "").replaceAll("§1", "")
				.replaceAll("§2", "").replaceAll("§3", "").replaceAll("§4", "").replaceAll("§5", "")
				.replaceAll("§6", "").replaceAll("§7", "").replaceAll("§8", "").replaceAll("§9", "")
				.replaceAll("§k", "").replaceAll("§l", "").replaceAll("§m", "").replaceAll("§n", "")
				.replaceAll("§o", "").replaceAll("§r", "").replaceAll("&a", "").replaceAll("&b", "")
				.replaceAll("&c", "").replaceAll("&d", "").replaceAll("&e", "").replaceAll("&f", "")
				.replaceAll("&0", "").replaceAll("&1", "").replaceAll("&2", "").replaceAll("&3", "")
				.replaceAll("&4", "").replaceAll("&5", "").replaceAll("&6", "").replaceAll("&7", "")
				.replaceAll("&8", "").replaceAll("&9", "").replaceAll("&k", "").replaceAll("&l", "")
				.replaceAll("&m", "").replaceAll("&n", "").replaceAll("&o", "").replaceAll("&r", "");
	}
	
	public static String c(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static void blank(Player player) {
		if (player == null) return;
		player.sendMessage(" ");
	}
	
	public static String loadingBar(Integer max, Integer current) {
		String finalString = "■■■■■■■■■■";
		Integer result = (current*10)/max;
		switch (result) {
			case 0:
				finalString = "&c■■■■■■■■■■";
				break;
			case 1:
				finalString = "&a■&c■■■■■■■■■";
				break;
			case 2:
				finalString = "&a■■&c■■■■■■■■";
				break;
			case 3:
				finalString = "&a■■■&c■■■■■■■";
				break;
			case 4:
				finalString = "&a■■■■&c■■■■■■";
				break;
			case 5:
				finalString = "&a■■■■■&c■■■■■";
				break;
			case 6:
				finalString = "&a■■■■■■&c■■■■";
				break;
			case 7:
				finalString = "&a■■■■■■■&c■■■";
				break;
			case 8:
				finalString = "&a■■■■■■■■&c■■";
				break;
			case 9:
				finalString = "&a■■■■■■■■■&c■";
				break;
			case 10:
				finalString = "&a■■■■■■■■■■";
				break;
		}
		
		return ChatColor.translateAlternateColorCodes('&', finalString);
	}
	
	public static void sendNoPerm(Player player) {
		if (player == null) return;
		player.sendMessage(C.cBlue + "Permissions>" + C.cGray + " " + ChatColor.translateAlternateColorCodes('&', "Lo sentimos, no tienes los permisos suficientes."));
	}
	
	public static void sendMessage(Player player, String title, String text) {
		if (player == null) return;
		player.sendMessage(C.cBlue + title + ">" + C.cGray + " " + ChatColor.translateAlternateColorCodes('&', text));
	}
	
	public static void sendUsageMessage(Player player, String text) {
		if (player == null) return;
		player.sendMessage(C.cRed + "Uso: /" + ChatColor.translateAlternateColorCodes('&', text));
	}
}
