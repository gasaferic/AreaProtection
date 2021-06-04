package com.gasaferic.areaprotection.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.gasaferic.areaprotection.main.Main;
import com.gasaferic.areaprotection.model.AreaPlayer;

public class ProtectionMode implements CommandExecutor, Listener {

	public ProtectionMode() {
		Main.registerEvent(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = (Player) sender;
		
		AreaPlayer areaPlayer = Main.getAreaPlayerManager().getAreaPlayerByPlayer(player);
		
		if (player.hasPermission("areaprotection.protectionmode")) {
			areaPlayer.toggleProtectionMode();
		}

		return false;
	}

	@EventHandler
	public void onInvClick(InventoryClickEvent event) {

		if (event.getInventory() == null || event.getCurrentItem() == null) {
			return;
		}

		AreaPlayer areaPlayer = Main.getAreaPlayerManager().getAreaPlayerByPlayer((Player) event.getWhoClicked());

		if (areaPlayer.protectionModeEnabled()) {
			areaPlayer.getPlayer().playSound(areaPlayer.getPlayer().getLocation(), Sound.NOTE_BASS, 5, 1);
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {

		AreaPlayer areaPlayer = Main.getAreaPlayerManager().getAreaPlayerByPlayer(event.getPlayer());

		if (areaPlayer.protectionModeEnabled()) {
			areaPlayer.getPlayer().playSound(areaPlayer.getPlayer().getLocation(), Sound.NOTE_BASS, 5, 1);
			event.setCancelled(true);
		}
	}

}