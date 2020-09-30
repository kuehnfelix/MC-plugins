package tk.wolfsbau.payback.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import tk.wolfsbau.payback.db.Messages;

public class CmdMgr {
    private static CmdMgr instance;

    private CmdMgr() {
        Bukkit.getPluginCommand("team").setExecutor(new Cmd_team());
        Bukkit.getPluginCommand("option").setExecutor(new Cmd_option());
        Bukkit.getPluginCommand("mitte").setExecutor(new Cmd_mitte());
        Bukkit.getPluginCommand("starttime").setExecutor(new Cmd_start());
        Bukkit.getPluginCommand("discord").setExecutor(new Cmd_discord());
        Bukkit.getPluginCommand("strike").setExecutor(new Cmd_strike());
    }

    public static void init() {
        if(instance == null) instance = new CmdMgr();
    }

    public static void noPermission(CommandSender sender) {
        sender.sendMessage(Messages.getInstance().NOPERMISSION);
    }


}
