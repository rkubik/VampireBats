package me.bukkit.critikull.VampireBats.listeners;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftBat;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import me.bukkit.critikull.VampireBats.VampireBats;
import me.bukkit.critikull.VampireBats.entity.EntityUtil;
import me.bukkit.critikull.VampireBats.entity.VampireBat;
import net.minecraft.server.v1_12_R1.World;

public class SpawnListener implements Listener {
	private VampireBats plugin;

	public SpawnListener(VampireBats plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCreatureSpawn(CreatureSpawnEvent e) {
		if (e.isCancelled() || e.getEntityType() != EntityType.BAT || e.getSpawnReason() == SpawnReason.CUSTOM || EntityUtil.isVampireBat(e.getEntity())) {
			return;
		}

		Location location = e.getLocation();
		World world = ((CraftWorld) location.getWorld()).getHandle();
		VampireBat vampireBat = new VampireBat(world);
		vampireBat.setPosition(location.getX(), location.getY(), location.getZ());
		world.removeEntity(((CraftBat) e.getEntity()).getHandle());
		world.addEntity(vampireBat, SpawnReason.CUSTOM);

		this.plugin.log(vampireBat.getBukkitEntity(), "spawned from " + e.getSpawnReason().toString());
	}
}
