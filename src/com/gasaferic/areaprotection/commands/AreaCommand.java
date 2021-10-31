package com.gasaferic.areaprotection.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gasaferic.areaprotection.enums.AreaFlagTypes;
import com.gasaferic.areaprotection.main.Main;
import com.gasaferic.areaprotection.managers.AreaManager;
import com.gasaferic.areaprotection.managers.AreaPlayerManager;
import com.gasaferic.areaprotection.model.Area;
import com.gasaferic.areaprotection.model.AreaFlag;
import com.gasaferic.areaprotection.model.AreaFlags;
import com.gasaferic.areaprotection.model.AreaPlayer;
import com.gasaferic.areaprotection.model.MessageAreaFlag;

public class AreaCommand implements CommandExecutor {

	AreaPlayerManager areaPlayerManager = Main.getAreaPlayerManager();
	AreaManager areaManager = Main.getAreaManager();

	// areaPlayer.sendMessage("&c&lSelezione Area &f&l> &cWork in progress!");

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = (Player) sender;

		AreaPlayer areaPlayer = areaPlayerManager.getAreaPlayerByPlayer(player);

		if (player.hasPermission("areaprotection.area")) {
			if (areaPlayer.protectionModeEnabled()) {
				if (args.length == 0) {
					areaPlayer.sendMessage(
							"&c&lGestione Area &f&l> &cMancano dei parametri nell'esecuzione del comando, esempio: &7/area <create/modify/delete/flags/info> <nome> <flag> <valore>");
				} else if (args.length > 0) {
					if (args[0].equals("create") && posTaken(areaPlayer)) {
						createCommand(areaPlayer, args);
					} else if (args[0].equals("modify") && posTaken(areaPlayer)) {
						modifyCommand(areaPlayer, args);
					} else if (args[0].equals("delete")) {
						deleteCommand(areaPlayer, args);
					} else if (args[0].equals("flags")) {
						flagsCommand(areaPlayer, args);
					} else if (args[0].equals("info")) {
						infoCommand(areaPlayer, args);
					}
				}
			} else {
				areaPlayer.sendMessage("&c&lSelezione Area &f&l> &cNon sei in modalità protezione area!");
			}
		}

