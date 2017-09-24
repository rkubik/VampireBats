package me.bukkit.critikull.VampireBats.entity;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftBat;
import org.bukkit.entity.Bat;

import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityBat;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.Items;
import net.minecraft.server.v1_12_R1.MathHelper;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.World;

public class VampireBat extends EntityBat {
	private static final String CUSTOM_NAME = "Vampire Bat";
	private static final float ATTACK_DAMAGE = 2.0F;
	private static final double AGGRO_RADIUS = 10.0D;
	private static final double DAMAGE_RADIUS = 1.0D;
	private static final double MAX_HEALTH = 30.0D;
	private static final int MIN_TRANSFORM_HITS = 1;
	private static final int MAX_TRANSFORM_HITS = 5;
	private static final int TRANSFORM_DELAY_TICKS = 10;
	private static final int MAX_AGGRO_TICKS = 200;

	private Vampire vampire;
	private EntityHuman targetPlayer;
	private BlockPosition targetBlock;
	private int transformTick;
	private int maxPlayerHits;
	private int currentPlayerHits;
	private int lastAttackTick;

	public VampireBat(World world) {
		this(world, null);
	}

	public VampireBat(World world, Vampire vampire) {
		super(world);
		setCustomName(CUSTOM_NAME);
		this.vampire = vampire;
		init();
	}

	public void init() {
		/* @todo Set targetPlayer to target from vampire (if available) */
		this.lastAttackTick = 0;
		this.targetBlock = null;
		this.transformTick = MinecraftServer.currentTick;
		this.maxPlayerHits = MIN_TRANSFORM_HITS + this.random.nextInt(MAX_TRANSFORM_HITS - MIN_TRANSFORM_HITS + 1);
		this.currentPlayerHits = 0;
		this.motX = 0.0D;
		this.motY = 0.0D;
		this.motZ = 0.0D;
	}

