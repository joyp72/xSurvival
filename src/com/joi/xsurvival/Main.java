package com.joi.xsurvival;

import org.bukkit.plugin.java.JavaPlugin;

import com.joi.xsurvival.commands.CommandsManager;
import com.joi.xsurvival.maps.Items;
import com.joi.xsurvival.maps.MapListener;
import com.joi.xsurvival.maps.MapManager;

public class Main extends JavaPlugin {
	
	public static Main instance;
	
	public static Main get() {
		return instance;
	}
	
	public void onEnable() {
		instance = this;
		getLogger().info("Plugin Enabled!");
		CommandsManager.get().setup();
		Settings.get().setup(this);
		MapManager.get().setupMaps();
		MapListener.get().setup();
		Items.get().setupRecipes();
	}
	
	public void onDisable() {
		getLogger().info("Disabled!");
	}

}
