package com.gasaferic.areaprotection.api;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.gasaferic.areaprotection.main.Main;
import com.gasaferic.areaprotection.managers.AreaManager;
import com.gasaferic.areaprotection.managers.AreaPlayerManager;
import com.gasaferic.areaprotection.model.Area;
import com.gasaferic.areaprotection.model.AreaFlags;
import com.gasaferic.areaprotection.model.AreaPlayer;
import com.gasaferic.areaprotection.model.Selection;

public class AreaProtectionAPI {

	AreaPlayerManager areaPlayerManager = Main.getAreaPlayerManager();
	AreaManager areaManager = Main.getAreaManager();
	
	public AreaPlayer getAreaPlayerByPlayer(Player player) {
		return areaPlayerManager.getAreaPlayerByPlayer(player);
	}
	
	public AreaPlayer getAreaPlayerByUUID(Player player) {
		return areaPlayerManager.getAreaPlayerByPlayer(player);
	}
	
	public Area getAreaByLocation(Location location) {
		return areaManager.getAreaByLocation(location);
	}
	
	public Area getAreaByName(String areaName) {
		return areaManager.getAreaFromName(areaName);
	}
	
	public Area createNewArea(String areaName, UUID areaOwnerUniqueId, Selection selection, Location location, boolean overlappable, AreaFlags areaFlags, boolean enabled) {
		return new Area(areaName, areaOwnerUniqueId, selection, location, overlappable, areaFlags, enabled);
	}
	
	public AreaManager getAreaManager() {
		return areaManager;
	}
	
	public boolean registerArea(Area area) {
		return areaManager.registerArea(area);
	}
	
	public void unregisterArea(Area area) {
		areaManager.unregisterArea(area);
	}
	
}