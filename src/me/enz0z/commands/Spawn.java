package me.enz0z.commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.enz0z.systems.CombatLog;
import me.enz0z.utils.C;
import me.enz0z.utils.PacketSender;
import me.enz0z.utils.S;
import me.enz0z.utils.U;
import me.enz0z.utils.UpdateEventer.UpdateType;

public class Spawn implements CommandExecutor, Listener {

	public static HashMap<UUID, Long> spawnTimer = new HashMap<>();
	public static HashMap<UUID, Location> lastLocation = new HashMap<>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
		if (CombatLog.isCombatLog(player)) {
			C.sendMessage(player, "CombatLog", "Debes esperar a que se acabe tu tiempo de CombatLog.");
			return false;
		}
		Integer time = 10;
		if (U.isCurrentRegion(player.getLocation(), "Spawn")) {
			time = 3;
		}
		spawnTimer.put(player.getUniqueId(), S.currentTimeSeconds() + time);
		lastLocation.put(player.getUniqueId(), new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()));
		PacketSender.PacketPlayOutTitle(player, "&aTeletransportandote", "&f&l" + time, 0, 20, 10);
		return false;
	}
	
	@EventHandler
	private void UpdateEvent(me.enz0z.utils.UpdateEventer.UpdateEvent event) {
		if (event.getType() == UpdateType.FAST) {
			for (Player players : event.getPlayers()) {
				if (spawnTimer.containsKey(players.getUniqueId()) && lastLocation.containsKey(players.getUniqueId())) {
					if (S.elapsed(spawnTimer.get(players.getUniqueId())) > 0) {
						if (lastLocation.get(players.getUniqueId()).equals(new Location(players.getWorld(), players.getLocation().getBlockX(), players.getLocation().getBlockY(), players.getLocation().getBlockZ()))) {
							PacketSender.PacketPlayOutTitle(players, "&aTeletransportandote", "&f&l" + S.elapsed(spawnTimer.get(players.getUniqueId())), 0, 20, 10);
						} else {
							PacketSender.PacketPlayOutTitle(players, " ", " ", 0, 1, 0);
							C.sendMessage(players, "Spawn", "Te has movido.");
							spawnTimer.remove(players.getUniqueId());
							lastLocation.remove(players.getUniqueId());
						}
					} else {
						players.teleport(players.getWorld().getSpawnLocation().add(0.5, 0, 0.5));
						spawnTimer.remove(players.getUniqueId());
						lastLocation.remove(players.getUniqueId());
					}
				}
			}
		}
	}
	
	/*@EventHandler
	public void EntityDamageEvent(org.bukkit.event.entity.EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (U.isCurrentRegion(player.getLocation(), "Spawn")) {
				if (event.getCause() == DamageCause.FALL) {
					event.setCancelled(true);
				}
			}
		}
	}*/
}
