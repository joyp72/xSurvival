package com.joi.xsurvival.maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftStructureBlock;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.joi.xsurvival.Settings;
import com.joi.xsurvival.commands.MessageManager;
import com.joi.xsurvival.commands.MessageManager.MessageType;
import com.joi.xsurvival.scoreboard.ScoreBoard;
import com.joi.xsurvival.utils.LocationUtils;

import main.RollbackAPI;
import net.md_5.bungee.api.ChatColor;

public class Map {

	private String name;
	private MapState state;
	private List<Data> datas;
	private Location spawn;
	private int id;
	private List<Location> fires;
	private List<Location> chests;
	private List<Location> tempchests;

	public Map(String n) {
		name = n;
		datas = new ArrayList<Data>();
		fires = new ArrayList<Location>();
		tempchests = new ArrayList<Location>();
		loadFromConfig();
		setupSB();
		if (spawn != null) {
			chests = new ArrayList<Location>(
					RollbackAPI.getBlocksOfTypeInRegion(spawn.getWorld(), "xs", Material.CHEST));
		}
		saveToConfig();
		checkState();
	}

	public void setupSB() {
		if (spawn != null) {
			fires.clear();
			List<Location> locs = new ArrayList<Location>(
					RollbackAPI.getBlocksOfTypeInRegion(spawn.getWorld(), "xs", Material.STRUCTURE_BLOCK));
			for (Location l : locs) {
				CraftStructureBlock sb = (CraftStructureBlock) l.getBlock().getState();
				if (sb.getSnapshotNBT().getString("metadata").equalsIgnoreCase("fire")) {
					fires.add(l.clone().add(0, 2, 0));
				}
			}
		}
	}

	public void setupChest() {
		Random r = new Random();
		for (Location loc : chests) {
			int ch = r.nextInt(100) + 1;
			int slot = r.nextInt(26);
			ItemStack item = new ItemStack(Material.AIR);
			if (ch <= 25) {
				int ch2 = r.nextInt(2) + 1;
				if (ch2 == 1) {
					item = new ItemStack(Material.POTION, 1, (byte) 0);
				} else if (ch2 == 2) {
					item = new ItemStack(Material.POTION, 1, (byte) 0);
					item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2);
					ItemMeta meta = item.getItemMeta();
					meta.addItemFlags(ItemFlag.values());
					meta.setDisplayName("Medicine");
					item.setItemMeta(meta);
				}
				Bukkit.getServer().broadcastMessage(Integer.toString(ch2));
			}
			Block b = loc.getBlock();
			Chest c = (Chest) b.getState();
			Inventory ci = c.getInventory();
			ci.setItem(slot, item);
		}
	}

	public void onTimerTick(String arg, int timer) {

	}

	public void onTimerEnd(String arg) {

	}

	public void saveToConfig() {
		if (spawn != null) {
			Settings.get().set("maps." + getName() + ".spawn", LocationUtils.locationToString(spawn));
		}
	}

	public void loadFromConfig() {
		Settings s = Settings.get();
		if (s.get("maps." + getName() + ".spawn") != null) {
			String s3 = s.get("maps." + getName() + ".spawn");
			spawn = LocationUtils.stringToLocation(s3);
		}
	}

	public void stop() {
		if (spawn != null) {
			spawn.getWorld().setStorm(false);
		}
		for (Location loc : chests) {
			Block b = loc.getBlock();
			Chest c = (Chest) b.getState();
			Inventory ci = c.getInventory();
			ci.clear();
		}
		setState(MapState.WAITING);
	}

	public void start() {
		setState(MapState.STARTED);
		setupChest();
		for (Data d : datas) {
			d.update();
		}
		if (spawn != null) {
			spawn.getWorld().setStorm(true);
		}
	}

	public void checkState() {
		boolean flag = false;
		if (spawn == null) {
			flag = true;
		}
		if (flag) {
			setState(MapState.STOPPED);
			return;
		}
		setState(MapState.WAITING);
	}

	public void setState(MapState m) {
		state = m;
	}

	public void addPlayer(Player p) {
		if (!containsPlayer(p) && state.canJoin() && getNumberOfPlayers() < 2) {
			Data d = new Data(p, this);
			datas.add(d);
			d.ready();
			message(ChatColor.GREEN + p.getName() + " joined the game.");
			ScoreBoard.get().updateSB(p);
			if (state.equals(MapState.WAITING) && (getNumberOfPlayers() + getNumberOfPlayers()) == 4) {
				start();
			}
		}
	}

	public void removePlayer(Player p) {
		if (containsPlayer(p)) {
			Data d = getData(p);
			d.stopUpdate();
			d.restore();
			datas.remove(d);
			if (state.equals(MapState.STARTED) && getNumberOfPlayers() < 2) {
				stop();
				message(ChatColor.RED + "Players left, stopping game.");
			}
		}
	}

	public void kickAll(boolean b) {
		for (Player p : getPlayers()) {
			if (b) {
				kickPlayer(p);
			} else {
				removePlayer(p);
			}
		}
	}

	public void kickPlayer(Player p) {
		removePlayer(p);
		message(ChatColor.RED + p.getName() + " left the map.");
		MessageManager.get().message(p, "You left the map.", MessageType.BAD);
	}

	public void message(String msg) {
		for (Player p : getPlayers()) {
			MessageManager.get().message(p, msg);
		}
	}

	public int getNumberOfPlayers() {
		return datas.size();
	}

	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<Player>();
		for (Data d : datas) {
			players.add(d.getPlayer());
		}
		return players;
	}

	public boolean containsPlayer(Player p) {
		for (Data d : datas) {
			if (d.getPlayer().equals(p)) {
				return true;
			}
		}
		return false;
	}

	public List<Location> getFires() {
		return fires;
	}

	public Data getData(Player p) {
		for (Data a : datas) {
			if (a.getPlayer().equals(p)) {
				return a;
			}
		}
		return null;
	}

	public Location getSpawn() {
		return spawn;
	}

	public void setSpawn(Location l) {
		spawn = l;
		saveToConfig();
		checkState();
	}

	public String getName() {
		return name;
	}

	public Map getMap() {
		return this;
	}

	public boolean isStarted() {
		return state.equals(MapState.STARTED);
	}

	public String getStateName() {
		return state.getName();
	}

	public MapState getState() {
		return state;
	}

	public enum MapState {
		WAITING("WAITING", 0, "WAITING", true), STARTING("STARTING", 1, "STARTING", true), STARTED("STARTED", 2,
				"STARTED", false), STOPPED("STOPPED", 3, "STOPPED", false);

		private boolean allowJoin;
		private String name;

		private MapState(final String s, final int n, final String name, final Boolean allowJoin) {
			this.allowJoin = allowJoin;
			this.name = name;
		}

		public boolean canJoin() {
			return this.allowJoin;
		}

		public String getName() {
			return this.name;
		}
	}

}
