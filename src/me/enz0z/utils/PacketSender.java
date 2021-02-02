package me.enz0z.utils;

import java.lang.reflect.Field;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.enz0z.Core;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class PacketSender {

	public static void PacketPlayOutTitle(Player player, String title, String subtitle, Integer fadeIn, Integer stay, Integer fadeOut) {	
		IChatBaseComponent messageTitle = ChatSerializer.a("{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', title) + "\"}");
		IChatBaseComponent messageSubtitle = ChatSerializer.a("{\"text\":\""+ ChatColor.translateAlternateColorCodes('&', subtitle) +"\"}");
		PacketPlayOutTitle chatTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, messageTitle);
		PacketPlayOutTitle chatSubtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, messageSubtitle);
		PacketPlayOutTitle chatLength = new PacketPlayOutTitle(fadeIn, stay, fadeOut);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(chatTitle);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(chatSubtitle);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(chatLength);
	}

	public static void PacketPlayOutChat(Player player, String actionbar) {
		IChatBaseComponent messageActionBar = ChatSerializer.a("{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', actionbar) + "\"}");
		PacketPlayOutChat chatActionBar = new PacketPlayOutChat(messageActionBar, (byte) 2);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(chatActionBar);
	}

	public static void PacketPlayOutPlayerListHeaderFooter(Player player, String header, String footer) {
		try {
			PacketPlayOutPlayerListHeaderFooter packetPlayOutPlayerListHeaderFooter = new PacketPlayOutPlayerListHeaderFooter();
	        Field a = packetPlayOutPlayerListHeaderFooter.getClass().getDeclaredField("a");
	        a.setAccessible(true);
	        Field b = packetPlayOutPlayerListHeaderFooter.getClass().getDeclaredField("b");
	        b.setAccessible(true);
	        Object headerObject = new ChatComponentText(ChatColor.translateAlternateColorCodes('&', header));
	        Object footerObject = new ChatComponentText(ChatColor.translateAlternateColorCodes('&', footer));
	        a.set(packetPlayOutPlayerListHeaderFooter, headerObject);
	        b.set(packetPlayOutPlayerListHeaderFooter, footerObject);
	        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutPlayerListHeaderFooter);
	    } catch (NoSuchFieldException | IllegalAccessException e) {
	    	Core.getPlugin().getLogger().log(Level.SEVERE, "Error setting up the Header and Footer to " + player.getName() + ".");
	    }
	}
}
