package com.demeng7215.rankup.commands;

import com.demeng7215.demlib.api.CustomCommand;
import com.demeng7215.demlib.api.messages.MessageUtils;
import com.demeng7215.rankup.Rankup;
import com.demeng7215.rankup.entities.RankedPlayer;
import com.demeng7215.rankup.menus.RankupMenu;
import com.demeng7215.rankup.preferences.Message;
import com.demeng7215.rankup.preferences.Setting;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class RankupCmd extends CustomCommand {

	private Rankup i;

	public RankupCmd(Rankup i) {
		super("rankup");

		this.i = i;

		setDescription("Main command for Rankup.");
		setAliases(Collections.singletonList("ranks"));
	}

	@Override
	protected void run(CommandSender sender, String[] args) {

		if (args.length == 1) {

			if (args[0].equalsIgnoreCase("info")) {
				MessageUtils.tellWithoutPrefix(sender, "&aRunning Rankup v1.0.0 by Demeng7215.");
				return;
			}

			if (args[0].equalsIgnoreCase("reload")) {
				i.getSettingsFile().reloadConfig();
				i.getMessagesFile().reloadConfig();
				i.getDataFile().reloadConfig();

				Setting.refresh();
				Message.refresh();
				MessageUtils.setPrefix(Message.PREFIX);

				MessageUtils.tell(sender, Message.RELOADED);
				return;
			}
		}

		if (!checkIsPlayer(sender, Message.CONSOLE)) return;

		final Player p = (Player) sender;

		new RankupMenu(new RankedPlayer(p));
	}
}
