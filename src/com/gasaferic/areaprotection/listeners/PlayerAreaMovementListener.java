package com.gasaferic.areaprotection.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gasaferic.areaprotection.events.AreaEnterEvent;
import com.gasaferic.areaprotection.events.AreaLeaveEvent;
import com.gasaferic.areaprotection.main.Main;
import com.gasaferic.areaprotection.managers.AreaPlayerManager;
import com.gasaferic.areaprotection.model.AreaPlayer;

public class PlayerAreaMovementListener implements Listener {

	AreaPlayerManager areaPlayerManager = Main.getAreaPlayerManager();

	@EventHandler
	public void onAreaEnterEvent(AreaEnterEvent e) {
		Player player = e.getPlayer();
		AreaPlayer areaPlayer = new AreaPlayer(player);

		areaPlayer.sendMessage("&7&lSei entrato nell'area &a" + e.getArea().getAreaName());

	}

	@EventHandler
	public void onAreaLeaveEvent(AreaLeaveEvent e) {
		Player player = e.getPlayer();
		AreaPlayer areaPlayer = new AreaPlayer(player);

		areaPlayer.sendMessage("&7&lSei uscito dall'area &c" + e.getArea().getAreaName());

	}

}