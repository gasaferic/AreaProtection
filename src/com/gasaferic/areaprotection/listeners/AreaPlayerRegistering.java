package com.gasaferic.areaprotection.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gasaferic.areaprotection.exceptions.AreaPlayerAlreadyExistsException;
import com.gasaferic.areaprotection.main.Main;
import com.gasaferic.areaprotection.managers.AreaManager;
import com.gasaferic.areaprotection.managers.AreaPlayerManager;
import com.gasaferic.areaprotection.model.Area;
import com.gasaferic.areaprotection.model.AreaPlayer;

public class AreaPlayerRegistering implements Listener {

	AreaPlayerManager areaPlayerManager = Main.getAreaPlayerManager();
	AreaManager areaManager = Main.getAreaManager();

	@EventHandler
	public void onJoinRegister(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		
		AreaPlayer areaPlayer;
		try {
			areaPlayer = new AreaPlayer(player);
			areaPlayerManager.registerAreaPlayer(areaPlayer);
			
			Area area;
			
			if ((area = areaManager.getAreaByLocation(e.getPlayer().getLocation())) != null) {
				areaPlayer.updateCurrentArea(area);
			}
		} catch (AreaPlayerAlreadyExistsException ex) {
			ex.printStackTrace();
		}
		
		// DEBUG TO CHECK REGISTERED AREAS
		
		/* for (Area area : Main.getAreaManager().getAreas()) {
			Bukkit.getConsoleSender().sendMessage("§9§lAREA REGISTRATA");
			Bukkit.getConsoleSender().sendMessage("§9§lNOME AREA > " + area.getAreaName());
			Bukkit.getConsoleSender().sendMessage("§9§lPRIMO PUNTO AREA > " + area.getFirstPos().toString());
			Bukkit.getConsoleSender().sendMessage("§9§lSECONDO PUNTO AREA > " + area.getSecondPos().toString());
			Bukkit.getConsoleSender().sendMessage("§9§lABILITAZIONE AREA > " + area.isEnabled());
		} */
	}

	@EventHandler
	public void onLeftUnregister(PlayerQuitEvent e) {
		Player player = e.getPlayer();

		AreaPlayer areaPlayer = areaPlayerManager.getAreaPlayerByPlayer(player);

		if (areaPlayer.protectionModeEnabled()) {
			areaPlayer.toggleProtectionMode();
		}
		
		if (areaPlayer.getCurrentArea() != null) {
			areaPlayer.updateCurrentArea(null);
		}
		
		areaPlayerManager.unregisterAreaPlayer(areaPlayer);
	}

}