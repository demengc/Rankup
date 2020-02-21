package com.demeng7215.rankup.preferences;

import com.demeng7215.rankup.Rankup;
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
