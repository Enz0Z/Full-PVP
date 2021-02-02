package me.enz0z.guilds;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.enz0z.files.GuildsYML;
import me.enz0z.utils.C;

public class Guilds implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			C.sendMessage((Player) sender, "FullPVP", C.cGold + C.cBold + Core.getPluginFullName() + C.cGray + " creado por " + C.cYellow + "Enz0Z" + C.cGray + ".");
			return false;
		}
		Player player = (Player) sender;
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("aceptar") || args[0].equalsIgnoreCase("accept")) {
				if (args.length != 2) {
					C.sendUsageMessage(player, "clan aceptar <nombre del clan>");
				} else {
					GuildsManager.joinGuild(player, args[1]);
				}
			} else if (args[0].equalsIgnoreCase("denegar") || args[0].equalsIgnoreCase("deny")) {
				if (args.length != 2) {
					C.sendUsageMessage(player, "clan denegar <nombre del clan>");
				} else {
					if (GuildsManager.guildInvitations.get(player) != null && GuildsManager.guildInvitations.get(player).contains(args[1])) {
						GuildsManager.guildInvitations.put(player, GuildsManager.guildInvitations.get(player).replace(args[1], ""));
						C.sendMessage(player, "Clan", "Has denegado la invitación al clan " + C.cGreen + args[1] + C.cGray + ".");
					} else {
						C.sendMessage(player, "Clan", "Nunca fuiste invitado a este clan.");
					}
				}
			} else if (args[0].equalsIgnoreCase("invitar") || args[0].equalsIgnoreCase("invite")) {
				if (args.length != 2) {
					C.sendUsageMessage(player, "clan invitar <nombre del jugador>");
				} else {
					GuildsManager.sendRequest(player, args[1]);
				}
			} else if (args[0].equalsIgnoreCase("crear") || args[0].equalsIgnoreCase("create")) {
				if (args.length != 2) {
					C.sendUsageMessage(player, "clan crear <nombre del clan>");
				} else {
					GuildsManager.createGuild(player, args[1]);
				}
			} else if (args[0].equalsIgnoreCase("salir") || args[0].equalsIgnoreCase("leave")) {
				GuildsManager.leaveGuild(player, false);
			} else if (args[0].equalsIgnoreCase("expulsar") || args[0].equalsIgnoreCase("kick")) {
				if (args.length != 2) {
					C.sendUsageMessage(player, "clan expulsar <nombre del jugador>");
				} else {
					GuildsManager.kickFromGuild(player, Bukkit.getPlayerExact(args[1]).getUniqueId());
				}
			} else if (args[0].equalsIgnoreCase("eliminar") || args[0].equalsIgnoreCase("disband")) {
				if (Guilds.getGuild(player.getUniqueId()).equals("")) {
					C.sendMessage(player, "Clan", "No estás en ningún clan.");
				} else {
					if (args.length != 2) {
						GuildsManager.disbandGuild(player, false);
					} else if (args[1].equals(player.getUniqueId().toString())) {
						GuildsManager.disbandGuild(player, true);
					} else {
						GuildsManager.disbandGuild(player, false);
					}
				}
			} else {
				C.sendUsageMessage(player, "clan <aceptar/denegar/salir/invitar/crear/eliminar>");
			}
		} else {
			String guildName = Guilds.getGuild(player.getUniqueId());
			if (!guildName.equals("")) {
				GuildsMenues.openMenu(player);
			} else {
				C.sendUsageMessage(player, "clan <aceptar/denegar/salir/invitar/crear/eliminar>");
			}
			return true;
		}
		return false;
	}

	public static String getGuild(UUID uuid) {
		FileConfiguration c = new GuildsYML().get();
		if (c.contains("players." + uuid.toString())) {
			return c.getString("players." + uuid.toString() + ".guild");
		}
		return "";
	}

	public static ConfigurationSection getGuildConfig(String guildName) {
		FileConfiguration c = new GuildsYML().get();
		ConfigurationSection guild = c.getConfigurationSection("guilds." + guildName);
		return guild;
	}

	public static List<String> getGuildMembers(String guildName, Boolean useUUID) {
		GuildsYML yml = new GuildsYML();
		FileConfiguration c = yml.get();
		List<String> members = new ArrayList<String>();
		for (String uuids : yml.get().getConfigurationSection("players").getKeys(false)) {
			String memberGuild = c.getString("players." + uuids + ".guild");
			if (memberGuild.equals(guildName)) {
				if (useUUID) {
					members.add(uuids);
				} else {
					members.add(c.getString("players." + uuids + ".name"));
				}
			}
		}
		return members;
	}

	public static ConfigurationSection setGuildConfig(String guildName, String where, Object set) {
		GuildsYML guildyml = new GuildsYML();
		FileConfiguration c = guildyml.get();
		ConfigurationSection guild = c.getConfigurationSection("guilds." + guildName);
		guild.set(where, set);
		guildyml.save();
		return guild;
	}
	
	public static void sendGuildMessage(String guildName, String text) {
		for (String ps : Guilds.getGuildMembers(guildName, true)) {
			Player o = Bukkit.getPlayer(UUID.fromString(ps));
			if (o == null) continue;
			C.sendMessage(o, "Clan", text);
		}
	}
}
