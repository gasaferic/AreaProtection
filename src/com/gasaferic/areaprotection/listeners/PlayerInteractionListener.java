package com.gasaferic.areaprotection.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.gasaferic.areaprotection.main.Main;
import com.gasaferic.areaprotection.managers.AreaPlayerManager;
import com.gasaferic.areaprotection.model.AreaPlayer;

public class PlayerInteractionListener implements Listener {

	AreaPlayerManager areaPlayerManager = Main.getAreaPlayerManager();

	@EventHandler
	public void onBlockInteraction(PlayerInteractEvent event) {

		if (event.getClickedBlock() == null) {
			return;
		}

		Block clickedBlock = event.getClickedBlock();
		Vector currentPos = new Vector(clickedBlock.getX(), clickedBlock.getY(), clickedBlock.getZ());

		AreaPlayer areaPlayer = areaPlayerManager.getAreaPlayerByPlayer(event.getPlayer());
		
		if (areaPlayer.protectionModeEnabled()
				&& event.getPlayer().getInventory().getItemInHand().equals(areaPlayer.getProtectionItem())) {
			event.setCancelled(true);
			if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				if (areaPlayer.getSelection().getFirstPos() != null && areaPlayer.getSelection().getFirstPos().equals(currentPos)) { return; }
				areaPlayer.getSelection().setFirstPos(currentPos);
				areaPlayer
						.sendMessage("&c&lSelezione Area &f&l> &7Hai selezionato il primo punto ("
								+ currentPos.getBlockX() + " , " + currentPos.getBlockY() + " , "
								+ currentPos.getBlockZ() + "), seleziona l'altro con il tasto destro");
			} else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				if (areaPlayer.getSelection().getSecondPos() != null && areaPlayer.getSelection().getSecondPos().equals(currentPos)) { return; }
				areaPlayer.getSelection().setSecondPos(currentPos);
				areaPlayer
						.sendMessage("&c&lSelezione Area &f&l> &7Hai selezionato il secondo punto ("
								+ currentPos.getBlockX() + " , " + currentPos.getBlockY() + " , "
								+ currentPos.getBlockZ() + "), seleziona l'altro con il tasto sinistro");
			}
		}

	}

}