package bog.bardcore.mixin;

import bog.bardcore.Bardcore;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Gamemode;
import net.minecraft.src.command.CommandError;
import net.minecraft.src.command.CommandHandler;
import net.minecraft.src.command.CommandSender;
import net.minecraft.src.command.commands.GamemodeCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = GamemodeCommand.class, remap = false)
public class GamemodeCommandMixin {

    /**
     * @author bog
     * @reason felt like it mate
     */
    @Overwrite
    public boolean execute(CommandHandler handler, CommandSender sender, String[] args) {
        Gamemode gamemode;
        if (args.length == 0) {
            return false;
        }
        String gamemodeString = args[0];
        try {
            gamemode = Bardcore.gamemodesList[Integer.parseInt(gamemodeString)];
        }
        catch (Exception e) {
            gamemode = GamemodeCommand.getGamemode(gamemodeString);
        }
        if (gamemode == null) {
            throw new CommandError("Can't find gamemode \"" + gamemodeString + "\"!");
        }
        EntityPlayer player = sender.getPlayer();
        if (args.length > 1) {
            player = handler.getPlayer(args[1]);
        }
        if (player == null) {
            throw new CommandError("Must be used by a player, or a player name must be defined!");
        }
        player.setGamemode(gamemode);
        handler.sendCommandFeedback(sender, "Set gamemode to " + gamemode.languageKey.substring(9) + " for " + player.getDisplayName());
        return true;
    }

}
