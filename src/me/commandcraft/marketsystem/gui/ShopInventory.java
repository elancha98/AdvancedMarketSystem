package me.commandcraft.marketsystem.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import me.commandcraft.marketsystem.Main;
import me.commandcraft.marketsystem.Utils;
import me.commandcraft.marketsystem.market.LightItem;
import me.commandcraft.marketsystem.market.events.AdminRemoveEvent;
import me.commandcraft.marketsystem.market.events.MarketUpdatedEvent;
import me.commandcraft.marketsystem.market.events.PlayerBuyEvent;

public class ShopInventory implements Listener {

	public static int pages = 1;

	private String owner;
	private int page;
	private boolean admin;

	public ShopInventory(Player player) {
		this.admin = false;
		this.page = 0;
		this.owner = player.getName();
		Inventory inventory = Bukkit.createInventory(null, 54);
		populateInventory(inventory);
		addPages(inventory);
		Main.registerEvents(this);
		player.openInventory(inventory);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (player.getName().equals(owner)) {
			event.setCancelled(true);
			ItemStack item = event.getCurrentItem();
			if (item == null)
				return;
			if (event.getSlot() < 45) {
				if (admin) {
					new AdminRemoveEvent(Utils.getItemId(item));
				} else {
					double price = Utils.getPrice(item);
					if (Main.economy.has(player, price)) {
						PlayerBuyEvent e = new PlayerBuyEvent(Utils.getItemId(item));
						if (!e.isCancelled()) {
							if (player.getInventory().firstEmpty() != -1) {
								Main.economy.withdrawPlayer(player, price);
								Main.economy.depositPlayer(Utils.getPlayer(item), price);
								player.getInventory().addItem(e.getItem());
							} else
								player.sendMessage(ChatColor.RED + "You need more space");
						} else
							player.sendMessage(ChatColor.RED + "an error occurred, please try again");
					} else
						player.sendMessage(ChatColor.RED + "You need more money");
				}
			}
			if (event.getSlot() == 49) {
				this.admin = !this.admin;
				populateInventory(event.getClickedInventory());
				addPages(event.getClickedInventory());
			}
			if (event.getSlot() == 52) {
				this.page++;
				populateInventory(event.getClickedInventory());
				addPages(event.getClickedInventory());
			}
			if (event.getSlot() == 46) {
				this.page--;
				populateInventory(event.getClickedInventory());
				addPages(event.getClickedInventory());
			}
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		if (event.getPlayer().getName().equals(owner)) {
			HandlerList.unregisterAll(this);
		}
	}

	@EventHandler
	public void onMarketUpdate(MarketUpdatedEvent event) {
		InventoryView inventoryView = Bukkit.getPlayer(owner).getOpenInventory();
		Inventory inventory = inventoryView.getTopInventory();
		populateInventory(inventory);
		addPages(inventory);
	}

	public void populateInventory(Inventory inventory) {
		inventory.clear();
		List<LightItem> items = Main.market.getItems();
		pages = (int) Math.ceil(items.size() / 45.0);
		pages = Math.max(pages, 1);
		page = Math.min(page, pages - 1);
		page = Math.max(0, page);
		for (int j = page * 45; j < (page + 1) * 45 && j < items.size(); j++) {
			inventory.setItem(j % 45, Utils.createItem(items.get(j)));
		}
	}

	public void addPages(Inventory inventory) {
		if (page != 0)
			inventory.setItem(46, Items.getPrev(admin).getItem());
		if (page + 1 < pages)
			inventory.setItem(52, Items.getNext(admin).getItem());
		Player player = Bukkit.getPlayer(owner);
		if (player.hasPermission("ams.admin"))
			inventory.setItem(49, Items.getMid(admin).getItem());
	}
}
