package com.gasaferic.areaprotection.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gasaferic.areaprotection.main.Main;

import net.md_5.bungee.api.ChatColor;

public class AreaPlayer {

	// The model package contains all classes that contain data, but do nothing (or
	// barely anything) on their own. They don't interact with other classes that do
	// things
	// For instance, The SurvivaorManager does all kind of things and interacts with
	// stuff, but the Survivor class only interacts with itself, not with anything
	// else

	private Player player;
	private UUID uuid;
	private Area currentArea;
	private boolean protectionMode;
	private Selection selection;
	private ItemStack protectionItem;

	private boolean sendingMessages;
	private ArrayList<String> pendingMessages;

	private ItemStack[] inventoryContents;

	public AreaPlayer(Player player) {

		this.player = player.getPlayer();

		this.uuid = player.getUniqueId();

		this.currentArea = null;
		this.protectionMode = false;

		this.selection = new Selection();

		this.protectionItem = createProtectionItem();

		this.sendingMessages = false;
		this.pendingMessages = new ArrayList<String>();
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String getName() {
		return this.player.getName();
	}

	public UUID getUniqueId() {
		return this.uuid;
	}

	public Area getCurrentArea() {
		return currentArea;
	}

	public void setCurrentArea(Area currentArea) {
		this.currentArea = currentArea;
	}

	public boolean protectionModeEnabled() {
		return protectionMode;
	}

	public void toggleProtectionMode() {
		protectionMode = !protectionMode;
		setupModeInventory(protectionMode);
	}

	public Selection getSelection() {
		return selection;
	}

	public void setSelection(Selection selection) {
		this.selection = selection;
	}

	public ItemStack getProtectionItem() {
		return protectionItem;
	}

	public void setupModeInventory(boolean enable) {
		Inventory playerInventory = getPlayer().getInventory();

		if (enable) {
			inventoryContents = playerInventory.getContents();
			playerInventory.clear();
			playerInventory.addItem(getProtectionItem());
		} else {
			this.selection.clearSelection();
			playerInventory.clear();
			playerInventory.setContents(inventoryContents);
		}

		sendMessage("§6Modalità Protezione Area " + (enable ? "abilitata" : "disabilitata"));
	}

	private ItemStack createProtectionItem() {
		List<String> protectionItemLore = Arrays.asList("§7Tasto-Sinistro per selezionare il primo punto",
				"§7Tasto-Destro per selezionare il secondo punto");

		return getSetupItem(Material.GOLD_SPADE, (byte) 0, "§4§lOggetto Protezione", protectionItemLore);
	}

	public ItemStack getSetupItem(Material type, byte data, String name, List<String> lore) {
		ItemStack setupItem = new ItemStack(type, 1, data);
		ItemMeta itemMeta = setupItem.getItemMeta();
		itemMeta.setLore(lore);
		itemMeta.setDisplayName(name);
		setupItem.setItemMeta(itemMeta);
		return setupItem;
	}

	public void sendMessage(String message) {
		if (!sendingMessages) {
			sendMessages();
		}

		this.pendingMessages.add(message);
	}

	public void sendMessages() {
		sendingMessages = true;

		Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
			@Override
			public void run() {
				if (pendingMessages.size() > 0) {
					getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', pendingMessages.get(0)) + "§r");
					pendingMessages.remove(0);
				}
			}
		}, 5l, 0l);
	}

}