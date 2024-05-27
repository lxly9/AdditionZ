package net.additionz.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ExperiencePacket(int amount) implements CustomPayload {

    public static final CustomPayload.Id<ExperiencePacket> PACKET_ID = new CustomPayload.Id<>(new Identifier("additionz", "experience_packet"));

    public static final PacketCodec<RegistryByteBuf, ExperiencePacket> PACKET_CODEC = PacketCodec.of((value, buf) -> {
        buf.writeInt(value.amount);
    }, buf -> new ExperiencePacket(buf.readInt()));

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}
