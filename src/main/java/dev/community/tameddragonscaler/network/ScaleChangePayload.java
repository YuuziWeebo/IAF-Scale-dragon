package dev.community.tameddragonscaler.network;

import com.iafenvoy.iceandfire.entity.DragonBaseEntity;
import com.iafenvoy.iceandfire.screen.menu.DragonMenu;
import dev.community.tameddragonscaler.DragonScaling;
import dev.community.tameddragonscaler.TamedDragonScaler;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

/**
 * Security: never trust an incoming dragon ID or scale. Only the owner with the
 * matching dragon inventory currently open can update it on the server.
 */
public record ScaleChangePayload(int dragonId, int percent) implements CustomPacketPayload {
    public static final Type<ScaleChangePayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(TamedDragonScaler.MOD_ID, "set_scale")
    );
    public static final StreamCodec<ByteBuf, ScaleChangePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ScaleChangePayload::dragonId,
            ByteBufCodecs.VAR_INT, ScaleChangePayload::percent,
            ScaleChangePayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ScaleChangePayload packet, IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer player)) return;
        if (!DragonScaling.validPercent(packet.percent())) return;
        if (!(player.containerMenu instanceof DragonMenu dragonMenu)) return;
        if (!dragonMenu.stillValid(player)) return;
        DragonBaseEntity dragon = dragonMenu.getDragon();
        if (dragon == null || !dragon.isAlive() || !dragon.isTame()) return;
        if (dragon.getId() != packet.dragonId()) return;
        if (!dragon.isOwnedBy(player)) return;
        if (dragon.level() != player.level()) return;
        DragonScaling.setPercent(dragon, packet.percent());
    }
}
