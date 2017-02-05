package me.commandcraft.marketsystem;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.commandcraft.marketsystem.gui.ShopInventory;
import me.commandcraft.marketsystem.market.LightItem;
import me.commandcraft.marketsystem.market.events.PlayerSellEvent;

public class CommandManager {

	public static void process(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "only players can do that");
			return;
		}
		Player player = (Player) sender;
		if (args.length == 0) {
			if (player.hasPermission("ams.open")) {
				new ShopInventory(player);
			} else player.sendMessage(ChatColor.RED + "you don't have permission to do that");
		} else if (args.length > 1) {
			if (args[0].equals("sell")) {
				if (player.hasPermission("ams.user")) {
					try {
						double price = Double.parseDouble(args[1]);
						LightItem item = new LightItem(player, price);
						new PlayerSellEvent(item);
						player.setItemInHand(null);
					}
					catch (Exception e) {
						player.sendMessage(ChatColor.RED + "enter a valid price");
					}
				} else player.sendMessage(ChatColor.RED + "you don't have permission to do that");
			} else usage(player);
		} else usage(player);
	}
	
	public static void usage(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "usage:");
		if (sender.hasPermission("ams.open"))
			sender.sendMessage(ChatColor.GREEN + " - /shop " + ChatColor.YELLOW + "to open the market GUI");
		if (sender.hasPermission("ams.user"))
			sender.sendMessage(ChatColor.GREEN + " - /shop sell <price> " + ChatColor.YELLOW + "to sell item in your hand");
	}
}
