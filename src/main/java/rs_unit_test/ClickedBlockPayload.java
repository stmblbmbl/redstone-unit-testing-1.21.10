package rs_unit_test;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.ClickType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record ClickedBlockPayload(BlockPos pos, ClickType click) implements CustomPayload {
    public static final CustomPayload.Id<ClickedBlockPayload> ID =
            new CustomPayload.Id<>(Identifier.of("rs_unit_test", "clicked_block_payload"));
    public static final PacketCodec<ByteBuf, ClickType> CLICK_TYPE_CODEC = PacketCodecs.indexed(
            index -> ClickType.values()[index],
            ClickType::ordinal
    );
    public static final PacketCodec<RegistryByteBuf, ClickedBlockPayload> CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC,
            ClickedBlockPayload::pos,
            ClickedBlockPayload.CLICK_TYPE_CODEC,
            ClickedBlockPayload::click,
            ClickedBlockPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}