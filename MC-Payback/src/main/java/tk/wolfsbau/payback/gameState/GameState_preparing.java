package tk.wolfsbau.payback.gameState;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import tk.wolfsbau.payback.db.Data;
import tk.wolfsbau.payback.db.Option;
import tk.wolfsbau.payback.discord_bot.Bot;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class GameState_preparing implements IGameState {
    @Override
    public void trigger() {
        Bukkit.getWorlds().get(0).setTime(0);
        tpPlayers();
        countdown();

        WorldBorder border = Bukkit.getWorlds().get(0).getWorldBorder();
        border.setCenter(Data.getInstance().getOption(Option.MITTEX),Data.getInstance().getOption(Option.MITTEZ));
        border.setSize(Data.getInstance().getOption(Option.BORDERSTARTSIZE));

        Iterator<Entity> ents = Bukkit.getWorlds().get(0).getNearbyEntities(new Location(Bukkit.getWorlds().get(0),0,64,0), 120,30,120).iterator();
        while(ents.hasNext()) {
            Entity ent = ents.next();
            if(!ent.getType().equals(EntityType.PLAYER)) ent.remove();
        }
    }


    @Override
    public void onItemDrop(PlayerDropItemEvent e) {
        if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            e.setCancelled(true);
        }
    }

    @Override
    public void onEntityExplode(EntityExplodeEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bot.sendMessageBotChannel(":heavy_plus_sign:   **" + event.getPlayer().getName() + "** hat den Server betreten!");
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        Bot.sendMessageBotChannel(":heavy_minus_sign:   **" + event.getPlayer().getName() + "** hat den Server verlassen!");
    }

    @Override
    public void onPreLogin(PlayerLoginEvent event) {
        if(!Data.getInstance().isWhiteListed(event.getPlayer().getName())&&!event.getPlayer().isOp()) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Dein NAme steht leider nicht auf der Gästeliste");
        }
    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event) {

    }

    @Override
    public void onRespawn(PlayerRespawnEvent event) {

    }

    @Override
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {

    }

    @Override
    public void onChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        event.setFormat(ChatColor.GRAY + "[" + ChatColor.DARK_AQUA + Data.getInstance().getTeam(p.getName()) + ChatColor.GRAY + "] " + ChatColor.AQUA + p.getName() + ChatColor.GRAY + ": " + ChatColor.WHITE + event.getMessage());
        Bot.chatLog("[**"+Data.getInstance().getTeam(p.getName()) + "**] **"+p.getName() + "**: " + event.getMessage());
    }

    @Override
    public void craftItem(PrepareItemCraftEvent e) {

    }

    @Override
    public void onBrewEvent(BrewEvent event) {

    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if(block.getLocation().getBlockX()<=2&&block.getLocation().getBlockX()>=-2&&block.getLocation().getBlockY()<=70&&block.getLocation().getBlockY()>=64&&block.getLocation().getBlockZ()==0) {
            event.setCancelled(true);

        }
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if(block.getLocation().getBlockX()<=2&&block.getLocation().getBlockX()>=-2&&block.getLocation().getBlockY()<=70&&block.getLocation().getBlockY()>=64&&block.getLocation().getBlockZ()==0) {
            event.setCancelled(true);
            
        }
    }

    @Override
    public void onSignPlace(SignChangeEvent event) {

    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {

    }

    @Override
    public void onHopper(InventoryMoveItemEvent event) {

    }


    private void countdown() {
        long startMillis = Data.getInstance().getOption(Option.STARTTIME);
        long timeLeft = startMillis-System.currentTimeMillis();
        long days = TimeUnit.MILLISECONDS.toDays(timeLeft);
        long hours = TimeUnit.MILLISECONDS.toHours(timeLeft-TimeUnit.DAYS.toMillis(days));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeft-(TimeUnit.HOURS.toMillis(hours)+TimeUnit.DAYS.toMillis(days)));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeft-(TimeUnit.MINUTES.toMillis(minutes)+TimeUnit.HOURS.toMillis(hours)+TimeUnit.DAYS.toMillis(days)));

        //Countdown sound
        if(days==0 && hours==0 && minutes==0 && seconds<=10) {
            for(Player p : Bukkit.getOnlinePlayers()) {
                if(seconds==10) {
                    sendTitles_CountDown(""+seconds);
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.flat(1, Note.Tone.C));
                } else if(seconds>5) {
                    sendTitles_CountDown(""+seconds);
                    p.playNote(p.getLocation(), Instrument.BASS_GUITAR, Note.flat(1, Note.Tone.C));
                } else if(seconds==5) {
                    sendTitles_CountDown(""+seconds);
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.flat(1, Note.Tone.C));
                } else if(seconds>0) {
                    sendTitles_CountDown(""+seconds);
                    p.playNote(p.getLocation(), Instrument.BASS_GUITAR, Note.flat(1, Note.Tone.C));
                } else {
                    sendTitles_CountDown("START");
                    p.playNote(p.getLocation(), Instrument.PIANO, Note.sharp(2, Note.Tone.F));
                }
            }
        }

        if(hours==0 && seconds == 0 && minutes ==0) {
            if(days==0) {
                Bot.sendMessageBotChannel(":alarm_clock: Payback startet jetzt!");
            } else {
                Bot.sendMessageBotChannel(":clock1: Noch **" + days + "** Tag"+ (days==1?"":"e") + " bis zum Payback Start!");
            }
        } else if(days==0 && hours<12 && hours>0 && minutes==0 && seconds==0) {
            Bot.sendMessageBotChannel(":clock1: Noch **" + hours + "** Stunde" + (hours==1?"":"n") + " bis zum Payback Start!");
        } else if(days==0 && hours==0 && (minutes==20||minutes==10||(minutes<=5&&minutes>1)) && seconds==0) {
            Bot.sendMessageBotChannel(":clock1: Noch **" + minutes + "** Minuten bis zum Payback Start!");
        } else if(days==0 && hours==0 && minutes==1 && seconds==0) {
            Bot.sendMessageBotChannel(":clock1: Noch **EINE** Minute bis zum Payback Start!");
        }


        sendSubTitles(ChatColor.AQUA + "Payback startet in " + ChatColor.RED + days + ChatColor.RESET + " Tagen, " + ChatColor.RED + hours + ChatColor.RESET + " Stunden, " + ChatColor.RED + minutes + ChatColor.RESET + " Minuten, " + ChatColor.RED + seconds + ChatColor.RESET + " Sekunden");
        //sendSubTitles(days + " Tage, " +  hours  + " Stunden, "  + minutes + " Minuten, " + seconds + " Sekunden");
        for(Player p : Bukkit.getOnlinePlayers()) {
            Data.getInstance().setAlive(p.getName(), true);

            if(p.getGameMode()!=GameMode.CREATIVE) p.getInventory().clear();

            Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective objective = scoreboard.registerNewObjective("stats", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName("Payback");
            Score score16 = objective.getScore(ChatColor.GOLD + "" + ChatColor.UNDERLINE + p.getName());
            score16.setScore(14);
            Score score15 = objective.getScore(ChatColor.GOLD + "       ");
            score15.setScore(13);
            Score score13 = objective.getScore(ChatColor.DARK_AQUA + "[" + Data.getInstance().getTeam(p.getName())+"]");
            score13.setScore(12);
            Score score14 = objective.getScore(ChatColor.GOLD + "");
            score14.setScore(11);
            Score score9 = objective.getScore(ChatColor.GOLD + "Lebende Spieler: ");
            score9.setScore(10);
            Score score10 = objective.getScore(ChatColor.GREEN + (Data.getInstance().getPlayerCountAlive() + " "));
            score10.setScore(9);
            Score score11 = objective.getScore("     ");
            score11.setScore(8);
            Score score12 = objective.getScore(ChatColor.GOLD + "Lebende Teams: ");
            score12.setScore(7);
            Score score2 = objective.getScore(ChatColor.GREEN + (Data.getInstance().getAliveTeamCount() + ""));
            score2.setScore(6);
            Score score3 = objective.getScore(" ");
            score3.setScore(5);
            Score score4 = objective.getScore(ChatColor.GOLD + "Folge: ");
            score4.setScore(4);
            Score score5 = objective.getScore(ChatColor.GREEN + "" + Data.getInstance().getFolge(p.getName()));
            score5.setScore(3);
            Score score6 = objective.getScore("  ");
            score6.setScore(2);
            Score score7 = objective.getScore(ChatColor.GOLD + "Start in:");
            score7.setScore(1);
            String time = days+"d "+hours+"h "+minutes+"m "+seconds+"s";
            Score score8 = objective.getScore(ChatColor.GREEN + "" + time);
            score8.setScore(0);
            p.setScoreboard(scoreboard);
        }
    }

    private void tpPlayers() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(!p.getGameMode().equals(GameMode.CREATIVE) && Data.getInstance().isWhiteListed(p.getName())) {
                if(Data.getInstance().isWhiteListed(p.getName()) || p.isOp()) {
                    p.setGameMode(GameMode.ADVENTURE);
                    p.setSaturation(6);
                    p.setFoodLevel(20);
                    p.setHealth(20);
                    Location loc;


                    if(Data.getInstance().getPlayerLocation(p.getName())==null) {
                        loc = Bukkit.getWorlds().get(0).getSpawnLocation();
                    }
                        loc = Data.getInstance().getPlayerLocation(p.getName()).add(0.5,0,0.5);


                    if((p.getLocation().getBlockX()!=loc.getBlockX() || (p.getLocation().getBlockY()!=loc.getBlockY() && p.getLocation().getBlockY()!=loc.getBlockY()+1) || p.getLocation().getBlockZ()!=loc.getBlockZ())) {
                        p.teleport(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), p.getLocation().getYaw(), p.getLocation().getPitch()));
                    }
                } else {
                    p.kickPlayer("Du stehst nicht auf der Gästeliste!");
                }
            }
        }
    }

    private void sendTitles_CountDown(String msg) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + msg + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

            PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
            PacketPlayOutTitle length = new PacketPlayOutTitle(0, 10, 9);

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
        }
    }

    private void sendSubTitles(String msg) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + msg + "\"}"), (byte) 2);
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        }
    }
}
