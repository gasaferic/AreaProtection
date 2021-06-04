package com.gasaferic.areaprotection.enums;

public enum AreaFlagTypes {
	
	BUILD, BREAK, ENTER, LEAVE, DROP_ITEM, PICKUP_ITEM, BLOCK_INTERACT;

	public static String getAvailableFlags() {
		StringBuilder stringBuilder = new StringBuilder();
		
		AreaFlagTypes[] areaFlagTypes = AreaFlagTypes.values();
		
		for (AreaFlagTypes areaFlagType : areaFlagTypes) {
			stringBuilder.append(areaFlagType.toString().replace("_", "-").toLowerCase() + (areaFlagType == areaFlagTypes[areaFlagTypes.length - 1] ? "" : ","));
		}
		
		
		return stringBuilder.toString();
	}
	
	public static boolean validFlag(String flagString) {
		boolean valid = true;

		try {
			valueOf(flagString);
		} catch (IllegalArgumentException e) {
			valid = false;
		}
		
		return valid;
	}

}
