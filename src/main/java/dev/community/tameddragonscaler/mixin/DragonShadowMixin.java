package dev.community.tameddragonscaler.mixin;

import com.iafenvoy.iceandfire.entity.DragonBaseEntity;
import dev.community.tameddragonscaler.DragonScaling;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class DragonShadowMixin {
    @Inject(
        method = "getShadowRadius(Lnet/minecraft/world/entity/LivingEntity;)F",
        at = @At("RETURN"),
        cancellable = true
    )
    private void tds$resizeTamedDragonShadow(
        LivingEntity entity,
        CallbackInfoReturnable<Float> cir
    ) {
        if (!(entity instanceof DragonBaseEntity dragon)
                || !dragon.isTame()) return;

        float scale = DragonScaling.factor(dragon);

        if (scale < 1.0F) {
            cir.setReturnValue(cir.getReturnValueF() * scale);
        }
    }
}