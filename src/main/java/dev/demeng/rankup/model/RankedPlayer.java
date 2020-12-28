package dev.demeng.rankup.model;

import dev.demeng.demlib.message.MessageUtils;
import dev.demeng.rankup.Rankup;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.io.IOException;

public class RankedPlayer {

  @Getter private final Player player;

  @Getter private final String name;

  @Getter private final String rankName;

  @Getter private final int prestigeNumber;

  public RankedPlayer(Player p) {
    this.player = p;
    this.name = p.getName();

    if (Rankup.getInstance().getData().getString("players." + p.getUniqueId() + ".rank") == null)
      Rankup.getInstance().getData().set("players." + p.getUniqueId() + ".rank", "A");

    if (Rankup.getInstance().getData().getInt("players." + p.getUniqueId() + ".prestige") == 0)
      Rankup.getInstance().getData().set("players." + p.getUniqueId() + ".prestige", 0);

    try {
      Rankup.getInstance().getDataFile().saveConfig();
    } catch (IOException ex) {
      MessageUtils.error(ex, "Failed to save data.", false);
    }

    this.rankName =
        Rankup.getInstance().getData().getString("players." + p.getUniqueId() + ".rank");
    this.prestigeNumber =
        Rankup.getInstance().getData().getInt("players." + p.getUniqueId() + ".prestige");
  }

  public void setRank(String rankName) {
    Rankup.getInstance().getData().set("players." + player.getUniqueId() + ".rank", rankName);

    try {
      Rankup.getInstance().getDataFile().saveConfig();
    } catch (IOException ex) {
      MessageUtils.error(ex, "Failed to save data.", false);
    }
  }

  public void setPrestige(int prestigeNumber) {
    Rankup.getInstance()
        .getData()
        .set("players." + player.getUniqueId() + ".prestige", prestigeNumber);

    try {
      Rankup.getInstance().getDataFile().saveConfig();
    } catch (IOException ex) {
      MessageUtils.error(ex, "Failed to save data.", false);
    }
  }
}
