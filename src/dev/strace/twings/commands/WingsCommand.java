package dev.strace.twings.commands;

import java.util.ArrayList;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.strace.twings.Main;
import dev.strace.twings.utils.WingUtils;
import dev.strace.twings.utils.gui.WingGUI;

/**
 * 
 * @author Jason Holweg [STRACE] <b>TWINGS</b><br>
 *         Website: <a>https://strace.dev/</a><br>
 *         GitHub: <a>https://github.com/MrStrace</a><br>
 *         Created: May 30, 2021<br>
 *
 */

public class WingsCommand implements CommandExecutor {

	// Liste aller SubCommands von Setup.
	private ArrayList<SubCommands> subcommands = new ArrayList<SubCommands>();

	/**
	 * Initialisierung der SubCommands. Der ArrayListe werden die SubCommands
	 * hinzugefügt und beim Start des Plugins initialisiert.
	 */
	public WingsCommand() {
		subcommands.add(new Preview());
		subcommands.add(new Edit());
		subcommands.add(new UnEquip());
		subcommands.add(new Equip());
		subcommands.add(new List());
		subcommands.add(new Reload());
		subcommands.add(new Give());
		subcommands.add(new Create());
		subcommands.add(new Add());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			// if no arguments where given it will open the GUI.
			if (args.length == 0) {
				new WingGUI(WingUtils.winglist.size()).openGUI(p);
				return true;
			}

			handleMainCommand(args, p);

		}

		return false;
	}

	/**
	 * this function will run all commands and subcommands.
	 * 
	 * @param args subcommands
	 * @param p    player
	 */
	public void handleMainCommand(String[] args, Player p) {

		// for each subcommand it will check if the second argument equals the
		// subcommand name if so it will perform the command.
		for (int i = 0; i < subcommands.size(); i++) {
			if (args[0].equalsIgnoreCase(subcommands.get(i).getName())) {
				subcommands.get(i).perform(p, args);
				return;
			}
		}
		// if no command was found it will send the player this message.
		p.sendMessage(Main.getInstance().getMsg().getNosuchcommand());
		p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_BURP, 0.4F, 3);
	}

}
