package dev.demeng.rankup.preferences;

import dev.demeng.rankup.Rankup;
import org.bukkit.configuration.file.FileConfiguration;

public class Message {

  public static String PREFIX;

  public static String RELOADED;
  public static String CONSOLE;

  public static String INSUFFICIENT_BALANCE;

  public static String RANKED_UP;
  public static String PRESTIGED;

  public static void refresh() {

    final FileConfiguration messages = Rankup.getInstance().getMessages();

    PREFIX = messages.getString("prefix");

    RELOADED = messages.getString("reloaded");
    CONSOLE = messages.getString("console");

    INSUFFICIENT_BALANCE = messages.getString("insufficient-balance");

    RANKED_UP = messages.getString("ranked-up");
    PRESTIGED = messages.getString("prestiged");
  }
}
