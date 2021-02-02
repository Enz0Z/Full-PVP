package me.enz0z.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.enz0z.Core;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;

public class U {
	
	public static ItemStack fixItemNames(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		if (meta != null && meta.getDisplayName() != null) {
			meta.setDisplayName(meta.getDisplayName().replaceAll("Â", ""));
		}
		if (meta != null && meta.getLore() != null) {
			ArrayList<String> loreLines = new ArrayList<String>();
			for (String lores : meta.getLore()) {
				loreLines.add(lores.replaceAll("Â", ""));
			}
			meta.setLore(loreLines);
		}
		item.setItemMeta(meta);
		return item;
	}
	
	public static String limitDecimals(Double number) {
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
	    return df.format(number);
	}
	
	public static void restartAll(Player player) {
		player.setHealth(20D);
		player.setFoodLevel(20);
		player.setExp(0);
		player.setLevel(0);
		player.setTotalExperience(0);
		player.setFireTicks(0);
		player.getInventory().setHelmet(null);
		player.getInventory().setChestplate(null);
		player.getInventory().setLeggings(null);
		player.getInventory().setBoots(null);
		player.getInventory().clear();
		for (PotionEffect effect : player.getActivePotionEffects()) {
			player.removePotionEffect(effect.getType());
		}
	}

	public static List<ItemStack> getInventory(Player player) {
		List<ItemStack> items = new ArrayList<>();
		for (ItemStack itemStack : player.getInventory().getArmorContents()) {
			if (itemStack != null) {
				items.add(itemStack);
			}
		}
		for (ItemStack itemStack : player.getInventory()) {
			if (itemStack != null) {
				items.add(itemStack);
			}
		}
		return items;
	}
	
	public static ItemStack createItem(Material material, Boolean glow, String name, String... lines) {
		ItemStack item = new ItemStack(material);
		if (item.getType() == Material.SKULL_ITEM) {
			item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			SkullMeta skullmeta = (SkullMeta) item.getItemMeta();
			skullmeta.setOwner(ChatColor.stripColor(name));
			skullmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
			ArrayList<String> loreLines = new ArrayList<String>();
			for (String lore : lines) {
				loreLines.add(ChatColor.translateAlternateColorCodes('&', "&7" + lore));
			}
			skullmeta.setLore(loreLines);
			for (ItemFlag flag : ItemFlag.values()) {
				skullmeta.addItemFlags(flag);
			}
			item.setItemMeta(skullmeta);
			if (glow) {
				item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
			}
		} else {
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
			ArrayList<String> loreLines = new ArrayList<String>();
			for (String lore : lines) {
				loreLines.add(ChatColor.translateAlternateColorCodes('&', "&7" + lore));
			}
			itemMeta.setLore(loreLines);
			for (ItemFlag flag : ItemFlag.values()) {
				itemMeta.addItemFlags(flag);
			}
			item.setItemMeta(itemMeta);
			if (glow) {
				item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
			}
		}
		return item;
	}
	
	public static ItemStack createItem(ItemStack item, Boolean glow, String name, String... lines) {
		if (item.getType() == Material.SKULL_ITEM) {
			item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			SkullMeta skullmeta = (SkullMeta) item.getItemMeta();
			skullmeta.setOwner(ChatColor.stripColor(name));
			skullmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
			ArrayList<String> loreLines = new ArrayList<String>();
			for (String lore : lines) {
				loreLines.add(ChatColor.translateAlternateColorCodes('&', "&7" + lore));
			}
			skullmeta.setLore(loreLines);
			for (ItemFlag flag : ItemFlag.values()) {
				skullmeta.addItemFlags(flag);
			}
			item.setItemMeta(skullmeta);
			if (glow) {
				item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
			}
		} else {
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
			ArrayList<String> loreLines = new ArrayList<String>();
			for (String lore : lines) {
				loreLines.add(ChatColor.translateAlternateColorCodes('&', "&7" + lore));
			}
			itemMeta.setLore(loreLines);
			for (ItemFlag flag : ItemFlag.values()) {
				itemMeta.addItemFlags(flag);
			}
			item.setItemMeta(itemMeta);
			if (glow) {
				item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
			}
		}
		return item;
	}
	
	public static ItemStack createSwitchItem(Boolean value, String name, String... lines) {
		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0);
		if (value == true) {
			item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
		} else if (value == false) {
			item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
		}		
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		ArrayList<String> loreLines = new ArrayList<String>();
		for (String lore : lines) {
			loreLines.add(ChatColor.translateAlternateColorCodes('&', "&7" + lore));
		}
		itemMeta.setLore(loreLines);
		for (ItemFlag flag : ItemFlag.values()) {
			itemMeta.addItemFlags(flag);
		}
		item.setItemMeta(itemMeta);
		return item;
	}
	
	public static String secToTime(Long seconds) {
		long hs = seconds / 3600;
		long ms = seconds / 60;
		long ss = seconds % 60;
		
		if (hs > 0 && ms > 0 && ss == 0) {
			return hs + " hora(s) con " + ms + " minuto(s)";
		} else if (hs > 0 && ms > 0 && ss > 0) {
			return hs + " hora(s) con " + ms + " minuto(s) y " + ss + " segundo(s)";
			
		} else if (hs == 0 && ms > 0 && ss > 0) {
			return ms + " minuto(s) y " + ss + " segundo(s)";

		} else if (hs > 0 && ms == 0 && ss == 0) {
			return hs + " hora(s)";
		} else if (hs == 0 && ms > 0 && ss == 0) {
			return ms + " minuto(s)";
		} else if (hs == 0 && ms == 0 && ss > 0) {
			return ss + " segundo(s)";
			
		} else {
			return hs + ":" + ms + ":" + ss;
		}
	}
	
	public static Boolean isCurrentRegion(Location loc, String region) {
	    WorldGuardPlugin guard = Core.getWorldGuard();
	    Vector v = BukkitUtil.toVector(loc);
	    RegionManager manager = guard.getRegionManager(loc.getWorld());
	    ApplicableRegionSet set = manager.getApplicableRegions(v);
	    for (ProtectedRegion each : set) {
	        if (each.getId().equalsIgnoreCase(region)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public static void changeInventoryTitle(Player player, String title) {
		EntityPlayer entityplayer = ((CraftPlayer) player).getHandle();
		PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(entityplayer.activeContainer.windowId, "minecraft:chest", new ChatMessage(ChatColor.translateAlternateColorCodes('&', title)), player.getOpenInventory().getTopInventory().getSize());
		entityplayer.playerConnection.sendPacket(packet);
		entityplayer.updateInventory(entityplayer.activeContainer);
	}
}
