package com.gasaferic.areaprotection.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerOperatorStatusChangeEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList(); 

	private boolean operator;
	private Player player;
	
	public PlayerOperatorStatusChangeEvent(boolean operator, Player player) {
		this.operator = operator;
		this.player = player;
	}

	public boolean isOp() {
		return operator;
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