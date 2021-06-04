package com.gasaferic.areaprotection.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.gasaferic.areaprotection.model.Area;


public class AreaEnterEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList(); 

	private Area area;
	private Player player;
	
	public AreaEnterEvent(Area area, Player player) {
		this.area = area;
		this.player = player;
	}

	public Area getArea() {
		return area;
	}

	public Player getPlayer() {
		return player;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}