package me.commandcraft.marketsystem.market.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MarketUpdatedEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	
	public MarketUpdatedEvent() {
		Bukkit.getPluginManager().callEvent(this);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
