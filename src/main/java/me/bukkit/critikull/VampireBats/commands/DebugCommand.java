package me.bukkit.critikull.VampireBats.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.bukkit.critikull.VampireBats.VampireBats;
import online.lethalsurvival.smc.chat.Message;
import online.lethalsurvival.smc.command.Arguments;
import online.lethalsurvival.smc.command.SubCommand;

public final class DebugCommand extends SubCommand {
	private VampireBats plugin;

	public DebugCommand(VampireBats plugin) {
		super("debug", "Toggle debugging", "critikull.vampirebats.debug");
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, Command command, String label, Arguments args) {
		boolean debug = this.plugin.getConfig().getBoolean("debug", false);
		if (debug) {
			sender.sendMessage(Message.create(this.plugin.getConfig().getString("messages.commands.debug.off")).colorize().getMessage());
		} else {
			sender.sendMessage(Message.create(this.plugin.getConfig().getString("messages.commands.debug.on")).colorize().getMessage());
		}
		this.plugin.getConfig().set("debug", !debug);
		this.plugin.saveConfig();
		this.plugin.reloadConfig();
	}
}
