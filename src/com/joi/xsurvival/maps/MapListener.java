package com.joi.xsurvival.maps;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

import com.joi.xsurvival.Main;
import com.joi.xsurvival.commands.MessageManager;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.npc.NPC;
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
	public void onNPCDeath(NPCDeathEvent e) {
		NPC npc = e.getNPC();
		Location loc = npc.getEntity().getLocation();
		World world = loc.getWorld();
		if (Monsters.get().getMonsters().contains(npc)) {
			e.setDroppedExp(0);
			Random r = new Random();
			if (npc.getEntity().getType() == EntityType.ZOMBIE) {
				int ch = r.nextInt(100) + 1;
				ItemStack leather = new ItemStack(Material.LEATHER);
				if (ch <= 50) {
					world.dropItem(loc, leather);
				}
			} else if (npc.getEntity().getType() == EntityType.SKELETON) {
				int ch = r.nextInt(100) + 1;
				int ch2 = r.nextInt(100) + 1;
				ItemStack arrow = new ItemStack(Material.ARROW);
				ItemStack bone = new ItemStack(Material.BONE);
				if (ch <= 50) {
					world.dropItem(loc, arrow);
				}
				if (ch2 <= 50) {
					world.dropItem(loc, bone);
				}
			}
			CitizensAPI.getNPCRegistry().deregister(npc);
			Monsters.get().getMonsters().remove(npc);
		}
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
