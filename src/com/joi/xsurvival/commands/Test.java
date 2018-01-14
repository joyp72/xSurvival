package com.joi.xsurvival.commands;

import org.bukkit.entity.Player;

public class Test extends Commands {

	public Test() {
		super("xs.admin", "Test", "", new String[] { "t" });
	}

	@Override
	public void onCommand(Player sender, String[] args) {
		Player p = sender;
		p.getWorld().setStorm(true);
		p.sendMessage("tested");

	}

}
