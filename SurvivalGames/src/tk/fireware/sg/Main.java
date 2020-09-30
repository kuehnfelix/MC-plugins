package tk.fireware.sg;


import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import tk.fireware.sg.coins.Money;
import tk.fireware.sg.gamestates.GameStateManager;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public final class Main extends JavaPlugin {

    public static Main plugin;
    public static OSType detectedOS;
	private File file;

    @Override
    public void onEnable() {
        plugin = this;
        Data.getInstance().deserialize();
        loadConfig();
        ChestItems.init();
        new Money();
        new CommandManager();
        new GameStateManager();
        new Countdown().startLobbyCountdown();
        BuildManager.getInstance();
    }

    private void loadConfig() {
    	file=new File(Main.plugin.getDataFolder().getAbsolutePath(),"items.yml");
		YamlConfiguration config=YamlConfiguration.loadConfiguration(file);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public enum OSType{
    	MacOS,Windows,Linux,Other;
    }
    
    public static OSType getOperatingSystemType() {
		if (detectedOS == null) {
          String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
          if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
            detectedOS = OSType.MacOS;
          } else if (OS.indexOf("win") >= 0) {
            detectedOS = OSType.Windows;
          } else if (OS.indexOf("nux") >= 0) {
            detectedOS = OSType.Linux;
          } else {
            detectedOS = OSType.Other;
          }
        }
        return detectedOS;
     }
}
