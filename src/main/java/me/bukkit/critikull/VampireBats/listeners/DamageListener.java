package me.bukkit.critikull.VampireBats.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.bukkit.critikull.VampireBats.VampireBats;
import me.bukkit.critikull.VampireBats.entity.EntityUtil;

public class DamageListener implements Listener {
	private VampireBats plugin;

	public DamageListener(VampireBats plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onVampireBatDeath(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && (EntityUtil.isVampire(e.getEntity()) || EntityUtil.isVampireBat(e.getEntity()))) {
			this.plugin.log(e.getEntity(), "damaged by player: " + e.getDamage());
		}
	}
}
