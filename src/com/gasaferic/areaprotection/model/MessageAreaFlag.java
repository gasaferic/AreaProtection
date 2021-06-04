package com.gasaferic.areaprotection.model;

import com.gasaferic.areaprotection.enums.AreaFlagTypes;

public class MessageAreaFlag extends AreaFlag {
	
	private String message;
	
	public MessageAreaFlag(AreaFlagTypes areaFlagType, boolean allow, String message) {
		super(areaFlagType, allow);
		
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
