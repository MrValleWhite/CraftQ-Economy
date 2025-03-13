package de.craftq.tim.economy.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import de.craftq.tim.economy.CEconomy;

import javax.xml.transform.Result;

public class EconomyMySQLAPI {

	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");
	private static final double START_BALANCE = 750.00;

	public static String formatMoney(double amount) {
		return DECIMAL_FORMAT.format(amount);
	}

	public static boolean playerExists(String uuid) {
		String query = "SELECT 1 FROM Economy WHERE UUID = ? LIMIT 1";
		try (Connection conn = CEconomy.getMoneyConnection();
			 PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, uuid);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static synchronized void createPlayer(String uuid, String name) {
		if(!playerExists(uuid)) {
			String update = "INSERT INTO Economy(UUID, NAME, EURO) VALUES (?, ?, ?)";
			try(Connection conn = CEconomy.getMoneyConnection();
			PreparedStatement ps = conn.prepareStatement(update)) {
				ps.setString(1, uuid);
				ps.setString(2, name);
				ps.setDouble(3, START_BALANCE);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			updatePlayerName(uuid, name);
		}
	}

	public static void updatePlayerName(String uuid, String name) {
		String update = "UPDATE Economy SET NAME = ? WHERE UUID = ?";
		try (Connection conn = CEconomy.getMoneyConnection();
			 PreparedStatement ps = conn.prepareStatement(update)) {
			ps.setString(1, name);
			ps.setString(2, uuid);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static double getCoins(String uuid) {
		String query = "SELECT EURO FROM Economy WHERE UUID = ?";
		try (Connection conn = CEconomy.getMoneyConnection();
			 PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, uuid);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getDouble("EURO");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0.0;
	}

	public static void setCoins(String uuid, double euro) {
		String update = "UPDATE Economy SET EURO = ? WHERE UUID = ?";
		if (!playerExists(uuid)) {
			return;
		}
		try (Connection conn = CEconomy.getMoneyConnection();
			 PreparedStatement ps = conn.prepareStatement(update)) {
			ps.setDouble(1, euro);
			ps.setString(2, uuid);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addCoins(String uuid, double euro) {
		double newBalance = getCoins(uuid) + euro;
		setCoins(uuid, newBalance);
	}

	public static void removeCoins(String uuid, double euro) {
		double newBalance = getCoins(uuid) - euro;
		setCoins(uuid, Math.max(newBalance, 0)); // verhindert negative Kontostände
	}
}
