package de.craftq.tim.economy.cmd;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftq.tim.economy.Economy;
import de.craftq.tim.economy.mysql.EconomyMySQLAPI;
import de.craftq.tim.economy.utils.ConsoleManager;

public class PayCommand implements CommandExecutor {
	
	private double formatToTwoDecimals(double value) {
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		return Double.parseDouble(decimalFormat.format(value));
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Player player = (Player)sender;
		
		if(!(sender instanceof Player)) {
			ConsoleManager.sendMessage(Economy.noPlayer);
			return true;
		}
		
		if(args.length < 2) {
			player.sendMessage(Economy.pr + "§cVerwendung: /pay <Spieler> <Betrag>");
			return true;
		}
		
		Player target = Bukkit.getPlayer(args[0]);
		
		if(target == null) {
			player.sendMessage(Economy.pr + "§cDer angegebene Spieler ist nicht online");
			return true;
		}
		
		double amount;
		try {
			amount = Double.parseDouble(args[1]);
			amount = formatToTwoDecimals(amount);
		} catch (NumberFormatException e) {
			player.sendMessage(Economy.pr + "§cUngültiger Betrag. Verwendung: /pay <Spieler> <Betrag>");
			return true;
		}
		
		if(amount <= 0) {
			player.sendMessage("Der Betrag muss größer als 0 sein.");
			return true;
		}
		
		String senderUUID = player.getUniqueId().toString();
		String receiverUUID = target.getUniqueId().toString();
		
		double senderBalance = EconomyMySQLAPI.getCoins(senderUUID, player.getName());
		
		if(senderBalance < amount) {
			player.sendMessage(Economy.pr + "§cDu hast nicht genug Geld.");
			return true;
		}
		
		EconomyMySQLAPI.removeCoins(senderUUID, player.getName(), amount);
        EconomyMySQLAPI.addCoins(receiverUUID, target.getName(), amount);

        player.sendMessage(Economy.pr + "§7Du hast §e" + amount + "€ §7an §e" + target.getName() + " §7überwiesen.");
        target.sendMessage(Economy.pr + "§7Du hast §e" + amount + "€ §7von §e" + player.getName() + " §7erhalten.");
		
		return false;
	}

}