		return false;

	}

	public void createCommand(AreaPlayer areaPlayer, String[] args) {
		if (args.length == 1) {
			areaPlayer.sendMessage("&c&lGestione Area &f&l> &cDevi inserire un nome per l'area");
		} else {
			if (!areaManager.areaAlreadyExists(args[1])) {
				Area area = new Area(args[1], areaPlayer.getPlayer(), areaPlayer.getSelection(),
						areaPlayer.getPlayer().getLocation(), false, new AreaFlags(), true);

				Area overlappingArea;
				if ((overlappingArea = areaManager.getOverlappingArea(area)) == null) {
					areaManager.registerArea(area);
					areaPlayer.sendMessage(
							"&c&lGestione Area &f&l> &7Hai creato l'area &a" + args[1] + " &7con successo.");
					Main.updateAreaForOnlinePlayers(area, false);
				} else {
					areaPlayer.sendMessage("&c&lGestione Area &f&l> &7Quest'area è in conflitto con l'area &c"
							+ overlappingArea.getAreaName());
				}
			} else {
				areaPlayer.sendMessage("&c&lGestione Area &f&l> &7Esiste già un'area con il nome &c" + args[1]);
			}
		}
	}

	public void modifyCommand(AreaPlayer areaPlayer, String[] args) {
		if (args.length == 1) {
			areaPlayer.sendMessage("&c&lGestione Area &f&l> &cDevi inserire il nome dell'area che vuoi modificare");
		} else {
			if (areaManager.areaAlreadyExists(args[1])) {
				Area area = areaManager.getAreaFromName(args[1]);

				area.updateBounds(areaPlayer.getSelection());
				areaPlayer.sendMessage("&c&lGestione Area &f&l> &7Hai aggiornato i bordi per l'area &a" + args[1]
						+ " &7con successo.");
			} else {
				areaPlayer.sendMessage("&c&lGestione Area &f&l> &7Non esiste nessuna area con il nome &c" + args[1]);
			}
		}
	}

	public void deleteCommand(AreaPlayer areaPlayer, String[] args) {
		if (args.length == 1) {
			areaPlayer.sendMessage("&c&lGestione Area &f&l> &cDevi inserire il nome di un area");
		} else {
			if (areaManager.areaAlreadyExists(args[1])) {
				areaManager.unregisterArea(areaManager.getAreaFromName(args[1]));
				areaPlayer.sendMessage(
						"&c&lGestione Area &f&l> &7Hai eliminato l'area &c" + args[1] + " &7con successo.");
			} else {
				areaPlayer.sendMessage("&c&lGestione Area &f&l> &7Non esiste nessuna area con il nome &c" + args[1]);
			}
		}
	}

	public void flagsCommand(AreaPlayer areaPlayer, String[] args) {
		if (args.length == 1) {
			areaPlayer.sendMessage("&c&lGestione Area &f&l> &cDevi inserire il nome di un area");
		} else {
			if (areaManager.areaAlreadyExists(args[1])) {
				AreaFlags areaFlags = areaManager.getAreaFromName(args[1]).getAreaFlags();
				if (args.length == 2
						|| (args.length > 2 && !AreaFlagTypes.validFlag(args[2].toUpperCase().replace("-", "_")))) {
					areaPlayer.sendMessage("&c&lGestione Area &f&l> &cDevi inserire una regola valida, lista regole: &7"
							+ AreaFlagTypes.getAvailableFlags());
				} else {
					AreaFlagTypes areaFlagType = AreaFlagTypes.valueOf(args[2].toUpperCase().replace("-", "_"));
					if (args.length == 3) {
						areaPlayer.sendMessage(
								"&c&lGestione Area &f&l> &7Devi inserire lo stato della regola (true/false)");
					} else {
						boolean allow = Boolean.valueOf(args[3]);

						AreaFlag areaFlag = areaFlags.getAreaFlagByExactFlagType(areaFlagType);

						if (!(areaFlagType.equals(AreaFlagTypes.GREET_ON_ENTER)
								|| areaFlagType.equals(AreaFlagTypes.GREET_ON_LEAVE))) {
							if (areaFlag != null) {
								areaFlag.setAllowed(allow);
								areaPlayer.sendMessage(
										"&c&lGestione Area &f&l> &7Aggiornata la regola " + args[2] + " a " + allow);
							} else {
								areaFlags.addAreaFlag(new AreaFlag(areaFlagType, allow));
								areaPlayer.sendMessage(
										"&c&lGestione Area &f&l> &7Aggiunta la regola " + args[2] + " a " + allow);
							}
						} else {
							if (areaFlag != null) {
								areaFlag.setAllowed(allow);
								if (args.length > 4) {
									StringBuilder stringBuilder = new StringBuilder();

									for (int i = 4; i < args.length; i++) {
										stringBuilder.append(args[i] + (i < args.length - 1 ? " " : ""));
									}

									((MessageAreaFlag) areaFlag).setMessage(stringBuilder.toString());
									areaPlayer.sendMessage(
											"&c&lGestione Area &f&l> &7Aggiornata la regola " + args[2] + " a " + allow
													+ " , con il messaggio &r'" + stringBuilder.toString() + "&r'");
								} else {
									((MessageAreaFlag) areaFlag).setMessage(null);
									areaPlayer.sendMessage(
											"&c&lGestione Area &f&l> &7Aggiornata la regola " + args[2] + " a " + allow
													+ " , con il messaggio " + (args.length > 4 ? args[4] : "default"));
								}
							} else {
								if (args.length > 4) {
									StringBuilder stringBuilder = new StringBuilder();

									for (int i = 4; i < args.length; i++) {
										stringBuilder.append(args[i] + (i < args.length - 1 ? " " : ""));
									}

									areaFlags.addAreaFlag(
											new MessageAreaFlag(areaFlagType, allow, stringBuilder.toString()));
									areaPlayer.sendMessage(
											"&c&lGestione Area &f&l> &7Aggiunta la regola " + args[2] + " a " + allow
													+ " , con il messaggio &r'" + stringBuilder.toString() + "&r'");

								} else {
									areaFlags.addAreaFlag(new MessageAreaFlag(areaFlagType, allow, null));
									areaPlayer.sendMessage(
											"&c&lGestione Area &f&l> &7Aggiornata la regola " + args[2] + " a " + allow
													+ " , con il messaggio " + (args.length > 4 ? args[4] : "default"));
								}

							}

						}
					}
				}
			} else {
				areaPlayer.sendMessage("&c&lGestione Area &f&l> &7Non esiste nessuna area con il nome &c" + args[1]);
			}
		}
	}

	public void infoCommand(AreaPlayer areaPlayer, String[] args) {
		if (args.length == 1) {
			if (areaPlayer.getCurrentArea() != null) {
				areaPlayer.sendMessage(getInfoMessage(areaPlayer.getCurrentArea()));
			} else {
				areaPlayer.sendMessage("&c&lGestione Area &f&l> &cNon ti trovi all'interno di un'area!");
			}
		} else {
			areaPlayer.sendMessage(getInfoMessage(areaManager.getAreaFromName(args[1])));
		}
	}

	public String getInfoMessage(Area area) {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("             &f&l(?) &c&lInformazioni Area &f&l(?)&r\n\n\n");
		stringBuilder.append("  &cNome Area &f&l- &7" + area.getAreaName() + "\n");
		stringBuilder.append("  &cNome Proprietario &f&l- &7" + Bukkit.getPlayer(area.getAreaOwner()).getName() + "\n");
		stringBuilder.append("  &cUUID Proprietario &f&l- &7" + area.getAreaOwner() + "\n");
		stringBuilder.append("  &cPunti Area &f&l- &cFirst Pos &7(" + area.getFirstPos().toString()
				+ ") &cSecond Pos &7 (" + area.getSecondPos().toString() + ")\n");
		stringBuilder.append("  &cMondo Area &f&l- &7" + area.getAreaLocation().getWorld().getName() + "\n");

		stringBuilder.append("  &cRegole Area &f&l- &7");

		ArrayList<AreaFlag> areaFlags = area.getAreaFlags().getAreaFlags();
		for (AreaFlag areaFlag : areaFlags) {
			stringBuilder.append(areaFlag.getAreaFlagType().toString().toLowerCase() + "("
					+ (areaFlag.isAllowed() ? "&a" : "&4") + areaFlag.isAllowed() + "&7)"
					+ (areaFlags.indexOf(areaFlag) != areaFlags.size() - 1 ? "," : ""));
		}

		stringBuilder.append("\n");

		stringBuilder.append("             &f&l(✔) &c&lFine Informazioni Area &f&l(✔)");

		return stringBuilder.toString();
	}

	public boolean posTaken(AreaPlayer areaPlayer) {

		if (areaPlayer.getSelection().getFirstPos() == null || areaPlayer.getSelection().getSecondPos() == null) {
			areaPlayer.sendMessage("&c&lSelezione Area &f&l> &cDevi selezionare due punti opposti!");
			return false;
		} else {
			return true;
		}

	}

}