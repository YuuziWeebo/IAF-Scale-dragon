package dev.community.tameddragonscaler.client;

import com.iafenvoy.iceandfire.entity.DragonBaseEntity;
import dev.community.tameddragonscaler.DragonScaling;
import dev.community.tameddragonscaler.network.ScaleChangePayload;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.network.PacketDistributor;

/** Preview the number locally; send one serverbound packet when dragging stops. */
public final class DragonScaleSlider extends AbstractSliderButton {
    private final DragonBaseEntity dragon;
    private int lastSent;

    public DragonScaleSlider(int x, int y, int width, DragonBaseEntity dragon) {
        super(x, y, width, 20, Component.empty(), sliderValue(DragonScaling.percent(dragon)));
        this.dragon = dragon;
        this.lastSent = DragonScaling.percent(dragon);
        this.updateMessage();
    }

    private static double sliderValue(int percent) {
        return Mth.clamp((percent - DragonScaling.MIN_PERCENT) /
                (double) (DragonScaling.MAX_PERCENT - DragonScaling.MIN_PERCENT), 0.0D, 1.0D);
    }

    private int currentPercent() {
        int steps = (DragonScaling.MAX_PERCENT - DragonScaling.MIN_PERCENT) / DragonScaling.STEP_PERCENT;
        return DragonScaling.MIN_PERCENT + (int) Math.round(value * steps) * DragonScaling.STEP_PERCENT;
    }

    @Override
    protected void updateMessage() {
        setMessage(Component.translatable("tameddragonscaler.size", currentPercent()));
    }

    @Override
    protected void applyValue() {
        // Intentionally no network request on every drag/step.
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.onRelease(mouseX, mouseY);
        sendIfChanged();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean handled = super.keyPressed(keyCode, scanCode, modifiers);
        if (handled) sendIfChanged();
        return handled;
    }

    public void reset() {
        this.value = sliderValue(100);
        updateMessage();
        sendIfChanged();
    }

    private void sendIfChanged() {
        int current = currentPercent();
        if (current != lastSent && DragonScaling.validPercent(current)) {
            PacketDistributor.sendToServer(new ScaleChangePayload(dragon.getId(), current));
            lastSent = current;
        }
    }
}
