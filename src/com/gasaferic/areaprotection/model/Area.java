package com.gasaferic.areaprotection.model;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Area {

	private String areaName;
	
	private UUID areaOwner;

	private Vector firstPos;
	private Vector secondPos;

	private Location areaLocation;

	private int minX;
	private int minY;
	private int minZ;
	private int maxX;
	private int maxY;
	private int maxZ;

	private boolean overlappable;
	private boolean enabled;

	private AreaFlags areaFlags;

	public Area(String areaName, UUID areaOwnerUniqueId, Selection selection, Location areaLocation, boolean overlappable, AreaFlags areaFlags,
			boolean enabled) {
		this.areaName = areaName;
		this.areaOwner = areaOwnerUniqueId;
		this.firstPos = selection.getFirstPos();
		this.secondPos = selection.getSecondPos();
		this.areaLocation = areaLocation;
		this.overlappable = overlappable;
		this.enabled = enabled;
		this.areaFlags = areaFlags;

		prepareCoords();
	}

	private void prepareCoords() {
		minX = Math.min(firstPos.getBlockX(), secondPos.getBlockX());
		maxX = Math.max(firstPos.getBlockX(), secondPos.getBlockX());
		minY = Math.min(firstPos.getBlockY(), secondPos.getBlockY());
		maxY = Math.max(firstPos.getBlockY(), secondPos.getBlockY());
		minZ = Math.min(firstPos.getBlockZ(), secondPos.getBlockZ());
		maxZ = Math.max(firstPos.getBlockZ(), secondPos.getBlockZ());
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public UUID getAreaOwner() {
		return areaOwner;
	}

	public void setAreaOwner(OfflinePlayer areaOwner) {
		this.areaOwner = areaOwner.getUniqueId();
	}
	
	public boolean isAreaOwner(Player player) {
		return this.areaOwner.equals(player.getUniqueId()) || this.areaOwner == null;
	}
	
	public boolean isAreaOwner(OfflinePlayer offlinePlayer) {
		return this.areaOwner.equals(offlinePlayer.getUniqueId()) || this.areaOwner == null;
	}
	
	public boolean isAreaOwner(UUID uniqueId) {
		return this.areaOwner.equals(uniqueId) || this.areaOwner == null;
	}

	public Location getAreaLocation() {
		return areaLocation;
	}

	public Vector getFirstPos() {
		return firstPos;
	}

	public Vector getSecondPos() {
		return secondPos;
	}
	
	public void updateBounds(Selection selection) {
		this.firstPos = selection.getFirstPos();
		this.secondPos = selection.getSecondPos();

		prepareCoords();
	}
 
	public boolean isOverlappable() {
		return overlappable;
	}

	public void setOverlappable(boolean overlappable) {
		this.overlappable = overlappable;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public AreaFlags getAreaFlags() {
		return areaFlags;
	}

	public void setAreaFlags(AreaFlags areaFlags) {
		this.areaFlags = areaFlags;
	}

	public boolean inArea(Location loc) {
		
		boolean sameWorld = areaLocation.getWorld().equals(loc.getWorld());
		
		boolean betweenX = minX <= loc.getBlockX() && loc.getBlockX() <= maxX;
		boolean betweenY = minY <= loc.getBlockY() && loc.getBlockY() <= maxY;
		boolean betweenZ = minZ <= loc.getBlockZ() && loc.getBlockZ() <= maxZ;
		
		return (betweenX && betweenY && betweenZ) && sameWorld;

	}

}