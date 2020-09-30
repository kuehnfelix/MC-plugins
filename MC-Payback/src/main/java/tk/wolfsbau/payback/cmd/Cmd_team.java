package tk.wolfsbau.payback.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tk.wolfsbau.payback.db.Data;
import tk.wolfsbau.payback.db.Option;
import tk.wolfsbau.payback.util.SetSlots;

import java.util.ArrayList;

public class Cmd_team implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equals("team")) {
/*list cmd*/if(args.length == 1) {
                if(args[0].equals("list")) {
                    if(sender.hasPermission("payback.team.list")) {
                        ArrayList<String> list = Data.getInstance().getTeamList();
                        for(String s : list) {
                            sender.sendMessage(s);
                        }
                    } else {
                        CmdMgr.noPermission(sender);
                    }
                } else {
                    error(sender);                }
/*rmv cmd*/ } else if(args.length==2) {
                if(args[0].equals("remove")) {
                    if(sender.hasPermission("payback.team.remove")) {
                        Data.getInstance().removeTeam(args[1]);
                        sender.sendMessage(ChatColor.GREEN + "Team " + args[1] + " wurde entfernt!");
                    } else {
                        CmdMgr.noPermission(sender);
                    }
                } else {
                    error(sender);
                }
/*add cmd*/ } else if(args.length == (2+Data.getInstance().getOption(Option.TEAMSIZE))) {
                if(args[0].equals("add")) {
                    if(!sender.hasPermission("payback.team.add")) {
                        CmdMgr.noPermission(sender);
                        return true;
                    }
                    ArrayList<String> players = new ArrayList<>();
                    for(int i = 2; i<args.length; i++) {
                        players.add(args[i]);
                    }
                    Data.getInstance().setTeam(args[1], players);
                    String s = "(";
                    for(int i = 2; i< args.length;i++) s+= args[i] + "  ";
                    SetSlots.setSlots(Data.getInstance().getPlayerCount());
                    sender.sendMessage(ChatColor.GREEN + "Team " + args[1] + s + ") wurde hinzugefügt!");
                } else {
                    error(sender);
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
        sender.sendMessage(ChatColor.RED + "/team add <TeamName> <Name1> <Name2> ...");
        sender.sendMessage(ChatColor.RED + "/team remove <name>");
        sender.sendMessage(ChatColor.RED + "/team list\"");
    }

}
