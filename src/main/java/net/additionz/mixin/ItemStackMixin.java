package net.additionz.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.additionz.AdditionMain;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.Random;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/lang/Runnable;)V", at = @At(value = "INVOKE", target = "Ljava/lang/Runnable;run()V"), cancellable = true)
    private <T extends LivingEntity> void damage(int amount, Random random, @Nullable ServerPlayerEntity player, Runnable breakCallback, CallbackInfo info) {
        if (player != null && AdditionMain.tryUseTotemOfNonBreaking(player, (ItemStack) (Object) this)) {
            info.cancel();
        }
    }
}
