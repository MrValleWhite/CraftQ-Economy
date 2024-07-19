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

public class AddMoneyCommand implements CommandExecutor {

	private double formatToTwoDecimals(double value) {
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		return Double.parseDouble(decimalFormat.format(value));
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		Player player = (Player) sender;

		if (player.hasPermission("craftq.*")) {

			if (args.length < 2) {
				player.sendMessage(CEconomy.pr + "§cVerwendung: /addmoney <Spieler> <Betrag>");
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
				player.sendMessage(CEconomy.pr + "§cUngültiger Betrag. Verwendung: /addmoney <Spieler> <Betrag>");
				return true;
			}

			if (amount < 0) {
				player.sendMessage(CEconomy.pr + "§cDer Betrag muss größer oder gleich 0 sein.");
				return true;
			}

			EconomyMySQLAPI.addCoins(target.getName(), amount);

			player.sendMessage(CEconomy.pr + "§7Du hast erfolgreich das Geld von §e" + target.getName() + " §7um §e"
					+ EconomyMySQLAPI.formatMoney(amount) + "§eq §7aufgestockt.");

			return true;

		} else {
			player.sendMessage(CEconomy.noperm);
		}
		return false;
	}

}