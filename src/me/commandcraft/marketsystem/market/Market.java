package me.commandcraft.marketsystem.market;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import me.commandcraft.marketsystem.market.events.AdminRemoveEvent;
import me.commandcraft.marketsystem.market.events.MarketUpdatedEvent;
import me.commandcraft.marketsystem.market.events.PlayerBuyEvent;
import me.commandcraft.marketsystem.market.events.PlayerSellEvent;

public class Market implements Listener {

	private List<LightItem> items;

	@SuppressWarnings("serial")
	public Market(File folder) {
		try {
			File file = new File(folder, "market.json");
			Type type = new TypeToken<List<LightItem>>() {}.getType();
			BufferedReader reader = new BufferedReader(new FileReader(file));
			items = new Gson().fromJson(reader, type);
		} catch (IOException e) {
			items = new ArrayList<LightItem>();
		}
	}

	public void save(File folder) {
		try {
			Gson gson = new Gson();
			folder.mkdirs();
			File file = new File(folder, "market.json");
			if (!file.exists()) file.createNewFile();
			Writer writer = new FileWriter(file);
			writer.write(gson.toJson(items));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public List<LightItem> getItems() {
		return items;
	}

	@EventHandler
	public void onPlayerBuy(PlayerBuyEvent event) {
		for (LightItem item : items) {
			if (event.getItemId().equals(item.getId())) {
				items.remove(item);
				event.setItem(item.getItemStack());
				new MarketUpdatedEvent();
				return;
			}
		}
		event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerSell(PlayerSellEvent event) {
		items.add(event.getItem());
		new MarketUpdatedEvent();
	}

	@EventHandler
	public void onAdminRemove(AdminRemoveEvent event) {
		for (int i = items.size() - 1; i >= 0; i--) {
			if (items.get(i).getId().equals(event.getId())) {
				items.remove(i);
				new MarketUpdatedEvent();
				return;
			}
		}
	}
}