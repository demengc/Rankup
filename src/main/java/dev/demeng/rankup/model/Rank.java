package dev.demeng.rankup.model;

import dev.demeng.demlib.item.ItemCreator;
import dev.demeng.demlib.message.MessageUtils;
import dev.demeng.rankup.Rankup;
import dev.demeng.rankup.preferences.Message;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Rank {

  private final String name;
  private final int slot;
  private final double cost;
  private final List<String> commands;
  private final ItemStack material;
  private final String displayName;
  private final List<String> lore;

  public Rank(
      String name,
      int slot,
      double cost,
      List<String> commands,
      ItemStack material,
      String displayName,
      List<String> lore) {
    this.name = name;
    this.slot = slot;
    this.cost = cost;
    this.commands = commands;
    this.material = material;
    this.displayName = displayName;
    this.lore = lore;
  }

  public static Rank of(RankedPlayer p, String rankName) {

    final FileConfiguration settings = Rankup.getInstance().getSettings();
    final String path = "ranks." + rankName + ".";
    final int cSlot = settings.getInt(path + "slot");
    final double cCost = calculateCost(rankName, p.getPrestigeNumber());
    final List<String> cCommands = new ArrayList<>();

    for (String cmd : settings.getStringList(path + "commands")) {
      cCommands.add(cmd.replace("%player%", p.getName()));
    }

    final ItemStack cMaterial;
    final String cDisplayName;
    final List<String> cLore;

    if (p.getRankName().equals(rankName)) {
      cMaterial =
          ItemCreator.getMaterial(
              Objects.requireNonNull(settings.getString("rank-materials.current")));
      cDisplayName =
          setPlaceholders(
              p.getPlayer(),
              Objects.requireNonNull(settings.getString("rank-display-names.current")),
              rankName,
              cCost);
      cLore = settings.getStringList("rank-lores.current");

    } else if (getNextRankName(p.getRankName()).equals(rankName)) {
      cMaterial =
          ItemCreator.getMaterial(
              Objects.requireNonNull(settings.getString("rank-materials.next")));
      cDisplayName =
          setPlaceholders(
              p.getPlayer(),
              Objects.requireNonNull(settings.getString("rank-display-names.next")),
              rankName,
              cCost);
      cLore = settings.getStringList("rank-lores.next");

    } else if (getPreviousRankNames(p.getRankName()).contains(rankName)) {
      cMaterial =
          ItemCreator.getMaterial(
              Objects.requireNonNull(settings.getString("rank-materials.completed")));
      cDisplayName =
          setPlaceholders(
              p.getPlayer(),
              Objects.requireNonNull(settings.getString("rank-display-names.completed")),
              rankName,
              cCost);
      cLore = settings.getStringList("rank-lores.completed");

    } else {
      cMaterial =
          ItemCreator.getMaterial(
              Objects.requireNonNull(settings.getString("rank-materials.not-found")));
      cDisplayName =
          setPlaceholders(
              p.getPlayer(),
              Objects.requireNonNull(settings.getString("rank-display-names.not-found")),
              rankName,
              cCost);
      cLore = settings.getStringList("rank-lores.not-found");
    }

    final ArrayList<String> finalLore = new ArrayList<>();

    for (String line : cLore) {
      finalLore.add(setPlaceholders(p.getPlayer(), line, rankName, cCost));
    }

    return new Rank(rankName, cSlot, cCost, cCommands, cMaterial, cDisplayName, finalLore);
  }

  public void give(Player p) {

    final Economy eco = Rankup.getInstance().getEconomy();

    p.closeInventory();

    if (!eco.has(p, this.cost)) {
      MessageUtils.tell(p, setPlaceholders(p, Message.INSUFFICIENT_BALANCE, this.name, this.cost));

    } else {
      (new RankedPlayer(p)).setRank(this.name);
      Rankup.getInstance().getEconomy().withdrawPlayer(p, this.cost);

      for (String cmd : commands) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
      }

      MessageUtils.broadcast(
          Message.RANKED_UP.replace("%player%", p.getName()).replace("%rank%", this.name));
    }
  }

  private static String setPlaceholders(Player p, String s, String name, double cost) {
    final DecimalFormat formatter = new DecimalFormat("#,###.##");
    return s.replace("%rank%", name)
        .replace("%cost%", formatter.format(cost))
        .replace("%balance%", formatter.format(Rankup.getInstance().getEconomy().getBalance(p)));
  }

  public static String getNextRankName(String rankName) {
    return rankName.equals("Free")
        ? "Nothing"
        : getRankNames().get(getRankNames().indexOf(rankName) + 1);
  }

  public static List<String> getPreviousRankNames(String rankName) {
    final List<String> names = new ArrayList<>();

    for (int i = getRankNames().indexOf(rankName) - 1; i >= 0; --i) {
      names.add(getRankNames().get(i));
    }

    return names;
  }

  public static double calculateCost(String rankName, int prestige) {
    double og = Rankup.getInstance().getSettings().getInt("ranks." + rankName + ".cost");
    return prestige == 0 ? og : og * (double) prestige + og;
  }

  public static List<String> getRankNames() {
    final List<String> names = new ArrayList<>();
    names.add("A");
    names.add("B");
    names.add("C");
    names.add("D");
    names.add("E");
    names.add("F");
    names.add("G");
    names.add("H");
    names.add("I");
    names.add("J");
    names.add("K");
    names.add("L");
    names.add("M");
    names.add("N");
    names.add("O");
    names.add("P");
    names.add("Q");
    names.add("R");
    names.add("S");
    names.add("T");
    names.add("U");
    names.add("V");
    names.add("W");
    names.add("X");
    names.add("Y");
    names.add("Z");
    names.add("Free");
    return names;
  }

  public String getName() {
    return this.name;
  }

  public int getSlot() {
    return this.slot;
  }

  public ItemStack getMaterial() {
    return this.material;
  }

  public String getDisplayName() {
    return this.displayName;
  }

  public List<String> getLore() {
    return this.lore;
  }
}
