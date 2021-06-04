package com.gasaferic.areaprotection.managers;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.gasaferic.areaprotection.model.AreaPlayer;

public class AreaPlayerManager {

	private ArrayList<AreaPlayer> areaPlayers = new ArrayList<AreaPlayer>();

	public ArrayList<AreaPlayer> getAreaPlayers() {
		return this.areaPlayers;
	}

	public AreaPlayer getAreaPlayerByPlayer(Player player) {
		AreaPlayer targetAreaPlayer = null;

		for (AreaPlayer currentAreaPlayer : areaPlayers) {
			if (currentAreaPlayer.getPlayer() != null && currentAreaPlayer.getPlayer().equals(player)) {
				targetAreaPlayer = currentAreaPlayer;
				break;
			}
		}

		return targetAreaPlayer;
	}

	public AreaPlayer getAreaPlayerByUUID(UUID uuid) {
		AreaPlayer targetAreaPlayer = null;

		for (AreaPlayer currentAreaPlayer : areaPlayers) {
			if (currentAreaPlayer.getUniqueId().equals(uuid)) {
				targetAreaPlayer = currentAreaPlayer;
				break;
			}
		}

		return targetAreaPlayer;
	}

	public void registerAreaPlayer(AreaPlayer areaPlayer) {
		if (!areaPlayers.contains(areaPlayer) && getAreaPlayerByPlayer(areaPlayer.getPlayer()) == null) {
			this.areaPlayers.add(areaPlayer);
		}
	}

	public void unregisterAreaPlayer(AreaPlayer areaPlayer) {
		if (areaPlayers.contains(areaPlayer)) {
			areaPlayers.remove(areaPlayer);
		}
	}

	public void unregisterAreaPlayers() {
		areaPlayers.iterator().forEachRemaining((areaPlayer) -> {
			areaPlayer = null;
			areaPlayers.remove(areaPlayer);
		});
	}

	public void disableSetupMode() {
		for (AreaPlayer areaPlayer : areaPlayers) {
			if (areaPlayer.protectionModeEnabled()) {
				areaPlayer.toggleProtectionMode();
			}
		}
	}
}
