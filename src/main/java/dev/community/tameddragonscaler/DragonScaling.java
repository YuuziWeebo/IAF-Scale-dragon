package dev.community.tameddragonscaler;

import com.iafenvoy.iceandfire.entity.DragonBaseEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;

/**
 * Uses vanilla SCALE attribute for save data, syncing and visual scaling.
 * Wild dragons always use 1.0, and untouched tame dragons use vanilla's default 1.0.
 */
public final class DragonScaling {
    public static final int MIN_PERCENT = 10;
    public static final int MAX_PERCENT = 120;
    public static final int STEP_PERCENT = 5;
    private DragonScaling() { }

    public static float factor(DragonBaseEntity dragon) {
        if (!dragon.isTame()) return 1.0F;
        return (float) dragon.getScale();
    }

    public static int percent(DragonBaseEntity dragon) {
        return Math.round(factor(dragon) * 100.0F);
    }

    public static boolean validPercent(int percent) {
        return percent >= MIN_PERCENT && percent <= MAX_PERCENT
                && percent % STEP_PERCENT == 0;
    }

    /** Called only after server-side auth and menu checks. */
    public static boolean setPercent(DragonBaseEntity dragon, int percent) {
        if (!dragon.isTame() || !validPercent(percent)) return false;
        AttributeInstance scale = dragon.getAttribute(Attributes.SCALE);
        if (scale == null) return false;
        scale.setBaseValue(percent / 100.0D);
        dragon.refreshDimensions();
        // Force existing head/neck/wing/tail parts to resize without waiting for an age change.
        dragon.updateScale(dragon.getRenderSize() / 3.0F);
        dragon.updateParts();
        return true;
    }
}
