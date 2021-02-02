package me.enz0z.systems;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;

public class InfiniteAnvil implements Listener {
	
	@EventHandler
	public void PlayerInteractEvent(PlayerInteractEvent event) {
		if (event.getClickedBlock() == null) return;
		if (event.getClickedBlock().getType() == Material.ANVIL) {
			event.setCancelled(true);
    		event.getPlayer().openInventory(Bukkit.createInventory(event.getPlayer(), InventoryType.ANVIL));
		}
	}
}