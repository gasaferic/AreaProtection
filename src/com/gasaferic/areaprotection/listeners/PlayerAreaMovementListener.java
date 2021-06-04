package com.gasaferic.areaprotection.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gasaferic.areaprotection.enums.AreaFlagTypes;
import com.gasaferic.areaprotection.events.AreaEnterEvent;
import com.gasaferic.areaprotection.events.AreaLeaveEvent;
import com.gasaferic.areaprotection.main.Main;
import com.gasaferic.areaprotection.managers.AreaPlayerManager;
import com.gasaferic.areaprotection.model.AreaPlayer;
import com.gasaferic.areaprotection.model.MessageAreaFlag;

public class PlayerAreaMovementListener implements Listener {

	AreaPlayerManager areaPlayerManager = Main.getAreaPlayerManager();

	@EventHandler
	public void onAreaEnterEvent(AreaEnterEvent e) {
		Player player = e.getPlayer();
		AreaPlayer areaPlayer = areaPlayerManager.getAreaPlayerByPlayer(player);

		MessageAreaFlag areaFlag;
		if ((areaFlag = (MessageAreaFlag) e.getArea().getAreaFlags().getAreaFlagByExactFlagType(AreaFlagTypes.GREET_ON_ENTER)) != null) {
			if (areaFlag.isAllowed()) {
				areaPlayer.sendMessage(areaFlag.getMessage() == null ? "&7&lSei entrato nell'area &a" + e.getArea().getAreaName() : areaFlag.getMessage());
			}
		}

	}

	@EventHandler
	public void onAreaLeaveEvent(AreaLeaveEvent e) {
		Player player = e.getPlayer();
		AreaPlayer areaPlayer = areaPlayerManager.getAreaPlayerByPlayer(player);

		MessageAreaFlag areaFlag;
		if ((areaFlag = (MessageAreaFlag) e.getArea().getAreaFlags().getAreaFlagByExactFlagType(AreaFlagTypes.GREET_ON_LEAVE)) != null) {
			if (areaFlag.isAllowed()) {
				areaPlayer.sendMessage(areaFlag.getMessage() == null ? "&7&lSei uscito dall'area &c" + e.getArea().getAreaName() : areaFlag.getMessage());
			}
		}
		
	}

}