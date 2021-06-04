package com.gasaferic.areaprotection.model;

import org.bukkit.util.Vector;

public class Selection {
	
	private Vector firstPos;
	private Vector secondPos;
	
	public Vector getFirstPos() {
		return firstPos;
	}

	public void setFirstPos(Vector firstPos) {
		this.firstPos = firstPos;
	}

	public Vector getSecondPos() {
		return secondPos;
	}

	public void setSecondPos(Vector secondPos) {
		this.secondPos = secondPos;
	}

	public void clearSelection() {
		this.firstPos = null;
		this.secondPos = null;
	}
	
}
