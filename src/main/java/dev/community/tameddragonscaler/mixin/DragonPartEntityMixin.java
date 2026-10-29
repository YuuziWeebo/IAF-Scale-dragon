package dev.community.tameddragonscaler.mixin;

import com.iafenvoy.iceandfire.entity.DragonPartEntity;
import dev.community.tameddragonscaler.DragonScaling;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** CE's multipart updateScale controls each part's radius, Y offset, width and height. */
@Mixin(DragonPartEntity.class)
public abstract class DragonPartEntityMixin {
    @ModifyVariable(method = "updateScale", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float tds$scaleMultipart(float original) {
        DragonPartEntity part = (DragonPartEntity) (Object) this;
        return original * DragonScaling.factor(part.getParent());
    }
}
