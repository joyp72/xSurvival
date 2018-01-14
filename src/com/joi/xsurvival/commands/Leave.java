package com.joi.xsurvival.commands;

import java.util.UUID;

import org.bukkit.entity.Player;

import com.joi.xsurvival.commands.MessageManager.MessageType;
import com.joi.xsurvival.maps.Map;
import com.joi.xsurvival.maps.MapManager;

public class Leave extends Commands {

	public Leave() {
		super("plugin.default", "Leave a map", "", new String[] { "l" });
	}

	@Override
	public void onCommand(Player sender, String[] args) {
		Player p = sender;
		UUID id = p.getUniqueId();
		Map m = MapManager.get().getMap(p);
		if (m == null) {
			MessageManager.get().message(p, "You are not in a map.", MessageType.BAD);
			return;
		}
		m.kickPlayer(p);
	}

}
