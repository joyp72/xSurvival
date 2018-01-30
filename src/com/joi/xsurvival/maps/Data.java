package com.joi.xsurvival.maps;

import java.util.ArrayList;
import java.util.List;
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
						if (getColdResist() == 100) {
							temp = 20;
						} else {
							if (temp < 6) {
								p.damage(1);
							} else {
								if (getColdResist() == 75) {
									temp = temp - 1;
								} else if (getColdResist() == 50) {
									temp = temp - 1;
								} else if (getColdResist() == 25) {
									temp = temp - 2;
								} else if (getColdResist() == 0) {
									temp = temp - 3;
								}
							}
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

	public int getColdResist() {
		Player p = getPlayer();
		// if (p.getInventory().getHelmet() != null && p.getInventory().getChestplate()
		// != null
		// && p.getInventory().getLeggings() != null && p.getInventory().getBoots() !=
		// null) {
		List<ItemStack> items = Items.get().getArmor();
		List<ItemStack> temp = new ArrayList<ItemStack>();
		ItemStack helm = p.getInventory().getHelmet();
		ItemStack chest = p.getInventory().getChestplate();
		ItemStack legs = p.getInventory().getLeggings();
		ItemStack boots = p.getInventory().getBoots();
		int i = 0;
		if (items.contains(helm)) {
			temp.add(helm);
		}
		if (items.contains(chest)) {
			temp.add(chest);
		}
		if (items.contains(legs)) {
			temp.add(legs);
		}
		if (items.contains(boots)) {
			temp.add(boots);
		}
		if (temp.size() == 4) {
			i = 100;
		} else if (temp.size() == 3) {
			i = 75;
		} else if (temp.size() == 2) {
			i = 50;
		} else if (temp.size() == 1) {
			i = 25;
		} else if (temp.size() == 0) {
			i = 0;
		}
		// }
		return i;
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

	public Location getLoc() {
		return loc;
	}

}
