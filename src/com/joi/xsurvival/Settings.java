package com.joi.xsurvival;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Settings {

	private static Settings instance;
	private File mapFile;
	private FileConfiguration mapConfig;
	private Plugin plugin;
	private File dbFile;
	private FileConfiguration dbConfig;

	static {
		instance = new Settings();
	}

	public static Settings get() {
		return instance;
	}

	public void setup(Plugin p) {
		plugin = p;
		if (!p.getDataFolder().exists()) {
			p.getDataFolder().mkdirs();
		}
		mapFile = new File(p.getDataFolder() + "/maps.yml");
		if (!mapFile.exists()) {
			try {
				mapFile.createNewFile();
			} catch (Exception e) {
				p.getLogger().info("Failed to generate map file!");
				e.printStackTrace();
			}
		}
		mapConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(mapFile);
		dbFile = new File(p.getDataFolder() + "/db.yml");
		if (!dbFile.exists()) {
			try {
				dbFile.createNewFile();
			} catch (Exception e) {
				p.getLogger().info("Failed to generate db file!");
				e.printStackTrace();
			}
		}
		dbConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(dbFile);
	}

	public void set(String path, Object value) {
		mapConfig.set(path, value);
		try {
			mapConfig.save(mapFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> getLooted() {
		if (mapConfig.get("looted") != null) {
			List<String> s = (List<String>) mapConfig.get("looted");
			return s;
		} else {
			return null;
		}
	}

	public void setWins(String name, int wins) {
		dbConfig.set(name, wins);
		try {
			dbConfig.save(dbFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void resetDB() {
		dbFile.delete();
		dbFile = new File(Main.get().getDataFolder() + "/db.yml");
		if (!dbFile.exists()) {
			try {
				dbFile.createNewFile();
			} catch (Exception e) {
				Main.get().getLogger().info("Failed to generate db file!");
				e.printStackTrace();
			}
		}
		dbConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(dbFile);
		try {
			dbConfig.save(dbFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getWins(String name) {
		return dbConfig.getInt(name);
	}

	public void setDB(String path, Object value) {
		dbConfig.set(path, value);
		try {
			dbConfig.save(dbFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public <T> T get(String path) {
		return (T) mapConfig.get(path);
	}

	public <T> T getDB(String path) {
		return (T) dbConfig.get(path);
	}

	public ConfigurationSection getConfigSection() {
		return mapConfig.getConfigurationSection("maps");
	}

	public ConfigurationSection getConfigSectionDB() {
		return dbConfig.getConfigurationSection("db");
	}

	public ConfigurationSection createConfiguration(final String path) {
		final ConfigurationSection s = this.mapConfig.createSection(path);
		try {
			this.mapConfig.save(this.mapFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	public ConfigurationSection createConfigurationDB(String path) {
		final ConfigurationSection s = dbConfig.createSection(path);
		try {
			dbConfig.save(dbFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	public Plugin getPlugin() {
		return this.plugin;
	}

}
