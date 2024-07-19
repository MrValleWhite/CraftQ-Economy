package de.craftq.tim.economy.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.craftq.tim.economy.mysql.EconomyMySQLAPI;

public class JoinListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		if (!EconomyMySQLAPI.playerExists(player.getName())) {
			EconomyMySQLAPI.createPlayer(player.getName());
		}
	}

}
