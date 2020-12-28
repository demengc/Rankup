package dev.demeng.rankup;

import dev.demeng.demlib.Registerer;
import dev.demeng.demlib.command.CommandMessages;
import dev.demeng.demlib.core.DemLib;
import dev.demeng.demlib.file.YamlFile;
import dev.demeng.demlib.message.MessageUtils;
import dev.demeng.rankup.command.InfoSubCmd;
import dev.demeng.rankup.command.RankupCmd;
import dev.demeng.rankup.command.ReloadSubCmd;
import dev.demeng.rankup.preferences.Message;
import dev.demeng.rankup.preferences.Setting;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Rankup extends JavaPlugin {

  @Getter private static Rankup instance;

  @Getter private YamlFile settingsFile, messagesFile, dataFile;

  @Getter private Economy economy;

  @Override
  public void onEnable() {

    instance = this;

    DemLib.setPlugin(this);
    MessageUtils.setPrefix("&7[&aRankup&7] &r");

    MessageUtils.log("Loading files...");

    try {
      settingsFile = new YamlFile("settings.yml");
      messagesFile = new YamlFile("messages.yml");
      dataFile = new YamlFile("data.yml");

    } catch (Exception ex) {
      MessageUtils.error(ex, "Failed to load files.", true);
      return;
    }

    Setting.refresh();
    Message.refresh();

    MessageUtils.setPrefix(Message.PREFIX);

    DemLib.setCommandMessages(
        new CommandMessages(Message.CONSOLE, "&cInsufficient permission.", ""));

    MessageUtils.log("Registering commands...");
    Registerer.registerCommand(new RankupCmd());
    Registerer.registerCommand(new InfoSubCmd());
    Registerer.registerCommand(new ReloadSubCmd(this));

    MessageUtils.log("Hooking into Vault...");
    if (!setupEconomy()) {
      MessageUtils.error(null, "Failed to hook into Vault.", true);
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
    RegisteredServiceProvider<Economy> rsp =
        getServer().getServicesManager().getRegistration(Economy.class);
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
