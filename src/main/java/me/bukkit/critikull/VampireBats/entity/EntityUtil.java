package me.bukkit.critikull.VampireBats.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftBat;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftVillagerZombie;
import org.bukkit.entity.Bat;
import org.bukkit.entity.ZombieVillager;

import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntitySlice;
import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.MathHelper;
import net.minecraft.server.v1_12_R1.World;

public class EntityUtil {
	public static List<Entity> getEntitiesAroundPoint(double x, double y, double z, World world, double radius) {
		List<Entity> entities = new ArrayList<Entity>();

		/* To find chunks we use chunk coordinates (not block coordinates!) */
		int smallX = MathHelper.floor((x - radius) / 16.0D);
		int bigX = MathHelper.floor((x + radius) / 16.0D);
		int smallZ = MathHelper.floor((z - radius) / 16.0D);
		int bigZ = MathHelper.floor((z + radius) / 16.0D);

		for (int curX = smallX; curX <= bigX; curX++) {
			for (int curZ = smallZ; curZ <= bigZ; curZ++) {
				for (EntitySlice<Entity> entitySlice : world.getChunkAt(curX, curZ).getEntitySlices()) {
					entities.addAll(entitySlice);
				}
			}
		}

		Iterator<Entity> entityIterator = entities.iterator();
		while (entityIterator.hasNext()) {
			Entity entity = entityIterator.next();

			if ((new BlockPosition(entity.locX, entity.locY, entity.locZ)).distanceSquared(x, y, z) > radius * radius) {
				entityIterator.remove();
			}
		}

		return entities;
	}

	public static boolean isPlayerHolding(EntityHuman player, Item item) {
		return isPlayerHolding(player, item, null);
	}

	public static boolean isPlayerHolding(EntityHuman player, Item item, String itemName) {
		if (player == null) {
			return false;
		}

		if (player.getItemInMainHand() != null) {
			if (player.getItemInMainHand().getItem() == item
					&& (itemName == null || player.getItemInMainHand().getName().equalsIgnoreCase(itemName))) {
				return true;
			}
		}

		if (player.getItemInOffHand() != null) {
			if (player.getItemInOffHand().getItem() == item
					&& (itemName == null || player.getItemInOffHand().getName().equalsIgnoreCase(itemName))) {
				return true;
			}
		}

		return false;
	}

	public static boolean isVampire(org.bukkit.entity.Entity e) {
		return (e instanceof ZombieVillager) && (((CraftVillagerZombie) e).getHandle() instanceof Vampire);
	}

	public static boolean isVampireBat(org.bukkit.entity.Entity e) {
		return (e instanceof Bat) && (((CraftBat) e).getHandle() instanceof VampireBat);
	}
}
