package me.enz0z.guilds;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.inventivetalent.menubuilder.inventory.InventoryMenuBuilder;
import org.inventivetalent.menubuilder.inventory.ItemListener;

import me.enz0z.utils.C;
import me.enz0z.utils.U;

public class GuildsMenues {

	public static void openMenu(Player player) {
		String guildName = Guilds.getGuild(player.getUniqueId());
		ConfigurationSection config = Guilds.getGuildConfig(guildName);
		InventoryMenuBuilder menu = new InventoryMenuBuilder().withSize(36).withTitle("Clan");

		java.util.List<String> membersNames = Guilds.getGuildMembers(guildName, false);
		java.util.List<String> membersUUIDs = Guilds.getGuildMembers(guildName, true);
		
		String ownerUUID = config.getString("owner");
		String ownerName = config.getString("owner-name");
		Integer limit = config.getInt("limit");
		Integer kills = config.getInt("kills");
		Integer deaths = config.getInt("deaths");
		Integer bowhits = config.getInt("bowhits");

		Double kdr = Math.round(((float) kills) / ((float) deaths) * 10.0) / 10.0;
		Integer current = membersNames.size();
		
		menu.withItem(4, U.createItem(Material.GOLD_CHESTPLATE, false, "&bEstadísticas",
				" ",
				"&eMiembros: &f" + current + "/" + limit,
				"&eAsesinatos: &f" + kills,
				"&eMuertes: &f" + deaths,
				"&eKDR: &f" + kdr,
				"&eHits con Arco: &f" + bowhits,
				"&eCreador: &f" + ownerName,
				" ",
				"&aClick para ver la configuración"), 
				new ItemListener() {
		            @Override
		            public void onInteract(Player player, ClickType action, ItemStack item) {
		                openSettingsMenu(player);
		            }
        		}, ClickType.LEFT);
		
		if (!ownerUUID.equals(player.getUniqueId().toString())) {
			menu.withItem(5, U.createItem(Material.REDSTONE, false, "&bSalir del Clan",
					" ",
					"Al salir de un clan, ten en cuenta que",
					"nunca más podrás entrar, al menos que te",
					"vuelvan a invitar al mismo.",
					" ",
					"&aShift-Click para salir"), 
					new ItemListener() {
			            @Override
			            public void onInteract(Player player, ClickType action, ItemStack item) {
			                GuildsManager.leaveGuild(player, false);
			            }
	        		}, ClickType.SHIFT_LEFT);
		}
		
		Integer count = 0;
		for (int i = 0; i < membersNames.size(); i++) {
			//if (ownerUUID.equals(membersUUIDs.get(i))) continue;
			menu.withItem(getSlotItem(count, current), U.createItem(Material.SKULL_ITEM, false, "&b" + membersNames.get(i) + " ¦ " + (Bukkit.getPlayerExact(membersNames.get(i)) != null ? "&aConectado" : "&cDesconectado"),
					" ",
					membersUUIDs.get(i),
					"&aShift-Click para expulsar"), 
					new ItemListener() {
			            @Override
			            public void onInteract(Player player, ClickType action, ItemStack item) {
		        			if (!ownerUUID.equals(ChatColor.stripColor(item.getItemMeta().getLore().get(1)))) {
			                	GuildsManager.kickFromGuild(player, UUID.fromString(ChatColor.stripColor(item.getItemMeta().getLore().get(1))));
			                	openMenu(player);
		        			} else {
			        			C.sendMessage(player, "Clan", "No se puede expulsar al dueño del Clan.");
		        			}
			            }
			        }, ClickType.SHIFT_LEFT);
			count++;
		}
		menu.show(player);
	}
	
