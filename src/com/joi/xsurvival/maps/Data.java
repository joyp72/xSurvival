package com.joi.xsurvival.maps;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;

public class Data {

	private UUID id;
	private Location loc;
	private Map map;
	private GameMode gm;
	private ItemStack[] inv;
	private ItemStack[] armor;
	private int level;
	private float xp;
	private double health;
	private Scoreboard sb;

	public Data(Player p, Map m) {
		loc = p.getLocation();
		id = p.getUniqueId();
		map = m;
		gm = p.getGameMode();
		inv = p.getInventory().getContents();
		armor = p.getInventory().getArmorContents();
		level = p.getLevel();
		xp = p.getExp();
		health = p.getHealth();
		sb = p.getScoreboard();
	}

	public void ready() {
		Player p = Bukkit.getPlayer(id);
		if (p.getGameMode() != GameMode.SURVIVAL) {
			p.setGameMode(GameMode.SURVIVAL);
		}
		p.getInventory().clear();
		p.setLevel(0);
		p.setExp(0f);
		p.setHealth(20);
		p.setFoodLevel(20);
	}

	public void restore() {
		Player p = Bukkit.getPlayer(id);
		p.teleport(loc);
		p.setGameMode(gm);
		p.getInventory().setContents(inv);
		p.getInventory().setArmorContents(armor);
		p.setLevel(level);
		p.setExp(xp);
		p.setHealth(health);
		p.setScoreboard(sb);
	}

	public Map getMap() {
		return map;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(id);
	}

	public Location getLoc() {
		return loc;
	}

}
