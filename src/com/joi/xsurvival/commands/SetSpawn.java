package com.joi.xsurvival.commands;

import org.bukkit.entity.Player;

import com.joi.xsurvival.commands.MessageManager.MessageType;
import com.joi.xsurvival.maps.Map;
import com.joi.xsurvival.maps.MapManager;


public class SetSpawn extends Commands {
	
	public SetSpawn() {
		super("xs.admin", "Set the Spawn of a map", "<map>", new String[] { "sal" });
	}
	
	@Override
	public void onCommand(Player sender, String[] args) {
		if (args.length == 0) {
			MessageManager.get().message(sender, "You must specify a map!", MessageType.BAD);
			return;
		}
		String id = args[0];
		Map m = MapManager.get().getMap(id);
		if (m == null ) {
			MessageManager.get().message(sender, "Unknown map.", MessageType.BAD);
			return;
		}
		m.setSpawn(sender.getLocation());
		MessageManager.get().message(sender, "Spawn set for: " + m.getName(), MessageType.GOOD);
	}

}
