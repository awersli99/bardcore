package bog.bardcore.mixin;

import bog.bardcore.Bardcore;
import net.minecraft.src.Gamemode;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiCreateWorld;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = GuiCreateWorld.class, remap = false)
public class GuiCreateWorldMixin {

    Gamemode selectedGamemode;

    @Redirect(method = "actionPerformed", at = @At(value = "FIELD", target = "net/minecraft/src/GuiCreateWorld.selectedGamemode : Lnet/minecraft/src/Gamemode;", opcode = Opcodes.PUTFIELD))
    protected void gamemodeInjector(GuiCreateWorld guiCreateWorld, Gamemode unused) {
        guiCreateWorld.selectedGamemode = Bardcore.gamemodesList[(guiCreateWorld.selectedGamemode.id + 1) % Bardcore.gamemodesList.length];
        selectedGamemode = guiCreateWorld.selectedGamemode;
    }

    @Redirect(method = "actionPerformed", at = @At(value = "FIELD", target = "net/minecraft/src/GuiButton.displayString : Ljava/lang/String;", opcode = Opcodes.PUTFIELD, ordinal = 0))
    protected void gamemodeNameInjector(GuiButton instance, String value) {
        // LOL
        String gamemodeString = selectedGamemode.languageKey.substring(9);
        instance.displayString = gamemodeString.substring(0, 1).toUpperCase() + gamemodeString.substring(1);
    }
}

