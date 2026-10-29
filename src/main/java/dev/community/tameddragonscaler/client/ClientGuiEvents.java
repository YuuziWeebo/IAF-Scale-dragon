package dev.community.tameddragonscaler.client;

import com.iafenvoy.iceandfire.entity.DragonBaseEntity;
import com.iafenvoy.iceandfire.screen.gui.DragonScreen;
import dev.community.tameddragonscaler.DragonScaling;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

/** Add widgets to CE's existing inventory rather than replacing the screen. */
@EventBusSubscriber(Dist.CLIENT)
public final class ClientGuiEvents {
    private ClientGuiEvents() { }

    @SubscribeEvent
    public static void onInit(ScreenEvent.Init.Post event) {
        if (!(event.getScreen() instanceof DragonScreen screen)) return;
        if (Minecraft.getInstance().player == null) return;
        DragonBaseEntity dragon = screen.getMenu().getDragon();
        if (dragon == null || !dragon.isTame() || !dragon.isOwnedBy(Minecraft.getInstance().player)) return;

        final int guiLeft = (screen.width - 176) / 2;
        final int guiTop = (screen.height - 214) / 2;
        final int widgetWidth = 116;
        final int widgetX;
        final int widgetY;

        if (screen.width - (guiLeft + 176) >= widgetWidth + 8) {
            widgetX = guiLeft + 182;
            widgetY = guiTop + 28;
        } else if (guiLeft >= widgetWidth + 8) {
            widgetX = guiLeft - widgetWidth - 6;
            widgetY = guiTop + 28;
        } else {
            // On narrow GUI scales, show the controls above the normal inventory.
            widgetX = Math.max(2, screen.width / 2 - widgetWidth / 2);
            widgetY = Math.max(2, guiTop - 43);
        }

        DragonScaleSlider slider = new DragonScaleSlider(widgetX, widgetY, widgetWidth, dragon);
        event.addListener(slider);
        event.addListener(Button.builder(Component.translatable("tameddragonscaler.reset"), button -> slider.reset())
                .bounds(widgetX, widgetY + 23, widgetWidth, 20)
                .build());
    }
}
