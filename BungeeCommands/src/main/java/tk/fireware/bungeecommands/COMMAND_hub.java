package tk.fireware.bungeecommands;

import com.sun.media.jfxmedia.events.PlayerStateEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class COMMAND_hub extends Command {

    public COMMAND_hub(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) return;
        ProxiedPlayer pp = (ProxiedPlayer) sender;
        if(args.length == 0) {
            ((ProxiedPlayer)sender).connect(ProxyServer.getInstance().getServerInfo("lobby"));
        } else {
            ((ProxiedPlayer) sender).sendMessage(ChatColor.RED + "Usage: /hub");
        }
    }
}