	private static void openSettingsMenu(Player player) {
		String guildName = Guilds.getGuild(player.getUniqueId());
		ConfigurationSection config = Guilds.getGuildConfig(guildName);
		InventoryMenuBuilder menu = new InventoryMenuBuilder().withSize(36).withTitle("Configuración");
		
		String ownerUUID = config.getString("owner");
		Boolean friendlyFire = config.getBoolean("friendlyfire");
		Boolean announceJoinQuit = config.getBoolean("announcejoinquit");
		Boolean clanChat = config.getBoolean("clanchat");
		
		menu.withItem(11, U.createItem(Material.DIAMOND_SWORD, false, "&bFuego Amigo ¦ " + (friendlyFire ? "&aActivado" : "&cDesactivado"),
				" ",
				"Puedes cambiar, si deseas que entre",
				"integrantes del Clan se puedan hacer",
				"daño o no.",
				" ",
				"&aClick para alternar"), 
				new ItemListener() {
		            @Override
		            public void onInteract(Player player, ClickType action, ItemStack item) {
		            	if (ownerUUID.equals(player.getUniqueId().toString())) {
		                	Guilds.setGuildConfig(guildName, "friendlyfire", !friendlyFire);
		                    Guilds.sendGuildMessage(guildName, "Se ha " + (!friendlyFire ? "&aactivado" : "&cdesactivado") + " &7el &eFuego Amigo&7.");
		                    openSettingsMenu(player);
		            	} else {
		        			C.sendMessage(player, "Clan", "No eres el dueño del clan.");
		            	}
		            }
		        }, ClickType.LEFT);
		
		menu.withItem(20, U.createSwitchItem(friendlyFire, "&bFuego Amigo ¦ " + (friendlyFire ? "&aActivado" : "&cDesactivado"),
				" ",
				"Puedes cambiar, si deseas que entre",
				"integrantes del Clan se puedan hacer",
				"daño o no.",
				" ",
				"&aClick para alternar"), 
				new ItemListener() {
		            @Override
		            public void onInteract(Player player, ClickType action, ItemStack item) {
		            	if (ownerUUID.equals(player.getUniqueId().toString())) {
		                	Guilds.setGuildConfig(guildName, "friendlyfire", !friendlyFire);
		                    Guilds.sendGuildMessage(guildName, "Se ha " + (!friendlyFire ? "&aactivado" : "&cdesactivado") + " &7el &eFuego Amigo&7.");
		                    openSettingsMenu(player);
		            	} else {
		        			C.sendMessage(player, "Clan", "No eres el dueño del clan.");
		            	}
		            }
		        }, ClickType.LEFT);
		
		menu.withItem(12, U.createItem(Material.ITEM_FRAME, false, "&bAnuncio ¦ " + (announceJoinQuit ? "&aActivado" : "&cDesactivado"),
				" ",
				"Puedes cambiar, si deseas que se les",
				"avise a los miembros del Clan quien",
				"entró y salió del servidor.",
				" ",
				"&aClick para alternar"), 
				new ItemListener() {
		            @Override
		            public void onInteract(Player player, ClickType action, ItemStack item) {
		            	if (ownerUUID.equals(player.getUniqueId().toString())) {
		                	Guilds.setGuildConfig(guildName, "announcejoinquit", !announceJoinQuit);
		                    Guilds.sendGuildMessage(guildName, "Se ha " + (!announceJoinQuit ? "&aactivado" : "&cdesactivado") + " &7el &eAnuncio&7.");
		                    openSettingsMenu(player);
		            	} else {
		        			C.sendMessage(player, "Clan", "No eres el dueño del clan.");
		            	}
		            }
		        }, ClickType.LEFT);
		
		menu.withItem(21, U.createSwitchItem(announceJoinQuit, "&bAnuncio ¦ " + (announceJoinQuit ? "&aActivado" : "&cDesactivado"),
				" ",
				"Puedes cambiar, si deseas que se les",
				"avise a los miembros del Clan quien",
				"entró y salió del servidor.",
				" ",
				"&aClick para alternar"), 
				new ItemListener() {
		            @Override
		            public void onInteract(Player player, ClickType action, ItemStack item) {
		            	if (ownerUUID.equals(player.getUniqueId().toString())) {
		                	Guilds.setGuildConfig(guildName, "announcejoinquit", !announceJoinQuit);
		                    Guilds.sendGuildMessage(guildName, "Se ha " + (!announceJoinQuit ? "&aactivado" : "&cdesactivado") + " &7el &eAnuncio&7.");
		                    openSettingsMenu(player);
		            	} else {
		        			C.sendMessage(player, "Clan", "No eres el dueño del clan.");
		            	}
		            }
		        }, ClickType.LEFT);
		
		menu.withItem(13, U.createItem(Material.PAPER, false, "&bClan Chat ¦ " + (clanChat ? "&aActivado" : "&cDesactivado"),
				" ",
				"Puedes cambiar, si deseas que los",
				"miembros del Clan puedan enviar",
				"mensajes en el chat privado.",
				" ",
				"&aClick para alternar"), 
				new ItemListener() {
		            @Override
		            public void onInteract(Player player, ClickType action, ItemStack item) {
		            	if (ownerUUID.equals(player.getUniqueId().toString())) {
		                	Guilds.setGuildConfig(guildName, "clanchat", !clanChat);
		                    Guilds.sendGuildMessage(guildName, "Se ha " + (!clanChat ? "&aactivado" : "&cdesactivado") + " &7el &eClan Chat&7.");
		                    openSettingsMenu(player);
		            	} else {
		        			C.sendMessage(player, "Clan", "No eres el dueño del clan.");
		            	}
		            }
		        }, ClickType.LEFT);
		
		menu.withItem(22, U.createSwitchItem(clanChat, "&bClan Chat ¦ " + (clanChat ? "&aActivado" : "&cDesactivado"),
				" ",
				"Puedes cambiar, si deseas que los",
				"miembros del Clan puedan enviar",
				"mensajes en el chat privado.",
				" ",
				"&aClick para alternar"), 
				new ItemListener() {
		            @Override
		            public void onInteract(Player player, ClickType action, ItemStack item) {
		            	if (ownerUUID.equals(player.getUniqueId().toString())) {
		                	Guilds.setGuildConfig(guildName, "clanchat", !clanChat);
		                    Guilds.sendGuildMessage(guildName, "Se ha " + (!clanChat ? "&aactivado" : "&cdesactivado") + " &7el &eClan Chat&7.");
		                    openSettingsMenu(player);
		            	} else {
		        			C.sendMessage(player, "Clan", "No eres el dueño del clan.");
		            	}
		            }
		        }, ClickType.LEFT);
		
		menu.withItem(15, U.createItem(Material.REDSTONE_BLOCK, false, "&bBorrar el Clan",
				" ",
				"Al eliminar un clan, ten en cuenta que",
				"nunca más podrás recuperarlo. Todos los",
				"integrantes del clan se quedarían sin este",
				"y el nombre del clan puede volverse a utilizar",
				"por otra persona.",
				" ",
				"&aShift-Click para borrar"), 
				new ItemListener() {
		            @Override
		            public void onInteract(Player player, ClickType action, ItemStack item) {
		            	GuildsManager.disbandGuild(player, true);
		            }
		        }, ClickType.SHIFT_LEFT);
		
		menu.withItem(24, U.createItem(Material.INK_SACK, false, "&bColor del Clan",
				" ",
				"Puedes cambiar, de que color",
				"quieres que el Clan se vea",
				"en el Chat especialmente.",
				" ",
				"&aClick para abrir el menú de colores"), 
				new ItemListener() {
		            @Override
		            public void onInteract(Player player, ClickType action, ItemStack item) {
		            	openColorsMenu(player);
		            }
		        }, ClickType.LEFT);
		
		menu.withItem(35, U.createItem(Material.BARRIER, false, "&cVolver",
				"Volver al menú del Clan."), 
				new ItemListener() {
		            @Override
		            public void onInteract(Player player, ClickType action, ItemStack item) {
		            	openMenu(player);
		            }
		        }, ClickType.LEFT);
		menu.show(player);
	}
	
