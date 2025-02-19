package net.additionz.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.additionz.AdditionMain;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends HostileEntity {

    @Shadow
    private static TrackedData<Boolean> CHARGED;
    @Shadow
    private int explosionRadius;

    public CreeperEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData) {
        if (AdditionMain.CONFIG.charged_creeper_spawn_chance > 0.001F && world.getRandom().nextFloat() <= AdditionMain.CONFIG.charged_creeper_spawn_chance) {
            this.dataTracker.set(CHARGED, true);
        }
        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Inject(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/world/World$ExplosionSourceType;)Lnet/minecraft/world/explosion/Explosion;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void explode(CallbackInfo info, float f) {
        if (AdditionMain.CONFIG.creeper_on_fire && this.isOnFire()) {
            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), (float) this.explosionRadius * f, true, World.ExplosionSourceType.MOB);
            this.discard();
            this.spawnEffectsCloud();
            info.cancel();
        }
    }

    @Shadow
    private void spawnEffectsCloud() {
    }

}
