package me.commandcraft.marketsystem.market;

import org.bukkit.inventory.ItemStack;

public class Enchantment {

	private String enchantment;
	private int level;
	
	public Enchantment(org.bukkit.enchantments.Enchantment enchantment, int level) {
		this.enchantment = enchantment.getName();
		this.level = level;
	}
	
	public ItemStack addEnchantment(ItemStack item) {
		org.bukkit.enchantments.Enchantment ench = org.bukkit.enchantments.Enchantment.getByName(enchantment);
		item.addUnsafeEnchantment(ench, level);
		return item;
	}
}
