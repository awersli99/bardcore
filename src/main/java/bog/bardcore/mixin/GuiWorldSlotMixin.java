package bog.bardcore.mixin;

import bog.bardcore.Bardcore;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Date;

@Mixin(targets = "net.minecraft.src.GuiWorldSlot", remap = false)
public class GuiWorldSlotMixin {

    @Final
    @Shadow GuiSelectWorld parentWorldGui;

    @Inject(method="drawSlot", at=@At("HEAD"), cancellable = true)
    private void inject(int i, int j, int k, int l, Tessellator tessellator, CallbackInfo ci) {
        SaveFormatComparator saveformatcomparator = (SaveFormatComparator) GuiSelectWorld.getSize(this.parentWorldGui).get(i);
        String s = saveformatcomparator.getDisplayName();
        if (s == null || MathHelper.stringNullOrLengthZero(s)) {
            s = GuiSelectWorld.func_22087_f(this.parentWorldGui) + " " + (i + 1);
        }
        String s1 = saveformatcomparator.getFileName();
        s1 = s1 + " (" + GuiSelectWorld.getDateFormatter(this.parentWorldGui).format(new Date(saveformatcomparator.func_22163_e()));
        long l1 = saveformatcomparator.func_22159_c();
        s1 = s1 + ", " + (float)(l1 / 1024L * 100L / 1024L) / 100.0f + " MB)";
        String s2 = "";
        if (saveformatcomparator.func_22161_d()) {
            s2 = GuiSelectWorld.func_22088_h(this.parentWorldGui) + " " + s2;
        } else {
            WorldInfo wi = saveformatcomparator.getWorldInfo();
            StringTranslate st = StringTranslate.getInstance();
            s2 = s2 + st.translateNamedKey(Bardcore.gamemodesList[wi.getGamemode()].languageKey);
            if (wi.getCheatsEnabled()) {
                s2 = s2 + " | Cheats";
            }
            s2 = s2 + " | " + st.translateNamedKey(WorldType.worldTypes[wi.getWorldType(0)].languageKey);
        }
        this.parentWorldGui.drawString(this.parentWorldGui.fontRenderer, s, j + 2, k + 1, 0xFFFFFF);
        this.parentWorldGui.drawString(this.parentWorldGui.fontRenderer, s1, j + 2, k + 12, 0x808080);
        this.parentWorldGui.drawString(this.parentWorldGui.fontRenderer, s2, j + 2, k + 12 + 10, 0x808080);
        ci.cancel();
    }
}
