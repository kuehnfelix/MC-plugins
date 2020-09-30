package tk.wolfsbau.payback.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.wolfsbau.payback.discord_bot.Bot;

public class Cmd_discord implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==0) {
            error(sender);
            return true;
        }
        if(!sender.hasPermission("payback.discord")) {
            CmdMgr.noPermission(sender);
            return true;
        }
        String msg="";
        msg+=">" + sender.getName() + "< schreibt von Minecraft:";
        for(String s : args) {
            msg+=" " + s;
        }
        Bot.sendMessageBotChannel(msg);
        return true;
    }

    public void error(CommandSender sender) {
        sender.sendMessage(ChatColor.DARK_RED + "Syntaxfehler!");
        sender.sendMessage(ChatColor.RED + "Usage:");
        sender.sendMessage(ChatColor.RED + "/discord <message>");
    }
}
