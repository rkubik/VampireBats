package me.bukkit.critikull.VampireBats.entity;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.EntityType;

import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityTypes;
import net.minecraft.server.v1_12_R1.EntityZombieVillager;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.World;
import net.minecraft.server.v1_12_R1.EntityBat;

public enum CustomEntityType {
    VAMPIRE_BAT("vampire_bat", 65, EntityType.BAT, EntityBat.class, VampireBat.class),
    VAMPIRE("vampire", 27, EntityType.ZOMBIE_VILLAGER, EntityZombieVillager.class, Vampire.class);

	private String name;
	private int id;
	private EntityType entityType;
	private Class<? extends Entity> nmsClass;
	private Class<? extends Entity> customClass;
	private MinecraftKey key;
    private MinecraftKey oldKey;

    private CustomEntityType(String name, int id, EntityType entityType, Class<? extends Entity> nmsClass, Class<? extends Entity> customClass) {
        this.name = name;
        this.id = id;
        this.entityType = entityType;
        this.nmsClass = nmsClass;
        this.customClass = customClass;
        this.key = new MinecraftKey(name);
        this.oldKey = EntityTypes.b.b(nmsClass);
    }

    public static void registerEntities() {
    	for (CustomEntityType ce : CustomEntityType.values()) {
    		ce.register();
    	}
	}
    
    public static void unregisterEntities() {
    	for (CustomEntityType ce : CustomEntityType.values()) {
    		ce.unregister();
    	}
    }

	private void register() {
	    EntityTypes.d.add(key);
	    EntityTypes.b.a(id, key, customClass);
	}
	
	private void unregister() {
	    EntityTypes.d.remove(key);
	    EntityTypes.b.a(id, oldKey, nmsClass);
	}

    public String getName() {
        return name;
    }
    
    public int getID() {
        return id;
    }
    
    public EntityType getEntityType() {
        return entityType;
    }

    public Class<? extends Entity> getCustomClass() {
        return customClass;
    }

    public org.bukkit.entity.Entity spawnEntity(Location loc) {
        return spawnEntity(this.getCustomClass(), loc);
    }

    public org.bukkit.entity.Entity spawnEntity(Class<? extends Entity> clazz, Location loc) {
        Entity entity = null;
        // creates new instance of entity from given Class<? extends Entity>. Returns null if failed.
        try {
            CraftWorld world = (CraftWorld) loc.getWorld();
            entity = (Entity) clazz.getConstructor(World.class).newInstance(world.getHandle());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        // grabbing a bukkit version of the entity
        org.bukkit.entity.Entity bukkitEntity = entity.getBukkitEntity();
       
        // spawns entity at loc
        entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftWorld) loc.getWorld()).getHandle().addEntity(entity);
 
        return bukkitEntity;
    }
}