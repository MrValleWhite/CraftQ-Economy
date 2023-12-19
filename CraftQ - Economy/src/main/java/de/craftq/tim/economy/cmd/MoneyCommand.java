package de.craftq.tim.economy.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftq.tim.economy.Economy;
import de.craftq.tim.economy.mysql.EconomyMySQLAPI;

public class MoneyCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Player player = (Player)sender;
		if(command.getName().equalsIgnoreCase("money")) {
			player.sendMessage(Economy.pr + "§7Du hast §e" + EconomyMySQLAPI.getCoins(player.getUniqueId().toString(), player.getName()) + "§e€");
		}
		
		
		return false;
	}

	
	
}
