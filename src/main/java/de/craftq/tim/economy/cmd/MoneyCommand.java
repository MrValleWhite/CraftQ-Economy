package de.craftq.tim.economy.cmd;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftq.tim.economy.CEconomy;
import de.craftq.tim.economy.mysql.EconomyMySQLAPI;

public class MoneyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(CEconomy.pr + "§cDieser Befehl kann nur von Spielern ausgeführt werden.");
			return true;
		}

		Player player = (Player) sender;

		if (command.getName().equalsIgnoreCase("money")) {
			if (args.length == 0) {
				// Zeige das Geld des ausführenden Spielers an
				player.sendMessage(CEconomy.pr + "§7Du hast §e"
						+ EconomyMySQLAPI.formatMoney(EconomyMySQLAPI.getCoins(player.getName())) + "§eq");
			} else {
				// Zeige das Geld des angegebenen Spielers an
				String targetName = args[0];
				OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetName);

				if (targetPlayer == null || !targetPlayer.hasPlayedBefore()) {
					player.sendMessage(CEconomy.pr + "§cSpieler " + targetName + " wurde nicht gefunden.");
				} else {
					double targetBalance = EconomyMySQLAPI.getCoins(targetPlayer.getName());
					player.sendMessage(CEconomy.pr + "§7" + targetName + " hat §e"
							+ EconomyMySQLAPI.formatMoney(targetBalance) + "§eq");
				}
			}
			return true;
		}

		return false;
	}
}
