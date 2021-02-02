package me.enz0z.chest;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import me.enz0z.files.ChestsYML;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockAction;

public class ChestEvents implements Listener {

	private static final HashMap<Player, Location> lastChest = new HashMap<>();
	
	@EventHandler
	private void PlayerInteractEvent(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		Location location = block.getLocation();
		
		if (block.getType() == Material.CHEST) {
			if (player.getGameMode() == GameMode.CREATIVE) return;
			event.setCancelled(true);
		}

		if (!lastChest.containsKey(player)) {
			if (player.isSneaking()) return;
			String confName = location.getBlockX() + "-" + location.getBlockY() + "-" + location.getBlockZ();
			ConfigurationSection chest = new ChestsYML().get().getConfigurationSection(confName);
			if (chest == null) return;
			ChestManager.openInventory(player, location);
			lastChest.put(player, location);

			player.playSound(location, Sound.CHEST_OPEN, 0.5F, 0.95F);
			BlockPosition pos = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
			PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, Blocks.CHEST, 1, 1);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		}
	}
	
	@EventHandler
	private void InventoryCloseEvent(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		Inventory inv = event.getInventory();
		
		if (lastChest.containsKey(player)) {
			Location location = lastChest.get(player);
			String confName = location.getBlockX() + "-" + location.getBlockY() + "-" + location.getBlockZ();
			ChestManager.saveInventory(player, confName, inv);
			player.playSound(location, Sound.CHEST_CLOSE, 0.5F, 0.95F);
			BlockPosition pos = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
			PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, Blocks.CHEST, 1, 0);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
			lastChest.remove(player);
		}
	}
}