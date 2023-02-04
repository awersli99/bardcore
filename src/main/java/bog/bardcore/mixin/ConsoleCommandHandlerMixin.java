package bog.bardcore.mixin;

import bog.bardcore.Bardcore;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;
import net.minecraft.src.command.ChatColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.logging.Logger;


@Mixin(value = ConsoleCommandHandler.class, remap = false)
public abstract class ConsoleCommandHandlerMixin {

    @Shadow
    private
    MinecraftServer minecraftServer;

    @Shadow
    private static
    Logger minecraftLogger;

    @Shadow
    protected abstract void sendNoticeToOps(String s, String s1);
    @Shadow
    protected abstract void printHelp(ICommandListener icommandlistener);
    @Shadow
    protected abstract void handleWhitelist(String s, String s1, ICommandListener icommandlistener);

    /**
     * @author bog
     * @reason felt like it mate
     */
    @Inject(method="handleCommand", at = @At("HEAD"), cancellable = true)
    public void injectedHandleCommand(ServerCommand servercommand, CallbackInfo ci) {
        String s = servercommand.command;
        ICommandListener icommandlistener = servercommand.commandListener;
        String s1 = icommandlistener.getUsername();
        ServerConfigurationManager serverconfigurationmanager = minecraftServer.configManager;
        if (!((ConsoleCommandHandler)(Object)this).handleCommandNew(servercommand)) {
            if (s.toLowerCase().startsWith("help") || s.toLowerCase().startsWith("?")) {
                printHelp(icommandlistener);
            } else if (s.toLowerCase().startsWith("save-all")) {
                sendNoticeToOps(s1, "Forcing save..");
                if (serverconfigurationmanager != null) {
                    serverconfigurationmanager.savePlayerStates();
                }
                for (int i = 0; i < this.minecraftServer.worldMngr.length; ++i) {
                    WorldServer worldserver = this.minecraftServer.worldMngr[i];
                    worldserver.saveWorld(true, null);
                }
                sendNoticeToOps(s1, "Save complete.");
            } else if (s.toLowerCase().startsWith("save-off")) {
                sendNoticeToOps(s1, "Disabling level saving..");
                for (int j = 0; j < this.minecraftServer.worldMngr.length; ++j) {
                    WorldServer worldserver1 = this.minecraftServer.worldMngr[j];
                    worldserver1.levelSaving = true;
                }
            } else if (s.toLowerCase().startsWith("save-on")) {
                sendNoticeToOps(s1, "Enabling level saving..");
                for (int k = 0; k < this.minecraftServer.worldMngr.length; ++k) {
                    WorldServer worldserver2 = this.minecraftServer.worldMngr[k];
                    worldserver2.levelSaving = false;
                }
            } else if (s.toLowerCase().startsWith("ban-ip ")) {
                String s4 = s.substring(s.indexOf(" ")).trim();
                serverconfigurationmanager.banIP(s4);
                sendNoticeToOps(s1, "Banning ip " + s4);
            } else if (s.toLowerCase().startsWith("pardon-ip ")) {
                String s5 = s.substring(s.indexOf(" ")).trim();
                serverconfigurationmanager.pardonIP(s5);
                sendNoticeToOps(s1, "Pardoning ip " + s5);
            } else if (s.toLowerCase().startsWith("ban ")) {
                String s6 = s.substring(s.indexOf(" ")).trim();
                serverconfigurationmanager.banPlayer(s6);
                sendNoticeToOps(s1, "Banning " + s6);
                EntityPlayerMP entityplayermp = serverconfigurationmanager.getPlayerEntity(s6);
                if (entityplayermp != null) {
                    entityplayermp.playerNetServerHandler.kickPlayer("Banned by admin");
                }
            } else if (s.toLowerCase().startsWith("pardon ")) {
                String s7 = s.substring(s.indexOf(" ")).trim();
                serverconfigurationmanager.pardonPlayer(s7);
                sendNoticeToOps(s1, "Pardoning " + s7);
            } else if (s.toLowerCase().startsWith("kick ")) {
                String s8 = s.substring(s.indexOf(" ")).trim();
                EntityPlayerMP entityplayermp1 = null;
                for (int l = 0; l < serverconfigurationmanager.playerEntities.size(); ++l) {
                    EntityPlayerMP entityplayermp5 = serverconfigurationmanager.playerEntities.get(l);
                    if (!entityplayermp5.username.equalsIgnoreCase(s8)) continue;
                    entityplayermp1 = entityplayermp5;
                }
                if (entityplayermp1 != null) {
                    entityplayermp1.playerNetServerHandler.kickPlayer("Kicked by admin");
                    sendNoticeToOps(s1, "Kicking " + entityplayermp1.getDisplayName());
                } else {
                    icommandlistener.log("Can't find user " + s8 + ". No kick.");
                }
            } else if (s.toLowerCase().startsWith("say ")) {
                s = s.substring(s.indexOf(" ")).trim();
                minecraftLogger.info("[" + s1 + "] " + s);
                serverconfigurationmanager.sendPacketToAllPlayers(new Packet3Chat(ChatColor.pink + "[Server] " + s));
            } else if (s.toLowerCase().startsWith("whitelist ")) {
                handleWhitelist(s1, s, icommandlistener);
            } else if (s.startsWith("gamemode ")) {
                String[] as = s.split(" ");
                if (as.length == 3) {
                    int gamemodeId = -1;
                    try {
                        gamemodeId = Integer.parseInt(as[2]);
                    }
                    catch (Exception e) {
                        icommandlistener.log("Unknown or invalid gamemode ID.");
                    }
                    if (gamemodeId != -1) {
                        if (gamemodeId >= Bardcore.gamemodesList.length || Bardcore.gamemodesList[gamemodeId] == null) {
                            icommandlistener.log("Unknown or invalid gamemode ID.");
                        } else if (!serverconfigurationmanager.sendPacketToPlayer(as[1], new Packet41EntityPlayerGamemode(gamemodeId))) {
                            icommandlistener.log("There's no player by that name online.");
                        } else {
                            EntityPlayerMP player = serverconfigurationmanager.getPlayerEntity(as[1]);
                            player.setGamemode(Bardcore.gamemodesList[gamemodeId]);
                            sendNoticeToOps(s1, "Setting " + player.getDisplayName() + "\u00a77" + "'s gamemode to " + Bardcore.gamemodesList[gamemodeId].languageKey);
                        }
                    }
                }
            } else {
                minecraftLogger.info("Unknown console command. Type \"help\" for help.");
            }
            ci.cancel();
        }
    }

}
