package dev.demeng.rankup.command;

import dev.demeng.demlib.command.model.SubCommand;
import dev.demeng.demlib.message.MessageUtils;
import dev.demeng.rankup.Rankup;
import dev.demeng.rankup.preferences.Message;
import dev.demeng.rankup.preferences.Setting;
import org.bukkit.command.CommandSender;

public class ReloadSubCmd implements SubCommand {

  private final Rankup i;

  public ReloadSubCmd(Rankup i) {
    this.i = i;
  }

  @Override
  public String getBaseName() {
    return "rankup";
  }

  @Override
  public String getName() {
    return "reload";
  }

  @Override
  public String getDescription() {
    return "Reloads config files.";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getUsage() {
    return "";
  }

  @Override
  public boolean isPlayersOnly() {
    return false;
  }

  @Override
  public String getPermission() {
    return "rankup.reload";
  }

  @Override
  public int getMinArgs() {
    return 0;
  }

  @Override
  public String execute(CommandSender sender, String label, String[] args) {
    i.getSettingsFile().reloadConfig();
    i.getMessagesFile().reloadConfig();
    i.getDataFile().reloadConfig();

    Setting.refresh();
    Message.refresh();
    MessageUtils.setPrefix(Message.PREFIX);

    return Message.RELOADED;
  }
}
