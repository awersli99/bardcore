package bog.bardcore.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiIngame;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiIngame.class, remap = false)
public class GuiIngameMixin {

    @Shadow
    private
    Minecraft mc;
    @Inject(method="renderGameOverlay", at = @At(value = "INVOKE", target = "org/lwjgl/opengl/GL11.glBindTexture (II)V", shift = At.Shift.AFTER, ordinal = 1))
    protected void injectHeartTextures(float partialTicks, boolean flag, int mouseX, int mouseY, CallbackInfo ci) {
        if (mc.thePlayer.getGamemode().id == 2)
            GL11.glBindTexture(3553, mc.renderEngine.getTexture("/assets/bardcore/gui/icons.png"));
    }
}
