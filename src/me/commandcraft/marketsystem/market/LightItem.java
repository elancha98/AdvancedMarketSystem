package me.commandcraft.marketsystem.market;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LightItem {

	private String owner;
	private double price;
	private String id;
	
	private Material material;
	private byte data;
	private int amount;
	private String name;
	private Enchantment[] enchantments;
	
	public LightItem(Player player, double price) {
		this(player, player.getItemInHand(), price);
	}
	
	@SuppressWarnings("deprecation")
	public LightItem(Player player, ItemStack item, double price) {
		this.owner = player.getName();
		this.price = price;
		this.material = item.getType();
		this.data = item.getData().getData();
		this.amount = item.getAmount();
		this.name = item.getItemMeta().getDisplayName();
		Map<org.bukkit.enchantments.Enchantment, Integer> map = item.getEnchantments();
		enchantments = new Enchantment[map.size()];
		Iterator<org.bukkit.enchantments.Enchantment> iterator = map.keySet().iterator();
		int i = 0;
		while (iterator.hasNext()) {
			org.bukkit.enchantments.Enchantment enchantment = (org.bukkit.enchantments.Enchantment) iterator.next();
			enchantments[i++] = new Enchantment(enchantment, map.get(enchantment));
		}
		this.id = UUID.randomUUID().toString();
	}
	
	public ItemStack getRawItemStack() {
		return new ItemStack(material, amount, data);
	}
	
	public ItemStack getItemStack() {
		ItemStack itemStack = new ItemStack(material, amount, data);
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(name);
		itemStack.setItemMeta(meta);
		for (Enchantment ench : enchantments) {
			itemStack = ench.addEnchantment(itemStack);
		}
		return itemStack;
	}
	
	public boolean withdrawAmount(int n) {
		this.amount -= n;
		return amount == 0;
	}
	
	public String getId() {return this.id;}

	public String getOwner() {return owner;}

	public double getPrice() {return price;}

	public Material getMaterial() {return material;}

	public byte getData() {return data;}

	public int getAmount() {return amount;}

	public String getName() {return name;}

	public Enchantment[] getEnchantments() {return enchantments;}
}
