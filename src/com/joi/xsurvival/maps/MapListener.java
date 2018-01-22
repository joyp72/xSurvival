package com.joi.xsurvival.maps;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.joi.xsurvival.Main;
import com.joi.xsurvival.commands.MessageManager;

import net.md_5.bungee.api.ChatColor;

public class MapListener implements Listener {

	private static MapListener instance;

	static {
		instance = new MapListener();
	}

	public static MapListener get() {
		return instance;
	}

	public void setup() {
		Bukkit.getPluginManager().registerEvents(this, Main.get());
	}

	@EventHandler
	public void onPlayerConsume(PlayerItemConsumeEvent e) {
		Player p = e.getPlayer();
		Map m = MapManager.get().getMap(p);
		if (m != null) {
			Data d = m.getData(p);
			if (e.getItem().getType() == Material.POTION) {
				if (e.getItem().containsEnchantment(Enchantment.ARROW_DAMAGE)) {
					if (d.getInfection() < 20) {
						d.setInfection(d.getInfection() + 5);
					}
					MessageManager.get().message(p, "You consumed medecine!");
				} else {
					if (d.getThirst() < 20) {
						d.setThirst(d.getThirst() + 5);
					}
					MessageManager.get().message(p, "You consumed water!");
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (MapManager.get().getMap(p) != null) {
			MapManager.get().getMap(p).kickPlayer(p);
		}
	}

	@EventHandler
	public void onPlayerPlaceBlock(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		Map m = MapManager.get().getMap(p);
		if (m != null) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerPlaceBlock(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Map m = MapManager.get().getMap(p);
		if (m != null) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Map m = MapManager.get().getMap(e.getPlayer());
		String s = e.getMessage().toLowerCase();
		if (m != null) {
			e.setMessage(ChatColor.GRAY + s);
		}
	}
}
