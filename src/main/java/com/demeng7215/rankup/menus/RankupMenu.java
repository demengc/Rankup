package com.demeng7215.rankup.menus;

import com.demeng7215.demlib.api.items.ItemBuilder;
import com.demeng7215.demlib.api.menus.CustomButton;
import com.demeng7215.demlib.api.menus.CustomMenu;
import com.demeng7215.rankup.entities.Prestige;
import com.demeng7215.rankup.entities.Rank;
import com.demeng7215.rankup.entities.RankedPlayer;
import com.demeng7215.rankup.preferences.Setting;
import org.bukkit.inventory.ItemStack;

public class RankupMenu {

	public RankupMenu(RankedPlayer p) {

		final CustomMenu menu = new CustomMenu(Setting.MENU_SLOTS, Setting.MENU_TITLE);

		for (String name : Rank.getRankNames()) {
			final Rank rank = Rank.of(p, name);
			final ItemStack stack = ItemBuilder.build(rank.getMaterial(), rank.getDisplayName(), null);

			final CustomButton button = new CustomButton(rank.getSlot(), stack, e -> {
				if (Rank.getNextRankName(p.getRankName()).equals(rank.getName())) rank.give(p.getPlayer());
			});

			menu.setItem(button);
		}

		for (int number : Prestige.getPrestigeNumbers()) {

			final Prestige prestige = Prestige.of(p, number);
			final ItemStack stack = ItemBuilder.build(prestige.getMaterial(), prestige.getDisplayName(), null);

			if (number != 0) {
				final CustomButton button = new CustomButton(prestige.getSlot(), stack, e -> {
					if (p.getPrestigeNumber() + 1 == prestige.getNumber() &&
							p.getRankName().equals("Free")) prestige.give(p.getPlayer());
				});

				menu.setItem(button);
			}
		}

		menu.open(p.getPlayer());
	}
}
