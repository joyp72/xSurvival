package com.joi.xsurvival.commands;

import org.bukkit.entity.Player;

import com.joi.xsurvival.commands.MessageManager.MessageType;
import com.joi.xsurvival.maps.Map;
import com.joi.xsurvival.maps.MapManager;

import net.md_5.bungee.api.ChatColor;

public class List extends Commands {

	public List() {
		super("plugin.default", "List all maps", "", new String[] { "" });
	}

	@Override
	public void onCommand(Player sender, String[] args) {
		if (MapManager.get().getMaps().size() > 0) {
			MessageManager.get().message(sender, "Map list: ");
			for (Map m : MapManager.get().getMaps()) {
				MessageManager.get().message(sender,
						new StringBuilder().append(ChatColor.YELLOW).append(m.getName()).toString());
			}
		} else {
			MessageManager.get().message(sender, "No maps found", MessageType.BAD);
		}
	}

}
