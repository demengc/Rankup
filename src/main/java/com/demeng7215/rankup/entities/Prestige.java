package com.demeng7215.rankup.entities;

import com.demeng7215.demlib.api.Common;
import com.demeng7215.demlib.api.ReplaceableList;
import com.demeng7215.demlib.api.items.ItemBuilder;
import com.demeng7215.demlib.api.messages.MessageUtils;
import com.demeng7215.rankup.Rankup;
import com.demeng7215.rankup.preferences.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class Prestige {

	@Getter
	private int number;

	@Getter
	private int slot;

	@Getter
	private double cost;

	@Getter
	private List<String> commands;

	@Getter
	private ItemStack material;

	@Getter
	private String displayName;

	public static Prestige of(RankedPlayer p, int prestigeNumber) {

		final FileConfiguration settings = Rankup.getInstance().getSettings();
		final String path = "prestiges." + prestigeNumber + ".";

		final int cSlot = settings.getInt(path + "slot");
		final double cCost = settings.getInt(path + "cost");
		final List<String> cCommands = ReplaceableList.of(settings.getStringList(path + "commands"))
				.replace("%player%", p.getName());

		final ItemStack cMaterial;
		final String cDisplayName;

		if (p.getPrestigeNumber() == prestigeNumber) {
			cMaterial = ItemBuilder.getMaterial(settings.getString("prestige-materials.current"));
			cDisplayName = setPlaceholders(p.getPlayer(), settings.getString("prestige-display-names.current"),
					prestigeNumber, cCost);

		}else if(!p.getRankName().equals("Free")) {
			cMaterial = ItemBuilder.getMaterial(settings.getString("prestige-materials.not-found"));
			cDisplayName = setPlaceholders(p.getPlayer(), settings.getString("prestige-display-names.not-found"),
					prestigeNumber, cCost);

		} else if (p.getPrestigeNumber() + 1 == prestigeNumber) {
			cMaterial = ItemBuilder.getMaterial(settings.getString("prestige-materials.next"));
			cDisplayName = setPlaceholders(p.getPlayer(), settings.getString("prestige-display-names.next"),
					prestigeNumber, cCost);

		} else if (p.getPrestigeNumber() > prestigeNumber) {
			cMaterial = ItemBuilder.getMaterial(settings.getString("prestige-materials.completed"));
			cDisplayName = setPlaceholders(p.getPlayer(), settings.getString("prestige-display-names.completed"),
					prestigeNumber, cCost);

		} else {
			cMaterial = ItemBuilder.getMaterial(settings.getString("prestige-materials.not-found"));
			cDisplayName = setPlaceholders(p.getPlayer(), settings.getString("prestige-display-names.not-found"),
					prestigeNumber, cCost);
		}

		return new Prestige(prestigeNumber, cSlot, cCost, cCommands, cMaterial, cDisplayName);
	}

	public void give(Player p) {

		final Economy eco = Rankup.getInstance().getEconomy();

		p.closeInventory();

		if (!eco.has(p, cost)) {
			MessageUtils.tell(p, setPlaceholders(p, Message.INSUFFICIENT_BALANCE, number, cost));
			return;
		}

		final RankedPlayer rp = new RankedPlayer(p);
		rp.setRank("A");
		rp.setPrestige(number);

		Rankup.getInstance().getEconomy().withdrawPlayer(p, cost);
		Common.dispatch(commands);

		MessageUtils.broadcast(Message.PRESTIGED.replace("%player%", p.getName())
				.replace("%prestige%", "" + number));
	}

	private static String setPlaceholders(Player p, String s, int prestige, double cost) {
		final DecimalFormat formatter = new DecimalFormat("#,###.##");
		return s.replace("%prestige%", "" + prestige)
				.replace("%cost%", formatter.format(cost))
				.replace("%balance%", formatter.format(Rankup.getInstance().getEconomy().getBalance(p)));
	}

	public static List<Integer> getPrestigeNumbers() {
		final List<Integer> numbers = new ArrayList<>();

		for (String s : Rankup.getInstance().getSettings().getConfigurationSection("prestiges")
				.getKeys(false))
			numbers.add(Integer.parseInt(s));

		Collections.sort(numbers);

		return numbers;
	}
}
