package me.bukkit.critikull.VampireBats.entity.pathfinder;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import me.bukkit.critikull.VampireBats.entity.EntityUtil;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityCreature;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.IEntitySelector;
import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.NavigationAbstract;
import net.minecraft.server.v1_12_R1.PathEntity;
import net.minecraft.server.v1_12_R1.PathfinderGoal;
import net.minecraft.server.v1_12_R1.RandomPositionGenerator;
import net.minecraft.server.v1_12_R1.Vec3D;

public class PathfinderGoalAvoidPlayerItem extends PathfinderGoal {
	private final Predicate<Entity> c = new Predicate<Entity>() {
		@Override
		public boolean apply(@Nullable Entity paramAnonymousEntity) {
			return (paramAnonymousEntity.isAlive())
					&& (PathfinderGoalAvoidPlayerItem.this.a.getEntitySenses().a(paramAnonymousEntity))
					&& (!PathfinderGoalAvoidPlayerItem.this.a.r(paramAnonymousEntity));
		}
	};

	protected EntityCreature a;
	private final double d;
	private final double e;
	protected EntityHuman b;
	private final float f;
	private PathEntity g;
	private final NavigationAbstract h;
	private final Predicate<? super EntityHuman> j;
	private final Item k;
	private final String l;

	public PathfinderGoalAvoidPlayerItem(EntityCreature paramEntityCreature, Item item, float paramFloat,
			double paramDouble1, double paramDouble2) {
		this(paramEntityCreature, item, null, paramFloat, paramDouble1, paramDouble2);
	}

	public PathfinderGoalAvoidPlayerItem(EntityCreature paramEntityCreature, Item item, String itemName,
			float paramFloat, double paramDouble1, double paramDouble2) {
		this(paramEntityCreature, Predicates.alwaysTrue(), item, itemName, paramFloat, paramDouble1, paramDouble2);
	}

	public PathfinderGoalAvoidPlayerItem(EntityCreature paramEntityCreature,
			Predicate<? super EntityHuman> paramPredicate, Item item, String itemName, float paramFloat,
			double paramDouble1, double paramDouble2) {
		this.a = paramEntityCreature;
		this.j = paramPredicate;
		this.f = paramFloat;
		this.d = paramDouble1;
		this.e = paramDouble2;
		this.h = paramEntityCreature.getNavigation();
		this.k = item;
		this.l = itemName;
		a(1);
	}

	@Override
	public boolean a() {
		@SuppressWarnings("unchecked")
		List<EntityHuman> localList = this.a.world.a(EntityHuman.class,
				this.a.getBoundingBox().grow(this.f, 3.0D, this.f),
				Predicates.and(new Predicate[] { IEntitySelector.d, this.c, this.j }));

		if (localList.isEmpty()) {
			return false;
		}

		if (!EntityUtil.isPlayerHolding(localList.get(0), this.k, this.l)) {
			return false;
		}

		this.b = localList.get(0);

		Vec3D localVec3D = RandomPositionGenerator.b(this.a, 16, 7, new Vec3D(this.b.locX, this.b.locY, this.b.locZ));
		if (localVec3D == null) {
			return false;
		}
		if (this.b.d(localVec3D.x, localVec3D.y, localVec3D.z) < this.b.h(this.a)) {
			return false;
		}
		this.g = this.h.a(localVec3D.x, localVec3D.y, localVec3D.z);
		return this.g != null;
	}

	@Override
	public boolean b() {
		return !this.h.o();
	}

	@Override
	public void c() {
		this.h.a(this.g, this.d);
	}

	@Override
	public void d() {
		this.b = null;
	}

	@Override
	public void e() {
		if (this.a.h(this.b) < 49.0D) {
			this.a.getNavigation().a(this.e);
		} else {
			this.a.getNavigation().a(this.d);
		}
	}
}