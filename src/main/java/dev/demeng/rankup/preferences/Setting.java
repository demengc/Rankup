package dev.demeng.rankup.preferences;

import dev.demeng.rankup.Rankup;
import org.bukkit.configuration.file.FileConfiguration;

public class Setting {

  public static int MENU_SLOTS;
  public static String MENU_TITLE;

  public static void refresh() {
    final FileConfiguration settings = Rankup.getInstance().getSettings();

    MENU_SLOTS = settings.getInt("menu-slots");
    MENU_TITLE = settings.getString("menu-title");
  }
}
