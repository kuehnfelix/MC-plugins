package tk.fireware.bungeecommands;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public final class BungeeCommands extends Plugin {

    @Override
    public void onEnable() {
        registerCommands();
    }

    @Override
    public void onDisable() {}


    private void registerCommands() {
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new COMMAND_hub("hub"));
    }
}
