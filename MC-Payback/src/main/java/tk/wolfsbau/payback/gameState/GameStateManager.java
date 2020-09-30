package tk.wolfsbau.payback.gameState;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import tk.wolfsbau.payback.db.Data;
import tk.wolfsbau.payback.db.Option;
import tk.wolfsbau.payback.discord_bot.Bot;
import tk.wolfsbau.payback.Payback;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class GameStateManager implements Listener {
    public static IGameState state;
    private long startMillis;

    private static GameStateManager instance;

    public enum SDFE {
        PREPARING(new GameState_preparing()), RUNNING(new GameState_running()), FINISHED(new GameState_finished());

        SDFE(IGameState state) {

        }
    }

    private GameStateManager() {

        Bukkit.getPluginManager().registerEvents(this, Payback.plugin);
        startMillis = Data.getInstance().getOption(Option.STARTTIME);
        state=GameState.PREPARING;


        Bukkit.getScheduler().scheduleSyncRepeatingTask(Payback.plugin, new Runnable() {
            @Override
            public void run() {
                if((Data.getInstance().getOption(Option.STARTTIME) - System.currentTimeMillis())<1000&&(Data.getInstance().getOption(Option.STARTTIME) - System.currentTimeMillis())>0) {

                    WorldBorder border = Bukkit.getWorld("world").getWorldBorder();
                    WorldBorder borderNether = Bukkit.getWorld("world_nether").getWorldBorder();
                    border.setDamageAmount(0.5);
                    border.setWarningDistance((int)3);//(Data.getInstance().getOption(Option.BORDERSHRINKAMOUNT)*2));
                    border.setWarningTime(15);
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        p.setGameMode(GameMode.SURVIVAL);
                        p.getInventory().clear();
                        p.playSound(p.getLocation(), Sound.LEVEL_UP, 255, 0);
                        Data.getInstance().setFolge(p.getName(), 1);

                        //abwesende Spieler bannen
                        for(String name : Data.getInstance().getPlayers()) {
                            boolean isOnline=false;
                            for(Player player : Bukkit.getOnlinePlayers()) {
                                if(player.getName().equalsIgnoreCase(name)) isOnline = true;
                            }
                            if(!isOnline) Data.getInstance().setAlive(name, false);
                        }

                        Bot.chatLog("```\n\nPAYBACK STARTET!\n\n```");
                        new BukkitRunnable() {
                            int left = (int)(long)(Data.getInstance().getOption(Option.DAILYTIME)*60);
                            public void run(){
                                if (left <= 0) {
                                    List<Entity> entitylist = p.getNearbyEntities(25, 25, 35);
                                    boolean canKick = true;
                                    for(Entity ent : entitylist){
                                        if(ent.getType()==EntityType.PLAYER){
                                            if(!Data.getInstance().getTeam(((Player)ent).getName()).equals(Data.getInstance().getTeam(p.getName()))) {
                                                canKick=false;
                                            }
                                        }
                                    }
                                    if(canKick) {
                                        GameState_running.kicked.add(p);
                                        p.kickPlayer("Du hast die Aufnahmezeit für diese Folge erreicht!");
                                        cancel();
                                    } else {
                                        left = 10;
                                        p.sendMessage(ChatColor.AQUA + "[Payback] " + ChatColor.RED + "Du kannst nicht gekickt werden, weil Gegner in deiner Nähe sind!");
                                    }
                                }

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
                                Score score10 = objective.getScore(ChatColor.GREEN + (Data.getInstance().getPlayerCountAlive() + "    "));
                                score10.setScore(9);
                                Score score11 = objective.getScore("     ");
                                score11.setScore(8);
                                Score score12 = objective.getScore(ChatColor.GOLD + "Lebende Teams: ");
                                score12.setScore(7);
                                Score score2 = objective.getScore(ChatColor.GREEN + (Data.getInstance().getAliveTeamCount() + " "));
                                score2.setScore(6);
                                Score score3 = objective.getScore(" ");
                                score3.setScore(5);
                                Score score4 = objective.getScore(ChatColor.GOLD + "Folge: ");
                                score4.setScore(4);
                                Score score5 = objective.getScore(ChatColor.GREEN + "" + Data.getInstance().getFolge(p.getName()));
                                score5.setScore(3);
                                Score score6 = objective.getScore("  ");
                                score6.setScore(2);
                                Score score7 = objective.getScore(ChatColor.GOLD + "Verbleibende Zeit:");
                                score7.setScore(1);
                                String time = TimeUnit.SECONDS.toMinutes(left) + ":" + (left-(TimeUnit.SECONDS.toMinutes(left)*60));

                                Score score8 = objective.getScore(ChatColor.GREEN + "" + time);
                                score8.setScore(0);

                                p.setScoreboard(scoreboard);

                                if(left==30||left==15)p.sendMessage(ChatColor.AQUA + "[Payback] " + ChatColor.RED + "Du wirst in " + time + " Sekunden gekickt!");
                                left--;
                            }
                        }.runTaskTimer(Payback.plugin, 0, 20);
                    }
                }
                if(Data.getInstance().getOption(Option.STARTTIME) < System.currentTimeMillis()&&Data.getInstance().getAliveTeamCount()>1) {
                    state = GameState.RUNNING;
                } else if(Data.getInstance().getOption(Option.STARTTIME) < System.currentTimeMillis()&&Data.getInstance().getAliveTeamCount()==1) {
                    state = GameState.FINISHED;
                } else {
                    state=GameState.PREPARING;
                }
                state.trigger();
            }
        }, 0L, 20L);
    }

    public static GameStateManager getInstance() {
        if(instance == null) instance = new GameStateManager();
        return instance;
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent event){
        if (event.toWeatherState()) {
                event.setCancelled(true);
                event.getWorld().setWeatherDuration(0);
                event.getWorld().setThundering(false);
        }
    }

    //TODO prevent villager trade of depth strider and infinity

    @EventHandler
    public void onEnchantClick(InventoryClickEvent e){
        if(e.getCurrentItem()!=null && e.getCurrentItem().getItemMeta()!=null && e.getCurrentItem().getItemMeta().hasEnchant(Enchantment.ARROW_INFINITE)) {
            ItemStack resultItem = e.getCurrentItem();
            ItemMeta meta = e.getCurrentItem().getItemMeta();
            meta.removeEnchant(Enchantment.ARROW_INFINITE);
            resultItem.setItemMeta(meta);
            e.setCurrentItem(resultItem);
        }
        if(e.getCurrentItem()!=null && e.getCurrentItem().getItemMeta()!=null && e.getCurrentItem().getItemMeta().hasEnchant(Enchantment.DEPTH_STRIDER)) {
            ItemStack resultItem = e.getCurrentItem();
            ItemMeta meta = e.getCurrentItem().getItemMeta();
            meta.removeEnchant(Enchantment.DEPTH_STRIDER);
            resultItem.setItemMeta(meta);
            e.setCurrentItem(resultItem);
        }
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent e){
        if(e.getEnchantsToAdd().containsKey(Enchantment.ARROW_INFINITE)) {
            e.getEnchantsToAdd().remove(Enchantment.ARROW_INFINITE);
        }
        if(e.getEnchantsToAdd().containsKey(Enchantment.DEPTH_STRIDER)) {
            e.getEnchantsToAdd().remove(Enchantment.DEPTH_STRIDER);
        }
    }

    @EventHandler
    public final void onVillagerTrade(InventoryClickEvent e) {
        Player player = (Player)e.getWhoClicked();
        Inventory inventory = e.getInventory();
        InventoryType.SlotType slotType = e.getSlotType();
        if (inventory.getType().equals(InventoryType.MERCHANT) && slotType == InventoryType.SlotType.RESULT) {
            ItemStack resultItem = e.getCurrentItem();
            if(resultItem.getItemMeta().hasEnchant(Enchantment.DEPTH_STRIDER)) {
                ItemMeta meta = resultItem.getItemMeta();
                meta.removeEnchant(Enchantment.DEPTH_STRIDER);
                resultItem.setItemMeta(meta);
                e.setCurrentItem(resultItem);
            }
            if(resultItem.getItemMeta().hasEnchant(Enchantment.ARROW_INFINITE)) {
                ItemMeta meta = resultItem.getItemMeta();
                meta.removeEnchant(Enchantment.ARROW_INFINITE);
                resultItem.setItemMeta(meta);
                e.setCurrentItem(resultItem);
            }

        }

    }
    
    @EventHandler
    public void onAnvilUse(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player player = (Player)e.getWhoClicked();
            if (e.getView().getTopInventory().getType() == InventoryType.ANVIL) {

                if (e.getClickedInventory() == e.getView().getTopInventory() && e.getSlot() == 2 && e.getCurrentItem().getEnchantments().containsKey(Enchantment.ARROW_INFINITE)) {
                    ItemStack stack = e.getCurrentItem();
                    ItemMeta meta = stack.getItemMeta();
                    meta.removeEnchant(Enchantment.ARROW_INFINITE);
                    stack.setItemMeta(meta);
                    e.setCurrentItem(stack);
                }
                if (e.getClickedInventory() == e.getView().getTopInventory() && e.getSlot() == 2 && e.getCurrentItem().getEnchantments().containsKey(Enchantment.DEPTH_STRIDER)) {
                    ItemStack stack = e.getCurrentItem();
                    ItemMeta meta = stack.getItemMeta();
                    meta.removeEnchant(Enchantment.DEPTH_STRIDER);
                    stack.setItemMeta(meta);
                    e.setCurrentItem(stack);
                }
            }

        }
    }

    @EventHandler
    public void craftItem(PrepareItemCraftEvent e) {
        state.craftItem(e);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {state.onItemDrop(e);}

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {state.onEntityExplode(event);}

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {state.onEntityDamage(event);}

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        state.
            onPlayerJoin(event);
    }

    @EventHandler
    public void onBrewEvent(BrewEvent event){state.onBrewEvent(event);}

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if (!event.isCancelled()) {
            ItemStack item = event.getItem();
            if (item.getType() == Material.POTION) {
                if(item.getDurability()!=8261 && item.getDurability() != 8229) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onSplash(PotionSplashEvent event) {
        event.setCancelled(true);
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {state.onPlayerQuit(event);}

    @EventHandler
    public void onPreLogin(PlayerLoginEvent event) {state.onPreLogin(event);}

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        state.onPlayerDeath(event);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        state.onBlockPlace(event);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        state.onBlockBreak(event);
    }

    @EventHandler
    public void onSignPlace(SignChangeEvent event) {
        state.onSignPlace(event);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        state.onRespawn(event);
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        state.onEntityDamageEntity(event);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {state.onChat(event);}

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {state.onPlayerInteract(event);}

    @EventHandler
    public void onHopper(InventoryMoveItemEvent event) {
        state.onHopper(event);
    }



}
