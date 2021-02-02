package me.enz0z;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import me.enz0z.chest.ChestEvents;
import me.enz0z.commands.FullPVP;
import me.enz0z.commands.Restart;
import me.enz0z.commands.Settings;
import me.enz0z.commands.Spawn;
import me.enz0z.commands.Test;
import me.enz0z.events.MainEvents;
import me.enz0z.files.ChestsYML;
import me.enz0z.files.ConfigYML;
import me.enz0z.files.GuildsYML;
import me.enz0z.files.KothsYML;
import me.enz0z.files.SettingsYML;
import me.enz0z.guilds.Guilds;
import me.enz0z.guilds.GuildsEvents;
import me.enz0z.systems.AntiBlockSpam;
import me.enz0z.systems.AntiBowBoost;
import me.enz0z.systems.CombatLog;
import me.enz0z.systems.InfiniteAnvil;
import me.enz0z.utils.UpdateEventer;
import net.md_5.bungee.api.ChatColor;

public class Core extends JavaPlugin {

	private static Core instance;
	private static Plugin plugin;
	private static String version = "";
	private static String pluginFullName = "null";
	private static String pluginName = "null";
	private static String pluginVersion = "null";
	
	private static WorldGuardPlugin worldguardplugin;

	@Override
	public void onEnable() {
		instance = this;
		plugin = this;
		version = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);
		pluginFullName = this.getDescription().getFullName();
		pluginName = this.getDescription().getName();
		pluginVersion = this.getDescription().getVersion();
		
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
	    if (plugin != null && plugin instanceof WorldGuardPlugin) {
	    	worldguardplugin = (WorldGuardPlugin) plugin;
	    }
		
		this.getServer().getPluginManager().registerEvents(new MainEvents(), this);
		this.getServer().getPluginManager().registerEvents(new ChestEvents(), this);
		this.getServer().getPluginManager().registerEvents(new GuildsEvents(), this);
		this.getServer().getPluginManager().registerEvents(new CombatLog(), this);
		this.getServer().getPluginManager().registerEvents(new Settings(), this);
		this.getServer().getPluginManager().registerEvents(new AntiBowBoost(), this);
		this.getServer().getPluginManager().registerEvents(new AntiBlockSpam(), this);
		this.getServer().getPluginManager().registerEvents(new InfiniteAnvil(), this);
		this.getServer().getPluginManager().registerEvents(new Spawn(), this);
		this.getServer().getPluginCommand("fullpvp").setExecutor(new FullPVP());
		this.getServer().getPluginCommand("test").setExecutor(new Test());
		this.getServer().getPluginCommand("guild").setExecutor(new Guilds());
		this.getServer().getPluginCommand("settings").setExecutor(new Settings());
		this.getServer().getPluginCommand("restartserver").setExecutor(new Restart());
		this.getServer().getPluginCommand("spawn").setExecutor(new Spawn());
		this.getServer().getPluginCommand("combatlog").setExecutor(new CombatLog());

		this.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + pluginName + "> " + ChatColor.GRAY + "successfully loaded.");
		
		File ConfigData = new File("plugins/" + pluginName + "/");
		if (!ConfigData.exists()) {
			ConfigData.mkdir();
		}
		new ConfigYML().create();
		new ChestsYML().create();
		new GuildsYML().create();
		new SettingsYML().create();
		new KothsYML().create();
		
		World gameworld = Bukkit.getWorld("Game");
		for (Entity entities : gameworld.getEntities()) {
			if (entities != null) {
				entities.remove();
			}
		}
		gameworld.setPVP(true);
		gameworld.setStorm(false);
		gameworld.setThundering(false);
		gameworld.setAutoSave(false);
		gameworld.setTime(6000);
		gameworld.setGameRuleValue("doDaylightCycle", "false");
		gameworld.setGameRuleValue("doMobSpawning", "false");
		gameworld.setGameRuleValue("doFireTick", "0");
		gameworld.setDifficulty(Difficulty.EASY);
		gameworld.setWeatherDuration(Integer.MAX_VALUE);
		UpdateEventer.startUpdaters();
	}

	@Override
	public void onDisable() {
		UpdateEventer.endUpdaters();
		this.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + pluginName + "> " + ChatColor.GRAY + "successfully unloaded.");
		instance = null;
		plugin = null;
	}

	public static Plugin getPlugin() {
		return plugin;
	}

	public static Core getAPI() {
		return instance;
	}

	public static String getVersion() {
		return version;
	}

	public static String getPluginFullName() {
		return pluginFullName;
	}

	public static String getPluginName() {
		return pluginName;
	}

	public static String getPluginVersion() {
		return pluginVersion;
	}

	public static WorldGuardPlugin getWorldGuard() {
		return worldguardplugin;
	}
	
	public static ConfigYML getConfigFile() {
		ConfigYML config = new ConfigYML();
		return config;
	}
}
