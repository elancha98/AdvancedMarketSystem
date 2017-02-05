package me.commandcraft.marketsystem;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configuration {
	
	private FileConfiguration config;

	public Configuration(File folder) {
		folder.mkdirs();
		File file = new File(folder, "config.yml");
		if (!file.exists()) 
			Main.instance.saveDefaultConfig();
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public void save(File folder) {
		folder.mkdirs();
		File file = new File(folder, "config.yml");
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getString(String s) {
		return config.getString(s).toUpperCase();
	}
	
	public byte getByte(String s) {
		return (byte) config.getInt(s);
	}
}
