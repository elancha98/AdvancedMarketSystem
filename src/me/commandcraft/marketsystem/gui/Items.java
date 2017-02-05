package me.commandcraft.marketsystem.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.commandcraft.marketsystem.Main;

public enum Items {

	ADMIN(Main.config.getString("admin.material"), Main.config.getString("admin.name"), Main.config.getByte("admin.data")),
	NORMAL(Main.config.getString("normal.material"), Main.config.getString("normal.name"), Main.config.getByte("normal.data")),
	NEXT_NORMAL(Main.config.getString("next_normal.material"), Main.config.getString("next_normal.name"), Main.config.getByte("next_normal.data")),
	PREV_NORMAL(Main.config.getString("prev_normal.material"), Main.config.getString("prev_normal.name"), Main.config.getByte("prev_normal.data")),
	NEXT_ADMIN(Main.config.getString("next_admin.material"), Main.config.getString("next_admin.name"), Main.config.getByte("next_admin.data")),
	PREV_ADMIN(Main.config.getString("prev_admin.material"), Main.config.getString("prev_admin.name"), Main.config.getByte("prev_admin.data"));
	
	
	private String material;
	private String name;
	private byte data;
	
	Items(String material, String name, byte data) {
		this.material = material;
		this.name = name;
		this.data = data;
	}
	
	public ItemStack getItem() {
		ItemStack itemStack = new ItemStack(Material.getMaterial(material), 1, data);
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(name);
		itemStack.setItemMeta(meta);
		return itemStack;
	}
	
	public static Items getNext(boolean admin) {
		if (admin) return NEXT_ADMIN;
		return NEXT_NORMAL;
	}
	
	public static Items getPrev(boolean admin) {
		if (admin) return PREV_ADMIN;
		return PREV_NORMAL;
	}
	
	public static Items getMid(boolean admin) {
		if (admin) return NORMAL;
		return ADMIN;
	}
}
