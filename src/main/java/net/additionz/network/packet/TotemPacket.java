package net.additionz.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record TotemPacket() implements CustomPayload {

    public static final CustomPayload.Id<TotemPacket> PACKET_ID = new CustomPayload.Id<>(new Identifier("additionz", "totem_packet"));

    public static final PacketCodec<RegistryByteBuf, TotemPacket> PACKET_CODEC = PacketCodec.of((value, buf) -> {
    }, buf -> new TotemPacket());

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}
