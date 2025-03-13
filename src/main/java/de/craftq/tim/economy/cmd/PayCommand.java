package de.craftq.tim.economy.cmd;

import java.text.DecimalFormat;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftq.tim.economy.CEconomy;
import de.craftq.tim.economy.mysql.EconomyMySQLAPI;

public class PayCommand implements CommandExecutor {

	private double formatToTwoDecimals(double value) {
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		return Double.parseDouble(decimalFormat.format(value));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cNur Spieler können diesen Befehl benutzen.");
			return true;
		}

		Player player = (Player) sender;

		if (args.length < 2) {
			player.sendMessage(CEconomy.pr + "§cVerwendung: /pay <Spieler> <Betrag>");
			return true;
		}

		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		UUID targetUUID = target.getUniqueId();
		UUID senderUUID = player.getUniqueId();

		if (targetUUID == null) {
			player.sendMessage(CEconomy.pr + "§cDieser Spieler existiert nicht oder hat nie gespielt!");
			return true;
		}

		if (targetUUID.equals(senderUUID)) {
			player.sendMessage(CEconomy.pr + "§cDu kannst kein Geld an dich selbst senden!");
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
			player.sendMessage(CEconomy.pr + "§cDer Betrag muss größer als 0 sein.");
			return true;
		}

		double senderBalance = EconomyMySQLAPI.getCoins(senderUUID.toString());

		if (senderBalance < amount) {
			player.sendMessage(CEconomy.pr + "§cDu hast nicht genug Geld.");
			return true;
		}

		EconomyMySQLAPI.removeCoins(senderUUID.toString(), amount);
		EconomyMySQLAPI.addCoins(targetUUID.toString(), amount);

		player.sendMessage(CEconomy.pr + "§7Du hast §e" + EconomyMySQLAPI.formatMoney(amount) + "q §7an §e"
				+ target.getName() + " §7überwiesen.");

		if (target.isOnline() && target.getPlayer() != null) {
			target.getPlayer().sendMessage(CEconomy.pr + "§7Du hast §e" + EconomyMySQLAPI.formatMoney(amount)
					+ "q §7von §e" + player.getName() + " §7erhalten.");
		}

		return true;
	}
}