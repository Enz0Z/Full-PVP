package me.enz0z.systems;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.enz0z.utils.C;
import me.enz0z.utils.S;

public class AntiBowBoost implements Listener {

	private final HashMap<UUID, Long> bowRecharge = new HashMap<>();
	
	@EventHandler
	public void EntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if (event.getEntity().getType() != EntityType.PLAYER) return;
		if (event.getDamager() instanceof Arrow) {
			if ((Player) ((Arrow) event.getDamager()).getShooter() == (Player) event.getEntity()) {
				Player player = (Player) event.getEntity();
				if (bowRecharge.containsKey(player.getUniqueId())) {
					if (S.elapsed(bowRecharge.get(player.getUniqueId())) > 0) {
						event.setCancelled(true);
						C.sendMessage(player, "FullPVP", "Debes esperar &a" + S.elapsed(bowRecharge.get(player.getUniqueId())) + " segundos &7para volver a usar el Boost del Arco.");
					} else {
						bowRecharge.put(player.getUniqueId(), S.currentTimeSeconds() + 10);
					}
				} else {
					bowRecharge.put(player.getUniqueId(), S.currentTimeSeconds() + 10);
				}
			}
		}
	}
}
