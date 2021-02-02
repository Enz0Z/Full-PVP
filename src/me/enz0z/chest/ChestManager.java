package me.enz0z.chest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.enz0z.files.ChestsYML;
import me.enz0z.utils.C;
import me.enz0z.utils.S;
import me.enz0z.utils.U;

public class ChestManager {

	public static void openInventory(Player player, Location location) {
		String confName = location.getBlockX() + "-" + location.getBlockY() + "-" + location.getBlockZ();
		ChestsYML chestyml = new ChestsYML();
		ConfigurationSection chestSec = chestyml.get().getConfigurationSection(confName);
		ConfigurationSection playerSec = chestSec.getConfigurationSection("players." + player.getUniqueId());
		
		Integer refillTime = chestSec.getInt("refilltime");
		Inventory inv = Bukkit.createInventory(player, 27, "Cofre");
		if (refillTime == 0) {
			inv = Bukkit.createInventory(player, 27, "Cofre - Ilimitado");
		} else {
			inv = Bukkit.createInventory(player, 27, "Cofre - Rellenable");
		}
		
		if (refillTime == 0) {
			for (int i = 0; i < inv.getSize(); i++) {
				inv.setItem(i, U.fixItemNames(chestSec.getItemStack("contents." + i, new ItemStack(Material.AIR))));
			}
		} else if (playerSec == null) {
			for (int i = 0; i < inv.getSize(); i++) {
				inv.setItem(i, U.fixItemNames(chestSec.getItemStack("contents." + i, new ItemStack(Material.AIR))));
			}
			playerSec = chestSec.createSection("players." + player.getUniqueId());
			playerSec.set("refill", S.currentTimeSeconds() + (refillTime * 60));
		} else if (S.currentTimeSeconds() > playerSec.getLong("refill")) {
			for (int i = 0; i < inv.getSize(); i++) {
				inv.setItem(i, U.fixItemNames(chestSec.getItemStack("contents." + i, new ItemStack(Material.AIR))));
			}
			playerSec.set("refill", S.currentTimeSeconds() + (refillTime * 60));
		} else {
			for (int i = 0; i < inv.getSize(); i++) {
				inv.setItem(i, U.fixItemNames(playerSec.getItemStack("contents." + i, new ItemStack(Material.AIR))));
			}
			C.sendMessage(player, "Chest", "Este cofre será rellenado para tí en &a" + U.secToTime(S.elapsed(playerSec.getLong("refill"))) + "&7.");
		}
		player.openInventory(inv);
		chestyml.save();
		if (refillTime == 0) {
			U.changeInventoryTitle(player, "Cofre - Ilimitado");
		} else {
			U.changeInventoryTitle(player, "Cofre - Rellenable");
		}
	}

	public static void saveInventory(Player player, String confName, Inventory inv) {
		ChestsYML chestyml = new ChestsYML();
		ConfigurationSection chest = chestyml.get().getConfigurationSection(confName);
		Integer refillTime = chest.getInt("refilltime");
		if (refillTime == 0) return;
		ConfigurationSection playerSec = chest.getConfigurationSection("players." + player.getUniqueId());
		Boolean wasChanged = false;
		for (int i = 0; i < inv.getSize(); i++) {
			if (wasChanged == true) break;
			ItemStack playerItem = playerSec.getItemStack("contents." + i, new ItemStack(Material.AIR));
			if (playerItem.equals(new ItemStack(Material.AIR))) continue;
			if (inv.getItem(i) != null && inv.getItem(i).getType() != Material.AIR && !inv.getItem(i).equals(playerItem)) {
				wasChanged = true;
			}
		}
		if (wasChanged == true) {
			playerSec.set("contents", null);
			for (int i = 0; i < inv.getSize(); i++) {
				if (inv.getItem(i) != null && inv.getItem(i).getType() != Material.AIR) {
					playerSec.set("contents." + i, inv.getItem(i));
				}
			}
			chestyml.save();
		}
	}
}
