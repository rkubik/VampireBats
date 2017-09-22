package me.bukkit.critikull.VampireBats.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.bukkit.critikull.CritikullSDK.v1.chat.message.Message;
import me.bukkit.critikull.CritikullSDK.v1.command.Arguments;
import me.bukkit.critikull.CritikullSDK.v1.command.SubCommand;
import me.bukkit.critikull.VampireBats.VampireBats;

public class ReloadCommand extends SubCommand {
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
