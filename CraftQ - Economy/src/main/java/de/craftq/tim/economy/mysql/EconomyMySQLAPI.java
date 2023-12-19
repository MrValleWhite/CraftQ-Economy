package de.craftq.tim.economy.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import de.craftq.tim.economy.Economy;

public class EconomyMySQLAPI {

	 public static boolean playerExists(String uuid) {
	        try {
	            ResultSet rs = Economy.mysql.query("SELECT * FROM Economy WHERE UUID= '" + uuid + "'");
	            if (rs.next())
	                return (rs.getString("UUID") != null);
	            return false;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    public static void createPlayer(String uuid, String name) {
	        if (!playerExists(uuid))
	        	Economy.mysql.update("INSERT INTO Economy(UUID, NAME, EURO) VALUES ('" + uuid + "', '" + name + "', '1500.50');");
	    }

	    public static Double getCoins(String uuid, String name) {
	        Double amount = 0.0;
	        if (playerExists(uuid)) {
	            try {
	                ResultSet rs = Economy.mysql.query("SELECT * FROM Economy WHERE UUID= '" + uuid + "'");
	                if (rs.next()) {
	                    amount = rs.getDouble("EURO");
	                }
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        } else {
	            createPlayer(uuid, name);
	            return getCoins(uuid, name);
	        }
	        return amount;
	    }

	    public static void setCoins(String uuid, String name, Double EURO) {
	        if (playerExists(uuid)) {
	            Economy.mysql.update("UPDATE Economy SET EURO= '" + EURO + "' WHERE UUID= '" + uuid + "';");
	        } else {
	            createPlayer(uuid, name);
	            setCoins(uuid, name, EURO);
	        }
	    }

	    public static void addCoins(String uuid, String name, Double EURO) {
	        if (playerExists(uuid)) {
	            setCoins(uuid, name, getCoins(uuid, name) + EURO);
	        } else {
	            createPlayer(uuid, name);
	            addCoins(uuid, name, EURO);
	        }
	    }

	    public static void removeCoins(String uuid, String name, Double EURO) {
	        if (playerExists(uuid)) {
	            setCoins(uuid, name, getCoins(uuid, name) - EURO);
	        } else {
	            createPlayer(uuid, name);
	            removeCoins(uuid, name, EURO);
	        }
	    }
	
}
