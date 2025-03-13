package de.craftq.tim.economy.cmd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import de.craftq.tim.economy.CEconomy;
import de.craftq.tim.economy.mysql.EconomyMySQLAPI;

public class MoneyTopCommand implements CommandExecutor {

	private static final int PLAYERS_PER_PAGE = 10;

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
							 @NotNull String[] args) {
		if (command.getName().equalsIgnoreCase("moneytop")) {
			int page = 1;
			if (args.length > 0) {
				try {
					page = Integer.parseInt(args[0]);
					if (page < 1) {
						sender.sendMessage(CEconomy.pr + "§cUngültige Seitennummer.");
						return true;
					}
				} catch (NumberFormatException e) {
					sender.sendMessage(CEconomy.pr + "§cBitte gib eine gültige Zahl als Seitennummer an.");
					return true;
				}
			}

			List<String> topPlayers = getTopPlayers(page);

			if (topPlayers.isEmpty()) {
				sender.sendMessage(CEconomy.pr + "§cMehr Seiten gibt es nicht.");
			} else {
				sender.sendMessage(CEconomy.pr + "§7Die reichsten Spieler (Seite " + page + "):");
				for (String playerInfo : topPlayers) {
					sender.sendMessage(playerInfo);
				}
			}
		}
		return true;
	}

	private List<String> getTopPlayers(int page) {
		List<String> topPlayers = new ArrayList<>();
		int offset = (page - 1) * PLAYERS_PER_PAGE;
		String query = "SELECT NAME, EURO FROM Economy ORDER BY EURO DESC LIMIT ? OFFSET ?";

		try (Connection conn = CEconomy.getMoneyConnection();
			 PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, PLAYERS_PER_PAGE);
			ps.setInt(2, offset);
			try (ResultSet rs = ps.executeQuery()) {
				int rank = offset + 1;
				while (rs.next()) {
					String name = rs.getString("NAME");
					double euro = rs.getDouble("EURO");
					topPlayers.add("§8> " + rank + " §8| §7" + name + " §8| " + "§e" + EconomyMySQLAPI.formatMoney(euro) + "q");
					rank++;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return topPlayers;
	}
}