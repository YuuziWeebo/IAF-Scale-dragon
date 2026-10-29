package dev.community.tameddragonscaler;

import dev.community.tameddragonscaler.network.ScaleChangePayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(TamedDragonScaler.MOD_ID)
public final class TamedDragonScaler {
    public static final String MOD_ID = "tameddragonscaler";

    public TamedDragonScaler(IEventBus modBus) {
        modBus.addListener(TamedDragonScaler::registerPayloads);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToServer(
                ScaleChangePayload.TYPE,
                ScaleChangePayload.STREAM_CODEC,
                ScaleChangePayload::handle
        );
    }
}
