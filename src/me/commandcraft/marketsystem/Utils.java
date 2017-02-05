package me.commandcraft.marketsystem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.commandcraft.marketsystem.market.Enchantment;
import me.commandcraft.marketsystem.market.LightItem;

public class Utils {
	
	public static Class<?> getOBCClass(String name) {
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		try {
			return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Class<?> getNMSClass(String name) {
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		try {
			return Class.forName("net.minecraft.server." + version + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Class<?> getCraftItem() {
		return getOBCClass("inventory.CraftItemStack");
	}
	
	public static Class<?> getItem() {
		return getNMSClass("ItemStack");
	}
	
	public static Class<?> getNBTTag() {
		return getNMSClass("NBTTagCompound");
	}
	
	public static ItemStack createItem(LightItem lightItem) {
		ItemStack itemStack = lightItem.getRawItemStack();
		try {
			Object item = getCraftItem().getMethod("asNMSCopy", ItemStack.class).invoke(null, itemStack);
			Object nbt = getNBTTag().getConstructor().newInstance();
			nbt.getClass().getMethod("setString", String.class, String.class).invoke(nbt, "market_id", lightItem.getId());
			item.getClass().getMethod("setTag", getNBTTag()).invoke(item, nbt);
			ItemStack ret = (ItemStack) getCraftItem().getMethod("asBukkitCopy", getItem()).invoke(null, item);
			ItemMeta meta = ret.getItemMeta();
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.GOLD + "Owner: " + lightItem.getOwner());
			lore.add(ChatColor.GOLD + "Price: " + lightItem.getPrice());
			meta.setLore(lore);
			meta.setDisplayName(lightItem.getName());
			ret.setItemMeta(meta);
			for (Enchantment ench : lightItem.getEnchantments()) {
				ret = ench.addEnchantment(ret);
			}
			return ret;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getItemId(ItemStack itemStack) {
		try {
			Object item = getCraftItem().getMethod("asNMSCopy", ItemStack.class).invoke(null, itemStack);
			Object nbt = item.getClass().getMethod("getTag").invoke(item);
			return (String) nbt.getClass().getMethod("getString", String.class).invoke(nbt, "market_id");
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static double getPrice(ItemStack itemStack) {
		List<String> lore = itemStack.getItemMeta().getLore();
		return Double.parseDouble(lore.get(1).substring(9));
	}
	
	public static Player getPlayer(ItemStack itemStack) {
		List<String> lore = itemStack.getItemMeta().getLore();
		return Bukkit.getPlayer(lore.get(0).substring(9));
	}
}
