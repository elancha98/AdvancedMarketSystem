package me.commandcraft.marketsystem.market.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AdminRemoveEvent extends Event {
	
	public static final HandlerList handlers = new HandlerList();
	private String id;
	
	public AdminRemoveEvent(String id) {
		this.id = id;
		Bukkit.getPluginManager().callEvent(this);
	}
	
	public String getId() {
		return id;
	}

	@Override
	public HandlerList getHandlers() { 
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
