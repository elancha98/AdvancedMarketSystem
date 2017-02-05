package me.commandcraft.marketsystem;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.commandcraft.marketsystem.gui.ShopInventory;
import me.commandcraft.marketsystem.market.Market;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

	public static Economy economy;
	public static Configuration config;
	public static Market market;
	public static JavaPlugin instance;
	
	public void onEnable() {
		instance = this;
		config = new Configuration(getDataFolder());
		market = new Market(getDataFolder());
		Bukkit.getPluginManager().registerEvents(market, this);
		if (!setupEconomy() ) {
            Bukkit.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            return;
        }
	}
	
	public void onDisable() {
		config.save(getDataFolder());
		market.save(getDataFolder());
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("shop")) {
			CommandManager.process(sender, args);
		}
		return true;
	}
	
	public static void registerEvents(ShopInventory inventory) {
		Bukkit.getPluginManager().registerEvents(inventory, instance);
	}
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
}
