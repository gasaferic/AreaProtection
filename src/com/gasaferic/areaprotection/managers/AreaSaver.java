/*
 * THIS MADE BY GASAFERIC MADAFAKAS
 */

package com.gasaferic.areaprotection.managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.util.Vector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.gasaferic.areaprotection.enums.AreaFlagTypes;
import com.gasaferic.areaprotection.main.Main;
import com.gasaferic.areaprotection.model.Area;
import com.gasaferic.areaprotection.model.AreaFlag;
import com.gasaferic.areaprotection.model.AreaFlags;
import com.gasaferic.areaprotection.model.MessageAreaFlag;

public class AreaSaver {

	AreaManager areaManager = Main.getAreaManager();

	@SuppressWarnings("unchecked")
	public void saveToFile(ArrayList<Area> areas, File file) throws FileNotFoundException, IOException {

		JSONObject jsonObj = new JSONObject();

		JSONArray areasJSONArray = new JSONArray();

		for (Area area : areas) {
			areasJSONArray.add(areaToJSONObj(area));
		}

		jsonObj.put("Areas", areasJSONArray);

		if (file.exists()) {
			file.delete();
			file.createNewFile();
		}

		FileOutputStream outputStream = new FileOutputStream(file);
		outputStream.write(jsonObj.toJSONString().getBytes());
		outputStream.close();

	}

	@SuppressWarnings("unchecked")
	public JSONObject areaToJSONObj(Area area) {

		JSONObject areaJSONObj = new JSONObject();

		areaJSONObj.put("areaName", area.getAreaName());
		areaJSONObj.put("areaOwnerUUID", area.getAreaOwner().getUniqueId().toString());
		areaJSONObj.put("enabled", area.isEnabled());
		areaJSONObj.put("firstPoint", getJSONArrayFromVector(area.getFirstPos()));
		areaJSONObj.put("secondPoint", getJSONArrayFromVector(area.getSecondPos()));
		areaJSONObj.put("locationString", area.getAreaLocation().toString());
		areaJSONObj.put("overlappable", area.isOverlappable());
		areaJSONObj.put("areaFlags", getJSONArrayFromAreaFlags(area.getAreaFlags()));

		return areaJSONObj;

	}

	@SuppressWarnings("unchecked")
	public JSONArray getJSONArrayFromVector(Vector vector) {
		JSONArray vectorJSONArray = new JSONArray();

		vectorJSONArray.add(vector.getBlockX());
		vectorJSONArray.add(vector.getBlockY());
		vectorJSONArray.add(vector.getBlockZ());

		return vectorJSONArray;
	}

	@SuppressWarnings("unchecked")
	public JSONArray getJSONArrayFromAreaFlags(AreaFlags areaFlags) {
		JSONArray areaFlagsJSONArray = new JSONArray();

		for (AreaFlag areaFlag : areaFlags.getAreaFlags()) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("flagName", areaFlag.getAreaFlagType().toString());
			jsonObject.put("allow", areaFlag.isAllowed());
			if (areaFlag.getAreaFlagType().equals(AreaFlagTypes.GREET_ON_ENTER) || areaFlag.getAreaFlagType().equals(AreaFlagTypes.GREET_ON_LEAVE)) {
				jsonObject.put("message", ((MessageAreaFlag) areaFlag).getMessage());
			}
			areaFlagsJSONArray.add(jsonObject);
		}

		return areaFlagsJSONArray;
	}

}