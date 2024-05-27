package net.additionz.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record ChunkLoaderPacket(BlockPos pos, int chunkId, boolean enableChunkLoading) implements CustomPayload {

    public static final CustomPayload.Id<ChunkLoaderPacket> PACKET_ID = new CustomPayload.Id<>(new Identifier("additionz", "chunk_loader_packet"));

    public static final PacketCodec<RegistryByteBuf, ChunkLoaderPacket> PACKET_CODEC = PacketCodec.of((value, buf) -> {
        buf.writeBlockPos(value.pos);
        buf.writeInt(value.chunkId);
        buf.writeBoolean(value.enableChunkLoading);
    }, buf -> new ChunkLoaderPacket(buf.readBlockPos(), buf.readInt(), buf.readBoolean()));

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
