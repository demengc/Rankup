package dev.demeng.rankup.command;

import dev.demeng.demlib.command.model.SubCommand;
import dev.demeng.demlib.message.MessageUtils;
import org.bukkit.command.CommandSender;

public class InfoSubCmd implements SubCommand {

  @Override
  public String getBaseName() {
    return "rankup";
  }

  @Override
  public String getName() {
    return "info";
  }

  @Override
  public String getDescription() {
    return "Displays plugin information.";
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
    return null;
  }

  @Override
  public int getMinArgs() {
    return 0;
  }

  @Override
  public String execute(CommandSender sender, String label, String[] args) {
    MessageUtils.tellClean(sender, "&aRunning Rankup v1.0.0 by Demeng.");
    return null;
  }
}
