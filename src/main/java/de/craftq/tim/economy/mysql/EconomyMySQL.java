package de.craftq.tim.economy.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;

public class EconomyMySQL {

	private String HOST = "";

	private String DATABASE = "";

	private String USER = "";

	private String PASSWORD = "";

	public static Connection con;

	public EconomyMySQL(String host, String database, String user, String password) {
		this.HOST = host;
		this.DATABASE = database;
		this.USER = user;
		this.PASSWORD = password;
		connect();
	}

	public EconomyMySQL() {
		// TODO Auto-generated constructor stub
	}

	public void connect() {
		try {
			if (con == null) {
				con = DriverManager.getConnection(
						"jdbc:mysql://" + this.HOST + ":3306/" + this.DATABASE + "?autoReconnect=true", this.USER,
						this.PASSWORD);
				Bukkit.getConsoleSender().sendMessage("§8| §bCEconomy §8» §7MySQL erfolgreich §everbunden!");
			}
		} catch (SQLException e) {
			Bukkit.getConsoleSender().sendMessage(
					"§8| §bCEconomy §8» " + "§7Bei der Verbindung ist ein Fehler aufgetreten §8 | §cMYSQL");
		}

	}

	public Connection getConnection() {
		return con;
	}

	public void close() {
		try {
			if (con != null) {
				con.close();
				Bukkit.getConsoleSender().sendMessage("§8| §bCEconomy §8» §7MySQL erfolgreich §cbeendet!");
			}
		} catch (SQLException e) {
			Bukkit.getConsoleSender()
					.sendMessage("§8| §bCEconomy §8» §7Bei der beendung ist ein Fehler aufgetreten" + " §8| §cMYSQL");
		}
	}

	public void update(String qry) {
		try {
			Statement st = con.createStatement();
			st.executeUpdate(qry);
			st.close();
		} catch (SQLException e) {
			connect();
			System.err.println(e);
		}
	}

	public ResultSet query(String qry) {
		ResultSet rs = null;
		try {
			Statement st = con.createStatement();
			rs = st.executeQuery(qry);
		} catch (SQLException e) {
			connect();
			System.err.println(e);
		}
		return rs;
	}

}
