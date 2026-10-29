package dev.community.tameddragonscaler.mixin;

import com.iafenvoy.iceandfire.entity.DragonBaseEntity;
import dev.community.tameddragonscaler.DragonScaling;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * CE computes the body dimensions from dragon age and bypasses vanilla SCALE there.
 * Vanilla LivingEntityRenderer already multiplies model matrices by SCALE, so DO NOT
 * apply a second scale to the renderer.
 */
@Mixin(DragonBaseEntity.class)
public abstract class DragonBaseEntityMixin {
    @Inject(method = "getDimensions", at = @At("RETURN"), cancellable = true)
    private void tds$scaleBody(Pose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        DragonBaseEntity dragon = (DragonBaseEntity) (Object) this;
        float factor = DragonScaling.factor(dragon);
        if (factor != 1.0F) {
            cir.setReturnValue(cir.getReturnValue().scale(factor));
        }
    }

    /**
     * CE ordinarily refreshes multipart size only when age changes. Incorporating
     * the scale into its change detector also refreshes parts when the SCALE
     * attribute is synchronized to another client.
     */
    @ModifyVariable(method = "refreshDimensions", at = @At("STORE"), ordinal = 0)
    private float tds$trackChangedScale(float ageScale) {
        return ageScale * DragonScaling.factor((DragonBaseEntity) (Object) this);
    }

    @Inject(method = "getRiderPosition", at = @At("RETURN"), cancellable = true)
    private void tds$scaleRidingOffset(CallbackInfoReturnable<Vec3> cir) {
        tds$scaleRelativeOffset(cir);
    }

    @Inject(method = "getHeadPosition", at = @At("RETURN"), cancellable = true)
    private void tds$scaleBreathOrigin(CallbackInfoReturnable<Vec3> cir) {
        tds$scaleRelativeOffset(cir);
    }

    private void tds$scaleRelativeOffset(CallbackInfoReturnable<Vec3> cir) {
        DragonBaseEntity dragon = (DragonBaseEntity) (Object) this;
        float scale = DragonScaling.factor(dragon);
        if (scale == 1.0F) return;
        Vec3 origin = dragon.position();
        cir.setReturnValue(origin.add(cir.getReturnValue().subtract(origin).scale(scale)));
    }
}
