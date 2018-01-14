package com.joi.xsurvival.commands;

import org.bukkit.entity.Player;

import com.joi.xsurvival.commands.MessageManager.MessageType;
import com.joi.xsurvival.maps.Map;
import com.joi.xsurvival.maps.MapManager;

public class Join extends Commands {

	public Join() {
		super("plugin.default", "Join a map.", "<map>", new String[] { "j" });
	}

	@Override
	public void onCommand(Player sender, final String[] args) {
		if (args.length == 0) {
			MessageManager.get().message(sender, "You must specify a map!", MessageType.BAD);
			return;
		}
		Map m = MapManager.get().getMap(sender);
		if (m != null) {
			MessageManager.get().message(sender, "You are already in a map!", MessageType.BAD);
			return;
		}
		String id = args[0];
		Map m2 = MapManager.get().getMap(id);
		if (m2 == null) {
			MessageManager.get().message(sender, "Unknown map.", MessageType.BAD);
			return;
		}
		m2.addPlayer(sender);
	}
}
