package bog.bardcore;

import bog.bardcore.gamemode.GamemodeHardcore;
import bog.bardcore.gamemode.GamemodeSpectate;
import net.fabricmc.api.ModInitializer;
import net.minecraft.src.Gamemode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Bardcore implements ModInitializer {
    public static final String MOD_ID = "assets/bardcore";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Gamemode[] gamemodesList = new Gamemode[4];

    public void registerGamemode(Gamemode gamemode) {
        gamemodesList[gamemode.id] = gamemode;
        Bardcore.LOGGER.info("Registered gamemode: " + gamemode.languageKey);
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Bardcore initialized.");

        // Register regular game modes
        registerGamemode(Gamemode.gamemodesList[0]);
        registerGamemode(Gamemode.gamemodesList[1]);

        // Register new game modes and avoid Gamemode class throwing error
        Gamemode hardcore = new GamemodeHardcore(0).setLanguageKey("gamemode.hardcore").setConsumeBlocks().setDoBlockBreakingAnim().setToolDurability().setDropBlockOnBreak().setAreMobsHostile();
        hardcore.id=2;
        registerGamemode(hardcore);
        Gamemode spectate = new GamemodeSpectate(0).setLanguageKey("gamemode.spectate").setConsumeBlocks().setIsPlayerInvulnerable().setIsImmuneToFire().setCanPlayerFly();
        spectate.id=3;
        registerGamemode(spectate);
    }

}
