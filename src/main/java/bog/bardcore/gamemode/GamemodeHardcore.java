package bog.bardcore.gamemode;

import net.minecraft.src.ContainerPlayer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Gamemode;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.InventoryPlayer;

public class GamemodeHardcore
        extends Gamemode {

    public int id;

    public GamemodeHardcore(int id) {
        super(id);
        this.id = id;
    }

    @Override
    public ContainerPlayer getContainer(InventoryPlayer inventory, boolean isMultiplayer) {
        return new ContainerPlayer(inventory, isMultiplayer);
    }

    @Override
    public GuiInventory getInventoryGui(EntityPlayer player) {
        return new GuiInventory(player);
    }
}
