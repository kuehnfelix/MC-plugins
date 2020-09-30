package tk.fireware.lobbygadgets;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.*;
import org.bukkit.util.Vector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Board implements PluginMessageListener, Listener {
    ScoreboardManager sbm = Bukkit.getScoreboardManager();

    private static Board instance;

    private int playerCount;

    MySQL mySQL = new MySQL("localhost", "3306", "wolfsbau-stats", "plugin", "pLugin");
    MySQL nickDB = new MySQL("localhost", "3306", "nicknames", "pLugin", "pLugin");
    MySQL prefixDB = new MySQL("localhost", "3306", "wolfsbau-pex", "pex", "pEx");
    Connection c = null;
    static String owner = "[Owner]", dev ="[DEV]", admin ="[Admin]", mod ="[Mod]", youtuber ="[YT]", premium ="[Premium]", player = "[Player]";


    public static Board getInstance() {
        if(instance == null) {
            instance = new Board();

        }
        return instance;
    }

    public Board() {
        loadPrefixes();
        try {
            c = mySQL.openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Bukkit.getPluginManager().registerEvents(this, Plugin.instance);
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(Plugin.instance, "BungeeCord");
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(Plugin.instance, "BungeeCord", this);

        BukkitScheduler scheduler = Plugin.instance.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(Plugin.instance, new Runnable() {
            int count = 0;
            @Override
            public void run() {
                updatePlayers();
                if(count == 300) {
                    for(Player u2 : Bukkit.getOnlinePlayers()) {
                        Bukkit.getServer().dispatchCommand(
                                Bukkit.getConsoleSender(),
                                "tellraw " + u2.getName() + " [\"\",{\"text\":\"\\n\"},{\"text\":\"=======================\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"\\n\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"                        \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\" \",\"bold\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"     \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\" \",\"bold\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"    \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\" \",\"bold\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"\\n\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\" \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"Unser Discord-Server\",\"bold\":true,\"underlined\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"  \",\"bold\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"\\n\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"  \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"     \",\"bold\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"                             \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"\\n\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\" \",\"bold\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"http://Discord.gg/\",\"underlined\":true,\"color\":\"blue\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\" \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"<- click\",\"color\":\"white\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"  \",\"bold\":true,\"color\":\"white\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"\\n\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"                                     \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"\\n\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"=======================\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/9KYz9vc\"}},{\"text\":\"\\n\"},{\"text\":\"                         \",\"bold\":true,\"color\":\"gold\"}]");
                    }
                }
                if(count > 600) {
                    for(Player u2 : Bukkit.getOnlinePlayers()) {
                        Bukkit.getServer().dispatchCommand(
                                Bukkit.getConsoleSender(),
                                "tellraw " + u2.getName() + " [\"\",{\"text\":\"\\n\"},{\"text\":\"=================================\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\\n\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"                                                      \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\\n\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"  \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"Besuche doch mal unser Forum!\",\"bold\":true,\"underlined\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"     \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\\n\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"                                                      \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\\n\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"  \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"http://wolfsbau.tk/forum\",\"underlined\":true,\"color\":\"blue\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"       \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"       \",\"bold\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"     \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\\n\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"                                                      \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\\n\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"=================================\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\\n \"}]");          }
                }
                count++;
                if(count > 900) {
                    for(Player u2 : Bukkit.getOnlinePlayers()) {
                        Bukkit.getServer().dispatchCommand(
                                Bukkit.getConsoleSender(),
                                "tellraw " + u2.getName() + " [\"\",{\"text\":\"\\n\"},{\"text\":\"=================================\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\\n\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"                                                      \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"" +"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\\n\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"  \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"Unser TeamSpeak Server!\",\"bold\":true,\"underlined\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"     \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\\n\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"                                                      \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\\n\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"  \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"http://wolfsbau.tk/forum\",\"underlined\":true,\"color\":\"blue\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"       \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"       \",\"bold\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"     \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\\n\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"                                                      \",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\\n\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"================================\",\"bold\":true,\"obfuscated\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"http://wolfsbau.tk/forum\"}},{\"text\":\"\\n \"}]");          }
                    count = 0;
                }
                count++;
            }
        }, 0L, 20L);
    }

    private void loadPrefixes() {
        Statement statement = null;
        ResultSet res = null;

        try {
            c = prefixDB.openConnection();
            statement = c.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {  res = statement.executeQuery("SELECT * FROM permissions WHERE name = '1-default' AND permission = 'prefix';");
            res.next();
            player = res.getString("value");
        } catch (SQLException e) {
            e.printStackTrace(); }
        try {
            res = statement.executeQuery("SELECT * FROM permissions WHERE name = '2-premium' AND permission = 'prefix';");
            res.next();
            premium = res.getString("value");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            res = statement.executeQuery("SELECT * FROM permissions WHERE name = '3-youtuber' AND permission = 'prefix';");
            res.next();
            youtuber = res.getString("value");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            res = statement.executeQuery("SELECT * FROM permissions WHERE name = '4-mod' AND permission = 'prefix';");
            res.next();
            mod = res.getString("value");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            res = statement.executeQuery("SELECT * FROM permissions WHERE name = '5-admin' AND permission = 'prefix';");
            res.next();
            admin = res.getString("value");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            res = statement.executeQuery("SELECT * FROM permissions WHERE name = '6-owner' AND permission = 'prefix';");
            res.next();
            owner = res.getString("value");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            res = statement.executeQuery("SELECT * FROM permissions WHERE name = '7-dev' AND permission = 'prefix';");
            res.next();
            dev = res.getString("value");
        } catch  (SQLException e) {
            e.printStackTrace();
        }

        try {
            prefixDB.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getNickName(Player p) {
        Statement statement2 = null;
        ResultSet res2 = null;

        if(!isNicked(p)) return p.getDisplayName();
        Connection c2 = null;
        String s = "";
        try {
            c2 = nickDB.openConnection();

            statement2 = c2.createStatement();

            res2 = statement2.executeQuery("SELECT * FROM BetterNick WHERE UUID = '" + p.getUniqueId() + "';");
            res2.next();
            s = res2.getString("NICKNAME");
            c2.close();
            nickDB.closeConnection();
        } catch (Exception e) {

        }

        return s;
    }

    private boolean isNicked(Player p) {
        // checkifplayerisnicked
        Connection c2 = null;
        Statement statement2 = null;
        ResultSet res2 = null;
        boolean b = false;
        try {
            c2 = nickDB.openConnection();
            statement2 = c2.createStatement();
            res2 = statement2.executeQuery("SELECT * FROM BetterNick WHERE UUID = '" + p.getUniqueId() + "';");
            res2.next();
            return (res2.getBoolean("NICKED"));
        } catch (Exception e) {
            return false;
        }
    }

    private String getPrefix(Player p) {
        Scoreboard sb=sbm.getMainScoreboard();
        if(sb.getTeam("#10owner").hasPlayer(p)) {
            return owner;
        } else if(sb.getTeam("#20dev").hasPlayer(p)) {
            return dev;
        } else if(sb.getTeam("#30admin").hasPlayer(p)) {
            return admin;
        } else if(sb.getTeam("#40mod").hasPlayer(p)) {
            return mod;
        } else if(sb.getTeam("#50youtuber").hasPlayer(p)) {
            return youtuber;
        } else if(sb.getTeam("#60premi").hasPlayer(p)) {
            return premium;
        } else if(sb.getTeam("#70default").hasPlayer(p)) {
            return player;
        } else {
            if(p.hasPermission("wolfsbau.dev")) {
                sb.getTeam("#20dev").addPlayer(p);
            } else if(p.hasPermission("wolfsbau.owner")) {
                sb.getTeam("#10owner").addPlayer(p);
            }  else if(p.hasPermission("wolfsbau.admin")) {
                sb.getTeam("#30admin").addPlayer(p);
            }  else if(p.hasPermission("wolfsbau.mod")) {
                sb.getTeam("#40mod").addPlayer(p);
            } else if(p.hasPermission("wolfsbau.youtuber")) {
                sb.getTeam("#50youtuber").addPlayer(p);
            } else if(p.hasPermission("wolfsbau.premium")) {
                sb.getTeam("#60premi").addPlayer(p);
            } else {
                sb.getTeam("#70default").addPlayer(p);
            }
            return getPrefix(p);
        }
    }

    public void updateScoreboard(Player player) {
        Scoreboard board = sbm.getNewScoreboard();
        Objective objective = board.registerNewObjective("money", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "wolfsbau.network");
        Score score = objective.getScore(" ");
        score.setScore(8);
        Score score2 = objective.getScore("" + getNickName(player));
        score2.setScore(7);
        Score score3 = objective.getScore(ChatColor.DARK_GRAY+"=====================");
        score3.setScore(6);
        Score score4 = objective.getScore(ChatColor.GOLD + "Online Players");
        score4.setScore(5);
        Score score5 = objective.getScore(playerCount+"");
        score5.setScore(4);
        Score score6 = objective.getScore("   " );
        score6.setScore(3);
        Score score7 = objective.getScore(ChatColor.GOLD + "Deine Knochen" );
        score7.setScore(2);
        Score score8 = objective.getScore( getMoney(player) + "     ");
        score8.setScore(1);
        player.setScoreboard(board);
    }

    public void updatePlayers() {
        if(Bukkit.getOnlinePlayers().size()==0) return;
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerCount");
        out.writeUTF("ALL");
        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        player.sendPluginMessage(Plugin.instance, "BungeeCord", out.toByteArray());
    }


    private void setMoney(Player player, int money) {
        try {
            if(c.isClosed()) {
                c = mySQL.openConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Statement statement = null;
        try {
            statement = c.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            statement.executeUpdate("UPDATE money " +
                    "SET money = " + money + " WHERE uuid = '" + player.getUniqueId() + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getMoney(Player player) {
        try {
            if(c.isClosed()) {
                c = mySQL.openConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Statement statement = null;
        try {
            statement = c.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet res = null;
        int money = 0;
        try {
            res = statement.executeQuery("SELECT * FROM money WHERE uuid = '" + player.getUniqueId() + "';");
            res.next();
            money = res.getInt("money");

        } catch (SQLException e) {
            try {
                statement.executeUpdate("INSERT INTO money (uuid, money) VALUES ('" + player.getUniqueId() + "', 0);");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return money;
    }



    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("PlayerCount")) {
            String server = in.readUTF();
            int playercount = in.readInt();
            if(this.playerCount == playercount) return;
            this.playerCount = playercount;
            for(Player players : Bukkit.getOnlinePlayers()) {
                updateScoreboard(players);
            }
        }
    }
}
