package com.joi.xsurvival.commands;



import org.bukkit.entity.Player;

import com.joi.xsurvival.maps.Data;
import com.joi.xsurvival.maps.Map;
import com.joi.xsurvival.maps.MapManager;

public class Test extends Commands {

	public Test() {
		super("xs.admin", "Test", "", new String[] { "t" });
	}

	@Override
	public void onCommand(Player sender, String[] args) {
		Player p = sender;
		Map m = MapManager.get().getMap(p);
		Data d = m.getData(p);
		p.sendMessage(Integer.toString(d.getColdResist()));
	}
}
