package me.bukkit.critikull.VampireBats.commands;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.bukkit.critikull.CritikullSDK.v1.command.Argument;
import me.bukkit.critikull.CritikullSDK.v1.command.Arguments;
import me.bukkit.critikull.CritikullSDK.v1.command.ChoiceArgument;
import me.bukkit.critikull.CritikullSDK.v1.command.IntegerOptionalArgument;
import me.bukkit.critikull.CritikullSDK.v1.command.SubCommand;
import me.bukkit.critikull.VampireBats.VampireBats;
import me.bukkit.critikull.VampireBats.entity.CustomEntityType;

public class SpawnCommand extends SubCommand {
	private VampireBats plugin;

	public SpawnCommand(VampireBats plugin) {
		super(
			"spawn", 
			"Spawn a Vampire Bat", 
			"critikull.vampirebats.spawn",
			new Arguments(Arrays.asList(
				(Argument) new ChoiceArgument("form", Arrays.asList("bat", "vampire")),
				(Argument) new IntegerOptionalArgument("num")
			))	
		);
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, Command command, String label, Arguments args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			CustomEntityType type = null;
			String form = args.getChoiceArgument("form").getValue();
			if (form.equalsIgnoreCase("bat")) {
				type = CustomEntityType.VAMPIRE_BAT;
			} else if (form.equalsIgnoreCase("vampire")) {
				type = CustomEntityType.VAMPIRE;
			}

			for (int i = 0; i < args.getIntegerArgument("num", 1).getValue(); i++) {
				type.spawnEntity(player.getLocation());
			}
		}
	}
}