	private void transform() {
		if (aw() < 0.5F) {
			if (this.vampire == null) {
				this.vampire = new Vampire(this.world, this);
			} else {
				this.vampire.init();
			}
			this.vampire.setLocation(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
			this.world.removeEntity(this);
			this.dead = false;
			this.world.addEntity(this.vampire);
		}
	}

	@Override
	protected void initAttributes() {
	    getAttributeMap().b(GenericAttributes.c);
	    getAttributeMap().b(GenericAttributes.MOVEMENT_SPEED);
	    getAttributeMap().b(GenericAttributes.h);
	    getAttributeMap().b(GenericAttributes.i);
	    getAttributeMap().b(GenericAttributes.FOLLOW_RANGE).setValue(AGGRO_RADIUS);
		getAttributeMap().b(GenericAttributes.maxHealth).setValue(MAX_HEALTH);
	}

	@Override
	public boolean damageEntity(DamageSource damagesource, float f) {
		if (super.damageEntity(damagesource, f)) {
			if (damagesource != null && damagesource.getEntity() != null && damagesource.getEntity() instanceof EntityHuman) {
				if (((MinecraftServer.currentTick - this.transformTick) >= TRANSFORM_DELAY_TICKS) && 
					(this.currentPlayerHits >= this.maxPlayerHits) &&
					((getHealth() - f) > 0)) {
					/* Transform into a Vampire */
					transform();
				}
				this.currentPlayerHits++;
			}
			return true;
		}
		return false; 
	}
	
	@Override
	public void B_() {
		super.B_();
		if (validPlayerTarget(this.targetPlayer)) {
			if ((MinecraftServer.currentTick - this.lastAttackTick) > 20) {
				BlockPosition block = new BlockPosition(this.targetPlayer.locX, this.targetPlayer.locY, this.targetPlayer.locZ);
		    	if (block.distanceSquared(this.locX, this.locY, this.locZ) <= DAMAGE_RADIUS * DAMAGE_RADIUS) {
		    		this.targetPlayer.damageEntity(DamageSource.mobAttack(this), ATTACK_DAMAGE + this.random.nextFloat());
		    		this.lastAttackTick = MinecraftServer.currentTick;
		    	}
			}
		}
	}

	private static boolean validPlayerTarget(EntityHuman player) {
		return player != null && player.isAlive() && !player.isCreativeAndOp() && !player.isSpectator() && !EntityUtil.isPlayerHolding(player, Items.POISONOUS_POTATO, "garlic");
	}

	@Override
	protected void M() {
		BlockPosition localBlockPosition1 = new BlockPosition(this);
	    BlockPosition localBlockPosition2 = localBlockPosition1.up();

	    if (((MinecraftServer.currentTick - this.lastAttackTick) > MAX_AGGRO_TICKS) || (targetPlayer != null && !validPlayerTarget(targetPlayer))) {
	    	this.targetPlayer = null;
	    }

	    if (this.targetPlayer == null) {
	        for (Entity entity : EntityUtil.getEntitiesAroundPoint(this.locX, this.locY, this.locZ, this.world, AGGRO_RADIUS)) {
	        	if (entity instanceof EntityHuman && validPlayerTarget((EntityHuman) entity)) {
	        		this.targetPlayer = (EntityHuman) entity;
	        		this.lastAttackTick = MinecraftServer.currentTick;
	        		break;
	        	}
	        }
	    }

	    if (isAsleep()) {
	    	if (targetPlayer != null) {
	            setAsleep(false);
	            this.world.a(null, 1025, localBlockPosition1, 0);
	    	} else {
		        if (this.world.getType(localBlockPosition2).l()) {
		            if (this.random.nextInt(200) == 0) {
		                this.aP = this.random.nextInt(360);
		            }
		            if (this.world.b(this, 4.0D) != null) {
		            	setAsleep(false);
		                this.world.a(null, 1025, localBlockPosition1, 0);
		            }
		        } else {
		            setAsleep(false);
		            this.world.a(null, 1025, localBlockPosition1, 0);
		        }
	    	}
	    } else {
	        if ((this.targetBlock != null) && ((!this.world.isEmpty(this.targetBlock)) || (this.targetBlock.getY() < 1))) {
	            this.targetBlock = null;
	        }
	        if (targetPlayer != null) {
	        	this.targetBlock = new BlockPosition(targetPlayer.locX, targetPlayer.locY, targetPlayer.locZ);
	        } else if ((this.targetBlock == null) || (this.random.nextInt(30) == 0) || (this.targetBlock.distanceSquared((int)this.locX, (int)this.locY, (int)this.locZ) < 4.0D)) {
	            this.targetBlock = new BlockPosition((int)this.locX + this.random.nextInt(7) - this.random.nextInt(7), (int)this.locY + this.random.nextInt(6) - 2, (int)this.locZ + this.random.nextInt(7) - this.random.nextInt(7));
	        }
	        double d1 = this.targetBlock.getX() + 0.5D - this.locX;
	        double d2 = this.targetBlock.getY() + 0.1D - this.locY;
	        double d3 = this.targetBlock.getZ() + 0.5D - this.locZ;

	        this.motX += (Math.signum(d1) * 0.5D - this.motX) * 0.10000000149011612D;
	        this.motY += (Math.signum(d2) * 0.699999988079071D - this.motY) * 0.10000000149011612D;
	        this.motZ += (Math.signum(d3) * 0.5D - this.motZ) * 0.10000000149011612D;

	        float f1 = (float)(MathHelper.c(this.motZ, this.motX) * 57.2957763671875D) - 90.0F;
	        float f2 = MathHelper.g(f1 - this.yaw);
	        this.bg = 0.5F;
	        this.yaw += f2;
	        if (targetPlayer == null && (this.random.nextInt(100) == 0) && (this.world.getType(localBlockPosition2).l())) {
	            setAsleep(true);
	        }
		}
	}

	public static boolean isBat(org.bukkit.entity.Entity e) {
		return (e instanceof Bat) && (((CraftBat)e).getHandle() instanceof VampireBat);
	}
}
