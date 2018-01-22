package com.joi.xsurvival.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import main.RollbackAPI;

public class Test extends Commands {

	public Test() {
		super("xs.admin", "Test", "", new String[] { "t" });
	}

	@Override
	public void onCommand(Player sender, String[] args) {
		Player p = sender;
		List<Location> locs = new ArrayList<Location>(
				RollbackAPI.getBlocksOfTypeInRegion(p.getWorld(), "xs", Material.CHEST));
		p.sendMessage(Integer.toString(locs.size()));
	}
}
