package com.gasaferic.areaprotection.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.gasaferic.areaprotection.enums.AreaFlagTypes;
import com.gasaferic.areaprotection.main.Main;
import com.gasaferic.areaprotection.managers.AreaManager;
import com.gasaferic.areaprotection.managers.AreaPlayerManager;
import com.gasaferic.areaprotection.model.Area;
import com.gasaferic.areaprotection.model.AreaPlayer;

public class PlayerFlagListener implements Listener {

	AreaPlayerManager areaPlayerManager = Main.getAreaPlayerManager();
	AreaManager areaManager = Main.getAreaManager();

	@EventHandler
	public void onPlayerBuildBlockEvent(BlockPlaceEvent e) {
		Player player = e.getPlayer();

		Area area = areaManager.getAreaByLocation(e.getBlock().getLocation());

		if (area != null && !area.getAreaFlags().isPlayerAllowedByFlag(player, AreaFlagTypes.BUILD)) {
			areaPlayerManager.getAreaPlayerByPlayer(player)
					.sendMessage("&c&lAreaProtection &7Non puoi piazzare blocchi in questa area!");
			e.setCancelled(true);
		}

	}

	@EventHandler
	public void onPlayerBreakBlockEvent(BlockBreakEvent e) {
		Player player = e.getPlayer();

		Area area = areaManager.getAreaByLocation(e.getBlock().getLocation());

		if (area != null && !area.getAreaFlags().isPlayerAllowedByFlag(player, AreaFlagTypes.BREAK)) {
			areaPlayerManager.getAreaPlayerByPlayer(player)
					.sendMessage("&c&lAreaProtection &7Non puoi distruggere i blocchi di questa area!");
			e.setCancelled(true);
		}

	}

	@EventHandler
	public void onPlayerPickupItemEvent(EntityPickupItemEvent e) {
		if (!(e.getEntity() instanceof Player)) { return; }
		
		Player player = (Player) e.getEntity();
		
		AreaPlayer customPlayer = areaPlayerManager.getAreaPlayerByPlayer(player);

		Area area = customPlayer.getCurrentArea();

		if (area != null && !area.getAreaFlags().isPlayerAllowedByFlag(player, AreaFlagTypes.PICKUP_ITEM)) {
			customPlayer.sendMessage("&c&lAreaProtection &7Non puoi raccogliere gli oggetti in questa area!");
			e.setCancelled(true);
		}

	}

	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
		AreaPlayer customPlayer = areaPlayerManager.getAreaPlayerByPlayer(e.getPlayer());

		Area area = customPlayer.getCurrentArea();

		if (area != null && !area.getAreaFlags().isPlayerAllowedByFlag(e.getPlayer(), AreaFlagTypes.DROP_ITEM)) {
			customPlayer.sendMessage("&c&lAreaProtection &7Non puoi buttare a terra gli oggetti in questa area!");
			e.setCancelled(true);
		}

	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		AreaPlayer customPlayer = areaPlayerManager.getAreaPlayerByPlayer(e.getPlayer());

		Area area = customPlayer.getCurrentArea();

		if (area != null && !area.getAreaFlags().isPlayerAllowedByFlag(e.getPlayer(), AreaFlagTypes.BLOCK_INTERACT)) {
			customPlayer.sendMessage("&c&lAreaProtection &7Non puoi interagire con i blocchi di quest'area!");
			e.setCancelled(true);
		}

	}

}