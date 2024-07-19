package de.craftq.tim.economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import de.craftq.tim.economy.cmd.AddMoneyCommand;
import de.craftq.tim.economy.cmd.MoneyCommand;
import de.craftq.tim.economy.cmd.MoneyTopCommand;
import de.craftq.tim.economy.cmd.PayCommand;
import de.craftq.tim.economy.cmd.SetMoneyCommand;
import de.craftq.tim.economy.listener.JoinListener;
import de.craftq.tim.economy.mysql.EconomyMySQL;
import de.craftq.tim.economy.utils.ConsoleManager;
import de.craftq.tim.economy.utils.VaultHandler;

public class CEconomy extends JavaPlugin {

	private static CEconomy plugin;

	public static EconomyMySQL mysql;

	public static String pr = "§bCRAFTQ §8| §e";
	public static String noperm = pr + "§cDazu hast Du keine Rechte!";
	public static String noPlayer = pr + "§cDieser Befehl ist nur für Spieler";

	@Override
	public void onEnable() {

		if (getServer().getPluginManager().getPlugin("Vault") != null) {
			getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, new VaultHandler(this),
					this, ServicePriority.Normal);
		}

		registerListener();
		registerCommands();

		saveConfig();

		mysql = new EconomyMySQL();

		if (getConfig().getString("Host") == null)
			getConfig().set("Host", "127.0.0.1");
		if (getConfig().getString("Database") == null)
			getConfig().set("Database", "EconomyAPI");
		if (getConfig().getString("Nutzer") == null)
			getConfig().set("Nutzer", "Benutzername");
		if (getConfig().getString("Passwort") == null)
			getConfig().set("Passwort", "Passwort");

		saveConfig();

		plugin = this;

		connectMySQL();

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

		mysql.close();
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

	public void connectMySQL() {
		String host = getConfig().getString("Host");
		String database = getConfig().getString("Database");
		String user = getConfig().getString("Nutzer");
		String password = getConfig().getString("Passwort");
		if (host.equalsIgnoreCase("127.0.0.1") && database.equalsIgnoreCase("CoinAPI")
				&& user.equalsIgnoreCase("Benutzername") && password.equalsIgnoreCase("Passwort")) {
			Bukkit.getConsoleSender().sendMessage("§8| §bEconomy §8» §cDie MySQL Daten sind ungültig");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		mysql = new EconomyMySQL(host, database, user, password);
		mysql.connect();
		mysql.update("CREATE TABLE IF NOT EXISTS Economy(NAME varchar(64), EURO double)");
	}

}
