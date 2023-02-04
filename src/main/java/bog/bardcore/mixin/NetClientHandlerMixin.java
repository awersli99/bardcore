package bog.bardcore.mixin;

import bog.bardcore.Bardcore;
import net.minecraft.client.Minecraft;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.Packet41EntityPlayerGamemode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NetClientHandler.class, remap = false)
public class NetClientHandlerMixin {

    @Shadow
    private
    Minecraft mc;

    @Inject(method = "handleEntityPlayerGamemode", at = @At("HEAD"), cancellable = true)
    private void injector(Packet41EntityPlayerGamemode packet, CallbackInfo ci) {
        mc.thePlayer.setGamemode(Bardcore.gamemodesList[packet.gamemodeId]);
        ci.cancel();
    }
}
