package net.additionz.network.packet;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record StampedePacket(int entityId, int enchantmentLevel, boolean offhand) implements CustomPayload {

    public static final CustomPayload.Id<StampedePacket> PACKET_ID = new CustomPayload.Id<>(Identifier.of("additionz", "stampede_packet"));

    public static final PacketCodec<RegistryByteBuf, StampedePacket> PACKET_CODEC = PacketCodec.of((value, buf) -> {
        buf.writeInt(value.entityId);
        buf.writeInt(value.enchantmentLevel);
        buf.writeBoolean(value.offhand);
    }, buf -> new StampedePacket(buf.readInt(), buf.readInt(), buf.readBoolean()));

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}
