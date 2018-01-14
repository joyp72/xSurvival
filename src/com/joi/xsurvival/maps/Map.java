package com.joi.xsurvival.maps;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.joi.xsurvival.Settings;
import com.joi.xsurvival.commands.MessageManager;
import com.joi.xsurvival.commands.MessageManager.MessageType;
import com.joi.xsurvival.utils.LocationUtils;

import net.md_5.bungee.api.ChatColor;

public class Map {

	private String name;
	private MapState state;
	private List<Data> datas;
	private Location spawn;

	public Map(String n) {
		name = n;
		datas = new ArrayList<Data>();
		loadFromConfig();
		saveToConfig();
		checkState();

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
		
	}

	public void start() {
		
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
			message(ChatColor.GREEN + p.getName() + " joined the game.");
			if (state.equals(MapState.WAITING) && (getNumberOfPlayers() + getNumberOfPlayers()) == 4) {
				start();
			}
		}
	}

	public void removePlayer(Player p) {
		if (containsPlayer(p)) {
			Data d = getData(p);
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
