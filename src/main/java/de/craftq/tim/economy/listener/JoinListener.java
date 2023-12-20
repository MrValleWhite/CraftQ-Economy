package de.craftq.tim.economy.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.craftq.tim.economy.mysql.EconomyMySQLAPI;
import de.craftq.tim.economy.utils.ConsoleManager;

public class JoinListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		EconomyMySQLAPI.createPlayer(player.getUniqueId().toString(), player.getName());
		ConsoleManager.sendMessage("EIN SPIELER WURDE ERSTELLT!");
	}
	
}
