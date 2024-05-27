package net.additionz.network;

import io.netty.buffer.Unpooled;
import net.additionz.AdditionMain;
import net.additionz.access.ElytraAccess;
import net.additionz.block.entity.ChunkLoaderEntity;
import net.additionz.network.packet.ChunkLoaderPacket;
import net.additionz.network.packet.ExperiencePacket;
import net.additionz.network.packet.StampedePacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class AdditionServerPacket {

    public static final Identifier STAMPEDE_DAMAGE_PACKET = new Identifier("additionz", "stampede_damage");
    public static final Identifier TOTEM_OF_NON_BREAKING_PACKET = new Identifier("additionz", "totem_of_non_breaking");
    public static final Identifier CONSUME_EXPERIENCE_PACKET = new Identifier("additionz", "consume_experience");
    public static final Identifier ELYTRA_DISABLING_PACKET = new Identifier("additionz", "elytra_disabling");
    public static final Identifier CHUNK_LOADER_PACKET = new Identifier("additionz", "chunk_loader");

    public static void init() {
        // PayloadTypeRegistry.playS2C().register(TravelerPacket.PACKET_ID, TravelerPacket.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(StampedePacket.PACKET_ID, StampedePacket.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(ExperiencePacket.PACKET_ID, ExperiencePacket.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(ChunkLoaderPacket.PACKET_ID, ChunkLoaderPacket.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(StampedePacket.PACKET_ID, (payload, context) -> {
            int entityId = payload.entityId();
            int enchantmentLevel = payload.enchantmentLevel();
            boolean offhand = payload.offhand();
            context.player().server.execute(() -> {
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
            context.player().server.execute(() -> {
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

    // public static void writeS2CTotemOfNonBreakingPacket(ServerPlayerEntity serverPlayerEntity) {
    //     PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
    //     CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(TOTEM_OF_NON_BREAKING_PACKET, buf);
    //     serverPlayerEntity.networkHandler.sendPacket(packet);
    // }

    // public static void writeS2CElytraDisablingPacket(ServerPlayerEntity serverPlayerEntity) {
    //     if (AdditionMain.CONFIG.disable_elytra_on_damage_time > 0) {
    //         serverPlayerEntity.getItemCooldownManager().set(Items.ELYTRA, AdditionMain.CONFIG.disable_elytra_on_damage_time);
    //         ((ElytraAccess) serverPlayerEntity).setElytraDisablingTime(AdditionMain.CONFIG.disable_elytra_on_damage_time);
    //         serverPlayerEntity.stopFallFlying();

    //         PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
    //         buf.writeInt(AdditionMain.CONFIG.disable_elytra_on_damage_time);
    //         CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(ELYTRA_DISABLING_PACKET, buf);
    //         serverPlayerEntity.networkHandler.sendPacket(packet);
    //     }
    // }
}
