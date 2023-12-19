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

public class AddMoneyCommand implements CommandExecutor {

	private double formatToTwoDecimals(double value) {
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		return Double.parseDouble(decimalFormat.format(value));
	}
	
public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Player player = (Player)sender;
		
		if(player.hasPermission("craftq.*")) {
		
			if(!(sender instanceof Player)) {
				ConsoleManager.sendMessage(Economy.noPlayer);
			}
		
			if(args.length < 2) {
				player.sendMessage(Economy.pr + "§cVerwendung: /addmoney <Spieler> <Betrag>");
				return true;
			}
			
			Player target = Bukkit.getPlayer(args[0]);
			
			if(target == null) {
				player.sendMessage(Economy.pr + "§cDer angegeben Spieler ist nicht online.");
				return true;
			}
			
			double amount;
			try {
				amount = Double.parseDouble(args[1]);
				amount = formatToTwoDecimals(amount);
			} catch (NumberFormatException e) {
				player.sendMessage(Economy.pr + "§cUngültiger Betrag. Verwendung: /addmoney <Spieler> <Betrag>");
				return true;
			}
			
			if(amount < 0) {
				player.sendMessage(Economy.pr + "§cDer Betrag muss größer oder gleich 0 sein.");
				return true;
			}
			
			String receiverUUID = target.getUniqueId().toString();
			
			EconomyMySQLAPI.addCoins(receiverUUID, target.getName(), amount);
			
			player.sendMessage(Economy.pr + "§7Du hast erfolgreich das Geld von §e" + target.getName() + " §7um §e" + amount + "§e€ §7aufgestockt.");
		
			return true;
			
		} else {
			player.sendMessage(Economy.noperm);
		}
		return false;
	}

}
