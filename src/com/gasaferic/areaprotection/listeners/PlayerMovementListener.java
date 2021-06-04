package com.gasaferic.areaprotection.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.gasaferic.areaprotection.enums.AreaFlagTypes;
import com.gasaferic.areaprotection.events.AreaEnterEvent;
import com.gasaferic.areaprotection.events.AreaLeaveEvent;
import com.gasaferic.areaprotection.main.Main;
import com.gasaferic.areaprotection.managers.AreaManager;
import com.gasaferic.areaprotection.managers.AreaPlayerManager;
import com.gasaferic.areaprotection.model.Area;
import com.gasaferic.areaprotection.model.AreaPlayer;

public class PlayerMovementListener implements Listener {

	AreaPlayerManager areaPlayerManager = Main.getAreaPlayerManager();
	AreaManager areaManager = Main.getAreaManager();

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		AreaPlayer areaPlayer = areaPlayerManager.getAreaPlayerByPlayer(player);

		boolean differentX = e.getFrom().getBlockX() != e.getTo().getBlockX();
		boolean differentY = e.getFrom().getBlockY() != e.getTo().getBlockY();
		boolean differentZ = e.getFrom().getBlockZ() != e.getTo().getBlockZ();

		if (differentX || differentY || differentZ) {
			Area currentArea = areaManager.getAreaByLocation(e.getTo());
			if (currentArea != null) {
				if (canEnterArea(player, currentArea)) {
					if (areaPlayer.getCurrentArea() == null) {
						enterArea(areaPlayer, currentArea);
					} else {
						if (areaPlayer.getCurrentArea() != currentArea) {
							if (canLeaveArea(player, areaPlayer.getCurrentArea())) {
								leaveArea(areaPlayer);
								enterArea(areaPlayer, currentArea);
							} else {
								areaPlayer.sendMessage("&c&lAreaProtection &7Non puoi uscire da questa area!");
								kickBack(player, e.getFrom());
							}
						}
					}
				} else {
					areaPlayer.sendMessage("&c&lAreaProtection &7Non puoi entrare in questa area!");
					kickBack(player, e.getFrom());
				}
			} else {
				if (areaPlayer.getCurrentArea() != null) {
					if (canLeaveArea(player, areaPlayer.getCurrentArea())) {
						leaveArea(areaPlayer);
					} else {
						areaPlayer.sendMessage("&c&lAreaProtection &7Non puoi uscire da questa area!");
						kickBack(player, e.getFrom());
					}
				}
			}
		}

	}
	
	public void kickBack(Player player, Location from) {
		player.teleport(from);
		player.playSound(player.getLocation(), Sound.GHAST_FIREBALL, 1, 1);
	}

	public boolean canEnterArea(Player player, Area area) {
		return area.getAreaFlags().isPlayerAllowedByFlag(player, AreaFlagTypes.ENTER);
	}

	public boolean canLeaveArea(Player player, Area area) {
		return area.getAreaFlags().isPlayerAllowedByFlag(player, AreaFlagTypes.LEAVE);
	}

	public void enterArea(AreaPlayer areaPlayer, Area area) {

		AreaEnterEvent areaEnterEvent = new AreaEnterEvent(area, areaPlayer.getPlayer());
		Bukkit.getPluginManager().callEvent(areaEnterEvent);
		areaPlayer.setCurrentArea(area);

	}

	public void leaveArea(AreaPlayer areaPlayer) {

		AreaLeaveEvent areaLeaveEvent = new AreaLeaveEvent(areaPlayer.getCurrentArea(), areaPlayer.getPlayer());
		Bukkit.getPluginManager().callEvent(areaLeaveEvent);
		areaPlayer.setCurrentArea(null);

	}

}