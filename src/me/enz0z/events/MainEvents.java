package me.enz0z.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import me.enz0z.Core;
import me.enz0z.systems.Ranks;
import me.enz0z.utils.PacketSender;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class MainEvents implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void AsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
		if (event.getName().equals("SamuelCnc3")) {
			event.disallow(Result.KICK_BANNED, "No permitido en este servidor.");
		}
	}

	@EventHandler
	public void PlayerJoinEvent(PlayerJoinEvent event) {
		event.setJoinMessage("");
		Player player = event.getPlayer();
		if (!player.hasPlayedBefore()) {
			player.teleport(player.getWorld().getSpawnLocation().add(0.5, 0, 0.5));
		}
		player.setGameMode(GameMode.SURVIVAL);
		PacketSender.PacketPlayOutTitle(player, "&6&l" + Core.getPluginFullName().replace("MA", ""), "&f----------------", 0, 100, 30);
		
		for (Player players : Bukkit.getOnlinePlayers()) {
			Ranks.setRankTag(players);
		}
	}
	
	@EventHandler
	public void PlayerQuitEvent(PlayerQuitEvent event) {
		event.setQuitMessage("");
	}
	
	@EventHandler
	public void PlayerDeathEvent(PlayerDeathEvent event) {
		event.setDeathMessage("");
	}
	
	@EventHandler
	public void PlayerRespawnEvent(PlayerRespawnEvent event) {
		event.setRespawnLocation(event.getPlayer().getWorld().getSpawnLocation().add(0.5, 0, 0.5));
	}
	
	@EventHandler
	public void BlockBreakEvent(BlockBreakEvent event) {
		if (!event.getPlayer().hasPermission("fullpvp.break")) {
			event.setCancelled(true);
		} else {
			if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void BlockPlaceEvent(BlockPlaceEvent event) {
		if (!event.getPlayer().hasPermission("fullpvp.place")) {
			event.setCancelled(true);
		} else {
			if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void PlayerInteractEvent(PlayerInteractEvent event) {
		if (event.getClickedBlock() == null) return;
		if (event.getClickedBlock().getType() == Material.BEACON
				|| event.getClickedBlock().getType() == Material.FURNACE 
				|| event.getClickedBlock().getType() == Material.BURNING_FURNACE 
				|| event.getClickedBlock().getType() == Material.HOPPER 
				|| event.getClickedBlock().getType() == Material.LEVER) {
			event.setCancelled(true);
		}
	}
}