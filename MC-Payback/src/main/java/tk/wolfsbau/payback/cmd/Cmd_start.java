package tk.wolfsbau.payback.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.wolfsbau.payback.db.Data;
import tk.wolfsbau.payback.db.Option;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Cmd_start implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("payback.start")) {
            CmdMgr.noPermission(sender);
            return true;
        }
        if(args.length==2 && args[0].equalsIgnoreCase("set")) {
            String dateString = args[1];
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.kk:mm");
            Date date = null;
            try {
                date = sdf.parse(dateString);
            } catch (ParseException e) {
                error(sender);
            }
            Data.getInstance().setOption(Option.STARTTIME, date.getTime());

        } else {
            error(sender);
        }
        return true;
    }

    public void error(CommandSender sender) {
        sender.sendMessage(ChatColor.DARK_RED + "Syntaxfehler!");
        sender.sendMessage(ChatColor.RED + "Usage:");
        sender.sendMessage(ChatColor.RED + "/starttime set MM.dd.yyyy.hh:mm");
    }
}
