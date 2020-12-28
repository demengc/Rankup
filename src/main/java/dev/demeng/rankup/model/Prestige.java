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
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Prestige {

  private final int number;
  private final int slot;
  private final double cost;
  private final List<String> commands;
  private final ItemStack material;
  private final String displayName;
  private final List<String> lore;

  public Prestige(
      int number,
      int slot,
      double cost,
      List<String> commands,
      ItemStack material,
      String displayName,
      List<String> lore) {
    this.number = number;
    this.slot = slot;
    this.cost = cost;
    this.commands = commands;
    this.material = material;
    this.displayName = displayName;
    this.lore = lore;
  }

  public static Prestige of(RankedPlayer p, int prestigeNumber) {

    final List<String> cLore;
    final ItemStack cMaterial;
    final String cDisplayName;
    final FileConfiguration settings = Rankup.getInstance().getSettings();
    final String path = "prestiges." + prestigeNumber + ".";
    final int cSlot = settings.getInt(path + "slot");
    final double cCost = settings.getInt(path + "cost");
    final List<String> cCommands = new ArrayList<>();

    for (String cmd : settings.getStringList(path + "commands")) {
      cCommands.add(cmd.replace("%player%", p.getName()));
    }

    if (p.getPrestigeNumber() == prestigeNumber) {
      cMaterial =
          ItemCreator.getMaterial(
              Objects.requireNonNull(settings.getString("prestige-materials.current")));
      cDisplayName =
          Prestige.setPlaceholders(
              p.getPlayer(),
              Objects.requireNonNull(settings.getString("prestige-display-names.current")),
              prestigeNumber,
              cCost);
      cLore = settings.getStringList("prestige-lores.current");

    } else if (!p.getRankName().equals("Free")) {
      cMaterial =
          ItemCreator.getMaterial(
              Objects.requireNonNull(settings.getString("prestige-materials.not-found")));
      cDisplayName =
          Prestige.setPlaceholders(
              p.getPlayer(),
              Objects.requireNonNull(settings.getString("prestige-display-names.not-found")),
              prestigeNumber,
              cCost);
      cLore = settings.getStringList("prestige-lores.not-found");

    } else if (p.getPrestigeNumber() + 1 == prestigeNumber) {
      cMaterial =
          ItemCreator.getMaterial(
              Objects.requireNonNull(settings.getString("prestige-materials.next")));
      cDisplayName =
          Prestige.setPlaceholders(
              p.getPlayer(),
              Objects.requireNonNull(settings.getString("prestige-display-names.next")),
              prestigeNumber,
              cCost);
      cLore = settings.getStringList("prestige-lores.next");

    } else if (p.getPrestigeNumber() > prestigeNumber) {
      cMaterial =
          ItemCreator.getMaterial(
              Objects.requireNonNull(settings.getString("prestige-materials.completed")));
      cDisplayName =
          Prestige.setPlaceholders(
              p.getPlayer(),
              Objects.requireNonNull(settings.getString("prestige-display-names.completed")),
              prestigeNumber,
              cCost);
      cLore = settings.getStringList("prestige-lores.completed");

    } else {
      cMaterial =
          ItemCreator.getMaterial(
              Objects.requireNonNull(settings.getString("prestige-materials.not-found")));
      cDisplayName =
          Prestige.setPlaceholders(
              p.getPlayer(),
              Objects.requireNonNull(settings.getString("prestige-display-names.not-found")),
              prestigeNumber,
              cCost);
      cLore = settings.getStringList("prestige-lores.not-found");
    }

    final List<String> finalLore = new ArrayList<>();
    for (String s : cLore) {
      finalLore.add(Prestige.setPlaceholders(p.getPlayer(), s, prestigeNumber, cCost));
    }

    return new Prestige(
        prestigeNumber, cSlot, cCost, cCommands, cMaterial, cDisplayName, finalLore);
  }

  public void give(Player p) {

    final Economy eco = Rankup.getInstance().getEconomy();

    p.closeInventory();

    if (!eco.has(p, this.cost)) {
      MessageUtils.tell(
          p, Prestige.setPlaceholders(p, Message.INSUFFICIENT_BALANCE, this.number, this.cost));
      return;
    }

    final RankedPlayer rp = new RankedPlayer(p);
    rp.setRank("A");
    rp.setPrestige(this.number);

    Rankup.getInstance().getEconomy().withdrawPlayer(p, this.cost);

    for (String cmd : commands) {
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }

    MessageUtils.broadcast(
        Message.PRESTIGED.replace("%player%", p.getName()).replace("%prestige%", "" + this.number));
  }

  private static String setPlaceholders(Player p, String s, int prestige, double cost) {
    final DecimalFormat formatter = new DecimalFormat("#,###.##");
    return s.replace("%prestige%", "" + prestige)
        .replace("%cost%", formatter.format(cost))
        .replace("%balance%", formatter.format(Rankup.getInstance().getEconomy().getBalance(p)));
  }

  public static List<Integer> getPrestigeNumbers() {

    final List<Integer> numbers = new ArrayList<>();
    for (String s :
        Objects.requireNonNull(
                Rankup.getInstance().getSettings().getConfigurationSection("prestiges"))
            .getKeys(false)) {
      numbers.add(Integer.parseInt(s));
    }

    Collections.sort(numbers);
    return numbers;
  }

  public int getNumber() {
    return this.number;
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
