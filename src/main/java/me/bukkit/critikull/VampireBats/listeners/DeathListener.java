package me.bukkit.critikull.VampireBats.listeners;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftBat;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftVillagerZombie;
import org.bukkit.entity.Bat;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.bukkit.critikull.VampireBats.VampireBats;
import me.bukkit.critikull.VampireBats.entity.Vampire;
import me.bukkit.critikull.VampireBats.entity.VampireBat;

public class DeathListener implements Listener {
	private VampireBats plugin;
	private Random random;
	
	public DeathListener(VampireBats plugin) {
		this.plugin = plugin;
		this.random = new Random();
	}
	
	@EventHandler
	public void onVampireBatDeath(EntityDeathEvent e) {
		if (!(e.getEntity() instanceof Bat)) {
			return;
		}

		if (!(((CraftBat)e.getEntity()).getHandle() instanceof VampireBat)) {
			return;
		}

		e.setDroppedExp(5);
		e.getDrops().clear();
		for (int i = 0; i < this.random.nextInt(3); i++) {
			e.getDrops().add(new ItemStack(Material.PUMPKIN, 1));
		}

		this.plugin.log(e.getEntity(), "died");
	}

	@EventHandler
	public void onVampireDeath(EntityDeathEvent e) {
		if (!(e.getEntity() instanceof ZombieVillager)) {
			return;
		}

		if (!(((CraftVillagerZombie)e.getEntity()).getHandle() instanceof Vampire)) {
			return;
		}

		e.setDroppedExp(10);
		e.getDrops().clear();
		for (int i = 0; i < this.random.nextInt(3); i++) {
			e.getDrops().add(new ItemStack(Material.PUMPKIN, 1));
		}

		this.plugin.log(e.getEntity(), "died");
	}
}
