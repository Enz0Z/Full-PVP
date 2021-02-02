package me.enz0z.guilds;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.enz0z.commands.Settings;
import me.enz0z.files.ConfigYML;
import me.enz0z.files.GuildsYML;
import me.enz0z.utils.C;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class GuildsManager {

	public static HashMap<Player, String> guildInvitations = new HashMap<Player, String>();
	
	public static void createGuild(Player player, String name) {
		player.closeInventory();

		GuildsYML guildsyml = new GuildsYML();
		FileConfiguration c = guildsyml.get();
		name = C.r(name);
		
		if (c.contains("players." + player.getUniqueId().toString())) {
			C.sendMessage(player, "Clan", "¡Ya estás en un clan! Debes salirte de este para crear uno.");
			return;
		}

		if (name.length() < 3) {
			C.sendMessage(player, "Clan", "El nombre del Clan debe tener al menos 3 caracteres.");
			return;
		}
		
		if (name.length() > 11) {
			C.sendMessage(player, "Clan", "El nombre del Clan no debe sobrepasar los 11 caracteres.");
			return;
		}

		for (String names : new ConfigYML().get().getStringList("BlockedClanNames")) {
			if (name.equalsIgnoreCase(names)) {
				C.sendMessage(player, "Clan", "El nombre del Clan no está permitido.");
				return;
			}
		}

		if (c.getConfigurationSection("guilds") != null) {
			for (String guilds : c.getConfigurationSection("guilds").getKeys(false)) {
				if (guilds.toLowerCase().equals(name.toLowerCase())) {
					C.sendMessage(player, "Clan", "¡Ya existe un clan con ese nombre! Por favor, elige otro nombre.");
					return;
				}
			}
		}
		
		//if (c.contains("guilds." + name)) {
		//	C.sendMessage(player, "Clan", "¡Ya existe un clan con ese nombre! Por favor, elige otro nombre.");
		//	return;
		//}

		Integer clanLimit = 4;
		if (player.hasPermission("clan.limit.5")) {
			clanLimit = 5;
		} else if (player.hasPermission("clan.limit.6")) {
			clanLimit = 6;
		} else if (player.hasPermission("clan.limit.7")) {
			clanLimit = 7;
		}
		
		ConfigurationSection guild = c.createSection("guilds." + name);
		guild.set("owner", player.getUniqueId().toString());
		guild.set("owner-name", player.getName());
		guild.set("friendlyfire", false);
		guild.set("announcejoinquit", true);
		guild.set("clanchat", true);
		guild.set("limit", clanLimit);
		guild.set("kills", 0);
		guild.set("deaths", 0);
		guild.set("bowhits", 0);
		
		c.set("players." + player.getUniqueId().toString() + ".guild", name);
		c.set("players." + player.getUniqueId().toString() + ".name", player.getName());
		c.set("players." + player.getUniqueId().toString() + ".role", "owner");
		
		C.sendMessage(player, "Clan", "Has creado un nuevo clan llamado " + C.cGreen + name + C.cGray + ".");
		guildsyml.save();

	}

	public static void sendRequest(Player player, String nameSendTo) {
		player.closeInventory();

		GuildsYML guildsyml = new GuildsYML();
		FileConfiguration c = guildsyml.get();
		String playerGuild = Guilds.getGuild(player.getUniqueId());

		if (playerGuild.equals("")) {
			C.sendMessage(player, "Clan", "No estás en ningún clan.");
			return;
		}

		ConfigurationSection guild = c.getConfigurationSection("guilds." + playerGuild);

		if (!guild.getString("owner").equals(player.getUniqueId().toString())) {
			C.sendMessage(player, "Clan", "No eres el dueño del clan.");
			return;
		}

		Player playerSendTo = Bukkit.getPlayer(nameSendTo);

		if (playerSendTo == null || !playerSendTo.isOnline()) {
			C.sendMessage(player, "Clan", "Este jugador no está conectado.");
			return;
		}
		String playerSendToGuild = Guilds.getGuild(playerSendTo.getUniqueId());

		if (guildInvitations.get(playerSendTo) != null && guildInvitations.get(playerSendTo).contains(playerGuild)) {
			C.sendMessage(player, "Clan", "Ya has invitado a ese jugador a tu clan.");
			return;
		}

		if (!playerSendToGuild.equals("") && playerSendToGuild.equals(playerGuild)) {
			C.sendMessage(player, "Clan", "Este jugador ya está en tu clan.");
			return;
		}

		if (!playerSendToGuild.equals("")) {
			C.sendMessage(player, "Clan", "Este jugador ya está en un clan.");
			return;
		}

		if (Settings.getSettings(playerSendTo.getUniqueId()).getBoolean("guildsrequets")) {
			guildInvitations.put(playerSendTo, guildInvitations.get(playerSendTo) + " " + playerGuild);
			TextComponent accept = new TextComponent("§a§lACEPTAR");
			accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan aceptar " + playerGuild));
			accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClick para unirte al clan.").create()));

			TextComponent b = new TextComponent(" §7| ");
			accept.addExtra(b);

			TextComponent decline = new TextComponent("§c§lDENEGAR");
			decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan denegar " + playerGuild));
			decline.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClick para denegar la solicitud.").create()));
			accept.addExtra(decline);

			C.sendMessage(playerSendTo, "Clan", C.cYellow + player.getName() + C.cGray + " te ha invitado a su clan.");
			playerSendTo.spigot().sendMessage(accept);
			C.sendMessage(player, "Clan", C.cYellow + playerSendTo.getName() + C.cGray + " fue invitado a tu clan, esperando respuesta.");
		} else {
			C.sendMessage(player, "Clan", C.cYellow + playerSendTo.getName() + C.cGray + " tiene desactivadas las invitaciones.");
		}
	}

	public static void joinGuild(Player player, String guild) {
		String playerGuild = Guilds.getGuild(player.getUniqueId());

		if (guildInvitations.get(player) == null) {
			C.sendMessage(player, "Clan", "No fuiste invitado a ningún clan.");
			return;
		}
		
		if (!guildInvitations.get(player).contains(guild)) {
			C.sendMessage(player, "Clan", "No fuiste invitado a este clan.");
			return;
		}

		if (!playerGuild.equals("")) {
			C.sendMessage(player, "Clan", "Ya estás en un clan, debes salirte para unirte a otro.");
			return;
		}

		GuildsYML guildsyml = new GuildsYML();
		FileConfiguration conf = guildsyml.get();
		if (Guilds.getGuildMembers(guild, true).size() < Guilds.getGuildConfig(guild).getInt("limit")) {
			conf.set("players." + player.getUniqueId() + ".guild", guild);
			conf.set("players." + player.getUniqueId() + ".name", player.getName());
			conf.set("players." + player.getUniqueId() + ".rank", "user");
			guildInvitations.put(player, guildInvitations.get(player).replace(guild, ""));
			guildsyml.save();
			for (String ps : Guilds.getGuildMembers(guild, true)) {
				Player o = Bukkit.getPlayer(UUID.fromString(ps));
				if (o == null) continue;
				C.sendMessage(o, "Clan", C.cYellow + player.getName() + C.cGray + " se ha unido al clan.");
			}
		} else {
			C.sendMessage(player, "Clan", "Lo sentimos, este Clan se encuentra lleno.");
		}
	}

	public static void leaveGuild(Player player, Boolean kicked) {
		String playerGuild = Guilds.getGuild(player.getUniqueId());
		
		if (playerGuild.equals("")) {
			C.sendMessage(player, "Clan", "No estás en ningún clan.");
			return;
		}
		
		GuildsYML guildsyml = new GuildsYML();
		FileConfiguration conf = guildsyml.get();

		if (conf.getString("guilds." + playerGuild + ".owner").equals(player.getUniqueId().toString())) {
			//disbandGuild(player, true);
			C.sendMessage(player, "Clan", "Eres el dueño del Clan, si quieres salirte del mismo, debes eliminarlo primero. La propiedad de éste no puede ser transferida.");
			return;
		}

		for (String ps : Guilds.getGuildMembers(playerGuild, true)) {
			Player o = Bukkit.getPlayer(UUID.fromString(ps));
			if (o == null) continue;
			if (kicked) {
				C.sendMessage(o, "Clan", C.cYellow + player.getName() + C.cGray + " ha sido expulsado del clan.");
			} else {
				C.sendMessage(o, "Clan", C.cYellow + player.getName() + C.cGray + " ha salido del clan.");
			}
		}
		conf.set("players." + player.getUniqueId(), null);
		guildsyml.save();
	}

	public static void disbandGuild(Player player, Boolean confirmed) {
		if (!confirmed) {
			TextComponent accept = new TextComponent(" §c§lELIMINAR");
			accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan eliminar " + player.getUniqueId()));
			accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClick para eliminar el clan.").create()));
			C.blank(player);
			C.sendMessage(player, "Clan", C.cRed + "Al eliminar un clan, ten en cuenta que nunca más podrás recuperarlo. Todos los integrantes del clan se quedarían sin este y el nombre del clan puede volverse a utilizar por otra persona. ¿Seguro que quieres eliminar tu clan?");
			player.spigot().sendMessage(accept);
			C.blank(player);
			return;
		}
		String playerGuild = Guilds.getGuild(player.getUniqueId());
		if (playerGuild.equals("")) {
			C.sendMessage(player, "Clan", "No estás en ningún clan.");
			return;
		}
		GuildsYML guildsyml = new GuildsYML();
		FileConfiguration conf = guildsyml.get();
		if (!conf.getString("guilds." + playerGuild + ".owner").equals(player.getUniqueId().toString())) {
			C.sendMessage(player, "Clan", "No eres el dueño del clan.");
			return;
		}
		for (String uuids : Guilds.getGuildMembers(playerGuild, true)) {
			Player o = Bukkit.getPlayer(UUID.fromString(uuids));
			conf.set("players." + uuids, null);
			if (o != null && player != o) {
				o.closeInventory();
				C.sendMessage(o, "Clan", "El clan en el que estabas acaba de ser eliminado.");
			}
		}
		conf.set("guilds." + playerGuild, null);
		guildsyml.save();
		C.sendMessage(player, "Clan", "El clan fue eliminado.");
		player.closeInventory();
	}
	
	public static void kickFromGuild(Player player, UUID toKick) {
		String playerGuild = Guilds.getGuild(player.getUniqueId());
		if (playerGuild.equals("")) {
			C.sendMessage(player, "Clan", "No estás en ningún clan.");
			return;
		}
		GuildsYML guildsyml = new GuildsYML();
		FileConfiguration conf = guildsyml.get();
		if (!conf.getString("guilds." + playerGuild + ".owner").equals(player.getUniqueId().toString())) {
			C.sendMessage(player, "Clan", "No eres el dueño del clan.");
			return;
		}
		if (toKick == null) {
			C.sendMessage(player, "Clan", "El jugador que estás buscando parece no estar conectado, si deseas eliminarlo de todos modos, te recomendamos que lo hagas mediante el menú del Clan.");
			return;
		}
		String kickGuild = Guilds.getGuild(toKick);
		if (!kickGuild.equals(playerGuild)) {
			C.sendMessage(player, "Clan", "Ese jugador no está en tu clan.");
			return;
		}
		String name = conf.getString("players." + toKick + ".name");
		for (String ps : Guilds.getGuildMembers(playerGuild, true)) {
			Player o = Bukkit.getPlayer(UUID.fromString(ps));
			if (o == null) continue;
			C.sendMessage(o, "Clan", C.cYellow + name + C.cGray + " ha sido expulsado del clan.");
		}
		conf.set("players." + toKick, null);
		guildsyml.save();
	}
}
