package bog.bardcore.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.src.PlayerController;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value= PlayerController.class, remap = false)
public class PlayerControllerMixin {

    @Final
    @Shadow
    protected
    Minecraft mc;

    @Inject(method="destroyBlock", at = @At(value = "HEAD"), cancellable = true)
    protected void injectGamemodeCheck(int x, int y, int z, int side, CallbackInfoReturnable<Boolean> cir) {
        if (mc.thePlayer.getGamemode().id == 3) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

}
