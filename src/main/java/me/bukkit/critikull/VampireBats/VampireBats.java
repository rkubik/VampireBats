package me.bukkit.critikull.VampireBats;

import java.util.Arrays;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import me.bukkit.critikull.CritikullSDK.v1.command.CustomCommand;
import me.bukkit.critikull.VampireBats.commands.ReloadCommand;
import me.bukkit.critikull.VampireBats.commands.SpawnCommand;
import me.bukkit.critikull.VampireBats.entity.CustomEntityType;
import me.bukkit.critikull.VampireBats.listeners.DamageListener;
import me.bukkit.critikull.VampireBats.listeners.DeathListener;
import me.bukkit.critikull.VampireBats.listeners.SpawnListener;

public class VampireBats extends JavaPlugin  {

	@Override
	public void onEnable() {
		CustomEntityType.registerEntities();

		getConfig().addDefault("debug", false);
		getConfig().options().copyDefaults(true);
		saveConfig();

		getServer().getPluginManager().registerEvents(new DeathListener(this), this);
		getServer().getPluginManager().registerEvents(new DamageListener(this), this);
		getServer().getPluginManager().registerEvents(new SpawnListener(this), this);

		getCommand("vampirebats").setExecutor(new CustomCommand(
			"Vampire Bat's",
			"Vampire and Vampire Bat monsters!",
			Arrays.asList(
				new ReloadCommand(this),
				new SpawnCommand(this)
			)
		));
	}

	@Override
	public void onDisable() {
		CustomEntityType.unregisterEntities();
	}

	public void log(String message) {
		if (getConfig().getBoolean("debug")) {
			getLogger().info(message);
		}
	}

	public void log(Entity e, String message) {
		log(String.format("<entity uuid=%s type=%s name=%s hp=%.2f loc=%d,%d,%d> %s",
			e.getUniqueId().toString(),
			e.getType().toString(),
			e.getCustomName() != null ? e.getCustomName() : e.getName(),
			((e instanceof Damageable) ? ((Damageable)e).getHealth() : 0.0D),
			(int)e.getLocation().getX(),
			(int)e.getLocation().getY(),
			(int)e.getLocation().getZ(),
			message
		));
	}
}
