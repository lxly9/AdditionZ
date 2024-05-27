package net.additionz.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ElytraPacket(int disableElytraTime) implements CustomPayload {

    public static final CustomPayload.Id<ElytraPacket> PACKET_ID = new CustomPayload.Id<>(new Identifier("additionz", "elytra_packet"));

    public static final PacketCodec<RegistryByteBuf, ElytraPacket> PACKET_CODEC = PacketCodec.of((value, buf) -> {
        buf.writeInt(value.disableElytraTime);
    }, buf -> new ElytraPacket(buf.readInt()));

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}
