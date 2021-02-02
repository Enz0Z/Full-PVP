package me.enz0z.commands;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.inventivetalent.menubuilder.inventory.InventoryMenuBuilder;
import org.inventivetalent.menubuilder.inventory.ItemListener;

import me.enz0z.files.SettingsYML;
import me.enz0z.systems.Ranks;
import me.enz0z.utils.C;
import me.enz0z.utils.S;
import me.enz0z.utils.U;

public class Settings implements CommandExecutor, Listener {
	
	private static Long LastChange = S.currentTimeSeconds();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		openMenu((Player) sender);
		return false;
	}
	
	@EventHandler
	public void PlayerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		SettingsYML yml = new SettingsYML();
		FileConfiguration c = yml.get();
		Boolean anyChange = false;
		if (!c.contains("settings." + player.getUniqueId().toString())) {
			c.set("settings." + player.getUniqueId().toString() + ".fireworkjoin", false);
			c.set("settings." + player.getUniqueId().toString() + ".announcejoin", false);
			c.set("settings." + player.getUniqueId().toString() + ".guildsrequets", true);
			c.set("settings." + player.getUniqueId().toString() + ".scoreboard", "Por defecto");
			anyChange = true;
		}
		if (!player.hasPermission("fullpvp.fireworkjoin")) {
			c.set("settings." + player.getUniqueId().toString() + ".fireworkjoin", false);
			anyChange = true;
		}
		if (!player.hasPermission("fullpvp.announcejoin")) {
			c.set("settings." + player.getUniqueId().toString() + ".announcejoin", false);
			anyChange = true;
		}
		if (anyChange) {
			yml.save();
		}
		
		if (getSettings(player.getUniqueId()).getBoolean("fireworkjoin")) {
			Firework f = (Firework) player.getLocation().getWorld().spawn(player.getLocation(), Firework.class);
		    FireworkMeta m = f.getFireworkMeta();
		    Color color = Color.fromRGB(new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256));
		    m.addEffect(FireworkEffect.builder().withColor(color).build());
		    f.setFireworkMeta(m);
		}
		
		if (getSettings(player.getUniqueId()).getBoolean("announcejoin")) {
			for (Player onlineplayers : Bukkit.getOnlinePlayers()) {
				onlineplayers.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a¡" + Ranks.getRank(player) + " " + player.getDisplayName() + " &fse ha unido al servidor&a!"));
			}
		}
	}
	
	private static void openMenu(Player player) {
		InventoryMenuBuilder menu = new InventoryMenuBuilder().withSize(36).withTitle("Preferencias");
		SettingsYML yml = new SettingsYML();
		FileConfiguration c = yml.get();
		ConfigurationSection settings = c.getConfigurationSection("settings." + player.getUniqueId().toString());
		
		Boolean fireworkjoin = settings.getBoolean("fireworkjoin");
		Boolean announcejoin = settings.getBoolean("announcejoin");
		Boolean guildsrequets = settings.getBoolean("guildsrequets");
		String scoreboard = settings.getString("scoreboard");
		
		menu.withItem(10, U.createItem(Material.FIREWORK, false, "&bFuego artificial ¦ " + (fireworkjoin ? "&aActivado" : "&cDesactivado"),
				" ",
				"Cada vez que entres a un lobby, tirarás",
				"un cohete/fuego artificial.",
				" ",
				"&e¡Solo para los rangos &3Poseidón&e!",
				" ",
				"&aClick para alternar"),
				new ItemListener() {
		            @Override
		            public void onInteract(Player player, ClickType action, ItemStack item) {
		            	if (player.hasPermission("fullpvp.fireworkjoin")) {
		                	settings.set("fireworkjoin", !fireworkjoin);
		                	yml.save();
		                	openMenu(player);
		            	} else {
		            		C.sendNoPerm(player);
		            	}
		            }
		        }, ClickType.LEFT);
		
		menu.withItem(19, U.createSwitchItem(fireworkjoin, "&bFuego artificial ¦ " + (fireworkjoin ? "&aActivado" : "&cDesactivado"),
				" ",
				"Cada vez que entres a un lobby, tirarás",
				"un cohete/fuego artificial.",
				" ",
				"&e¡Solo para los rangos &3Poseidón&e!",
				" ",
				"&aClick para alternar"),
				new ItemListener() {
		            @Override
		            public void onInteract(Player player, ClickType action, ItemStack item) {
		            	if (player.hasPermission("fullpvp.fireworkjoin")) {
		                	settings.set("fireworkjoin", !fireworkjoin);
		                	yml.save();
		                	openMenu(player);
		            	} else {
		            		C.sendNoPerm(player);
		            	}
		            }
		        }, ClickType.LEFT);
		
		menu.withItem(11, U.createItem(Material.PAPER, false, "&bMensaje al entrar ¦ " + (announcejoin ? "&aActivado" : "&cDesactivado"),
				" ",
				"Cada vez que entres a un lobby, aparecerá",
				"un mensaje diciendo que has entrado.",
				" ",
				"&e¡Solo para los rangos &3Poseidón&e!",
				" ",
				"&aClick para alternar"),
				new ItemListener() {
		            @Override
		            public void onInteract(Player player, ClickType action, ItemStack item) {
		            	if (player.hasPermission("fullpvp.announcejoin")) {
		                	settings.set("announcejoin", !announcejoin);
		                	yml.save();
		                	openMenu(player);
		            	} else {
		            		C.sendNoPerm(player);
		            	}
		            }
		        }, ClickType.LEFT);
		
		menu.withItem(20, U.createSwitchItem(announcejoin, "&bMensaje al entrar ¦ " + (announcejoin ? "&aActivado" : "&cDesactivado"),
				" ",
				"Cada vez que entres a un lobby, aparecerá",
				"un mensaje diciendo que has entrado.",
				" ",
				"&e¡Solo para los rangos &3Poseidón&e!",
				" ",
				"&aClick para alternar"),
				new ItemListener() {
		            @Override
		            public void onInteract(Player player, ClickType action, ItemStack item) {
		            	if (player.hasPermission("fullpvp.announcejoin")) {
		                	settings.set("announcejoin", !announcejoin);
		                	yml.save();
		                	openMenu(player);
		            	} else {
		            		C.sendNoPerm(player);
		            	}
		            }
		        }, ClickType.LEFT);
		
		menu.withItem(12, U.createItem(Material.BANNER, false, "&bPeticiones de Clan ¦ " + (guildsrequets ? "&aActivado" : "&cDesactivado"),
				" ",
				"Puedes cambiar, si deseas recibir las",
				"notificaciones a Clanes en el caso de",
				"que no te encuentres en ninguno.",
				" ",
				"&aClick para alternar"),
				new ItemListener() {
		            @Override
		            public void onInteract(Player player, ClickType action, ItemStack item) {
	                	settings.set("guildsrequets", !guildsrequets);
	                	yml.save();
	                	openMenu(player);
		            }
		        }, ClickType.LEFT);
		
		menu.withItem(21, U.createSwitchItem(guildsrequets, "&bPeticiones de Clan ¦ " + (guildsrequets ? "&aActivado" : "&cDesactivado"),
				" ",
				"Puedes cambiar, si deseas recibir las",
				"notificaciones a Clanes en el caso de",
				"que no te encuentres en ninguno.",
				" ",
				"&aClick para alternar"),
				new ItemListener() {
		            @Override
		            public void onInteract(Player player, ClickType action, ItemStack item) {
	                	settings.set("guildsrequets", !guildsrequets);
	                	yml.save();
	                	openMenu(player);
		            }
		        }, ClickType.LEFT);
		
		switch (scoreboard) {
			case "Por defecto":
				menu.withItem(13, U.createItem(Material.ITEM_FRAME, false, "&bScoreboard ¦ &a" + scoreboard,
						" ",
						"Puedes cambiar, si deseas el diseño",
						"del Scoreboard a Por defecto, Modo PVP",
						"y Desactivado.",
						" ",
						"&aClick para alternar"),
						new ItemListener() {
				            @Override
				            public void onInteract(Player player, ClickType action, ItemStack item) {
			                	settings.set("scoreboard", "Modo PVP");
			                	yml.save();
			                	openMenu(player);
				            }
				        }, ClickType.LEFT);
				
				menu.withItem(22, U.createItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5), false, "&bScoreboard ¦ &a" + scoreboard,
						" ",
						"Puedes cambiar, si deseas el diseño",
						"del Scoreboard a Por defecto, Modo PVP",
						"y Desactivado.",
						" ",
						"&aClick para alternar"),
						new ItemListener() {
				            @Override
				            public void onInteract(Player player, ClickType action, ItemStack item) {
			                	settings.set("scoreboard", "Modo PVP");
			                	yml.save();
			                	openMenu(player);
				            }
				        }, ClickType.LEFT);
				
				break;
			case "Modo PVP":
				menu.withItem(13, U.createItem(Material.ITEM_FRAME, false, "&bScoreboard ¦ &e" + scoreboard,
						" ",
						"Puedes cambiar, si deseas el diseño",
						"del Scoreboard a Por defecto, Modo PVP",
						"y Desactivado.",
						" ",
						"&aClick para alternar"),
						new ItemListener() {
				            @Override
				            public void onInteract(Player player, ClickType action, ItemStack item) {
			                	settings.set("scoreboard", "Desactivado");
			                	yml.save();
			                	openMenu(player);
				            }
				        }, ClickType.LEFT);
				
				menu.withItem(22, U.createItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4), false, "&bScoreboard ¦ &e" + scoreboard,
						" ",
						"Puedes cambiar, si deseas el diseño",
						"del Scoreboard a Por defecto, Modo PVP",
						"y Desactivado.",
						" ",
						"&aClick para alternar"),
						new ItemListener() {
				            @Override
				            public void onInteract(Player player, ClickType action, ItemStack item) {
			                	settings.set("scoreboard", "Desactivado");
			                	yml.save();
			                	openMenu(player);
				            }
				        }, ClickType.LEFT);
				break;
			case "Desactivado":
				menu.withItem(13, U.createItem(Material.ITEM_FRAME, false, "&bScoreboard ¦ &c" + scoreboard,
						" ",
						"Puedes cambiar, si deseas el diseño",
						"del Scoreboard a Por defecto, Modo PVP",
						"y Desactivado.",
						" ",
						"&aClick para alternar"),
						new ItemListener() {
				            @Override
				            public void onInteract(Player player, ClickType action, ItemStack item) {
			                	settings.set("scoreboard", "Por defecto");
			                	yml.save();
			                	openMenu(player);
				            }
				        }, ClickType.LEFT);
				
				menu.withItem(22, U.createItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14), false, "&bScoreboard ¦ &c" + scoreboard,
						" ",
						"Puedes cambiar, si deseas el diseño",
						"del Scoreboard a Por defecto, Modo PVP",
						"y Desactivado.",
						" ",
						"&aClick para alternar"),
						new ItemListener() {
				            @Override
				            public void onInteract(Player player, ClickType action, ItemStack item) {
			                	settings.set("scoreboard", "Por defecto");
			                	yml.save();
			                	openMenu(player);
				            }
				        }, ClickType.LEFT);
				break;
		}
		
		menu.withItem(16, U.createItem(new ItemStack(Material.STAINED_CLAY, 1, (byte) 1), false, "&bCambiar a de Día",
				" ",
				"Puedes cambiar, si deseas que el",
				"servidor esté de Día o de Noche.",
				" ",
				"&e¡Solo para los rangos &3Poseidón&e!",
				" ",
				"&aClick para alternar"),
				new ItemListener() {
		            @Override
		            public void onInteract(Player player, ClickType action, ItemStack item) {
		            	if (player.hasPermission("fullpvp.changetime")) {
		                	player.closeInventory();
		                	if (LastChange < S.currentTimeSeconds()) {
		                		LastChange = S.currentTimeSeconds() + 1800;
		                		Bukkit.getWorld("Game").setTime(6000);
		                		C.sendMessage(player, "Settings", "Has cambiado el tiempo a de &aDía&7.");
		                	} else {
		                		C.sendMessage(player, "Settings", "Todavía no se puede cambiar el tiempo, debes esperar &a" + U.secToTime(S.elapsed(LastChange)) + "&7.");
		                	}
		            	} else {
		            		C.sendNoPerm(player);
		            	}
		            }
		        }, ClickType.LEFT);
		
		menu.withItem(25, U.createItem(new ItemStack(Material.STAINED_CLAY, 1, (byte) 2), false, "&bCambiar a de Noche",
				" ",
				"Puedes cambiar, si deseas que el",
				"servidor esté de Día o de Noche.",
				" ",
				"&e¡Solo para los rangos &3Poseidón&e!",
				" ",
				"&aClick para alternar"),
				new ItemListener() {
		            @Override
		            public void onInteract(Player player, ClickType action, ItemStack item) {
		            	if (player.hasPermission("fullpvp.changetime")) {
		                	player.closeInventory();
		                	if (LastChange < S.currentTimeSeconds()) {
		                		LastChange = S.currentTimeSeconds() + 1800;
		                		Bukkit.getWorld("Game").setTime(18000);
		                		C.sendMessage(player, "Settings", "Has cambiado el tiempo a de &aNoche&7.");
		                	} else {
		                		C.sendMessage(player, "Settings", "Todavía no se puede cambiar el tiempo, debes esperar &a" + U.secToTime(S.elapsed(LastChange)) + "&7.");
		                	}
		            	} else {
		            		C.sendNoPerm(player);
		            	}
		            }
		        }, ClickType.LEFT);
		
		menu.show(player);
	}
	
	public static ConfigurationSection getSettings(UUID uuid) {
		FileConfiguration c = new SettingsYML().get();
		ConfigurationSection settings = c.getConfigurationSection("settings." + uuid.toString());
		return settings;
	}
}
