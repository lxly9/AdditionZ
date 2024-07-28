package net.additionz.network;

import net.additionz.block.entity.ChunkLoaderEntity;
import net.additionz.network.packet.ChunkLoaderBlockPacket;
import net.additionz.network.packet.ChunkLoaderPacket;
import net.additionz.network.packet.ElytraPacket;
import net.additionz.network.packet.ExperiencePacket;
import net.additionz.network.packet.StampedePacket;
import net.additionz.network.packet.TotemPacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class AdditionServerPacket {

    public static void init() {
        PayloadTypeRegistry.playC2S().register(StampedePacket.PACKET_ID, StampedePacket.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(ExperiencePacket.PACKET_ID, ExperiencePacket.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(ChunkLoaderPacket.PACKET_ID, ChunkLoaderPacket.PACKET_CODEC);

        PayloadTypeRegistry.playS2C().register(TotemPacket.PACKET_ID, TotemPacket.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(ElytraPacket.PACKET_ID, ElytraPacket.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(ChunkLoaderBlockPacket.PACKET_ID, ChunkLoaderBlockPacket.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(StampedePacket.PACKET_ID, (payload, context) -> {
            int entityId = payload.entityId();
            int enchantmentLevel = payload.enchantmentLevel();
            boolean offhand = payload.offhand();
                context.server().execute(() -> {
                context.player().getWorld().getEntityById(entityId).damage(context.player().getDamageSources().playerAttack(context.player()), (float) enchantmentLevel * 2.0F);
                ((LivingEntity) context.player().getWorld().getEntityById(entityId)).takeKnockback((float) enchantmentLevel * 0.5f,
                        MathHelper.sin(context.player().getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(context.player().getYaw() * ((float) Math.PI / 180)));
                if (!context.player().isCreative()) {
                    if (offhand) {
                        context.player().getOffHandStack().damage(1, context.player(), EquipmentSlot.OFFHAND);
                    } else {
                        context.player().getMainHandStack().damage(1, context.player(), EquipmentSlot.MAINHAND);
                    }
                }
            });
        });
        ServerPlayNetworking.registerGlobalReceiver(ExperiencePacket.PACKET_ID, (payload, context) -> {
            int amount = payload.amount();
                context.server().execute(() -> {
                if (!context.player().isCreative()) {
                    context.player().addExperience(-amount);
                }
            });
        });
        ServerPlayNetworking.registerGlobalReceiver(ChunkLoaderPacket.PACKET_ID, (payload, context) -> {
            BlockPos pos = payload.pos();
            int chunkId = payload.chunkId();
            boolean enableChunkLoading = payload.enableChunkLoading();
            context.player().server.execute(() -> {
                if (context.player().getWorld().getBlockEntity(pos) instanceof ChunkLoaderEntity chunkLoaderEntity) {
                    // Check if it is owner, also in screen too

                    if (enableChunkLoading) {
                        if (chunkLoaderEntity.getMaxChunksLoaded() > chunkLoaderEntity.getChunkList().size()
                                && !ChunkLoaderEntity.isChunkForceLoaded(context.player().getServerWorld(), ChunkLoaderEntity.getChunkLoaderChunkPos(pos, chunkId))) {
                            chunkLoaderEntity.addChunk(chunkId);
                            ChunkLoaderEntity.updateChunkLoaderChunk(context.player().getServerWorld(), pos, chunkId, enableChunkLoading);
                        }
                    } else if (ChunkLoaderEntity.isChunkLoadedByChunkLoader(chunkLoaderEntity, ChunkLoaderEntity.getChunkLoaderChunkPos(pos, chunkId))) {
                        chunkLoaderEntity.removeChunk(chunkId);
                        ChunkLoaderEntity.updateChunkLoaderChunk(context.player().getServerWorld(), pos, chunkId, enableChunkLoading);
                    }
                }
            });
        });
    }
}
