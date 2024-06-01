package net.additionz.network.packet;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record ChunkLoaderBlockPacket(BlockPos pos, boolean active, int burnTime, IntList chunkList, IntList existingForcedChunkIds) implements CustomPayload {

    public static final CustomPayload.Id<ChunkLoaderBlockPacket> PACKET_ID = new CustomPayload.Id<>(new Identifier("additionz", "chunk_loader_block_packet"));

    public static final PacketCodec<RegistryByteBuf, ChunkLoaderBlockPacket> PACKET_CODEC = PacketCodec.of((value, buf) -> {
        buf.writeBlockPos(value.pos);
        buf.writeBoolean(value.active);
        buf.writeInt(value.burnTime);
        buf.writeIntList(value.chunkList);
        buf.writeIntList(value.existingForcedChunkIds);
    }, buf -> new ChunkLoaderBlockPacket(buf.readBlockPos(), buf.readBoolean(), buf.readInt(), buf.readIntList(), buf.readIntList()));

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
