package me.bukkit.critikull.VampireBats.entity;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftVillagerZombie;
import org.bukkit.entity.ZombieVillager;

import me.bukkit.critikull.VampireBats.entity.pathfinder.PathfinderGoalAvoidPlayerItem;
import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.DifficultyDamageScaler;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntitySkeleton;
import net.minecraft.server.v1_12_R1.EntityVillager;
import net.minecraft.server.v1_12_R1.EntityZombie;
import net.minecraft.server.v1_12_R1.EntityZombieVillager;
import net.minecraft.server.v1_12_R1.EnumHand;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.GroupDataEntity;
import net.minecraft.server.v1_12_R1.Items;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_12_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_12_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_12_R1.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.v1_12_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_12_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_12_R1.PathfinderGoalZombieAttack;
import net.minecraft.server.v1_12_R1.World;

public class Vampire extends EntityZombieVillager {
	private static final String CUSTOM_NAME = "Vampire";
	private static final double MAX_HEALTH = 40.0D;
	private static final double FOLLOW_RANGE = 15.0D;
	private static final double MOVEMENT_SPEED = 0.4D;
	private static final double ATTACK_DAMAGE = 5.0D;
	private static final int TRANSFORM_HITS_MIN = 1;
	private static final int TRANSFORM_HITS_MAX = 5;
	private static final int TRANSFORM_DELAY_TICKS = 10;

	private VampireBat bat;
	private int transformTick;
	private int maxPlayerHits;
	private int currentPlayerHits;

	public Vampire(World world) {
		this(world, null);
	}

	public Vampire(World world, VampireBat bat) {
		super(world);
		setCustomName(CUSTOM_NAME);
		this.bat = bat;
		init();
	}

	public void init() {
		/* @todo Set pathfinding target to target from vampire bat (if available) */
		this.transformTick = MinecraftServer.currentTick;
		this.maxPlayerHits = TRANSFORM_HITS_MIN + this.random.nextInt(TRANSFORM_HITS_MAX - TRANSFORM_HITS_MIN + 1);
		this.currentPlayerHits = 0;
		this.motX = 0.0D;
		this.motY = 0.0D;
		this.motZ = 0.0D;
	}

	private void transform() {
		if (this.bat == null) {
			this.bat = new VampireBat(this.world, this);
		} else {
			this.bat.init();
		}
		this.bat.setLocation(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
		this.world.removeEntity(this);
		this.dead = false;
		this.world.addEntity(this.bat);
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {
		if (super.damageEntity(damagesource, f)) {
			if (damagesource.getEntity() instanceof EntityHuman) {
				if (((MinecraftServer.currentTick - this.transformTick) >= TRANSFORM_DELAY_TICKS) && 
					(this.currentPlayerHits >= this.maxPlayerHits) &&
					((getHealth() - f) > 0)) {
					/* Transform into a Vampire Bat */
					transform();
				}
				this.currentPlayerHits++;
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean a(EntityHuman entityhuman, EnumHand enumhand) {
		/* Prevent transformation into villager */
		return false;
	}

	@Override
	public void B_() {
		super.B_();
		/* Sunlight level */
		if (aw() >= 0.3F) {
			if (this.random.nextBoolean()) {
				this.setHealth(0.0F);
			} else {
				transform();
			}
		}
	}

	@Override
	protected void r() {
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(1, new PathfinderGoalAvoidPlayerItem(this, Items.POISONOUS_POTATO, "garlic", 6.0F, 1.0D, 1.2D));
		this.goalSelector.a(2, new PathfinderGoalZombieAttack(this, 1.0D, false));
		this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
		this.goalSelector.a(7, new PathfinderGoalRandomStrollLand(this, 1.0D));
		this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
		do_();
	}

	@Override
	protected void do_() {
		this.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
		this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true, new Class[] { EntityZombie.class, EntitySkeleton.class }));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
		this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityVillager.class, false));
	}

	@Override
	public GroupDataEntity prepare(DifficultyDamageScaler dds, GroupDataEntity gde) {
		/* Don't call super, zombie prepare is... strange */
	    return gde;
	}

	@Override
	protected void initAttributes() {
		getAttributeMap().b(GenericAttributes.maxHealth);
		getAttributeMap().b(GenericAttributes.FOLLOW_RANGE);
	    getAttributeMap().b(GenericAttributes.c);
	    getAttributeMap().b(GenericAttributes.MOVEMENT_SPEED);
	    getAttributeMap().b(GenericAttributes.h);
	    getAttributeMap().b(GenericAttributes.i);
	    getAttributeMap().b(GenericAttributes.ATTACK_DAMAGE);
		getAttributeInstance(GenericAttributes.maxHealth).setValue(MAX_HEALTH);
		getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(FOLLOW_RANGE /* radius */);
		getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(MOVEMENT_SPEED);
		getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(ATTACK_DAMAGE);
		getAttributeInstance(GenericAttributes.h).setValue(2.0D /* height */);
		getAttributeMap().b(a).setValue(0.0D /* disable reinforcements */);
	}

	public static boolean isVampire(org.bukkit.entity.Entity e) {
		return (e instanceof ZombieVillager) && (((CraftVillagerZombie)e).getHandle() instanceof Vampire);
	}
}
