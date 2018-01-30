package com.joi.xsurvival.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.joi.xsurvival.Main;
import com.joi.xsurvival.maps.Data;
import com.joi.xsurvival.maps.Map;
import com.joi.xsurvival.maps.MapManager;

import net.md_5.bungee.api.ChatColor;

public class ScoreBoard {

	public static ScoreBoard instance;
	private int i = 20;

	static {
		instance = new ScoreBoard();
	}

	public static ScoreBoard get() {
		return instance;
	}

	public void updateSB(Player p) {

		new BukkitRunnable() {

			@Override
			public void run() {
				Map m = MapManager.get().getMap(p);
				if (m != null) {

					Data d = m.getData(p);

					ScoreboardManager manager = Bukkit.getScoreboardManager();
					Scoreboard board = manager.getNewScoreboard();

					Objective main = board.registerNewObjective("xSurvival", "dummy");
					main.setDisplaySlot(DisplaySlot.SIDEBAR);
					main.setDisplayName(ChatColor.RED + "xSurvival");

					Score life = main.getScore(ChatColor.WHITE + "" + ChatColor.BOLD + "Life Expectancy"
							+ ChatColor.RESET + "" + ChatColor.WHITE + ": ");
					life.setScore(i);
					i--;

					Score timer = main.getScore(ChatColor.GRAY + d.getLife());
					timer.setScore(i);
					i--;

					Score blank = main.getScore(" ");
					blank.setScore(i);
					i--;

					Score status = main.getScore(ChatColor.WHITE + "" + ChatColor.BOLD + "Status" + ChatColor.RESET + ""
							+ ChatColor.WHITE + ": ");
					status.setScore(i);
					i--;

					Score coldres = main.getScore(ChatColor.GRAY + "Cold Resist: " + d.getColdResist() + "%");
					coldres.setScore(i);
					i--;

					Score hunger = main.getScore(ChatColor.GRAY + "Hunger: " + d.getHungerStat());
					hunger.setScore(i);
					i--;

					Score thirst = main.getScore(ChatColor.GRAY + "Thirst: " + d.getThirstStat());
					thirst.setScore(i);
					i--;

					Score temp = main.getScore(ChatColor.GRAY + "Temp: " + d.getTempStat());
					temp.setScore(i);
					i--;

					Score infection = main.getScore(ChatColor.GRAY + "Infection: " + d.getInfectionStat());
					infection.setScore(i);
					i--;

					p.setScoreboard(board);
					i = 20;
				}

			}
		}.runTask(Main.get());
	}

}
