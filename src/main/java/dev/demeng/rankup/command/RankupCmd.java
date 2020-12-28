package dev.demeng.rankup.command;

import dev.demeng.demlib.command.model.BaseCommand;
import dev.demeng.rankup.menus.RankupMenu;
import dev.demeng.rankup.model.RankedPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankupCmd implements BaseCommand {

  @Override
  public String getName() {
    return "rankup";
  }

  @Override
  public String getDescription() {
    return "Main command for Rankup.";
  }

  @Override
  public String[] getAliases() {
    return new String[] {"ranks"};
  }

  @Override
  public String getUsage() {
    return "[info|reload]";
  }

  @Override
  public boolean isPlayersOnly() {
    return true;
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
    new RankupMenu(new RankedPlayer((Player) sender));
    return null;
  }
}
