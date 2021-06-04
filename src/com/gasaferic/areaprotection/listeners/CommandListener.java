package com.gasaferic.areaprotection.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import com.gasaferic.areaprotection.events.PlayerOperatorStatusChangeEvent;
import com.gasaferic.areaprotection.main.Main;
import com.gasaferic.areaprotection.managers.AreaPlayerManager;
import com.gasaferic.areaprotection.model.AreaPlayer;

public class CommandListener implements Listener {

	AreaPlayerManager areaPlayerManager = Main.getAreaPlayerManager();

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
		if (!e.getMessage().contains("op")) {
			return;
		}
		
		String[] commandParts = e.getMessage().replace("/", "").split(" ");
		
		callEvent(commandParts);
		
	}
	
	@EventHandler
	public void onConsoleCommand(ServerCommandEvent e) {
		if (!e.getCommand().contains("op")) {
			return;
		}
		
		String[] commandParts = e.getCommand().split(" ");
		
		callEvent(commandParts);
		
	}
	
	public void callEvent(String[] commandParts) {
		if (commandParts.length == 1) {
			return;
		}
		
		Player player = Bukkit.getPlayerExact(commandParts[1]);
		
		if (player != null) {
			if (commandParts[0].equals("op") && !player.isOp()) {
				PlayerOperatorStatusChangeEvent playerOperatorStatusChangeEvent = new PlayerOperatorStatusChangeEvent(true, player);
				Bukkit.getPluginManager().callEvent(playerOperatorStatusChangeEvent);
			} else if (commandParts[0].equals("deop") && player.isOp()) {
				PlayerOperatorStatusChangeEvent playerOperatorStatusChangeEvent = new PlayerOperatorStatusChangeEvent(false, player);
				Bukkit.getPluginManager().callEvent(playerOperatorStatusChangeEvent);
			}
		}
	}
	
	@EventHandler
	public void onPlayerLoseOp(PlayerOperatorStatusChangeEvent event) {
		AreaPlayer areaPlayer = null;
		if ((areaPlayer = areaPlayerManager.getAreaPlayerByPlayer(event.getPlayer())).protectionModeEnabled() && !event.isOp()) {
			areaPlayer.toggleProtectionMode();
		}
	}

}