	private static void openColorsMenu(Player player) {
		String guildName = Guilds.getGuild(player.getUniqueId());
		ConfigurationSection config = Guilds.getGuildConfig(guildName);
		InventoryMenuBuilder menu = new InventoryMenuBuilder().withSize(36).withTitle("Colores");
		
		String ownerUUID = config.getString("owner");
		
		menu.withItem(35, U.createItem(Material.BARRIER, false, "&cVolver",
				"Volver al menú de configuración."), 
				new ItemListener() {
		            @Override
		            public void onInteract(Player player, ClickType action, ItemStack item) {
		            	openSettingsMenu(player);
		            }
		        }, ClickType.LEFT);
		menu.show(player);
	}
		
	private static Integer getSlotItem(Integer current, Integer totalItems) {
		Integer slot = 0;
		if (totalItems < 8) {
			if (totalItems == 1) {
				slot = 22;
			} else if (totalItems == 2) {
				slot = 22 + current;
			} else if (totalItems == 3) {
				slot = 21 + current;
			} else if (totalItems == 4) {
				slot = 21 + current;
			} else if (totalItems == 5) {
				slot = 20 + current;
			} else if (totalItems == 6) {
				slot = 20 + current;
			} else if (totalItems == 7) {
				slot = 19 + current;
			}
		} else {
			slot = current;
		}
		return slot;
	}
}