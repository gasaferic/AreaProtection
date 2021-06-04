package com.gasaferic.areaprotection.managers;

import java.util.ArrayList;

import org.bukkit.Location;

import com.gasaferic.areaprotection.model.Area;

public class AreaManager {

	private ArrayList<Area> areas = new ArrayList<Area>();

	public boolean registerArea(Area area) {
		if (getAreaFromName(area.getAreaName()) == null) {
			areas.add(area);
			return true;
		} else {
			return false;
		}
	}

	public void unregisterArea(Area area) {
		if (areas.contains(area)) {
			areas.remove(area);
		}
	}

	public ArrayList<Area> getAreas() {
		return areas;
	}
	
	public boolean areaAlreadyExists(String areaName) {
		return getAreaFromName(areaName) != null;
	}
	
	public boolean isAreaOverlapping(Area area) {
		return getOverlappingArea(area) != null;
	}
	
	public Area getOverlappingArea(Area area) {
		Area overlappingArea = null;
		
		for (Area currentArea : areas) {
			if (currentArea.inArea(area.getFirstPos().toLocation(area.getAreaLocation().getWorld())) || currentArea.inArea(area.getSecondPos().toLocation(area.getAreaLocation().getWorld()))) {
				overlappingArea = currentArea;
				break;
			}
		}
		
		return overlappingArea;
	}
	
	public boolean isAreaProtected(Location loc) {
		if (getAreaByLocation(loc) != null) {
			return true;
		} else {
			return false;
		}
	}

	public Area getAreaFromName(String areaName) {
		Area targetArea = null;

		for (Area area : areas) {
			if (area.getAreaName().equals(areaName)) {
				targetArea = area;
				break;
			}
		}

		return targetArea;
	}

	public Area getAreaByLocation(Location loc) {

		Area targetArea = null;

		for (Area area : areas) {
			if (area.isEnabled() && area.inArea(loc)) {
				targetArea = area;
				break;
			}
		}

		return targetArea;
	}

}