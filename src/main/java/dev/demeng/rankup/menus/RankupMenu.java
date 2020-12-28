package dev.demeng.rankup.menus;

import dev.demeng.demlib.item.ItemCreator;
import dev.demeng.demlib.menu.Menu;
import dev.demeng.demlib.menu.MenuButton;
import dev.demeng.rankup.model.Prestige;
import dev.demeng.rankup.model.Rank;
import dev.demeng.rankup.model.RankedPlayer;
import dev.demeng.rankup.preferences.Setting;
import org.bukkit.inventory.ItemStack;

public class RankupMenu extends Menu {

  public RankupMenu(RankedPlayer p) {
    super(Setting.MENU_SLOTS, Setting.MENU_TITLE);

    for (String rankName : Rank.getRankNames()) {

      final ItemStack stack;
      final MenuButton button;

      Rank rank = Rank.of(p, rankName);
      stack = ItemCreator.quickBuild(rank.getMaterial(), rank.getDisplayName(), rank.getLore());
      button =
          new MenuButton(
              rank.getSlot(),
              stack,
              (e) -> {
                if (Rank.getNextRankName(p.getRankName()).equals(rank.getName())) {
                  rank.give(p.getPlayer());
                }
              });

      setItem(button);
    }

    for (int prestigeNumber : Prestige.getPrestigeNumbers()) {

      final ItemStack stack;
      final MenuButton button;

      final Prestige prestige = Prestige.of(p, prestigeNumber);
      stack =
          ItemCreator.quickBuild(
              prestige.getMaterial(), prestige.getDisplayName(), prestige.getLore());

      if (prestigeNumber != 0) {
        button =
            new MenuButton(
                prestige.getSlot(),
                stack,
                (e) -> {
                  if (p.getPrestigeNumber() + 1 == prestige.getNumber()
                      && p.getRankName().equals("Free")) {
                    prestige.give(p.getPlayer());
                  }
                });
        setItem(button);
      }
    }

    open(p.getPlayer());
  }
}
