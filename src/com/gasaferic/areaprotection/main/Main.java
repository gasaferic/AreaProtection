package com.gasaferic.areaprotection.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.parser.ParseException;

import com.gasaferic.areaprotection.commands.AreaCommand;
import com.gasaferic.areaprotection.commands.ProtectionMode;
import com.gasaferic.areaprotection.exceptions.AreaPlayerAlreadyExistsException;
import com.gasaferic.areaprotection.listeners.CommandListener;
import com.gasaferic.areaprotection.listeners.AreaPlayerRegistering;
import com.gasaferic.areaprotection.listeners.PlayerAreaMovementListener;
import com.gasaferic.areaprotection.listeners.PlayerFlagListener;
import com.gasaferic.areaprotection.listeners.PlayerInteractionListener;
import com.gasaferic.areaprotection.listeners.PlayerMovementListener;
import com.gasaferic.areaprotection.managers.AreaLoader;
import com.gasaferic.areaprotection.managers.AreaManager;
import com.gasaferic.areaprotection.managers.AreaPlayerManager;
import com.gasaferic.areaprotection.managers.AreaSaver;
import com.gasaferic.areaprotection.model.Area;
import com.gasaferic.areaprotection.model.AreaPlayer;

public class Main extends JavaPlugin {

	private static Main instance;

	public static ArrayList<UUID> playersUUID = new ArrayList<UUID>();

	private static AreaPlayerManager areaPlayerManager;
	private static AreaManager areaManager;

	private File areasJSON;

	public void onEnable() {

		instance = this;

		PluginDescriptionFile pdfFile = getDescription();

		areasJSON = new File(this.getDataFolder(), "areas.json");

		areaPlayerManager = new AreaPlayerManager();
		areaManager = new AreaManager();

		registerEvents();
		registerCommands();

		registerAreas();

		registerOnlinePlayers();

		Bukkit.getConsoleSender().sendMessage(
				"§8[§9§l" + pdfFile.getName() + "§8]§r" + " §a§lhas been enabled on version " + pdfFile.getVersion());

	}

	public void onDisable() {

		PluginDescriptionFile pdfFile = getDescription();

		areaPlayerManager.disableSetupMode();

		areaPlayerManager.unregisterAreaPlayers();

		saveAreas();

		Bukkit.getConsoleSender().sendMessage("§8[§9§l" + pdfFile.getName() + "§8]§r" + " §c§lhas been disabled");

	}

	public void registerEvents() {

		registerEvent(new AreaPlayerRegistering());

		registerEvent(new ProtectionMode());
		registerEvent(new PlayerMovementListener());
		registerEvent(new PlayerInteractionListener());
		registerEvent(new PlayerAreaMovementListener());
		registerEvent(new PlayerFlagListener());
		registerEvent(new CommandListener());

	}

	public static void registerEvent(Listener listener) {

		PluginManager pm = getInstance().getServer().getPluginManager();

		pm.registerEvents(listener, getInstance());

	}

	public void registerCommands() {

		getCommand("protectionmode").setExecutor(new ProtectionMode());
		getCommand("area").setExecutor(new AreaCommand());

	}

	public static AreaPlayerManager getAreaPlayerManager() {
		return areaPlayerManager;
	}

	public static AreaManager getAreaManager() {
		return areaManager;
	}

	public void registerAreas() {
		AreaLoader areaLoader = new AreaLoader();

		try {
			if (areasJSON.exists()) {
				areaLoader.loadFromFile(areasJSON);
			} else {
				areasJSON.createNewFile();
				areaLoader.loadFromFile(areasJSON);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			Bukkit.getConsoleSender().sendMessage("§7§lIl file delle aree non è formattato correttamente!");
		}
	}

	public void saveAreas() {
		AreaSaver areaLoader = new AreaSaver();

		try {
			areaLoader.saveToFile(areaManager.getAreas(), areasJSON);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void updateAreaForOnlinePlayers(Area area, boolean removed) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			areaPlayerManager.getAreaPlayerByPlayer(player)
					.updateCurrentArea(areaManager.getAreaByLocation(player.getLocation()));
		}
	}

	public void registerOnlinePlayers() {
		if (Bukkit.getOnlinePlayers().size() > 0) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				AreaPlayer areaPlayer;
				try {
					areaPlayer = new AreaPlayer(player);
					areaPlayerManager.registerAreaPlayer(areaPlayer);

					Area area = areaManager.getAreaByLocation(player.getLocation());
					areaPlayer.updateCurrentArea(area);
				} catch (AreaPlayerAlreadyExistsException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Main getInstance() {
		return instance;
	}

	public String getPrefixString(String string) {
		return ChatColor.translateAlternateColorCodes('&', getConfig().getString(string));
	}

	public static Location locationFromString(String loc) {
		loc = loc.substring(loc.indexOf("{") + 1);
		loc = loc.substring(loc.indexOf("{") + 1);
		String worldName = loc.substring(loc.indexOf("=") + 1, loc.indexOf("}"));
		loc = loc.substring(loc.indexOf(",") + 1);
		String xCoord = loc.substring(loc.indexOf("=") + 1, loc.indexOf(","));
		loc = loc.substring(loc.indexOf(",") + 1);
		String yCoord = loc.substring(loc.indexOf("=") + 1, loc.indexOf(","));
		loc = loc.substring(loc.indexOf(",") + 1);
		String zCoord = loc.substring(loc.indexOf("=") + 1, loc.indexOf(","));
		loc = loc.substring(loc.indexOf(",") + 1);
		String pitch = loc.substring(loc.indexOf("=") + 1, loc.indexOf(","));
		loc = loc.substring(loc.indexOf(",") + 1);
		String yaw = loc.substring(loc.indexOf("=") + 1, loc.indexOf("}"));
		return new Location(Bukkit.getWorld(worldName), Double.parseDouble(xCoord), Double.parseDouble(yCoord),
				Double.parseDouble(zCoord), Float.parseFloat(yaw), Float.parseFloat(pitch));
	}

}