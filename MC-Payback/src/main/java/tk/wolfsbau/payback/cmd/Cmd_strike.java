package tk.wolfsbau.payback.cmd;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.wolfsbau.payback.db.Data;
import tk.wolfsbau.payback.discord_bot.Bot;
import tk.wolfsbau.payback.gameState.GameState_running;

public class Cmd_strike implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission("revenge.strike")) {
            if(args.length>=1 && Data.getInstance().isWhiteListed(args[0])) {
                String reason = "";
                for(int i = 1; i < args.length; i++) {
                    reason += args[i];
                }
                if(reason.equals("")) reason = "Regelverstoß";
                strike(args[0], reason);
            } else {
                return false;
            }
        } else {
            sender.sendMessage("No Permission!");
        }
        return true;
    }

    public static void strike(String player, String reason) {
        System.out.println("strike " + player);
        Data data = Data.getInstance();
        int strikes = data.getStrikes(player);
        strikes++;
        data.setStrikes(player, strikes);


        String msg="";
        if(strikes==1) {
            Location loc = data.getPlayerLocation(player);
            msg = "Die Koordinaten sind: " + loc.getWorld().getName() + ", " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
        } else if(strikes == 2) {
            msg = "Das Inventar wurde gelöscht!";
            boolean online = false;
            for(Player p : Bukkit.getOnlinePlayers()) {
                if(p.getName().equals(player)) {
                    p.getInventory().clear();
                    online=true;
                }
            }
            if(!online) Data.getInstance().setClearInv(player, true);
        } else if(strikes>2) {
            msg = player + " ist aus PAYBACK ausgeschieden!";
            data.setAlive(player, false);
            try {
                GameState_running.kicked.add(Bukkit.getPlayer(player));
                GameState_running.getInstance().endeNachBan();
                Bukkit.getPlayer(player).kickPlayer("Du hast deinen 3.Strike erhalten!");
            } catch (Exception e) {}
        }

        Bot.sendStrikeChannel("```Spieler " + player + " [" + data.getTeam(player) + "] hat seinen " + strikes + ". Strike für " + reason + " erhalten!\n" + msg + "```");

    }




}
