package com.joi.xsurvival.commands;

import java.net.InetAddress;

import org.bukkit.entity.Player;

public class Test extends Commands {

	public Test() {
		super("plugin.admin", "Test", "", new String[] { "t" });
	}

	@Override
	public void onCommand(Player sender, String[] args) {
		Player p = sender;
		InetAddress norm = p.getAddress().getAddress();
		p.sendMessage(norm.toString());

	}

}
