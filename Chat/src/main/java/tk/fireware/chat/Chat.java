package tk.fireware.chat;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public final class Chat extends JavaPlugin implements Listener {

    MySQL mySQL = new MySQL("localhost", "3306", "wolfsbau-pex", "pex", "pEx");
    Connection c = null;
    Connection c2 = null;


    private HashMap<Player, String> names = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);


        try {
            c = mySQL.openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new BukkitRunnable() {
            @Override
            public void run() {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    updateName(p);
                }
            }
        }, 0L, 20L);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerLoginEvent event) {
        Player p = event.getPlayer();
        updateName(p);
    }

    private void updatenamealt(Player p) {
        Scoreboard sb=p.getScoreboard();
        if(p.hasPermission("wolfsbau.dev")) {
            sb.getTeam("20dev").addPlayer(p);
        } else if(p.hasPermission("wolfsbau.owner")) {
            sb.getTeam("10owner").addPlayer(p);
        }  else if(p.hasPermission("wolfsbau.admin")) {
            sb.getTeam("30admin").addPlayer(p);
        }  else if(p.hasPermission("wolfsbau.mod")) {
            sb.getTeam("40mod").addPlayer(p);
        } else if(p.hasPermission("wolfsbau.youtuber")) {
            sb.getTeam("50youtuber").addPlayer(p);
        } else if(p.hasPermission("wolfsbau.premium")) {
            sb.getTeam("60premi").addPlayer(p);
        } else {
            sb.getTeam("70default").addPlayer(p);
        }
    }

    private void updateName(Player p) {
        Statement statement = null;
        try {
            c = mySQL.openConnection();
            statement = c.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        MySQL mySQL2 = new MySQL("localhost", "3306", "nicknames", "pLugin", "pLugin");
        Statement statement2 = null;
        ResultSet res2 = null;
        boolean b = false;
        try {
            c2 = mySQL2.openConnection();

            statement2 = c2.createStatement();

            res2 = statement2.executeQuery("SELECT * FROM BetterNick WHERE UUID = '" + p.getUniqueId() + "';");
            res2.next();
            b = (res2.getBoolean("NICKED"));
        } catch (Exception e) {}
        ResultSet res = null;
        String rank = "";
        try {
            res = mySQL.querySQL("SELECT * FROM permissions_inheritance WHERE child = '" + p.getUniqueId() + "';");
            res.next();
            rank = res.getString("parent");
            res = mySQL.querySQL("SELECT * FROM permissions WHERE name = '" + rank + "' AND permission = 'prefix';");
            res.next();
            if(!b) {
                if(res.getString("NAME").equalsIgnoreCase("")) {
                    p.setDisplayName(res.getString("value").equals("") ? (ChatColor.GRAY + "[Spieler] ") : res.getString("value") + p.getName());//res2.getString("NAME"));
                } else {
                    p.setDisplayName(res.getString("value").equals("") ? (ChatColor.GRAY + "[Spieler] ") : res.getString("value") + res2.getString("NAME"));
                }
            } else {
                p.setDisplayName("§7[§6Premium§7] §6" + res2.getString("NICKNAME"));
            }

            p.setPlayerListName(p.getDisplayName());

        } catch (SQLException | ClassNotFoundException e) {
            try {
                res = mySQL.querySQL("SELECT * FROM permissions_inheritance WHERE child = '" + p.getUniqueId() + "';");
                if(!res.next()) {
                    System.out.println("no result");
                    p.setDisplayName(ChatColor.GRAY + "[Spieler] " +  p.getName());

                } else {
                    rank = res.getString("parent");
                    res = statement.executeQuery("SELECT * FROM permissions WHERE name = '" + rank + "' AND permission = 'prefix';");
                    res.next();
                    p.setDisplayName(res.getString("value").equals("") ? (ChatColor.GRAY + "[Spieler] ") : res.getString("value") + p.getName());//res2.getString("NAME"));
                }
                p.setPlayerListName(p.getDisplayName());
            } catch (Exception ex) {ex.printStackTrace();}
        }
        try {
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            c2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            mySQL2.closeConnection();
            mySQL.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();

        ResultSet res = null;
        event.setFormat(p.getDisplayName() + ChatColor.BLACK + " > " + ChatColor.RESET + event.getMessage());
    }
}
