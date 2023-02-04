package bog.bardcore.mixin;

import bog.bardcore.Bardcore;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityPlayer.class, remap = false)
public abstract class EntityPlayerMixin {

    @Shadow public abstract void setGamemodeOnLogin(Gamemode gamemode);
    @Shadow
    protected boolean sleeping;
    @Shadow private ChunkCoordinates playerSpawnCoordinate;
    @Shadow
    protected Gamemode gamemode;

    @Inject(method="readEntityFromNBT", at = @At(value = "INVOKE", target = "net/minecraft/src/EntityPlayer.setGamemodeOnLogin (Lnet/minecraft/src/Gamemode;)V", shift = At.Shift.BEFORE), cancellable = true)
    private void injected(NBTTagCompound nbttagcompound, CallbackInfo ci) {
        setGamemodeOnLogin(Bardcore.gamemodesList[nbttagcompound.getInteger("Gamemode")]);
        ((EntityPlayer)(Object)this).noClip = nbttagcompound.getBoolean("Noclip") && gamemode.canPlayerFly;
        if (sleeping) {
            ((EntityPlayer)(Object)this).bedChunkCoordinates = new ChunkCoordinates(MathHelper.floor_double(((EntityPlayer)(Object)this).posX), MathHelper.floor_double(((EntityPlayer)(Object)this).posY), MathHelper.floor_double(((EntityPlayer)(Object)this).posZ));
            ((EntityPlayer)(Object)this).wakeUpPlayer(true, true);
        }

        if (nbttagcompound.hasKey("SpawnX") && nbttagcompound.hasKey("SpawnY") && nbttagcompound.hasKey("SpawnZ")) {
            playerSpawnCoordinate = new ChunkCoordinates(nbttagcompound.getInteger("SpawnX"), nbttagcompound.getInteger("SpawnY"), nbttagcompound.getInteger("SpawnZ"));
        }
        ci.cancel();
    }

}
