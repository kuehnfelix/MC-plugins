package tk.fireware.bedwars;


import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class Main extends JavaPlugin {

    public static Main plugin;


    @Override
    public void onEnable() {
        plugin = this;
        new CommandManager();
        new GameStateManager(GameState.LOBBY);
        new Countdown().startLobbyCountdown();
        BuildManager.getInstance();
        Data.getInstance().deserialize();

    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
