package com.gasaferic.areaprotection.model;

import com.gasaferic.areaprotection.enums.AreaFlagTypes;

public class AreaFlag {
	
	private AreaFlagTypes areaFlagType;
	private boolean allow;
	
	public AreaFlag(AreaFlagTypes areaFlagType, boolean allow) {
		this.areaFlagType = areaFlagType;
		this.allow = allow;
	}

	public AreaFlagTypes getAreaFlagType() {
		return areaFlagType;
	}

	public boolean isAllowed() {
		return allow;
	}

	public void setAllowed(boolean allow) {
		this.allow = allow;
	}
	
}
