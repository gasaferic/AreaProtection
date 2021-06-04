/*
 * THIS MADE BY GASAFERIC MADAFAKAS
 */

package com.gasaferic.areaprotection.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.util.Vector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.gasaferic.areaprotection.enums.AreaFlagTypes;
import com.gasaferic.areaprotection.main.Main;
import com.gasaferic.areaprotection.model.Area;
import com.gasaferic.areaprotection.model.AreaFlag;
import com.gasaferic.areaprotection.model.AreaFlags;
import com.gasaferic.areaprotection.model.MessageAreaFlag;
import com.gasaferic.areaprotection.model.Selection;

public class AreaLoader {

	AreaManager areaManager = Main.getAreaManager();

	private JSONParser parser = new JSONParser();

	public void loadFromFile(File file) throws FileNotFoundException, IOException, ParseException {

		JSONObject jsonObj = (JSONObject) parser.parse(new InputStreamReader(new FileInputStream(file)));

		JSONArray areasJSONArray = (JSONArray) jsonObj.get("Areas");

		for (Object areaObj : areasJSONArray) {
			areaManager.registerArea(areaFromJSONObj((JSONObject) areaObj));
		}

	}

	public Area areaFromJSONObj(JSONObject areaJSONObj) {

		String areaName = (String) areaJSONObj.get("areaName");

		OfflinePlayer areaOwner = Bukkit.getOfflinePlayer(UUID.fromString((String) areaJSONObj.get("areaOwnerUUID")));

		Selection selection = new Selection();
		selection.setFirstPos(vectorFromJSONArray((JSONArray) areaJSONObj.get("firstPoint")));
		selection.setSecondPos(vectorFromJSONArray((JSONArray) areaJSONObj.get("secondPoint")));

		Location location = Main.locationFromString((String) areaJSONObj.get("locationString"));

		boolean overlappable = (boolean) areaJSONObj.get("overlappable");

		AreaFlags areaFlags = new AreaFlags();

		JSONArray areaFlagsArray = (JSONArray) areaJSONObj.get("areaFlags");

		JSONObject currentAreaFlagJSONObj = null;

		AreaFlagTypes areaFlagType;
		AreaFlag currentAreaFlag = null;

		for (Object areaFlagObj : areaFlagsArray) {
			currentAreaFlagJSONObj = (JSONObject) areaFlagObj;
			areaFlagType = AreaFlagTypes.valueOf((String) currentAreaFlagJSONObj.get("flagName"));

			if (areaFlagType.equals(AreaFlagTypes.GREET_ON_ENTER)
					|| areaFlagType.equals(AreaFlagTypes.GREET_ON_LEAVE)) {
				currentAreaFlag = new MessageAreaFlag(areaFlagType, (boolean) currentAreaFlagJSONObj.get("allow"),
						(String) currentAreaFlagJSONObj.get("message"));
			} else {
				currentAreaFlag = new AreaFlag(areaFlagType, (boolean) currentAreaFlagJSONObj.get("allow"));
			}
			
			boolean success = areaFlags.addAreaFlag(currentAreaFlag);
			if (!success) {
				Bukkit.getConsoleSender().sendMessage("§7§lDuplicated entry for §c§l"
						+ (String) currentAreaFlagJSONObj.get("flagName") + " §7§lFlag!");
			}
		}

		boolean enabled = true;
		if (areaJSONObj.get("enabled") != null) {
			enabled = (boolean) areaJSONObj.get("enabled");
		}

		return new Area(areaName, areaOwner, selection, location, overlappable, areaFlags, enabled);

	}

	public Vector vectorFromJSONArray(JSONArray pointArray) {
		return new Vector((long) pointArray.get(0), (long) pointArray.get(1), (long) pointArray.get(2));
	}

}