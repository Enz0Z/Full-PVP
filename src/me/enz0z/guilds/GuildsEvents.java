package me.enz0z.guilds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.enz0z.files.GuildsYML;
import me.enz0z.systems.Ranks;
import me.enz0z.utils.C;

public class GuildsEvents implements Listener {

	@EventHandler
	public void AsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String part = "";
		String name = Ranks.getRank(player) + " " + player.getDisplayName();
		String msg = event.getMessage();
		String guild = Guilds.getGuild(player.getUniqueId());
		
		if (player.hasPermission("rank.colorchat")) msg = ChatColor.translateAlternateColorCodes('&', msg);
		
		if (msg.startsWith("@")) {
			event.setCancelled(true);
			if (Guilds.getGuildConfig(guild).getBoolean("clanchat") == true) {
				for (String members : Guilds.getGuildMembers(guild, false)) {
					Player o = Bukkit.getPlayerExact(members);
					if (o == null) continue;
					o.sendMessage(C.c("&6&lCLAN &7" + player.getDisplayName() + "&f: ") + msg.substring(1));
				}
			} else {
				C.sendMessage(player, "Clan", "El &eClan Chat &7se encuentra actualmente &cdesactivado&7.");
			}
		} else {
			if (!guild.equals("")) {
				part = "§6[" + guild + "§6] §7" + name + "§f: ";
			} else {
				part = "§7" + name + "§f: ";
			}

			event.setFormat(part + msg);
		}
	}
	
	@EventHandler
	public void PlayerJoinEvent(PlayerJoinEvent event) {
		String playerGuild = Guilds.getGuild(event.getPlayer().getUniqueId());
		if (!playerGuild.equals("") && Guilds.getGuildConfig(playerGuild).getBoolean("announcejoinquit") == true) {
			for (String members : Guilds.getGuildMembers(playerGuild, false)) {
				Player o = Bukkit.getPlayerExact(members);
				if (o == null) continue;
				o.sendMessage(C.c("&6&lCLAN &7" + event.getPlayer().getDisplayName() + " &fentró al servidor."));
			}
		}
	}
	
	@EventHandler
	public void PlayerQuitEvent(PlayerQuitEvent event) {
		String playerGuild = Guilds.getGuild(event.getPlayer().getUniqueId());
		if (!playerGuild.equals("") && Guilds.getGuildConfig(playerGuild).getBoolean("announcejoinquit") == true) {
			for (String members : Guilds.getGuildMembers(playerGuild, false)) {
				Player o = Bukkit.getPlayerExact(members);
				if (o == null) continue;
				o.sendMessage(C.c("&6&lCLAN &7" + event.getPlayer().getDisplayName() + " &fsalió al servidor."));
			}
		}
	}
	
	@EventHandler
	public void PlayerDeathEvent(PlayerDeathEvent event) {
		if (event.getEntity() != null) {
			Player player = event.getEntity();
			String playerGuild = Guilds.getGuild(player.getUniqueId());
			if (!playerGuild.equals("")) {				
				GuildsYML guildyml = new GuildsYML();
				FileConfiguration c = guildyml.get();
				ConfigurationSection guild = c.getConfigurationSection("guilds." + playerGuild);
				guild.set("deaths", guild.getInt("deaths") + 1);
				guildyml.save();
			}
		}
			
		if (event.getEntity().getKiller() != null) {
			Player killer = event.getEntity().getKiller();
			String killerGuild = Guilds.getGuild(killer.getUniqueId());
			if (!killerGuild.equals("")) {				
				GuildsYML guildyml = new GuildsYML();
				FileConfiguration c = guildyml.get();
				ConfigurationSection guild = c.getConfigurationSection("guilds." + killerGuild);
				guild.set("kills", guild.getInt("kills") + 1);
				guildyml.save();
			}
		}
	}

	@EventHandler
	public void EntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		
		if (event.getEntity().getType() != EntityType.PLAYER) return;
		if (((Player) event.getEntity()).getGameMode() == GameMode.CREATIVE) return;
		if ((event.getDamager() != null) && (event.getDamager().getType() == EntityType.PLAYER)) {
			if (((Player) event.getDamager()).getGameMode() == GameMode.CREATIVE) return;
		}
		
		if (event.getDamager() instanceof Arrow) {
			Player player = (Player) event.getEntity();
			Player damager = ((Player) ((Arrow) event.getDamager()).getShooter());
			if (player == damager) return;
			
			String playerGuild = Guilds.getGuild(player.getUniqueId());
			String damagerGuild = Guilds.getGuild(damager.getUniqueId());

			if (playerGuild.equals("") && damagerGuild.equals("")) {
				damager.sendMessage(C.c("&e" + player.getName() + " &7tiene &c" + ((int) player.getHealth()) + " �?�"));
				return;
			}
			
			if (playerGuild.equals(damagerGuild)) {
				if (Guilds.getGuildConfig(playerGuild).getBoolean("friendlyfire") == false) {
					event.setCancelled(true);
					return;
				} else {
					damager.sendMessage(C.c("&e" + player.getName() + " &7tiene &c" + ((int) player.getHealth()) + " �?�"));
				}
			} else {
				if (!damagerGuild.equals("")) {				
					GuildsYML guildyml = new GuildsYML();
					FileConfiguration c = guildyml.get();
					ConfigurationSection guild = c.getConfigurationSection("guilds." + damagerGuild);
					guild.set("bowhits", guild.getInt("bowhit") + 1);
					guildyml.save();
				}
				damager.sendMessage(C.c("&e" + player.getName() + " &7tiene &c" + ((int) player.getHealth()) + " �?�"));
			}
		} else if (event.getDamager() instanceof Egg) {
			Player player = (Player) event.getEntity();
			Player damager = ((Player) ((Egg) event.getDamager()).getShooter());
			if (player == damager) return;
			
			String playerGuild = Guilds.getGuild(player.getUniqueId());
			String damagerGuild = Guilds.getGuild(damager.getUniqueId());

			if (playerGuild.equals("") && damagerGuild.equals("")) return;
			if (playerGuild.equals(damagerGuild)) {
				if (Guilds.getGuildConfig(playerGuild).getBoolean("friendlyfire") == false) {
					event.setCancelled(true);
					return;
				}
			}
		} else if (event.getDamager() instanceof Snowball) {
			Player player = (Player) event.getEntity();
			Player damager = ((Player) ((Snowball) event.getDamager()).getShooter());
			if (player == damager) return;
			
			String playerGuild = Guilds.getGuild(player.getUniqueId());
			String damagerGuild = Guilds.getGuild(damager.getUniqueId());

			if (playerGuild.equals("") && damagerGuild.equals("")) return;
			if (playerGuild.equals(damagerGuild)) {
				if (Guilds.getGuildConfig(playerGuild).getBoolean("friendlyfire") == false) {
					event.setCancelled(true);
					return;
				}
			}
		} else if (event.getDamager() instanceof FishHook) {
			Player player = (Player) event.getEntity();
			Player damager = ((Player) ((FishHook) event.getDamager()).getShooter());
			if (player == damager) return;
			
			String playerGuild = Guilds.getGuild(player.getUniqueId());
			String damagerGuild = Guilds.getGuild(damager.getUniqueId());
			
			if (playerGuild.equals("") && damagerGuild.equals("")) return;
			if (playerGuild.equals(damagerGuild)) {
				if (Guilds.getGuildConfig(playerGuild).getBoolean("friendlyfire") == false) {
					event.setCancelled(true);
					return;
				}
			}
		}
		
		if (event.getDamager().getType() != EntityType.PLAYER) return;
		Player player = (Player) event.getEntity();
		Player damager = (Player) event.getDamager();
		if (player == damager) return;
		
		String playerGuild = Guilds.getGuild(player.getUniqueId());
		String damagerGuild = Guilds.getGuild(damager.getUniqueId());

		if (playerGuild.equals("") && damagerGuild.equals("")) return;
		if (playerGuild.equals(damagerGuild)) {
			if (Guilds.getGuildConfig(playerGuild).getBoolean("friendlyfire") == false) {
				event.setCancelled(true);
				return;
			}
		}
	}
}