package com.joi.xsurvival.maps;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;

import com.joi.xsurvival.Main;
import com.joi.xsurvival.commands.MessageManager;
import com.joi.xsurvival.commands.MessageManager.MessageType;
import com.joi.xsurvival.scoreboard.ScoreBoard;

import net.md_5.bungee.api.ChatColor;

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
	private int temp;
	private int coldres;
	private int hunger;
	private int thirst;
	private int infection;
	private int life;
	private int taskid = 0;

	public Data(Player p, Map m) {
		loc = p.getLocation();
		id = p.getUniqueId();
		map = m;
		temp = 0;
		coldres = 0;
		thirst = 20;
		infection = 0;
		life = 1800;
		hunger = p.getFoodLevel();
		gm = p.getGameMode();
		inv = p.getInventory().getContents();
		armor = p.getInventory().getArmorContents();
		level = p.getLevel();
		xp = p.getExp();
		health = p.getHealth();
		sb = p.getScoreboard();
	}

	public void update() {
		taskid = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.get(), new Runnable() {
			Player p = getPlayer();
			Map m = MapManager.get().getMap(p);
			int t = 0;
			boolean nearFire = false;

			@Override
			public void run() {
				for (Location l : m.getFires()) {
					if (p.getLocation().distance(l) < 4) {
						nearFire = true;
					} else {
						nearFire = false;
					}
				}
				if (nearFire) {
					if (temp < 20) {
						temp = temp + 1;
					}
				}
				if (infection < 6) {
					life--;
				}
				if (life <= 0) {
					m.removePlayer(p);
					return;
				}
				t++;
				if (t >= 10) {
					if (!nearFire) {
						if (temp < 6) {
							p.damage(1);
						} else {
							temp = temp - 2;
						}
					}
					if (infection >= 6) {
						infection = infection - 2;
					}
					if (thirst < 6) {
						p.damage(1);
					} else {
						thirst = thirst - 2;
					}
					t = 0;
				}
				ScoreBoard.get().updateSB(p);
			}
		}, 0, 20L);
	}

	public void stopUpdate() {
		Bukkit.getServer().getScheduler().cancelTask(taskid);
	}

	public void ready() {
		Player p = Bukkit.getPlayer(id);
		if (p.getGameMode() != GameMode.SURVIVAL) {
			p.setGameMode(GameMode.SURVIVAL);
		}
		p.getInventory().clear();
		p.setLevel(0);
		p.setExp(0f);
		p.setMaxHealth(6);
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
		p.setMaxHealth(20);
		p.setHealth(health);
		p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		p.setScoreboard(sb);
	}

	public String getHungerStat() {
		if (getHunger() >= 14) {
			return ChatColor.GREEN + "NORMAL";
		}
		if (getHunger() < 14 && getHunger() > 6) {
			return ChatColor.YELLOW + "MODERATE";
		}
		if (getHunger() <= 6) {
			return ChatColor.DARK_RED + "CRITICAL";
		}
		return null;
	}

	public String getThirstStat() {
		if (thirst >= 14) {
			return ChatColor.GREEN + "NORMAL";
		}
		if (thirst < 14 && thirst > 6) {
			return ChatColor.YELLOW + "MODERATE";
		}
		if (thirst <= 6) {
			return ChatColor.DARK_RED + "CRITICAL";
		}
		return null;
	}

	public String getLife() {

		final int MINUTES_IN_AN_HOUR = 60;
		final int SECONDS_IN_A_MINUTE = 60;

		int seconds = life % SECONDS_IN_A_MINUTE;
		int totalMinutes = life / SECONDS_IN_A_MINUTE;
		int minutes = totalMinutes % MINUTES_IN_AN_HOUR;

		return minutes + "min " + seconds + "sec";
	}

	public String getTempStat() {
		if (getTemp() >= 14) {
			return ChatColor.GREEN + "NORMAL";
		}
		if (getTemp() < 14 && getTemp() > 6) {
			return ChatColor.YELLOW + "MODERATE";
		}
		if (getTemp() <= 6) {
			return ChatColor.DARK_RED + "CRITICAL";
		}
		return null;
	}

	public String getInfectionStat() {
		if (infection >= 14) {
			return ChatColor.GREEN + "NORMAL";
		}
		if (infection < 14 && infection > 6) {
			return ChatColor.YELLOW + "MODERATE";
		}
		if (infection <= 6) {
			return ChatColor.DARK_RED + "CRITICAL";
		}
		return null;
	}

	public Map getMap() {
		return map;
	}

	public int getHunger() {
		hunger = getPlayer().getFoodLevel();
		return hunger;
	}

	public int getThirst() {
		return thirst;
	}

	public int getInfection() {
		return infection;
	}

	public void setInfection(int i) {
		infection = i;
	}

	public void setThirst(int i) {
		thirst = i;
	}

	public void setHunger(int i) {
		getPlayer().setFoodLevel(i);
		hunger = i;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(id);
	}

	public int getTemp() {
		return temp + coldres;
	}

	public void setTemp(int i) {
		temp = i;
	}

	public void setColdRes(int i) {
		coldres = i;
	}

	public int getColdRes() {
		return coldres;
	}

	public Location getLoc() {
		return loc;
	}

}
