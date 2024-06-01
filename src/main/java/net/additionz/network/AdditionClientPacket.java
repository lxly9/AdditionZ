package net.additionz.network;

import net.additionz.AdditionMain;
import net.additionz.access.ElytraAccess;
import net.additionz.network.packet.ElytraPacket;
import net.additionz.network.packet.TotemPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;

public class AdditionClientPacket {

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(TotemPacket.PACKET_ID, (payload, context) -> {
            context.client().execute(() -> {
                MinecraftClient client = context.client();
                client.particleManager.addEmitter(client.player, ParticleTypes.TOTEM_OF_UNDYING, 30);
                client.world.playSound(client.player.getX(), client.player.getY(), client.player.getZ(), SoundEvents.ITEM_TOTEM_USE, client.player.getSoundCategory(), 1.0f, 1.0f, false);
                client.gameRenderer.showFloatingItem(new ItemStack(AdditionMain.TOTEM_OF_NON_BREAKING));
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(ElytraPacket.PACKET_ID, (payload, context) -> {
            int disableElytraTime = payload.disableElytraTime();
            context.client().execute(() -> {
                MinecraftClient client = context.client();
                client.player.getItemCooldownManager().set(Items.ELYTRA, 100);
                ((ElytraAccess) client.player).setElytraDisablingTime(disableElytraTime);
            });
        });
    }

}
