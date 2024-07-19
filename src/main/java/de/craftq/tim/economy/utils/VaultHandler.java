package de.craftq.tim.economy.utils;

import java.util.List;

import org.bukkit.OfflinePlayer;

import de.craftq.tim.economy.CEconomy;
import de.craftq.tim.economy.mysql.EconomyMySQLAPI;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class VaultHandler implements Economy {

	private final CEconomy eco;

	public VaultHandler(CEconomy plugin) {
		this.eco = plugin;
	}

	@Override
	public boolean isEnabled() {
		return this.eco.isEnabled();
	}

	@Override
	public String getName() {
		return this.eco.getName();
	}

	@Override
	public boolean hasBankSupport() {
		return false;
	}

	@Override
	public int fractionalDigits() {
		return 0;
	}

	@Override
	public String format(double amount) {
		return EconomyMySQLAPI.formatMoney(amount);
	}

	@Override
	public String currencyNamePlural() {
		return currencyNameSingular();
	}

	@Override
	public String currencyNameSingular() {
		return "€";
	}

	@Override
	public boolean hasAccount(String playerName) {
		if (EconomyMySQLAPI.playerExists(playerName)) {
			return true;
		} else {
			EconomyMySQLAPI.createPlayer(playerName);
			return true;
		}
	}

	@Override
	public boolean hasAccount(OfflinePlayer player) {
		return EconomyMySQLAPI.playerExists(player.getName());
	}

	@Override
	public boolean hasAccount(String playerName, String worldName) {
		return false;
	}

	@Override
	public boolean hasAccount(OfflinePlayer player, String worldName) {
		return EconomyMySQLAPI.playerExists(player.getName());
	}

	@Override
	public double getBalance(String playerName) {
		return EconomyMySQLAPI.getCoins(getName());
	}

	@Override
	public double getBalance(OfflinePlayer player) {
		return EconomyMySQLAPI.getCoins(player.getName());
	}

	@Override
	public double getBalance(String playerName, String world) {
		return EconomyMySQLAPI.getCoins(playerName);
	}

	@Override
	public double getBalance(OfflinePlayer player, String world) {
		return EconomyMySQLAPI.getCoins(player.getName());
	}

	@Override
	public boolean has(String playerName, double amount) {
		return EconomyMySQLAPI.getCoins(playerName) != null && EconomyMySQLAPI.getCoins(playerName) >= amount;
	}

	@Override
	public boolean has(OfflinePlayer player, double amount) {
		return EconomyMySQLAPI.getCoins(player.getName()) != null
				&& EconomyMySQLAPI.getCoins(player.getName()) >= amount;
	}

	@Override
	public boolean has(String playerName, String worldName, double amount) {
		return EconomyMySQLAPI.getCoins(playerName) != null && EconomyMySQLAPI.getCoins(playerName) >= amount;
	}

	@Override
	public boolean has(OfflinePlayer player, String worldName, double amount) {
		return EconomyMySQLAPI.getCoins(player.getName()) != null
				&& EconomyMySQLAPI.getCoins(player.getName()) >= amount;
	}

	@Override
	public EconomyResponse withdrawPlayer(String playerName, double amount) {
		EconomyMySQLAPI.removeCoins(playerName, amount);
		return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, "Erfolgreich");
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
		EconomyMySQLAPI.removeCoins(player.getName(), amount);
		return new EconomyResponse(amount, getBalance(player.getName()), EconomyResponse.ResponseType.SUCCESS,
				"Erfolgreich");
	}

	@Override
	public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
		EconomyMySQLAPI.removeCoins(playerName, amount);
		return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, "Erfolgreich");
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
		EconomyMySQLAPI.removeCoins(player.getName(), amount);
		return new EconomyResponse(amount, getBalance(player.getName()), EconomyResponse.ResponseType.SUCCESS,
				"Erfolgreich");
	}

	@Override
	public EconomyResponse depositPlayer(String playerName, double amount) {
		EconomyMySQLAPI.addCoins(playerName, amount);
		return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, "Erfolgreich");
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
		EconomyMySQLAPI.addCoins(player.getName(), amount);
		return new EconomyResponse(amount, getBalance(player.getName()), EconomyResponse.ResponseType.SUCCESS,
				"Erfolgreich");
	}

	@Override
	public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
		EconomyMySQLAPI.addCoins(playerName, amount);
		return new EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, "Erfolgreich");
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
		EconomyMySQLAPI.addCoins(player.getName(), amount);
		return new EconomyResponse(amount, getBalance(player.getName()), EconomyResponse.ResponseType.SUCCESS,
				"Erfolgreich");
	}

	@Override
	public EconomyResponse createBank(String name, String player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse createBank(String name, OfflinePlayer player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse deleteBank(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse bankBalance(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse bankHas(String name, double amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse bankWithdraw(String name, double amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse bankDeposit(String name, double amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse isBankOwner(String name, String playerName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse isBankMember(String name, String playerName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse isBankMember(String name, OfflinePlayer player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getBanks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createPlayerAccount(String playerName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createPlayerAccount(String playerName, String worldName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
		// TODO Auto-generated method stub
		return false;
	}

}
