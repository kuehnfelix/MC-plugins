package tk.wolfsbau.payback.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.wolfsbau.payback.db.Data;
import tk.wolfsbau.payback.db.Option;

public class Cmd_option implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equals("option")) {
            if(args.length==1) {
                if(args[0].equalsIgnoreCase("list")) {
                    if(sender.hasPermission("payback.option.list")) {
                        for(String s : Data.getInstance().getOptionList()) {
                            sender.sendMessage(s);
                        }
                    } else {
                        CmdMgr.noPermission(sender);
                    }
                } else {
                    error(sender);
                }
            } else if(args.length==2) {
                if(args[0].equalsIgnoreCase("get")) {
                    try {
                        sender.sendMessage(ChatColor.AQUA + args[1] + ChatColor.BLACK + " -> " + ChatColor.WHITE + Data.getInstance().getOption(Option.fromString(args[1])));
                    } catch(IllegalArgumentException e) {
                        sender.sendMessage(ChatColor.RED + "Diese Option existiert nicht!");
                    }
                } else {
                    error(sender);
                }
            } else if(args.length==3) {
                if(args[0].equalsIgnoreCase("set")) {
                    Data.getInstance().setOption(Option.fromString(args[1]), Long.valueOf(args[2]));
                    sender.sendMessage(ChatColor.GREEN + "Option geändert!");
                }
            } else {
                error(sender);
            }
        }


        return true;
    }

    public static void error(CommandSender sender) {
        sender.sendMessage(ChatColor.DARK_RED + "Syntaxfehler!");
        sender.sendMessage(ChatColor.RED + "Usage:");
        sender.sendMessage(ChatColor.RED + "/option list");
        sender.sendMessage(ChatColor.RED + "/option set <option>");
        sender.sendMessage(ChatColor.RED + "/option get <option>");
    }



}
