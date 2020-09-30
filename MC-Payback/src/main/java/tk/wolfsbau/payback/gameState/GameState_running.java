package tk.wolfsbau.payback.gameState;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
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
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.bukkit.util.Vector;
import tk.wolfsbau.payback.db.Data;
import tk.wolfsbau.payback.db.Option;
import tk.wolfsbau.payback.discord_bot.Bot;
import tk.wolfsbau.payback.Payback;
import tk.wolfsbau.payback.cmd.Cmd_strike;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GameState_running implements IGameState {
    private ArrayList<Player> invincible = new ArrayList<>();
    public static ArrayList<Player> kicked = new ArrayList<>();

    static boolean vorbei = false;
    boolean bordershrink = false;

    private static GameState_running instance;

    public static GameState_running getInstance() {
        if(instance==null) instance = new GameState_running();
        return instance;
    }

    int count = 0;
    int c2=0;

    @Override
    public void trigger() {
        setGamemode();

        for(World world : Bukkit.getWorlds()) {
            WorldBorder border = world.getWorldBorder();

            border.setCenter(Data.getInstance().getOption(Option.MITTEX), Data.getInstance().getOption(Option.MITTEZ));
            border.setDamageAmount(0.5);

            if ((Data.getInstance().getDay() >= Data.getInstance().getOption(Option.BORDERSTARTSHRINKDAY) && c2 == 2) || border.getSize() != Data.getInstance().getBorderRadius()) {
                border.setSize(Data.getInstance().getBorderRadius(), 1);
                c2 = 0;
            }
        }


        c2++;

        if(count>30) {
            count = 0;
            System.out.println("Bordersize: " + Data.getInstance().getBorderRadius());
        }
        count++;
        if(Data.getInstance().getDay()==Data.getInstance().getOption(Option.BORDERSTARTSHRINKDAY) && !bordershrink) {
            bordershrink=true;
            Bot.sendMessageBotChannel("´´´ \n   Die Border fangt nun an zu schrumpfen!\n    Jeden Tag wird sie um "
                    + Data.getInstance().getOption(Option.BORDERSHRINKAMOUNT)
                    + " Blöcke kleiner.\n ´´´");
        }

    }


    private void setGamemode() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getGameMode().equals(GameMode.ADVENTURE)) p.setGameMode(GameMode.SURVIVAL);
            if(p.getGameMode().equals(GameMode.SURVIVAL)) Data.getInstance().setPlayerLocation(p.getName(), p.getLocation());
        }
    }


    @Override
    public void onItemDrop(PlayerDropItemEvent e) {

    }

    @Override
    public void onEntityExplode(EntityExplodeEvent event) {
        Location loc=event.getLocation();
        List<Block> destroyed = event.blockList();
        Iterator<Block> it = destroyed.iterator();
        while (it.hasNext()) {
            Block block = it.next();
            Location bloc = block.getLocation();
            Material mat = block.getType();

            double x = 0;
            double y = 0;
            double z = 0;

            x = loc.getX() - bloc.getX();
            y = loc.getY() - bloc.getY();
            z = loc.getZ() - bloc.getZ();

            x = -x * 0.3 * Math.random();
            y = y * 0.5 * Math.random() + 0.5;
            z = -z * 0.3 * Math.random();

            @SuppressWarnings("deprecation")
            FallingBlock fallingb = bloc.getWorld().spawnFallingBlock(bloc, mat, (byte) 0);
            fallingb.setVelocity(new Vector(x, y, z));
        }
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        if(invincible.contains(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if(p.isOp()) return;
        if (!Data.getInstance().isWhiteListed(event.getPlayer().getName())) {
            p.kickPlayer("Du stehst nicht auf der Gästeliste!");
            return;
        } else if (!Data.getInstance().isAlive(event.getPlayer().getName())) {
            p.kickPlayer("Du bist schon tot!");
            return;
        } else if (Data.getInstance().getFolgeMax() <= (Data.getInstance().getFolge(p.getName()))) {
            p.kickPlayer("Du hast schon die maximale Anzahl an Folgen vorproduziert!");
            return;
        } else {
            if(Data.getInstance().isClearInv(p.getName())) {
                p.getInventory().clear();
                Data.getInstance().setClearInv(p.getName(), false);
            }
            int folge = Data.getInstance().getFolge(event.getPlayer().getName());
            folge++;
            Data.getInstance().setFolge(p.getName(), folge);
            try {
                Bot.sendMessageBotChannel(":heavy_plus_sign:   **" + p.getName() + "** hat den Server betreten! \n" +
                        " Folge: **" + Data.getInstance().getFolge(p.getName()) + "**");
            } catch(Exception e) {}
            invincible.add(p);
                new BukkitRunnable() {
                    int left = (int) (long) (Data.getInstance().getOption(Option.DAILYTIME) * 60)+15;
                    int schutzzeit = 15;
                    Location loc = p.getLocation();

                    public void run() {
                        if(!GameStateManager.state.equals(GameState.RUNNING)||vorbei) {
                            cancel();
                        }
                        if(schutzzeit>=0) {
                            p.teleport(new Location(loc.getWorld(), loc.getX(), loc.getY(),
                                    loc.getZ(), p.getLocation().getYaw(), p.getLocation().getPitch()));
                            if(schutzzeit==15||schutzzeit==10||schutzzeit<=5) {
                                p.sendMessage(ChatColor.RED + "Du bist noch " + schutzzeit + " Sekunden in der Schutzzeit!");
                            }
                            schutzzeit--;
                        } else {
                            invincible.remove(p);
                        }
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
                                kicked.add(p);
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
                        objective.setDisplayName("PAYBACK");
                        Score score16 = objective.getScore(ChatColor.GOLD + "" + ChatColor.UNDERLINE + p.getName());
                        score16.setScore(16);
                        Score score15 = objective.getScore(ChatColor.GOLD + "       ");
                        score15.setScore(15);
                        Score score13 = objective.getScore(ChatColor.GOLD + "Border: " + ChatColor.GREEN + (int)(Data.getInstance().getBorderRadius()/2) + " Blöcke");
                        score13.setScore(14);
                        Score score14 = objective.getScore(ChatColor.RESET + " " + ChatColor.GREEN);
                        score14.setScore(13);
                        Score score17 = objective.getScore(ChatColor.DARK_AQUA + "[" + Data.getInstance().getTeam(p.getName())+"]");
                        score17.setScore(12);
                        Score score18 = objective.getScore(ChatColor.GOLD + "");
                        score18.setScore(11);
                        Score score9 = objective.getScore(ChatColor.GOLD + "Lebende Spieler: ");
                        score9.setScore(10);
                        Score score10 = objective.getScore(ChatColor.GREEN + (Data.getInstance().getPlayerCountAlive() + "  "));
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
                        String time = TimeUnit.SECONDS.toMinutes(left) + ":" + (left - (TimeUnit.SECONDS.toMinutes(left) * 60));

                        Score score8 = objective.getScore(ChatColor.GREEN + "" + time);
                        score8.setScore(0);

                        p.setScoreboard(scoreboard);
                        if(left==30||left==15)p.sendMessage(ChatColor.AQUA + "[Payback] " + ChatColor.RED + "Du wirst in " + time + " Sekunden gekickt!");

                        left--;
                    }
                }.runTaskTimer(Payback.plugin, 0, 20);
        }
    }

    @Override
    public void craftItem(PrepareItemCraftEvent e) {
        Material itemType = e.getRecipe().getResult().getType();
        Byte itemData = e.getRecipe().getResult().getData().getData();
        if (itemType == Material.GOLDEN_APPLE && itemData == 1) {
            e.getInventory().setResult(new ItemStack(Material.AIR));
            Iterator var5 = e.getViewers().iterator();

            while(var5.hasNext()) {
                HumanEntity he = (HumanEntity)var5.next();
                if (he instanceof Player) {
                    ((Player)he).sendMessage(ChatColor.RED + "You cannot craft this!");
                }
            }
        }

    }

    @Override
    public void onBrewEvent(BrewEvent event) {

        ItemStack ingredient = new ItemStack(event.getContents().getIngredient().getType(), 1);
        ItemStack[] stacks = new ItemStack[3];
        for (int i=0; i<stacks.length; i++) {
            ItemStack stack = event.getContents().getItem(i);
            if (stack != null) {
                stacks[i] = new ItemStack(stack.getType(), 1);
                            }
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (event.getContents() != null) {
                    BrewerInventory inventory = event.getContents();

                    int size = inventory.getSize();
                    boolean shouldSetIngredientBack = false;

                    for (int i = 0; i<size; i++) {
                        ItemStack aStack = inventory.getItem(i);
                        if (aStack == null) {
                            continue;
                        }

                        if (shouldSetIngredientBack) {
                            inventory.setItem(i, stacks[i]);
                            continue;
                        }


                        if (aStack.getType() == Material.POTION) {
                            Potion p = Potion.fromItemStack(aStack);

                            if (p.getType() == PotionType.STRENGTH) {
                                //CANCELL BUT WE CANT! Cause the event is now ended in reality...
                                inventory.setItem(i, stacks[i]);
                                shouldSetIngredientBack = true;
                            }
                        }
                    }

                    if (shouldSetIngredientBack) {
                        inventory.setIngredient(ingredient);
                    }
                }
            }
        }, 1);
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
        if(block.getType().equals(Material.WALL_SIGN)) {
            org.bukkit.block.Sign sign = (org.bukkit.block.Sign) block.getState();
            String text = sign.getLine(0) + sign.getLine(1) + sign.getLine(2) + sign.getLine(3);
            for (String s : Data.getInstance().getTeamListAlive()) {
                String team = ChatColor.stripColor(s.split(" ")[0]);
                if (text.contains(team)) {
                    Player player = event.getPlayer();
                    if (Data.getInstance().getTeam(player.getName()).equalsIgnoreCase(team)) {
                        player.sendMessage(ChatColor.AQUA + "[Payback] " + ChatColor.YELLOW + "Deine Teamkiste wurde entsperrt!");
                        Data.getInstance().setChestLocation(team, null);
                    } else {
                        player.sendMessage(ChatColor.AQUA + "[Payback] " + ChatColor.RED + "Du darfst das nicht zerstören!");
                        event.setCancelled(true);
                    }
                }
            }
        } else if(block.getType().equals(Material.CHEST)) {
            Chest chest = (Chest) block.getState();
            InventoryHolder ih = chest.getInventory().getHolder();
            if(ih instanceof DoubleChest) {
                ArrayList<Block> b = new ArrayList<>();
                if(block.getRelative(BlockFace.NORTH).getType().equals(Material.WALL_SIGN)) {
                    b.add(block.getRelative(BlockFace.NORTH));
                } else if(block.getRelative(BlockFace.EAST).getType().equals(Material.WALL_SIGN)) {
                    b.add(block.getRelative(BlockFace.EAST));
                } else if(block.getRelative(BlockFace.SOUTH).getType().equals(Material.WALL_SIGN)) {
                    b.add(block.getRelative(BlockFace.SOUTH));
                } else if(block.getRelative(BlockFace.WEST).getType().equals(Material.WALL_SIGN)) {
                    b.add(block.getRelative(BlockFace.WEST));
                }

                if(getOtherChestPart(block).getRelative(BlockFace.NORTH).getType().equals(Material.WALL_SIGN)) {
                    b.add(getOtherChestPart(block).getRelative(BlockFace.NORTH));
                } else if(getOtherChestPart(block).getRelative(BlockFace.EAST).getType().equals(Material.WALL_SIGN)) {
                    b.add(getOtherChestPart(block).getRelative(BlockFace.EAST));
                } else if(getOtherChestPart(block).getRelative(BlockFace.SOUTH).getType().equals(Material.WALL_SIGN)) {
                    b.add(getOtherChestPart(block).getRelative(BlockFace.SOUTH));
                } else if(getOtherChestPart(block).getRelative(BlockFace.WEST).getType().equals(Material.WALL_SIGN)) {
                    b.add(getOtherChestPart(block).getRelative(BlockFace.WEST));
                }

                for(Block blocks : b) {
                    org.bukkit.block.Sign sign = (org.bukkit.block.Sign) blocks.getState();
                    String text = sign.getLine(0) + sign.getLine(1) + sign.getLine(2) + sign.getLine(3);

                    for (String string : Data.getInstance().getTeamListAlive()) {
                        String team = ChatColor.stripColor(string.split(" ")[0]);
                        if (text.contains(team)) {
                            Player player = event.getPlayer();
                            if (Data.getInstance().getTeam(player.getName()).equalsIgnoreCase(team)) {
                                player.sendMessage(ChatColor.AQUA + "[Payback] " + ChatColor.YELLOW + "Du hast deine Teamkiste zerstört!");
                                Data.getInstance().setChestLocation(team, null);
                            } else {
                                player.sendMessage(ChatColor.AQUA + "[Payback] " + ChatColor.RED + "Du darfst das nicht zerstören!");
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            } else {
                ArrayList<Block> b = new ArrayList<>();
                if(block.getRelative(BlockFace.NORTH).getType().equals(Material.WALL_SIGN)) {
                    b.add(block.getRelative(BlockFace.NORTH));
                } else if(block.getRelative(BlockFace.EAST).getType().equals(Material.WALL_SIGN)) {
                    b.add(block.getRelative(BlockFace.EAST));
                } else if(block.getRelative(BlockFace.SOUTH).getType().equals(Material.WALL_SIGN)) {
                    b.add(block.getRelative(BlockFace.SOUTH));
                } else if(block.getRelative(BlockFace.WEST).getType().equals(Material.WALL_SIGN)) {
                    b.add(block.getRelative(BlockFace.WEST));
                }

                for(Block blocks : b) {
                    org.bukkit.block.Sign sign = (org.bukkit.block.Sign) blocks.getState();
                    String text = sign.getLine(0) + sign.getLine(1) + sign.getLine(2) + sign.getLine(3);

                    for (String string : Data.getInstance().getTeamListAlive()) {
                        String team = ChatColor.stripColor(string.split(" ")[0]);
                        if (text.contains(team)) {
                            Player player = event.getPlayer();
                            if (Data.getInstance().getTeam(player.getName()).equalsIgnoreCase(team)) {
                                player.sendMessage(ChatColor.AQUA + "[Payback] " + ChatColor.YELLOW + "Du hast deine Teamkiste zerstört!");
                                Data.getInstance().setChestLocation(team, null);
                            } else {
                                player.sendMessage(ChatColor.AQUA + "[Payback] " + ChatColor.RED + "Du darfst das nicht zerstören!");
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    private Block getOtherChestPart(Block block) {
        if(block.getRelative(BlockFace.NORTH).getType().equals(Material.CHEST)) {
            return block.getRelative(BlockFace.NORTH);
        } else if(block.getRelative(BlockFace.EAST).getType().equals(Material.CHEST)) {
            return block.getRelative(BlockFace.EAST);
        } else if(block.getRelative(BlockFace.SOUTH).getType().equals(Material.CHEST)) {
            return block.getRelative(BlockFace.SOUTH);
        } else if(block.getRelative(BlockFace.WEST).getType().equals(Material.CHEST)) {
            return block.getRelative(BlockFace.WEST);
        }
        return null;
    }



    @Override
    public void onSignPlace(SignChangeEvent event) {
        String text = event.getLine(0) + event.getLine(1) + event.getLine(2) + event.getLine(3);
        Player player = event.getPlayer();
        Data data = Data.getInstance();
        String team = data.getTeam(player.getName());
        if(text.contains(team)) {
            Location chestLocation = data.getChestLocation(team);
            if(chestLocation !=null) {
                Bukkit.getWorld("world").getBlockAt(chestLocation).breakNaturally();
                player.sendMessage(ChatColor.AQUA + "[Payback] " + ChatColor.YELLOW + "Deine letzte Teamkiste wurde entsperrt!");
            }
            data.setChestLocation(team, event.getBlock().getLocation());
            player.sendMessage(ChatColor.AQUA + "[Payback] " + ChatColor.YELLOW + "Die Kiste wurde gesperrt!");
        }

        for (String s : Data.getInstance().getTeamListAlive()) {
            String team2 = ChatColor.stripColor(s.split(" ")[0]);

            if (text.contains(team2)) {
                if (!team.equalsIgnoreCase(team2)) {
                    System.out.println(team + " != " + team2);
                    event.getBlock().breakNaturally();
                }
            }
        }

    }



    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getClickedBlock()==null) {
            return;
        }
        Block block = event.getClickedBlock();
        if(block.getType().equals(Material.CHEST)) {
            Chest chest = (Chest) block.getState();
            InventoryHolder ih = chest.getInventory().getHolder();
            if(ih instanceof DoubleChest) {
                ArrayList<Block> b = new ArrayList<>();
                if(block.getRelative(BlockFace.NORTH).getType().equals(Material.WALL_SIGN)) {
                    b.add(block.getRelative(BlockFace.NORTH));
                } else if(block.getRelative(BlockFace.EAST).getType().equals(Material.WALL_SIGN)) {
                    b.add(block.getRelative(BlockFace.EAST));
                } else if(block.getRelative(BlockFace.SOUTH).getType().equals(Material.WALL_SIGN)) {
                    b.add(block.getRelative(BlockFace.SOUTH));
                } else if(block.getRelative(BlockFace.WEST).getType().equals(Material.WALL_SIGN)) {
                    b.add(block.getRelative(BlockFace.WEST));
                }

                if(getOtherChestPart(block).getRelative(BlockFace.NORTH).getType().equals(Material.WALL_SIGN)) {
                    b.add(getOtherChestPart(block).getRelative(BlockFace.NORTH));
                } else if(getOtherChestPart(block).getRelative(BlockFace.EAST).getType().equals(Material.WALL_SIGN)) {
                    b.add(getOtherChestPart(block).getRelative(BlockFace.EAST));
                } else if(getOtherChestPart(block).getRelative(BlockFace.SOUTH).getType().equals(Material.WALL_SIGN)) {
                    b.add(getOtherChestPart(block).getRelative(BlockFace.SOUTH));
                } else if(getOtherChestPart(block).getRelative(BlockFace.WEST).getType().equals(Material.WALL_SIGN)) {
                    b.add(getOtherChestPart(block).getRelative(BlockFace.WEST));
                }

                for(Block blocks : b) {
                    org.bukkit.block.Sign sign = (org.bukkit.block.Sign) blocks.getState();
                    String text = sign.getLine(0) + sign.getLine(1) + sign.getLine(2) + sign.getLine(3);

                    for (String string : Data.getInstance().getTeamListAlive()) {
                        String team = ChatColor.stripColor(string.split(" ")[0]);
                        if (text.contains(team)) {
                            Player player = event.getPlayer();
                            if (Data.getInstance().getTeam(player.getName()).equalsIgnoreCase(team)) {

                            } else {
                                player.sendMessage(ChatColor.AQUA + "[Payback] " + ChatColor.RED + "Du darfst diese Kiste nicht verwenden!");
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            } else {
                ArrayList<Block> b = new ArrayList<>();
                if(block.getRelative(BlockFace.NORTH).getType().equals(Material.WALL_SIGN)) {
                    b.add(block.getRelative(BlockFace.NORTH));
                } else if(block.getRelative(BlockFace.EAST).getType().equals(Material.WALL_SIGN)) {
                    b.add(block.getRelative(BlockFace.EAST));
                } else if(block.getRelative(BlockFace.SOUTH).getType().equals(Material.WALL_SIGN)) {
                    b.add(block.getRelative(BlockFace.SOUTH));
                } else if(block.getRelative(BlockFace.WEST).getType().equals(Material.WALL_SIGN)) {
                    b.add(block.getRelative(BlockFace.WEST));
                }

                for(Block blocks : b) {
                    org.bukkit.block.Sign sign = (org.bukkit.block.Sign) blocks.getState();
                    String text = sign.getLine(0) + sign.getLine(1) + sign.getLine(2) + sign.getLine(3);
                    System.out.println("Locked Chest broken");
                    for (String string : Data.getInstance().getTeamListAlive()) {
                        String team = ChatColor.stripColor(string.split(" ")[0]);
                        if (text.contains(team)) {
                            Player player = event.getPlayer();
                            if (Data.getInstance().getTeam(player.getName()).equalsIgnoreCase(team)) {

                            } else {
                                player.sendMessage(ChatColor.AQUA + "[Payback] " + ChatColor.RED + "Du darfst diese Kiste nicht verwenden!");
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onHopper(InventoryMoveItemEvent event) {
        if(event.getSource().getHolder() instanceof Chest) {
            Block from = ((Chest) event.getSource().getHolder()).getBlock();

                ArrayList<Block> b = new ArrayList<>();
                if(from.getRelative(BlockFace.NORTH).getType().equals(Material.WALL_SIGN)) {
                    b.add(from.getRelative(BlockFace.NORTH));
                } else if(from.getRelative(BlockFace.EAST).getType().equals(Material.WALL_SIGN)) {
                    b.add(from.getRelative(BlockFace.EAST));
                } else if(from.getRelative(BlockFace.SOUTH).getType().equals(Material.WALL_SIGN)) {
                    b.add(from.getRelative(BlockFace.SOUTH));
                } else if(from.getRelative(BlockFace.WEST).getType().equals(Material.WALL_SIGN)) {
                    b.add(from.getRelative(BlockFace.WEST));
                }

                for(Block blocks : b) {
                    org.bukkit.block.Sign sign = (org.bukkit.block.Sign) blocks.getState();
                    String text = sign.getLine(0) + sign.getLine(1) + sign.getLine(2) + sign.getLine(3);

                    for (String string : Data.getInstance().getTeamListAlive()) {
                        String team = ChatColor.stripColor(string.split(" ")[0]);
                        if (text.contains(team)) {
                            event.setCancelled(true);
                            ((Hopper)event.getDestination().getHolder()).getBlock().breakNaturally();
                        }
                    }
                }

        } else if(event.getSource().getHolder() instanceof DoubleChest) {
            Block from = ((Chest)((DoubleChest) event.getSource().getHolder()).getLeftSide()).getBlock();

            ArrayList<Block> b = new ArrayList<>();
            if(from.getRelative(BlockFace.NORTH).getType().equals(Material.WALL_SIGN)) {
                b.add(from.getRelative(BlockFace.NORTH));
            } else if(from.getRelative(BlockFace.EAST).getType().equals(Material.WALL_SIGN)) {
                b.add(from.getRelative(BlockFace.EAST));
            } else if(from.getRelative(BlockFace.SOUTH).getType().equals(Material.WALL_SIGN)) {
                b.add(from.getRelative(BlockFace.SOUTH));
            } else if(from.getRelative(BlockFace.WEST).getType().equals(Material.WALL_SIGN)) {
                b.add(from.getRelative(BlockFace.WEST));
            }

            if(getOtherChestPart(from)!=null) {
                if (getOtherChestPart(from).getRelative(BlockFace.NORTH).getType().equals(Material.WALL_SIGN)) {
                    b.add(getOtherChestPart(from).getRelative(BlockFace.NORTH));
                } else if (getOtherChestPart(from).getRelative(BlockFace.EAST).getType().equals(Material.WALL_SIGN)) {
                    b.add(getOtherChestPart(from).getRelative(BlockFace.EAST));
                } else if (getOtherChestPart(from).getRelative(BlockFace.SOUTH).getType().equals(Material.WALL_SIGN)) {
                    b.add(getOtherChestPart(from).getRelative(BlockFace.SOUTH));
                } else if (getOtherChestPart(from).getRelative(BlockFace.WEST).getType().equals(Material.WALL_SIGN)) {
                    b.add(getOtherChestPart(from).getRelative(BlockFace.WEST));
                }
            }

            for(Block blocks : b) {
                org.bukkit.block.Sign sign = (org.bukkit.block.Sign) blocks.getState();
                String text = sign.getLine(0) + sign.getLine(1) + sign.getLine(2) + sign.getLine(3);

                for (String string : Data.getInstance().getTeamListAlive()) {
                    String team = ChatColor.stripColor(string.split(" ")[0]);
                    if (text.contains(team)) {
                        event.setCancelled(true);
                        ((Hopper)event.getDestination().getHolder()).getBlock().breakNaturally();
                    }
                }
            }
        }

        if(event.getDestination().getHolder() instanceof Chest) {
            Block to = ((Chest) event.getDestination().getHolder()).getBlock();

            ArrayList<Block> b = new ArrayList<>();
            if(to.getRelative(BlockFace.NORTH).getType().equals(Material.WALL_SIGN)) {
                b.add(to.getRelative(BlockFace.NORTH));
            } else if(to.getRelative(BlockFace.EAST).getType().equals(Material.WALL_SIGN)) {
                b.add(to.getRelative(BlockFace.EAST));
            } else if(to.getRelative(BlockFace.SOUTH).getType().equals(Material.WALL_SIGN)) {
                b.add(to.getRelative(BlockFace.SOUTH));
            } else if(to.getRelative(BlockFace.WEST).getType().equals(Material.WALL_SIGN)) {
                b.add(to.getRelative(BlockFace.WEST));
            }

            for(Block blocks : b) {
                org.bukkit.block.Sign sign = (org.bukkit.block.Sign) blocks.getState();
                String text = sign.getLine(0) + sign.getLine(1) + sign.getLine(2) + sign.getLine(3);

                for (String string : Data.getInstance().getTeamListAlive()) {
                    String team = ChatColor.stripColor(string.split(" ")[0]);
                    if (text.contains(team)) {
                        event.setCancelled(true);
                        ((Hopper)event.getSource().getHolder()).getBlock().breakNaturally();
                    }
                }
            }

        } else if(event.getSource().getHolder() instanceof DoubleChest) {
            Block from = ((Chest)
                    ((DoubleChest)
                            event.getDestination()
                                    .getHolder())
                                    .getLeftSide())
                                    .getBlock();

            ArrayList<Block> b = new ArrayList<>();
            if(from.getRelative(BlockFace.NORTH).getType().equals(Material.WALL_SIGN)) {
                b.add(from.getRelative(BlockFace.NORTH));
            } else if(from.getRelative(BlockFace.EAST).getType().equals(Material.WALL_SIGN)) {
                b.add(from.getRelative(BlockFace.EAST));
            } else if(from.getRelative(BlockFace.SOUTH).getType().equals(Material.WALL_SIGN)) {
                b.add(from.getRelative(BlockFace.SOUTH));
            } else if(from.getRelative(BlockFace.WEST).getType().equals(Material.WALL_SIGN)) {
                b.add(from.getRelative(BlockFace.WEST));
            }

            if(getOtherChestPart(from)!=null) {
                if (getOtherChestPart(from).getRelative(BlockFace.NORTH).getType().equals(Material.WALL_SIGN)) {
                    b.add(getOtherChestPart(from).getRelative(BlockFace.NORTH));
                } else if (getOtherChestPart(from).getRelative(BlockFace.EAST).getType().equals(Material.WALL_SIGN)) {
                    b.add(getOtherChestPart(from).getRelative(BlockFace.EAST));
                } else if (getOtherChestPart(from).getRelative(BlockFace.SOUTH).getType().equals(Material.WALL_SIGN)) {
                    b.add(getOtherChestPart(from).getRelative(BlockFace.SOUTH));
                } else if (getOtherChestPart(from).getRelative(BlockFace.WEST).getType().equals(Material.WALL_SIGN)) {
                    b.add(getOtherChestPart(from).getRelative(BlockFace.WEST));
                }
            }

            for(Block blocks : b) {
                org.bukkit.block.Sign sign = (org.bukkit.block.Sign) blocks.getState();
                String text = sign.getLine(0) + sign.getLine(1) + sign.getLine(2) + sign.getLine(3);

                for (String string : Data.getInstance().getTeamListAlive()) {
                    String team = ChatColor.stripColor(string.split(" ")[0]);
                    if (text.contains(team)) {
                        event.setCancelled(true);
                        ((Hopper)event.getSource().getHolder()).getBlock().breakNaturally();
                    }
                }
            }
        }
    }



    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        if(kicked.contains(event.getPlayer())) {
            kicked.remove(event.getPlayer());
            try {
                Bot.sendMessageBotChannel(":heavy_minus_sign:   **" + event.getPlayer().getName() + "** hat den Server verlassen! \n" +
                    " Folge: **" + Data.getInstance().getFolge(event.getPlayer().getName()) + "**  -   " + dtf.format(now));
            } catch(Exception e) {}
                event.setQuitMessage(ChatColor.AQUA + "[Payback] " + ChatColor.YELLOW + event.getPlayer().getName() + ChatColor.RED + " wurde gekickt!");
        } else {
            try {
                Bot.sendMessageBotChannel(":heavy_minus_sign:   **" + event.getPlayer().getName() + "** hat den Server **vorzeitig** verlassen! \n" +
                    " Folge: **" + Data.getInstance().getFolge(event.getPlayer().getName()) + "**  -   " + dtf.format(now));
            } catch(Exception e) {}
            event.setQuitMessage(ChatColor.AQUA + "[Payback] " + ChatColor.RED + event.getPlayer().getName() + ChatColor.DARK_RED + " hat den Server " + ChatColor.RED + "vorzeitig " + ChatColor.DARK_RED + "verlassen!");
            boolean canKick = true;
            List<Entity> entitylist =event.getPlayer().getNearbyEntities(25, 25, 35);
            for(Entity ent : entitylist){
                if(ent.getType()==EntityType.PLAYER){
                    if(!Data.getInstance().getTeam(((Player)ent).getName()).equals(Data.getInstance().getTeam(event.getPlayer().getName()))) {
                        canKick=false;
                    }
                }
            }
            if(!canKick) Cmd_strike.strike(event.getPlayer().getName(), "Ausloggen im Kampf");
        }
        Bukkit.getScheduler().runTaskLater(Payback.plugin, new BukkitRunnable() {
            @Override
            public void run() {
                if(Bukkit.getOnlinePlayers().size()==0) {
                    Bukkit.shutdown();
                }
            }
        }, 20L);
    }

    @Override
    public void onPreLogin(PlayerLoginEvent event) {
        if(event.getPlayer().isOp()) return;
        if (!Data.getInstance().isWhiteListed(event.getPlayer().getName())) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Du stehst nicht auf der Gästeliste!");
            return;
        } else if (!Data.getInstance().isAlive(event.getPlayer().getName())) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Du bist schon tot!");
            return;
        } else if (Data.getInstance().getFolgeMax() <= (Data.getInstance().getFolge(event.getPlayer().getName()))) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Du hast schon die maximale Anzahl an Folgen vorproduziert!");
            return;
        }
    }


    @Override
    public void onPlayerDeath(PlayerDeathEvent event) {
        Data.getInstance().setAlive(event.getEntity().getName(), false);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try {
            Bot.sendMessageBotChannel(
                    ":dizzy_face: " + dtf.format(now) + "** " + event.getEntity().getName() + " [" + Data.getInstance().getTeam(event.getEntity().getName()) + "]"
                            + "** ist nach " + Data.getInstance().getFolge(event.getEntity().getName())
                            + " Folgen aus Minecraft Payback ausgeschieden! \n" + "```" + event.getDeathMessage() + "```");
        } catch(Exception e) {}
        String msg = event.getDeathMessage();

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(
                ChatColor.AQUA + "[Payback] " + ChatColor.WHITE + dtf.format(now));
        Bukkit.broadcastMessage(
                ChatColor.YELLOW + "    " + event.getEntity().getName() + " ist nach "
                        + Data.getInstance().getFolge(event.getEntity().getName())
                        + " Folgen aus Minecraft Payback ausgeschieden!");
        Bukkit.broadcastMessage("    ->" + msg);
        Bukkit.broadcastMessage("");



        event.setDeathMessage("");
        //SetSlots.setSlots(Data.getInstance().getPlayerCountAlive());

        if(Data.getInstance().getAliveTeamCount()==1) {
            String winnerTeam=Data.getInstance().getTeamListAlive().get(0).split(" ")[0];
            winnerTeam=ChatColor.stripColor(winnerTeam);

            for(Player p : Bukkit.getOnlinePlayers()) {
                if(Data.getInstance().isAlive(p.getName())) {
                    sendTitle(
                            p,
                            "Sieger!",
                            ChatColor.AQUA + winnerTeam + ChatColor.YELLOW + " hat gewonnen!",
                            0,
                            15,
                            3
                    );
                }
            }
            Bukkit.broadcastMessage("Team " + winnerTeam + " hat gewonnen!" );
            vorbei=true;
            try {
                Bot.chatLog("```\n\nPAYBACK IST BEENDET!\n\n```");
            } catch(Exception e) {}
            try {
                Bot.sendMessageBotChannel("```\n\nPAYBACK IST ZUENDE!\nTeam " + winnerTeam + " hat gewonnen!\n\n```");
            } catch(Exception e) {}
        }
    }

    public void endeNachBan() {
        if(Data.getInstance().getAliveTeamCount()==1) {
            String winnerTeam=Data.getInstance().getTeamListAlive().get(0).split(" ")[0];
            winnerTeam=ChatColor.stripColor(winnerTeam);

            for(Player p : Bukkit.getOnlinePlayers()) {
                if(Data.getInstance().isAlive(p.getName())) {
                    sendTitle(
                            p,
                            "Sieger!",
                            ChatColor.AQUA + winnerTeam + ChatColor.YELLOW + " hat gewonnen!",
                            0,
                            15,
                            3
                    );
                }
            }
            Bukkit.broadcastMessage("Team " + winnerTeam + " hat gewonnen!" );
            vorbei=true;
            try {
                Bot.chatLog("```\n\nPAYBACK IST BEENDET!\n\n```");
            } catch(Exception e) {}
            try {
                Bot.sendMessageBotChannel("```\n\nPAYBACK IST ZUENDE!\nTeam " + winnerTeam + " hat gewonnen!\n\n```");
            } catch(Exception e) {}
        }
    }

    @Override
    public void onRespawn(PlayerRespawnEvent event) {
        kicked.add(event.getPlayer());
        new BukkitRunnable() {
            @Override
            public void run() {
                event.getPlayer().kickPlayer(ChatColor.RED + "Du bist in der " + Data.getInstance().getFolge(event.getPlayer().getName()) + " Folge aus PAYBACK ausgeschieden");
            }
        }.runTaskLater(Payback.plugin, 1L);
    }

    @Override
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if (Data.getInstance().getTeam(((Player) event.getDamager()).getName()).equalsIgnoreCase(Data.getInstance().getTeam(((Player) event.getEntity()).getName()))) {
                event.setCancelled(true);
            }
            if(invincible.contains(event.getDamager())) {
                event.setCancelled(true);
            }
        }
        if((event.getDamager() instanceof Snowball || event.getDamager() instanceof Egg || event.getDamager() instanceof FishHook)) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        event.setFormat(ChatColor.GRAY + "[" + ChatColor.DARK_AQUA + Data.getInstance().getTeam(p.getName()) + ChatColor.GRAY + "] " + ChatColor.AQUA + p.getName() + ChatColor.GRAY + ": " + ChatColor.WHITE + event.getMessage());
        try {
            Bot.chatLog("[**"+Data.getInstance().getTeam(p.getName()) + "**] **"+p.getName() + "**: " + event.getMessage());
        } catch(Exception e) {}
    }


    public void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Class<?> getNMSClass(String name) {
        // org.bukkit.craftbukkit.v1_8_R3...
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        try {
            Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
            Object enumSubTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
            Object chat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
            Object subchat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");
            Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
            Object packet = titleConstructor.newInstance(enumTitle, chat, fadeIn, stay, fadeOut);
            Object packet2 = titleConstructor.newInstance(enumSubTitle, subchat, fadeIn, stay, fadeOut);
            sendPacket(player, packet);
            sendPacket(player, packet2);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
