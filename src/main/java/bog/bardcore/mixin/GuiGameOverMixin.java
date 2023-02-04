package bog.bardcore.mixin;

import bog.bardcore.Bardcore;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiGameOver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiGameOver.class, remap = false)
public class GuiGameOverMixin {


    @Inject(method="initGui", at = @At(value = "HEAD"), cancellable = true)
    protected void injectSpectateButton(CallbackInfo ci) {
        if (((GuiGameOver)(Object)this).mc.thePlayer.getGamemode().id == 2) {
            ((GuiGameOver) (Object) this).controlList.clear();
            ((GuiGameOver) (Object) this).controlList.add(new GuiButton(1, ((GuiGameOver) (Object) this).width / 2 - 100, ((GuiGameOver) (Object) this).height / 4 + 72, "Spectate"));
            //((GuiGameOver) (Object) this).controlList.add(new GuiButton(2, ((GuiGameOver) (Object) this).width / 2 - 100, ((GuiGameOver) (Object) this).height / 4 + 96, "Title menu"));
            // TODO: Fix the crash when leaving and dead
            if (((GuiGameOver) (Object) this).mc.session == null) {
                (((GuiGameOver) (Object) this).controlList.get(1)).enabled = false;
            }
            ci.cancel();
        }
    }

    @Inject(method="actionPerformed", at = @At(value="INVOKE", target = "net/minecraft/src/EntityPlayerSP.respawnPlayer ()V", shift = At.Shift.AFTER))
    protected void injectGamemodeChangeOnRespawn(GuiButton guibutton, CallbackInfo ci) {
        if (((GuiGameOver)(Object)this).mc.thePlayer.getGamemode().id == 2) {
            // change gamemode to spectator when respawning in hardcore
            ((GuiGameOver) (Object) this).mc.thePlayer.setGamemode(Bardcore.gamemodesList[3]);
        }
    }

    @Inject(method="actionPerformed", at = @At(value="INVOKE", target = "net/minecraft/client/Minecraft.changeWorld1 (Lnet/minecraft/src/World;)V", shift = At.Shift.BEFORE))
    protected void injectGamemodeChangeOnLeaveWorld(GuiButton guibutton, CallbackInfo ci) {
        if (((GuiGameOver)(Object)this).mc.thePlayer.getGamemode().id == 2) {
            // change gamemode to spectator when leaving world after dying in hardcore
            ((GuiGameOver) (Object) this).mc.thePlayer.setGamemode(Bardcore.gamemodesList[3]);
            ((GuiGameOver) (Object) this).mc.thePlayer.isDead = false;
        }
    }

}
