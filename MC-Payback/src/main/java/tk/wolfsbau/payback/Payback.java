package tk.wolfsbau.payback;

import org.bukkit.Material;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;
import tk.wolfsbau.payback.discord_bot.Bot;
import tk.wolfsbau.payback.cmd.CmdMgr;
import tk.wolfsbau.payback.gameState.GameStateManager;

import java.util.Iterator;

public final class Payback extends JavaPlugin {
    public static Payback plugin;

    @Override
    public void onEnable() {

        Iterator<Recipe> it = getServer().recipeIterator();
        while(it.hasNext()) {
            Recipe r = it.next();
            Material m = r.getResult().getType();

            if(m.equals(Material.GOLDEN_APPLE)&&r.getResult().getDurability()==1) {
                it.remove();
            }
        }

        plugin = this;
        GameStateManager.getInstance();
        CmdMgr.init();
        Bot.main();
        Bot.sendMessageBotChannel("```DER SERVER IST GESTARTET!```");
    }

    @Override
    public void onDisable() {
        Bot.sendMessageBotChannel("```DER SERVER IST GESTOPPT!```");
    }
}
