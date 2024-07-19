package de.craftq.tim.economy.cmd;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftq.tim.economy.CEconomy;
import de.craftq.tim.economy.mysql.EconomyMySQLAPI;
import de.craftq.tim.economy.utils.ConsoleManager;

public class PayCommand implements CommandExecutor {

	private double formatToTwoDecimals(double value) {
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		return Double.parseDouble(decimalFormat.format(value));
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		Player player = (Player) sender;

		if (!(sender instanceof Player)) {
			ConsoleManager.sendMessage(CEconomy.noPlayer);
			return true;
		}

		if (args.length < 2) {
			player.sendMessage(CEconomy.pr + "§cVerwendung: /pay <Spieler> <Betrag>");
			return true;
		}

		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

		if (!target.hasPlayedBefore()) {
			player.sendMessage(CEconomy.pr + "§cDieser Spieler war noch nie auf CraftQ!");
			return true;
		}

		double amount;
		try {
			amount = Double.parseDouble(args[1]);
			amount = formatToTwoDecimals(amount);
		} catch (NumberFormatException e) {
			player.sendMessage(CEconomy.pr + "§cUngültiger Betrag. Verwendung: /pay <Spieler> <Betrag>");
			return true;
		}

		if (amount <= 0) {
			player.sendMessage("Der Betrag muss größer als 0 sein.");
			return true;
		}

		double senderBalance = EconomyMySQLAPI.getCoins(player.getName());

		if (senderBalance < amount) {
			player.sendMessage(CEconomy.pr + "§cDu hast nicht genug Geld.");
			return true;
		}

		EconomyMySQLAPI.removeCoins(player.getName(), amount);
		EconomyMySQLAPI.addCoins(target.getName(), amount);

		player.sendMessage(CEconomy.pr + "§7Du hast §e" + EconomyMySQLAPI.formatMoney(amount) + "q §7an §e"
				+ target.getName() + " §7überwiesen.");
		if (target.isOnline()) {
			target.getPlayer().sendMessage(CEconomy.pr + "§7Du hast §e" + EconomyMySQLAPI.formatMoney(amount)
					+ "q §7von §e" + player.getName() + " §7erhalten.");
		}

		return false;
	}

}
