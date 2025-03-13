package de.craftq.tim.economy;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import de.craftq.tim.economy.cmd.AddMoneyCommand;
import de.craftq.tim.economy.cmd.MoneyCommand;
import de.craftq.tim.economy.cmd.MoneyTopCommand;
import de.craftq.tim.economy.cmd.PayCommand;
import de.craftq.tim.economy.cmd.SetMoneyCommand;
import de.craftq.tim.economy.listener.JoinListener;
import de.craftq.tim.economy.utils.ConsoleManager;
import de.craftq.tim.economy.utils.VaultHandler;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class CEconomy extends JavaPlugin {

	private static CEconomy plugin;

	private static HikariDataSource moneyDataSource;

	File configFile = new File(getDataFolder(), "mysql.yml");

	public static String pr = translateHexColorCodes("&#8BC1DB§lCraftQ §8| ");
	public static String noperm = pr + "§cDazu hast Du keine Rechte.";

	public static String translateHexColorCodes(String message) {
		return message.replaceAll("&#([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])", "§x§$1§$2§$3§$4§$5§$6");
	}


	@Override
	public void onEnable() {

		if (getServer().getPluginManager().getPlugin("Vault") != null) {
			getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, new VaultHandler(this),
					this, ServicePriority.Normal);
		}

		loadMySQL();

		registerListener();
		registerCommands();

		plugin = this;

		ConsoleManager.sendMessage("============================================");
		ConsoleManager.sendMessage("        §6CraftQ-Economy §eVersion 1.0");
		ConsoleManager.sendMessage("============================================");
		ConsoleManager.sendMessage("§aDas Plugin wurde erfolgreich gestartet!");
		ConsoleManager.sendMessage("§bProgrammierung von Tim Nawratil!");
		ConsoleManager.sendMessage("============================================");

	}

	@Override
	public void onDisable() {
		ConsoleManager.sendMessage("============================================");
		ConsoleManager.sendMessage("        §6CraftQ-Economy §eVersion 1.0");
		ConsoleManager.sendMessage("============================================");
		ConsoleManager.sendMessage("§aDas Plugin wurde erfolgreich gestoppt!");
		ConsoleManager.sendMessage("§bProgrammierung von Tim Nawratil!");
		ConsoleManager.sendMessage("============================================");

		if(moneyDataSource != null) moneyDataSource.close();
	}

	private void registerListener() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new JoinListener(), this);
	}

	private void registerCommands() {
		getCommand("money").setExecutor(new MoneyCommand());
		getCommand("pay").setExecutor(new PayCommand());
		getCommand("setmoney").setExecutor(new SetMoneyCommand());
		getCommand("addmoney").setExecutor(new AddMoneyCommand());
		getCommand("moneytop").setExecutor(new MoneyTopCommand());
	}

	public static CEconomy getPlugin() {
		return plugin;
	}

	public void loadMySQL() {

		// Falls die Datei nicht existiert, erstelle sie mit Standardwerten
		if (!configFile.exists()) {
			saveDefaultMySQLConfig(configFile);
		}

		// Lade die Konfigurationsdatei
		FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

		// MySQL-Daten aus der Config laden
		String host = config.getString("mysql.host", "127.0.0.1");
		int port = config.getInt("mysql.port", 3306);
		String database = config.getString("mysql.database", "CraftQEconomyV2");
		String username = config.getString("mysql.username", "root");
		String password = config.getString("mysql.password", "password");
		int maxPoolSize = config.getInt("mysql.maxPoolSize", 10); // Standardwert 10 falls nicht gesetzt

		// HikariCP-Config setzen
		HikariConfig moneyConfig = new HikariConfig();
		moneyConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
		moneyConfig.setUsername(username);
		moneyConfig.setPassword(password);
		moneyConfig.setMaximumPoolSize(maxPoolSize);

		// Datenquelle starten
		moneyDataSource = new HikariDataSource(moneyConfig);
	}

	public static Connection getMoneyConnection() throws SQLException {
		return moneyDataSource.getConnection();
	}

	private void saveDefaultMySQLConfig(File configFile) {
		FileConfiguration config = new YamlConfiguration();

		// Standardwerte für MySQL-Verbindung
		config.set("mysql.host", "127.0.0.1");
		config.set("mysql.port", 3306);
		config.set("mysql.database", "CraftQEconomyV2");
		config.set("mysql.username", "root");
		config.set("mysql.password", "password");
		config.set("mysql.maxPoolSize", 10);

		try {
			config.save(configFile);
			System.out.println("[CraftQ-Economy] mysql.yml wurde erstellt!");
		} catch (IOException e) {
			System.out.println("[CraftQ-Economy] Fehler beim Erstellen der mysql.yml!");
			e.printStackTrace();
		}
	}

}
