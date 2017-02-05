package me.commandcraft.marketsystem.market.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.commandcraft.marketsystem.market.LightItem;

public class PlayerSellEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private LightItem item;
	
	public PlayerSellEvent(LightItem item) {
		this.item = item;
		Bukkit.getPluginManager().callEvent(this);
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public LightItem getItem() {
		return item;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
