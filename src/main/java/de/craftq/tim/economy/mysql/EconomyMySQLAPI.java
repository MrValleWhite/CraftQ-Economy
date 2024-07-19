package de.craftq.tim.economy.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import de.craftq.tim.economy.CEconomy;

public class EconomyMySQLAPI {

	public static String formatMoney(double amount) {
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
		return decimalFormat.format(amount);
	}

	public static boolean playerExists(String name) {
		String query = "SELECT * FROM Economy WHERE NAME = ?";
		try (PreparedStatement ps = CEconomy.mysql.getConnection().prepareStatement(query)) {
			ps.setString(1, name);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static synchronized void createPlayer(String name) {
		if (!playerExists(name)) {
			String update = "INSERT INTO Economy(NAME, EURO) VALUES (?, ?)";
			try (PreparedStatement ps = CEconomy.mysql.getConnection().prepareStatement(update)) {
				ps.setString(1, name);
				ps.setDouble(2, 750.00);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static Double getCoins(String name) {
		Double amount = 0.0;
		if (playerExists(name)) {
			try {
				ResultSet rs = CEconomy.mysql.query("SELECT * FROM Economy WHERE NAME= '" + name + "'");
				if (rs.next()) {
					amount = rs.getDouble("EURO");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			createPlayer(name);
			return getCoins(name);
		}
		return amount;
	}

	public static void setCoins(String name, Double EURO) {
		if (playerExists(name)) {
			CEconomy.mysql.update("UPDATE Economy SET EURO= '" + EURO + "' WHERE NAME= '" + name + "';");
		} else {
			createPlayer(name);
			setCoins(name, EURO);
		}
	}

	public static void addCoins(String name, Double EURO) {
		if (playerExists(name)) {
			setCoins(name, getCoins(name) + EURO);
		} else {
			createPlayer(name);
			addCoins(name, EURO);
		}
	}

	public static void removeCoins(String name, Double EURO) {
		if (playerExists(name)) {
			setCoins(name, getCoins(name) - EURO);
		} else {
			createPlayer(name);
			removeCoins(name, EURO);
		}
	}
}
