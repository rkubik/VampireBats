package me.bukkit.critikull.VampireBats.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.bukkit.critikull.VampireBats.VampireBats;
import online.lethalsurvival.smc.chat.Message;
import online.lethalsurvival.smc.command.Arguments;
import online.lethalsurvival.smc.command.SubCommand;

public final class ReloadCommand extends SubCommand {
	private VampireBats plugin;

	public ReloadCommand(VampireBats plugin) {
		super("reload", "Reload config", "critikull.vampirebats.reload");
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, Command command, String label, Arguments args) {
		this.plugin.reloadConfig();
		sender.sendMessage(Message.create(this.plugin.getConfig().getString("messages.commands.reload")).colorize().getMessage());
	}
}
