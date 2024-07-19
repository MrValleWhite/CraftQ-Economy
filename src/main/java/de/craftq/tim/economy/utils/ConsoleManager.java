package de.craftq.tim.economy.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class ConsoleManager {

	private static ConsoleCommandSender console = Bukkit.getConsoleSender();
	
	public static ConsoleCommandSender getConsole() {
		return console;
	}
	
	public static void sendMessage(String message) {
		console.sendMessage(message);
	}
	
}
