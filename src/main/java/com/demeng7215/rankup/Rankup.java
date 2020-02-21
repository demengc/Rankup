package com.demeng7215.rankup;

import com.demeng7215.demlib.DemLib;
import com.demeng7215.demlib.api.Registerer;
import com.demeng7215.demlib.api.connections.WebUtils;
import com.demeng7215.demlib.api.files.CustomConfig;
import com.demeng7215.demlib.api.messages.MessageUtils;
import com.demeng7215.rankup.commands.RankupCmd;
import com.demeng7215.rankup.preferences.Message;
import com.demeng7215.rankup.preferences.Setting;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class Rankup extends JavaPlugin {

	@Getter
	private static Rankup instance;

	@Getter
	private CustomConfig settingsFile, messagesFile, dataFile;

	@Getter
	private Economy economy;

	@Override
	public void onEnable() {

		instance = this;

		DemLib.setPlugin(this);
		MessageUtils.setPrefix("&7[&aRankup&7] &r");

		try {
			if (new WebUtils("https://demeng7215.com/antipiracy/blacklist.txt").contains("FZrRD5wV96rCvvmx")) {
				MessageUtils.error(null, 0, "Plugin has been forcibly disabled.", true);
				return;
			}
		} catch (IOException ex) {
			MessageUtils.error(ex, 2, "Failed to authenticate.", false);
		}

		MessageUtils.log("Loading files...");

		try {

			settingsFile = new CustomConfig("settings.yml");
			messagesFile = new CustomConfig("messages.yml");
			dataFile = new CustomConfig("data.yml");

			Setting.refresh();
			Message.refresh();

			MessageUtils.setPrefix(Message.PREFIX);

		} catch (Exception ex) {
			MessageUtils.error(ex, 1, "Failed to load files.", true);
			return;
		}

		MessageUtils.log("Registering commands...");
		Registerer.registerCommand(new RankupCmd(this));

		MessageUtils.log("Hooking into Vault...");
		if (!setupEconomy()) {
			MessageUtils.error(null, 2, "Failed to hook into Vault.", true);
			return;
		}

		MessageUtils.console("&aRankup v1.0.0 by Demeng7215 has been successfully enabled!");
	}

	@Override
	public void onDisable() {
		MessageUtils.console("&cRankup v1.0.0 by Demeng7215 has been successfully disabled.");
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) return false;
		economy = rsp.getProvider();
		return economy != null;
	}

	public FileConfiguration getSettings() {
		return settingsFile.getConfig();
	}

	public FileConfiguration getMessages() {
		return messagesFile.getConfig();
	}

	public FileConfiguration getData() {
		return dataFile.getConfig();
	}
}
