package com.gasaferic.areaprotection.model;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.gasaferic.areaprotection.enums.AreaFlagTypes;

public class AreaFlags {
	
	private ArrayList<AreaFlag> areaFlags;
	
	public AreaFlags() {
		this.areaFlags = new ArrayList<AreaFlag>();
	}
	
	public boolean addAreaFlag(AreaFlag areaFlag) {
		if (!areaFlags.contains(areaFlag) && getAreaFlagByFlagType(areaFlag) == null) {
			areaFlags.add(areaFlag);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean removeAreaFlag(AreaFlag areaFlag) {
		if (areaFlags.contains(areaFlag)) {
			areaFlags.remove(areaFlag);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean removeAreaByFlagType(AreaFlagTypes areaFlagType) {
		AreaFlag foundAreaFlag = getAreaFlagByExactFlagType(areaFlagType);
		
		if (foundAreaFlag != null) {
			areaFlags.remove(foundAreaFlag);
			return true;
		} else {
			return false;
		}
	}
	
	public AreaFlag getAreaFlagByFlagType(AreaFlag areaFlag) {		
		return getAreaFlagByExactFlagType(areaFlag.getAreaFlagType());
	}
	
	public AreaFlag getAreaFlagByExactFlagType(AreaFlagTypes areaFlagType) {
		AreaFlag foundAreaFlag = null;
		
		for (AreaFlag currentAreaFlag : areaFlags) {
			if (areaFlagType.equals(currentAreaFlag.getAreaFlagType())) {
				foundAreaFlag = currentAreaFlag;
				break;
			}
		}
		
		return foundAreaFlag;
	}

	public ArrayList<AreaFlag> getAreaFlags() {
		return areaFlags;
	}

	public void updateAreaFlagAllow(AreaFlagTypes areaFlagType, boolean allow) {
		getAreaFlagByExactFlagType(areaFlagType).setAllowed(allow);
	}
	
	public boolean isPlayerAllowedByFlag(Player player, AreaFlagTypes areaFlagType) {
		AreaFlag areaFlag = null;
		if ((areaFlag = getAreaFlagByExactFlagType(areaFlagType)) != null) {
			if (areaFlag.isAllowed()) {
				return true;
			} else {
				if (player.hasPermission("areaprotection.bypass")) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			return true;
		}
	}

}
