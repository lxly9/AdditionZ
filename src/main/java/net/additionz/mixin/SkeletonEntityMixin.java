package net.additionz.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.additionz.AdditionMain;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.BowItem;
import net.minecraft.world.World;

@Mixin(SkeletonEntity.class)
public abstract class SkeletonEntityMixin extends AbstractSkeletonEntity {

    public SkeletonEntityMixin(EntityType<? extends AbstractSkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void shootAt(LivingEntity target, float pullProgress) {
        if (this.getMainHandStack().getItem() instanceof BowItem bowItem) {
            if (AdditionMain.CONFIG.skeleton_bow_damaged)
                this.getMainHandStack().damage(1, this, LivingEntity.getSlotForHand(ProjectileUtil.getHandPossiblyHolding(this, bowItem)));
            if (AdditionMain.CONFIG.break_skeleton_bow_chance > 0.001F && this.getWorld().getRandom().nextFloat() <= AdditionMain.CONFIG.break_skeleton_bow_chance) {
                this.getMainHandStack().damage(this.getMainHandStack().getMaxDamage(), this, LivingEntity.getSlotForHand(ProjectileUtil.getHandPossiblyHolding(this, bowItem)));
                this.updateAttackType();
            }
        }
        super.shootAt(target, pullProgress);
    }

}
