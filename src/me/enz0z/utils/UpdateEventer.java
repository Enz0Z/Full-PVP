package me.enz0z.utils;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;

import me.enz0z.Core;

public class UpdateEventer {

	public enum UpdateType {
		ULTRAFAST, FAST, NORMAL, SLOW, ULTRASLOW;
	}

	private static Integer ULTRAFAST;
	private static Integer FAST;
	private static Integer NORMAL;
	private static Integer SLOW;
	private static Integer ULTRASLOW;

	public static void startUpdaters() {
		ULTRAFAST = new BukkitRunnable() {
			@Override
			public void run() {
				UpdateEvent updateEvent = new UpdateEvent(UpdateType.ULTRAFAST, Bukkit.getOnlinePlayers());
				Bukkit.getPluginManager().callEvent(updateEvent);
			}
		}.runTaskTimer(Core.getPlugin(), 0, 5).getTaskId();
		FAST = new BukkitRunnable() {
			@Override
			public void run() {
				UpdateEvent updateEvent = new UpdateEvent(UpdateType.FAST, Bukkit.getOnlinePlayers());
				Bukkit.getPluginManager().callEvent(updateEvent);
			}
		}.runTaskTimer(Core.getPlugin(), 0, 10).getTaskId();
		NORMAL = new BukkitRunnable() {
			@Override
			public void run() {
				UpdateEvent updateEvent = new UpdateEvent(UpdateType.NORMAL, Bukkit.getOnlinePlayers());
				Bukkit.getPluginManager().callEvent(updateEvent);
			}
		}.runTaskTimer(Core.getPlugin(), 0, 20).getTaskId();
		SLOW = new BukkitRunnable() {
			@Override
			public void run() {
				UpdateEvent updateEvent = new UpdateEvent(UpdateType.SLOW, Bukkit.getOnlinePlayers());
				Bukkit.getPluginManager().callEvent(updateEvent);
			}
		}.runTaskTimer(Core.getPlugin(), 0, 40).getTaskId();
		ULTRASLOW = new BukkitRunnable() {
			@Override
			public void run() {
				UpdateEvent updateEvent = new UpdateEvent(UpdateType.ULTRASLOW, Bukkit.getOnlinePlayers());
				Bukkit.getPluginManager().callEvent(updateEvent);
			}
		}.runTaskTimer(Core.getPlugin(), 0, 80).getTaskId();
	}

	public static void endUpdaters() {
		Bukkit.getScheduler().cancelTask(ULTRAFAST);
		Bukkit.getScheduler().cancelTask(FAST);
		Bukkit.getScheduler().cancelTask(NORMAL);
		Bukkit.getScheduler().cancelTask(SLOW);
		Bukkit.getScheduler().cancelTask(ULTRASLOW);
	}
	
	public static class UpdateEvent extends Event {

		private static final HandlerList handlers = new HandlerList();
		private final UpdateType typeEvent;
		private final Collection<? extends Player> playersEvent;

		public UpdateEvent(UpdateType type, Collection<? extends Player> players) {
			this.typeEvent = type;
			this.playersEvent = players;
		}

		public UpdateType getType() {
			return this.typeEvent;
		}

		public Collection<? extends Player> getPlayers() {
			return this.playersEvent;
		}

		@Override
		public HandlerList getHandlers() {
			return handlers;
		}

		public static HandlerList getHandlerList() {
			return handlers;
		}
	}
}
