package me.enz0z.systems;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class AntiBlockSpam implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void BlockPlaceEvent(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
		if (block == null || block.getType() == Material.AIR) {
			//player.teleport(player.getLocation().subtract(0, 1, 0));
		}
	}
}