package me.enz0z.systems;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import me.enz0z.utils.C;
import me.enz0z.utils.PacketSender;
import me.enz0z.utils.S;
import me.enz0z.utils.U;
import me.enz0z.utils.UpdateEventer.UpdateType;

public class CombatLog implements CommandExecutor, Listener {

	private static HashMap<UUID, Long> log = new HashMap<>();
	
	public static Boolean isCombatLog(Player player) {
		return log.containsKey(player.getUniqueId());
	}
	
	@EventHandler
	public void PlayerRespawnEvent(PlayerRespawnEvent event) {
		log.remove(event.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void PlayerQuitEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		Location location = player.getLocation();
		if (CombatLog.isCombatLog(player)) {
			log.remove(player.getUniqueId());
			player.teleport(player.getWorld().getSpawnLocation().add(0.5, 0, 0.5));
			for (ItemStack items : U.getInventory(player)) {
				location.getWorld().dropItemNaturally(location, items);
			}
			U.restartAll(player);
			for (Player players : Bukkit.getOnlinePlayers()) {
				C.sendMessage(players, "CombatLog", "&e" + player.getName() + " &7se ha desconectado con el CombatLog activado, sus items están en &aX: " + location.getBlockX() + " Y: " + location.getBlockY() + " Z: " + location.getBlockZ() + "&7.");
			}
		}
	}
	
	@EventHandler
	private void EntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		Player damager = null;
		if (event.getDamager() instanceof Arrow) {
			damager = ((Player) ((Arrow) event.getDamager()).getShooter());
		} else if (event.getDamager() instanceof Egg) {
			damager = ((Player) ((Egg) event.getDamager()).getShooter());
		} else if (event.getDamager() instanceof Snowball) {
			damager = ((Player) ((Snowball) event.getDamager()).getShooter());
		} else if (event.getDamager() instanceof FishHook) {
			damager = ((Player) ((FishHook) event.getDamager()).getShooter());
		} else if (event.getDamager() instanceof Player) {
			 damager = ((Player) event.getDamager());
		}
		if (event.getEntity() instanceof Player && damager != null) {
			if (event.isCancelled()) return;
			log.put(((Player) event.getEntity()).getUniqueId(), S.currentTimeSeconds() + 20);
			log.put((damager).getUniqueId(), S.currentTimeSeconds() + 20);
		}
	}
	
	@EventHandler
	private void UpdateEvent(me.enz0z.utils.UpdateEventer.UpdateEvent event) {
		if (event.getType() == UpdateType.FAST) {
			for (Player players : event.getPlayers()) {
				if (log.containsKey(players.getUniqueId())) {
					if (S.elapsed(log.get(players.getUniqueId())) > 0) {
						PacketSender.PacketPlayOutChat(players, C.loadingBar(20, log.get(players.getUniqueId()).intValue() - S.currentTimeSeconds().intValue()));
					} else {
						PacketSender.PacketPlayOutChat(players, "&c¡Ya no estás en combate!");
						log.remove(players.getUniqueId());
					}
				}
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
		if (args == null || args.length != 2) {
			if (player.hasPermission("fullpvp.combatlog")) {
				C.sendUsageMessage(player, "combatlog <clear/check> <jugador/all>");
			} else {
				C.sendNoPerm(player);
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("clear")) {
				if (player.hasPermission("fullpvp.combatlog")) {
					if (args[1].equalsIgnoreCase("all")) {
						for (Player players : Bukkit.getOnlinePlayers()) {
							if (log.containsKey(players.getUniqueId())) {
								log.remove(players.getUniqueId());
								C.sendMessage(players, "CombatLog", "Tu CombatLog ha sido borrado por &e" + player.getName() + "&7.");
							}
							C.sendMessage(player, "CombatLog", "Has borrado el CombatLog de &e" + players.getName() + "&7.");
						}
					} else {
						Player target = S.getMarchPlayers(player, args[1]);
						if (target != null) {
							log.remove(target.getUniqueId());
							C.sendMessage(target, "CombatLog", "Tu CombatLog ha sido borrado por &e" + player.getName() + "&7.");
							C.sendMessage(player, "CombatLog", "Has borrado el CombatLog de &e" + target.getName() + "&7.");
						}
					}
				} else {
					C.sendNoPerm(player);
				}
			} else if (args[0].equalsIgnoreCase("check")) {
				if (player.hasPermission("fullpvp.combatlog")) {
					if (args[1].equalsIgnoreCase("all")) {
						Integer combatloggers = 0;
						Integer not_combatloggers = 0;
						for (Player players : Bukkit.getOnlinePlayers()) {
							if (log.containsKey(players.getUniqueId())) {
								combatloggers++;
							} else {
								not_combatloggers++;
							}
						}

						String CTS = "null";
						if (combatloggers == 0) CTS = "0 jugadores";
						if (combatloggers == 1) CTS = "1 jugador";
						if (combatloggers > 1) CTS = combatloggers + " jugadores";
						
						String NOT_CTS = "null";
						if (not_combatloggers == 0) NOT_CTS = "0 jugadores";
						if (not_combatloggers == 1) NOT_CTS = "1 jugador";
						if (not_combatloggers > 1) NOT_CTS = not_combatloggers + " jugadores";
						
						C.sendMessage(player, "CombatLog", "Hay &e" + CTS + " &7con el CombatLog &aactivo&7.");
						C.sendMessage(player, "CombatLog", "Hay &e" + NOT_CTS + " &7con el CombatLog &cdesactivado&7.");
					} else {
						Player target = S.getMarchPlayers(player, args[1]);
						if (target != null) {
							C.sendMessage(player, "CombatLog", "&e" + target.getName() + " &7 tiene " + (log.containsKey(target.getUniqueId()) ? "&aactivado" : "&cdesactivado") + " &7el CombatLog.");
						}
					}
				} else {
					C.sendNoPerm(player);
				}
			} else if (args[0].equalsIgnoreCase("add")) {
				if (player.hasPermission("fullpvp.combatlog")) {
					if (args[1].equalsIgnoreCase("all")) {
						for (Player players : Bukkit.getOnlinePlayers()) {
							log.put(players.getUniqueId(), S.currentTimeSeconds() + 20);
							C.sendMessage(players, "CombatLog", "Tu CombatLog ha sido agregado por &e" + player.getName() + "&7.");
							C.sendMessage(player, "CombatLog", "Has agregado el CombatLog de &e" + players.getName() + "&7.");
						}
					} else {
						Player target = S.getMarchPlayers(player, args[1]);
						if (target != null) {
							log.put(target.getUniqueId(), S.currentTimeSeconds() + 20);
							C.sendMessage(target, "CombatLog", "Tu CombatLog ha sido agregado por &e" + player.getName() + "&7.");
							C.sendMessage(player, "CombatLog", "Has agregado el CombatLog de &e" + target.getName() + "&7.");
						}
					}
				} else {
					C.sendNoPerm(player);
				}
			}
		}
		return false;
	}
}