package tk.fireware.knockffa;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public final class KnockFFA extends JavaPlugin implements Listener {



    ScoreboardManager sbm;

    MySQL mySQL = new MySQL("localhost", "3306", "wolfsbau-stats", "plugin", "pLugin");
    Connection c = null;
    MySQL mySQL2 = new MySQL("localhost", "3306", "wolfsbau", "plugin", "pLugin");

    public static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "Knock" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new Shop(), this);
        sbm = Bukkit.getScoreboardManager();
        for (Player p : Bukkit.getOnlinePlayers()) {
            giveItems(p);
            p.updateInventory();
        }

        try {
            c = mySQL.openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        String worldname = Bukkit.getWorlds().get(0).getName();
        event.setMotd(ChatColor.DARK_GRAY+"["+ChatColor.DARK_GREEN+"KnockFFA"+ChatColor.DARK_GRAY+"];"+ ChatColor.GREEN+"ONLINE;"+ChatColor.BLUE+ worldname +";"+event.getNumPlayers()+"/"+event.getMaxPlayers());
    }

    public void addMysteryDust(Player p, int amount) {
        try {
            Connection c = mySQL2.openConnection();
            c.createStatement().executeUpdate("UPDATE GadgetsMenu_Data SET Mystery_Dust = (Mystery_Dust + 1) WHERE UUID='" + p.getUniqueId() + "';");
            p.sendMessage(ChatColor.WHITE + "+" + amount + " Mystery Dust");
            mySQL2.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent event) {
        Player p = event.getPlayer();

        if(p.hasPermission("knock.joinfull")) {
            if(Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if(!player.hasPermission("knock.joinfull")) {
                        player.kickPlayer("Du wurdest gekickt, um einem Premium-Spieler Platz zu machen!");
                        event.allow();
                        return;
                    }
                }
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Es wurde kein Spieler zu kicken gefunden!");
            }
        } else {
            if(Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
                event.disallow(PlayerLoginEvent.Result.KICK_FULL, "Der Server ist voll!");
            } else {
                event.allow();
            }
        }

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        giveItems(event.getPlayer());
        event.getPlayer().teleport(event.getPlayer().getLocation().getWorld().getSpawnLocation());
        updateScoreboard();

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(ChatColor.GRAY+"["+ChatColor.RED+"-"+ChatColor.GRAY+"] " + event.getPlayer().getDisplayName());
        Data.getInstance().removePlayer(event.getPlayer().getUniqueId());
    }

    private void giveItems(Player p) {
        if(Data.getInstance().getData(p.getUniqueId())==null) {
            Data.getInstance().addPlayer(p.getUniqueId());
        }
        /*Scoreboard board = sbm.getNewScoreboard();
        Objective objective = board.registerNewObjective("kills", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.setDisplayName("KnockFFA");
        Score score = objective.getScore(ChatColor.GREEN + "Kills: 0"); //Get a fake offline player
        scor    e.setScore(3);
        Score score2 = objective.getScore(ChatColor.GREEN + "Deaths: 0"); //Get a fake offline player
        score2.setScore(2);
        Score score3 = objective.getScore(ChatColor.GREEN + "K/D: 0"); //Get a fake offline player
        score3.setScore(1);
        p.setScoreboard(board);*/

        p.getInventory().clear();
        p.getInventory().setContents(getItems(p.getName().equals("letsFEX")||p.getName().equals("ZAO77")));
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity().getLocation().getBlockY()>80) {
            event.setCancelled(true);
        }
        event.setDamage(0);

        Data.getInstance().getData(event.getEntity().getUniqueId()).hit(event.getDamager().getUniqueId());
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        event.setDamage(0);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getLocation().getBlockY() < 50) {
            if ((Data.getInstance().getData(player.getUniqueId()).getLastDamage().getTime() > (System.currentTimeMillis() - 7000))) {
                try {
                    Player damager = Bukkit.getPlayer(Data.getInstance().getData(player.getUniqueId()).getLastDamage().getDamager());
                    Data.getInstance().getData(damager.getUniqueId()).addKill();
                    int i = Data.getInstance().getData(damager.getUniqueId()).getKillStreak();
                    if(i == 3 ||i == 5 ||i == 10 ||i == 15 || i == 20 || i == 50 || i == 100) {
                        Bukkit.broadcastMessage(damager.getDisplayName() + ChatColor.GOLD + " hat einen " + ChatColor.GREEN + i + "er " + ChatColor.GOLD + "Killstreak erreicht!");
                        killStreakMoney(damager, i);
                        addMysteryDust(damager, i);
                    }
                    Bukkit.broadcastMessage(PREFIX + ChatColor.YELLOW+player.getDisplayName() + ChatColor.RESET + " wurde von " + ChatColor.YELLOW +damager.getDisplayName()+ChatColor.RESET + " getötet!");
                    sendMoney(damager, player);
                    addMysteryDust(damager, 1);
                } catch(Exception e) {
                    Bukkit.broadcastMessage(PREFIX + ChatColor.YELLOW + player.getDisplayName() + ChatColor.RESET + " ist gestorben!");
                }
            } else {
                Bukkit.broadcastMessage(PREFIX + ChatColor.YELLOW + player.getDisplayName() + ChatColor.RESET + " ist gestorben!");
            }
            player.teleport(new Location(event.getTo().getWorld(), 0, 107, 0));

            Data.getInstance().getData(player.getUniqueId()).addDeath();
            updateScoreboard();
            player.setFallDistance(0);

            giveItems(player);
            player.updateInventory();
        }
        player.setFallDistance(0);
    }

    private int getMoney(Player player) {
        Statement statement = null;
        try {
            c = mySQL.openConnection();
            statement = c.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
        try {
            mySQL.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return money;
    }

    private void killStreakMoney(Player p, int money) {
        setMoney(p, getMoney(p) + money);
        p.sendMessage(ChatColor.GRAY+"[" + ChatColor.GOLD +"Bank" + ChatColor.GRAY + "]" + ChatColor.RESET + " Du hast einen " + ChatColor.GOLD + money + "er " + ChatColor.RESET + "Killstreak erreicht: " + (money < 0 ? ChatColor.RED : ChatColor.GREEN) + (money < 0 ? "" : "+") + money + " Knochen");
    }

    private void sendMoney(Player killer, Player killed) {
        int money = 1;
        if(killed.hasPermission("wolfsbau.dev")) money = -5;
        else if(killed.hasPermission("wolfsbau.owner")) money = 11;
        else if(killed.hasPermission("wolfsbau.admin")) money = 3;
        else if(killed.hasPermission("wolfsbau.mod")) money = 2;
        else if(killed.hasPermission("wolfsbau.youtuber")) money = 3;
        else if(killed.hasPermission("wolfsbau.premium")) money = 2;
        money = 1;

        setMoney(killer, getMoney(killer) + money);
        killer.sendMessage(ChatColor.GRAY+"[" + ChatColor.GOLD +"Bank" + ChatColor.GRAY + "]" + ChatColor.RESET + " Du hast " + killed.getDisplayName() + " getötet: " + (money < 0 ? ChatColor.RED : ChatColor.GREEN) + (money < 0 ? "" : "+") + money + " Knochen");
    }

    private void updateScoreboard() {
        Player[] top5 = new Player[5];
        ArrayList<Player> players  = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()) {
            players.add(p);
        }
        for(int i = 0; i< 5; i++) {
            Player best = null;
            Player toRemove = null;
            for(Player p : players) {

                if(best == null) {
                    best = p;
                    toRemove = p;
                } else  if(Data.getInstance().getData(p.getUniqueId()).getKD() > Data.getInstance().getData(best.getUniqueId()).getKD()) {
                    best = p;
                    toRemove = p;
                }

            }
            if(toRemove != null) players.remove(toRemove);
            top5[i] = best;
        }



        for(Player p : getServer().getOnlinePlayers()) {
            PlayerData data = Data.getInstance().getData(p.getUniqueId());
            Scoreboard board = sbm.getNewScoreboard();
            Objective objective = board.registerNewObjective("kills", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            objective.setDisplayName("KnockFFA");
            Score score20 = objective.getScore(ChatColor.GREEN + "Kills: " + data.getKills() );
            score20.setScore(20);
            Score score19 = objective.getScore(ChatColor.GREEN + "Deaths: " + data.getDeaths());
            score19.setScore(19);
            Score score18 = objective.getScore(ChatColor.GREEN + "K/D: " + data.getKDString());
            score18.setScore(18);
            Score score17 = objective.getScore(ChatColor.GREEN + "Killstreak: " + data.getKillStreak());
            score17.setScore(17);
            Score score16 = objective.getScore(ChatColor.RESET + "---------------------");
            score16.setScore(16);

            if(top5[0] != null) {
                Score score15 = objective.getScore("     ");
                Score score14 = objective.getScore(ChatColor.GREEN + "1. " + top5[0].getDisplayName().split("] ")[1].substring(0,top5[0].getDisplayName().split("] ")[1].length() >= 14 ? 14 : top5[0].getDisplayName().split("] ")[1].length()));
                Score score13 = objective.getScore(ChatColor.GREEN + Data.getInstance().getData(top5[0].getUniqueId()).getKDString()+"     ");
                score15.setScore(15);
                score14.setScore(14);
                score13.setScore(13);
                if (top5[1] != null) {
                    Score score12 = objective.getScore("    ");
                    Score score11 = objective.getScore(ChatColor.GREEN + "2. " + top5[1].getDisplayName().split("] ")[1].substring(0,top5[1].getDisplayName().split("] ")[1].length() >= 14 ? 14 : top5[1].getDisplayName().split("] ")[1].length()));
                    Score score10 = objective.getScore(ChatColor.GREEN + Data.getInstance().getData(top5[1].getUniqueId()).getKDString()+"    ");
                    score12.setScore(12);
                    score11.setScore(11);
                    score10.setScore(10);
                    if (top5[2] != null) {
                        Score score9 = objective.getScore("   ");
                        Score score8 = objective.getScore(ChatColor.GREEN + "3. " + top5[2].getDisplayName().split("] ")[1].substring(0,top5[2].getDisplayName().split("] ")[1].length() >= 14 ? 14 : top5[2].getDisplayName().split("] ")[1].length()));
                        Score score7 = objective.getScore(ChatColor.GREEN + Data.getInstance().getData(top5[2].getUniqueId()).getKDString()+"   ");
                        score9.setScore(9);
                        score8.setScore(8);
                        score7.setScore(7);
                            if (top5[3] != null) {
                                Score score6 = objective.getScore("  ");
                                Score score5 = objective.getScore(ChatColor.GREEN + "4. " + top5[3].getDisplayName().split("] ")[1].substring(0,top5[3].getDisplayName().split("] ")[1].length() >= 14 ? 14 : top5[3].getDisplayName().split("] ")[1].length()));
                                Score score4 = objective.getScore(ChatColor.GREEN + Data.getInstance().getData(top5[3].getUniqueId()).getKDString()+"  ");
                                score6.setScore(6);
                                score5.setScore(5);
                                score4.setScore(4);
                                if (top5[4] != null) {
                                    Score score3 = objective.getScore(" ");
                                    Score score2 = objective.getScore(ChatColor.GREEN + "5. " + top5[4].getDisplayName().split("] ")[1].substring(0,top5[4].getDisplayName().split("] ")[1].length() >= 14 ? 14 : top5[4].getDisplayName().split("] ")[1].length()));
                                    Score score1 = objective.getScore(ChatColor.GREEN + Data.getInstance().getData(top5[4].getUniqueId()).getKDString()+" ");
                                    score3.setScore(3);
                                    score2.setScore(2);
                                    score1.setScore(1);
                                }
                            }
                        }
                }
            }

            p.setScoreboard(board);
        }
    }

    private void setMoney(Player player, int money) {
        Statement statement = null;
        try {
            c = mySQL.openConnection();
            statement = c.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            statement.executeUpdate("UPDATE money SET money = " + money + " WHERE uuid = '" + player.getUniqueId() + "';");
            mySQL.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @EventHandler
    public void onBlockPlace(BlockBreakEvent event) {
        if(!event.getPlayer().hasPermission("knock.build")) event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(!event.getPlayer().hasPermission("knock.build")) event.setCancelled(true);
    }

    @EventHandler
    public void onInventroyEvent(InventoryInteractEvent event) {
        //event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventroyEvent(InventoryClickEvent event) {
        //event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventroyEvent(InventoryDragEvent event) {
        //event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventroyEvent(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventroyEvent(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    private ItemStack[] getItems(boolean op) {
        ItemStack[] itsts = new ItemStack[9];
        ItemStack itst = null;
        ItemMeta meta = null;
        itst = new ItemStack(Material.STICK);
        meta = itst.getItemMeta();
        meta.addEnchant(Enchantment.KNOCKBACK, op ? 2 : 1, true);
        itst.setItemMeta(meta);
        itsts[0] = itst;


        return itsts;
    }
}
