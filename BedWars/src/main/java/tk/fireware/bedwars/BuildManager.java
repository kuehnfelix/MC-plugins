package tk.fireware.bedwars;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.BlockState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;


import java.util.*;

@SuppressWarnings("deprecation")
public class BuildManager implements Listener {
    private static BuildManager instance;

    private List<UUID> builders = new ArrayList<>();

    private HashMap<UUID, Boolean> mainMenuOpened = new HashMap<>();
    private HashMap<UUID, String> subMenuOpened = new HashMap<>();




    private BuildManager() {
        Bukkit.getPluginManager().registerEvents(this, Main.plugin);

    }

    static BuildManager getInstance() {
        if(instance==null) {
            instance = new BuildManager();
        }
        return instance;
    }

    void setBuilder(Player p, boolean b) {
        if(b) {
            if (!builders.contains(p.getUniqueId())) {
                builders.add(p.getUniqueId());
                Main.plugin.getLogger().info("Player >" + p.getDisplayName() + "< is now in building-mode");
                p.sendMessage(R.MESSAGES_BUILDINGMODE_ENABLED);
            } else {
                Main.plugin.getLogger().warning("Player >" + p.getDisplayName() + "< is already in building-mode");
            }
        } else {
            if (builders.contains(p.getUniqueId())) {
                builders.remove(p.getUniqueId());
                Main.plugin.getLogger().info("Player >" + p.getDisplayName() + "< is no longer in building-mode");
                p.sendMessage(R.MESSAGES_BUILDINGMODE_DISABLED);
            } else {
                Main.plugin.getLogger().warning("Player >" + p.getDisplayName() + "< wasn't in building-mode");
            }
        }
        setItem(p);
    }

    @SuppressWarnings("unused")
    public List<UUID> getBuilders() {
        return builders;
    }

    boolean isBuilder(Player p) {
        return builders.contains(p.getUniqueId());
    }

    private void setItem(Player p) {
        if(isBuilder(p)) {
            p.getInventory().clear();
            p.getInventory().setItem(0, Items.getBuildMenuItem());
        } else {
            for(ItemStack i : p.getInventory().getContents()) {
                if(i!=null && Arrays.asList(R.BLOCKED_ITEMS).contains(i.getItemMeta().getDisplayName())) {
                    p.getInventory().remove(i);
                }
            }
        }
    }



    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Location loc = event.getBlock().getLocation();
        Data data = Data.getInstance();
        BuildManager bm = BuildManager.getInstance();
        if(bm.isBuilder(event.getPlayer()) && (loc.equals(data.getRedBED()) || loc.equals(data.getRedBED2()))) {
            redbedbroken(event);
            refreshMenu(event.getPlayer());
        } else if(bm.isBuilder(event.getPlayer()) && (loc.equals(data.getBlueBED()) || loc.equals(data.getBlueBED2()))) {
            bluebedbroken(event);
            refreshMenu(event.getPlayer());
        } else if(bm.isBuilder(event.getPlayer()) && (loc.equals(data.getGreenBED()) || loc.equals(data.getGreenBED2()))) {
            greenbedbroken(event);
            refreshMenu(event.getPlayer());
        } else if(bm.isBuilder(event.getPlayer()) && (loc.equals(data.getYellowBED()) || loc.equals(data.getYellowBED2()))) {
            yellowbedbroken(event);
            refreshMenu(event.getPlayer());
        } else if(bm.isBuilder(event.getPlayer()) && (loc.equals(data.getRedSpawn()))) {
            redspawnbroken(event);
            refreshMenu(event.getPlayer());
        } else if(bm.isBuilder(event.getPlayer()) && (loc.equals(data.getBlueSpawn()))) {
            bluespawnbroken(event);
            refreshMenu(event.getPlayer());
        } else if(bm.isBuilder(event.getPlayer()) && (loc.equals(data.getGreenSpawn()))) {
            greenspawnbroken(event);
            refreshMenu(event.getPlayer());
        } else if(bm.isBuilder(event.getPlayer()) && (loc.equals(data.getYellowSpawn()))) {
            yellowspawnbroken(event);
            refreshMenu(event.getPlayer());
        } else if(bm.isBuilder(event.getPlayer()) && Data.getInstance().getSpawners_bronze().contains(event.getBlock().getLocation())) {
            Data.getInstance().removeSpawners_bronze(event.getBlock().getLocation());
            event.getPlayer().sendMessage(R.MESSAGES_BUILDER_BREAK_ITEMSPAWNER.replaceAll("<spawner>", R.NAMES_BRONZE_SPAWNER).replaceAll("<location>", event.getBlock().getLocation().getBlockX() + "," + event.getBlock().getLocation().getBlockY() + "," + event.getBlock().getLocation().getBlockZ()));
            refreshMenu(event.getPlayer());
        } else if(bm.isBuilder(event.getPlayer()) && Data.getInstance().getSpawners_silver().contains(event.getBlock().getLocation())) {
            Data.getInstance().removeSpawners_silver(event.getBlock().getLocation());
            event.getPlayer().sendMessage(R.MESSAGES_BUILDER_BREAK_ITEMSPAWNER.replaceAll("<spawner>", R.NAMES_SILVER_SPAWNER).replaceAll("<location>", event.getBlock().getLocation().getBlockX() + "," + event.getBlock().getLocation().getBlockY() + "," + event.getBlock().getLocation().getBlockZ()));
            refreshMenu(event.getPlayer());
        } else if(bm.isBuilder(event.getPlayer()) && Data.getInstance().getSpawners_gold().contains(event.getBlock().getLocation())) {
            Data.getInstance().removeSpawners_gold(event.getBlock().getLocation());
            event.getPlayer().sendMessage(R.MESSAGES_BUILDER_BREAK_ITEMSPAWNER.replaceAll("<spawner>", R.NAMES_GOLD_SPAWNER).replaceAll("<location>", event.getBlock().getLocation().getBlockX() + "," + event.getBlock().getLocation().getBlockY() + "," + event.getBlock().getLocation().getBlockZ()));
            refreshMenu(event.getPlayer());
        }
    }

    private void redspawnbroken(BlockBreakEvent event) {
        Data.getInstance().setRedSpawn(null);
        event.getPlayer().sendMessage(R.MESSAGES_BUILDER_BREAK_SPAWN.replaceAll("<team>", R.NAMES_RED_TEAM));
    }

    private void bluespawnbroken(BlockBreakEvent event) {
        Data.getInstance().setBlueSpawn(null);
        event.getPlayer().sendMessage(R.MESSAGES_BUILDER_BREAK_SPAWN.replaceAll("<team>", R.NAMES_BLUE_TEAM));
    }

    private void greenspawnbroken(BlockBreakEvent event) {
        Data.getInstance().setGreenSpawn(null);
        event.getPlayer().sendMessage(R.MESSAGES_BUILDER_BREAK_SPAWN.replaceAll("<team>", R.NAMES_GREEN_TEAM));
    }

    private void yellowspawnbroken(BlockBreakEvent event) {
        Data.getInstance().setYellowSpawn(null);
        event.getPlayer().sendMessage(R.MESSAGES_BUILDER_BREAK_SPAWN.replaceAll("<team>", R.NAMES_YELLOW_TEAM));
    }

    private void redbedbroken(BlockBreakEvent event) {
        Data.getInstance().setRedBED(null);
        Data.getInstance().setRedBED2(null);
        event.getPlayer().sendMessage(R.MESSAGES_BUILDER_BREAK_BED.replaceAll("<team>", R.NAMES_RED_TEAM));
    }

    private void bluebedbroken(BlockBreakEvent event) {
        Data.getInstance().setBlueBED(null);
        Data.getInstance().setBlueBED2(null);
        event.getPlayer().sendMessage(R.MESSAGES_BUILDER_BREAK_BED.replaceAll("<team>", R.NAMES_BLUE_TEAM));
    }

    private void greenbedbroken(BlockBreakEvent event) {
        Data.getInstance().setGreenBED(null);
        Data.getInstance().setGreenBED2(null);
            event.getPlayer().sendMessage(R.MESSAGES_BUILDER_BREAK_BED.replaceAll("<team>", R.NAMES_GREEN_TEAM));
    }

    private void yellowbedbroken(BlockBreakEvent event) {
        Data.getInstance().setYellowBED(null);
        Data.getInstance().setYellowBED2(null);
        event.getPlayer().sendMessage(R.MESSAGES_BUILDER_BREAK_BED.replaceAll("<team>", R.NAMES_YELLOW_TEAM));
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {

        if (BuildManager.getInstance().isBuilder(event.getPlayer()) && event.getPlayer().getItemInHand().getType().equals(Material.CARPET)) {

            String displayname = event.getPlayer().getItemInHand().getItemMeta().getDisplayName();
            if (displayname.contains(R.NAMES_RED_SPAWN)) redspawnplaced(event);
            if (displayname.contains(R.NAMES_BLUE_SPAWN)) bluespawnplaced(event);
            if (displayname.contains(R.NAMES_GREEN_SPAWN)) greenspawnplaced(event);
            try {
                if (displayname.contains(R.NAMES_YELLOW_SPAWN)) yellowspawnplaced(event);
            } catch(AssertionError ignored) {}


        }
        if(event.getItemInHand()==null || event.getItemInHand().getItemMeta()==null || event.getItemInHand().getItemMeta().getDisplayName()==null) {
            return;
        }

        String displayname = event.getItemInHand().getItemMeta().getDisplayName();

        if (BuildManager.getInstance().isBuilder(event.getPlayer()) && displayname.contains(R.NAMES_BRONZE_SPAWNER)) {
            Data.getInstance().addSpawners_bronze(event.getBlock().getLocation());
            refreshMenu(event.getPlayer());
            event.getPlayer().sendMessage(R.MESSAGES_BUILDER_SET_ITEMSPAWNER.replaceAll("<spawner>", R.NAMES_BRONZE_SPAWNER).replaceAll("<location>", event.getBlock().getLocation().getBlockX() + "," + event.getBlock().getLocation().getBlockY() + "," + event.getBlock().getLocation().getBlockZ()));
        }
        if (BuildManager.getInstance().isBuilder(event.getPlayer()) && displayname.contains(R.NAMES_SILVER_SPAWNER)) {
            Data.getInstance().addSpawners_silver(event.getBlock().getLocation());
            refreshMenu(event.getPlayer());
            event.getPlayer().sendMessage(R.MESSAGES_BUILDER_SET_ITEMSPAWNER.replaceAll("<spawner>", R.NAMES_SILVER_SPAWNER).replaceAll("<location>", event.getBlock().getLocation().getBlockX() + "," + event.getBlock().getLocation().getBlockY() + "," + event.getBlock().getLocation().getBlockZ()));
        }
        if (BuildManager.getInstance().isBuilder(event.getPlayer()) && displayname.contains(R.NAMES_GOLD_SPAWNER)) {
            Data.getInstance().addSpawners_gold(event.getBlock().getLocation());
            refreshMenu(event.getPlayer());
            event.getPlayer().sendMessage(R.MESSAGES_BUILDER_SET_ITEMSPAWNER.replaceAll("<spawner>", R.NAMES_GOLD_SPAWNER).replaceAll("<location>", event.getBlock().getLocation().getBlockX() + "," + event.getBlock().getLocation().getBlockY() + "," + event.getBlock().getLocation().getBlockZ()));
        }
        //if(!isBuilder(event.getPlayer()) && event.getItemInHand()!=null && event.getItemInHand().getItemMeta()!=null && event.getItemInHand().getItemMeta().getDisplayName()!=null && Arrays.asList(R.BLOCKED_ITEMS).contains(event.getItemInHand().getItemMeta().getDisplayName())) {
        //    event.setCancelled(true);
        //}
    }


    private void redspawnplaced(BlockPlaceEvent event) {
        if(Data.getInstance().getRedSpawn()!=null) {
            Data.getInstance().getRedSpawn().getBlock().setType(Material.AIR);
        }

        Location l1 = event.getBlockPlaced().getLocation();
        Data.getInstance().setRedSpawn(l1);
        refreshMenu(event.getPlayer());
        event.getPlayer().sendMessage(R.MESSAGES_SET_BPAWN.replaceAll("<team>", R.NAMES_RED_TEAM).replaceAll("<location>", l1.getBlockX() + "," + l1.getBlockY() + "," + l1.getBlockZ() ));
        event.setCancelled(false);
    }

    private void bluespawnplaced(BlockPlaceEvent event) {
        if(Data.getInstance().getBlueSpawn()!=null) {
            Data.getInstance().getBlueSpawn().getBlock().setType(Material.AIR);
        }

        Location l1 = event.getBlockPlaced().getLocation();
        Data.getInstance().setBlueSpawn(l1);
        refreshMenu(event.getPlayer());
        event.getPlayer().sendMessage(R.MESSAGES_SET_BPAWN.replaceAll("<team>", R.NAMES_BLUE_TEAM).replaceAll("<location>", l1.getBlockX() + "," + l1.getBlockY() + "," + l1.getBlockZ() ));
        event.setCancelled(false);
    }

    private void greenspawnplaced(BlockPlaceEvent event) {
        if(Data.getInstance().getGreenSpawn()!=null) {
            Data.getInstance().getGreenSpawn().getBlock().setType(Material.AIR);
        }

        Location l1 = event.getBlockPlaced().getLocation();
        Data.getInstance().setGreenSpawn(l1);
        refreshMenu(event.getPlayer());
        event.getPlayer().sendMessage(R.MESSAGES_SET_BPAWN.replaceAll("<team>", R.NAMES_GREEN_TEAM).replaceAll("<location>", l1.getBlockX() + "," + l1.getBlockY() + "," + l1.getBlockZ()));
        event.setCancelled(false);
    }

    private void yellowspawnplaced(BlockPlaceEvent event) {
        if(Data.getInstance().getYellowSpawn()!=null) {
            Data.getInstance().getYellowSpawn().getBlock().setType(Material.AIR);
        }

        Location l1 = event.getBlockPlaced().getLocation();
        Data.getInstance().setYellowSpawn(l1);
        refreshMenu(event.getPlayer());
        event.getPlayer().sendMessage(R.MESSAGES_SET_BPAWN.replaceAll("<team>", R.NAMES_YELLOW_TEAM).replaceAll("<location>", l1.getBlockX() + "," + l1.getBlockY() + "," + l1.getBlockZ() ));
        event.setCancelled(false);
    }




    @EventHandler(priority = EventPriority.HIGH)
    public void onMultiBlockPlace(BlockMultiPlaceEvent event) {

        if (BuildManager.getInstance().isBuilder(event.getPlayer()) && event.getPlayer().getItemInHand().getType().equals(Material.BED)) {

            String displayname = event.getPlayer().getItemInHand().getItemMeta().getDisplayName();
            if(displayname==null) return;
            if (displayname.contains(R.NAMES_RED_BED)) redbedplaced(event);
            if (displayname.contains(R.NAMES_BLUE_BED)) bluebedplaced(event);
            if (displayname.contains(R.NAMES_GREEN_BED)) greenbedplaced(event);
            try {
                if (displayname.contains(R.NAMES_YELLOW_BED)) yellowbedplaced(event);
            } catch(AssertionError ignored) {}
        }
        //if(!isBuilder(event.getPlayer()) && event.getItemInHand()!=null && event.getItemInHand().getItemMeta()!=null && event.getItemInHand().getItemMeta().getDisplayName()!=null && Arrays.asList(R.BLOCKED_ITEMS).contains(event.getItemInHand().getItemMeta().getDisplayName())) {
        //    event.setCancelled(true);
        //}
    }

    private void redbedplaced(BlockMultiPlaceEvent event) {
        if(Data.getInstance().getRedBED()!=null) {
            Data.getInstance().getRedBED().getBlock().setType(Material.AIR);
            Data.getInstance().getRedBED2().getBlock().setType(Material.AIR);
        }
        List<BlockState> blks = event.getReplacedBlockStates();
        Location l1 = blks.get(0).getLocation();
        Location l2 = blks.get(1).getLocation();
        Data.getInstance().setRedBED(l1);
        Data.getInstance().setRedBED2(l2);
        event.getPlayer().sendMessage(R.MESSAGES_SET_BED.replaceAll("<team>", R.NAMES_RED_TEAM).replaceAll("<location>", l1.getBlockX() + "," + l1.getBlockY() + "," + l1.getBlockZ() + " and " + l2.getBlockX() + "," + l2.getBlockY() + "," + l2.getBlockZ()));
        event.setCancelled(false);
    }

    private void bluebedplaced(BlockMultiPlaceEvent event) {
        if(Data.getInstance().getBlueBED()!=null) {
            Data.getInstance().getBlueBED().getBlock().setType(Material.AIR);
            Data.getInstance().getBlueBED2().getBlock().setType(Material.AIR);
        }
        List<BlockState> blks = event.getReplacedBlockStates();
        Location l1 = blks.get(0).getLocation();
        Location l2 = blks.get(1).getLocation();
        Data.getInstance().setBlueBED(l1);
        Data.getInstance().setBlueBED2(l2);
        refreshMenu(event.getPlayer());
        event.getPlayer().sendMessage(R.MESSAGES_SET_BED.replaceAll("<team>", R.NAMES_BLUE_TEAM).replaceAll("<location>", l1.getBlockX() + "," + l1.getBlockY() + "," + l1.getBlockZ() + " and " + l2.getBlockX() + "," + l2.getBlockY() + "," + l2.getBlockZ()));
        event.setCancelled(false);
    }

    private void greenbedplaced(BlockMultiPlaceEvent event) {
        if(Data.getInstance().getGreenBED()!=null) {
            Data.getInstance().getGreenBED().getBlock().setType(Material.AIR);
            Data.getInstance().getGreenBED2().getBlock().setType(Material.AIR);
        }
        List<BlockState> blks = event.getReplacedBlockStates();
        Location l1 = blks.get(0).getLocation();
        Location l2 = blks.get(1).getLocation();
        Data.getInstance().setGreenBED(l1);
        Data.getInstance().setGreenBED2(l2);
        refreshMenu(event.getPlayer());
        event.getPlayer().sendMessage(R.MESSAGES_SET_BED.replaceAll("<team>", R.NAMES_GREEN_TEAM).replaceAll("<location>", l1.getBlockX() + "," + l1.getBlockY() + "," + l1.getBlockZ() + " and " + l2.getBlockX() + "," + l2.getBlockY() + "," + l2.getBlockZ()));
        event.setCancelled(false);
    }

    private void yellowbedplaced(BlockMultiPlaceEvent event) {
        if(Data.getInstance().getYellowBED()!=null) {
            Data.getInstance().getYellowBED().getBlock().setType(Material.AIR);
            Data.getInstance().getYellowBED2().getBlock().setType(Material.AIR);
        }
        List<BlockState> blks = event.getReplacedBlockStates();
        Location l1 = blks.get(0).getLocation();
        Location l2 = blks.get(1).getLocation();
        Data.getInstance().setYellowBED(l1);
        Data.getInstance().setYellowBED2(l2);
        refreshMenu(event.getPlayer());
        event.getPlayer().sendMessage(R.MESSAGES_SET_BED.replaceAll("<team>", R.NAMES_YELLOW_TEAM).replaceAll("<location>", l1.getBlockX() + "," + l1.getBlockY() + "," + l1.getBlockZ() + " and " + l2.getBlockX() + "," + l2.getBlockY() + "," + l2.getBlockZ()));
        event.setCancelled(false);
    }




    @EventHandler
    public void onItemMove(InventoryClickEvent event) {
        if(event.getCurrentItem()!=null && event.getCurrentItem().getItemMeta()!=null && event.getCurrentItem().getItemMeta().getDisplayName()!=null && Arrays.asList(R.BLOCKED_ITEMS).contains(event.getCurrentItem().getItemMeta().getDisplayName()) || Arrays.asList(Items.getSpawnerBuildMenu()).contains(event.getCurrentItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemMove(PlayerDropItemEvent event) {
        //noinspection SuspiciousMethodCalls
        if(event.getItemDrop()!=null && event.getItemDrop().getItemStack().getItemMeta()!=null && event.getItemDrop().getItemStack().getItemMeta().getDisplayName()!=null && Arrays.asList(R.BLOCKED_ITEMS).contains(event.getItemDrop().getItemStack().getItemMeta().getDisplayName()) || Arrays.asList(Items.getSpawnerBuildMenu()).contains(event.getItemDrop())) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand()!=null && event.getPlayer().getItemInHand().getItemMeta()!=null && event.getPlayer().getItemInHand().getItemMeta().getDisplayName()!=null && event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(R.NAMES_BUILDING_MENU)) {
            toggleMainMenu(event.getPlayer());
        } else if (event.getPlayer().getItemInHand()!=null && event.getPlayer().getItemInHand().getItemMeta()!=null &&  event.getPlayer().getItemInHand().getItemMeta().getDisplayName()!=null && event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(R.NAMES_RED_ITEM)) {
            toggleredmenu(event.getPlayer());
        } else if (event.getPlayer().getItemInHand()!=null && event.getPlayer().getItemInHand().getItemMeta()!=null &&  event.getPlayer().getItemInHand().getItemMeta().getDisplayName()!=null && event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(R.NAMES_BLUE_ITEM)) {
            togglebluemenu(event.getPlayer());
        } else if (event.getPlayer().getItemInHand()!=null && event.getPlayer().getItemInHand().getItemMeta()!=null &&  event.getPlayer().getItemInHand().getItemMeta().getDisplayName()!=null && event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(R.NAMES_GREEN_ITEM)) {
            togglegreenemenu(event.getPlayer());
        } else if (event.getPlayer().getItemInHand()!=null && event.getPlayer().getItemInHand().getItemMeta()!=null &&  event.getPlayer().getItemInHand().getItemMeta().getDisplayName()!=null && event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(R.NAMES_YELLOW_ITEM)) {
            toggleyellowmenu(event.getPlayer());
        } else if (event.getPlayer().getItemInHand()!=null && event.getPlayer().getItemInHand().getItemMeta()!=null &&  event.getPlayer().getItemInHand().getItemMeta().getDisplayName()!=null && event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(R.NAMES_SPAWNER_ITEM)) {
            togglespawnermenu(event.getPlayer());
        } else if (event.getPlayer().getItemInHand()!=null && event.getPlayer().getItemInHand().getItemMeta()!=null &&  event.getPlayer().getItemInHand().getItemMeta().getDisplayName()!=null && event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(R.NAMES_SHOP)) {
            event.setCancelled(true);
            spawnShop(event);
        }
    }

    private void spawnShop(PlayerInteractEvent event) {
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            Location loc = event.getPlayer().getLocation();
            Data.getInstance().addShop(loc);
            ArmorStand as = loc.getWorld().spawn(loc, ArmorStand.class);
            as.setBasePlate(false);
            as.setCustomName(R.NAMES_SHOP);
            as.setCustomNameVisible(true);
            as.setArms(true);

            as.setBoots(new ItemStack(Material.LEATHER_BOOTS));
            as.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
            as.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));

            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwner("ZAO77");
            head.setItemMeta(meta);
            as.setHelmet(head);

        }
    }

    private void refreshMenu(Player p) {
        if(mainMenuOpened.get(p.getUniqueId())) {
            toggleMainMenu(p);
            toggleMainMenu(p);
        } else if(subMenuOpened.get(p.getUniqueId()).equals("red")) {
            toggleredmenu(p);
            toggleredmenu(p);
        } else if(subMenuOpened.get(p.getUniqueId()).equals("blue")) {
            togglebluemenu(p);
            togglebluemenu(p);
        } else if(subMenuOpened.get(p.getUniqueId()).equals("green")) {
            togglegreenemenu(p);
            togglegreenemenu(p);
        } else if(subMenuOpened.get(p.getUniqueId()).equals("yellow")) {
            toggleyellowmenu(p);
            toggleyellowmenu(p);
        } else if(subMenuOpened.get(p.getUniqueId()).equals("spawners")) {
            togglespawnermenu(p);
            togglespawnermenu(p);
        }
    }

    private void toggleMainMenu(Player p) {
        if(!mainMenuOpened.containsKey(p.getUniqueId())) mainMenuOpened.put(p.getUniqueId(), false);
        if(mainMenuOpened.get(p.getUniqueId())) {
            for(int i = 1; i<9; i++) {
                p.getInventory().clear(i);
            }
            mainMenuOpened.put(p.getUniqueId(), false);
            subMenuOpened.put(p.getUniqueId(), "none");
        } else {
            if(!Data.getInstance().redTeamActive()) {
                p.getInventory().setItem(1, Items.getGrayItem1());
            } else {
                p.getInventory().setItem(1, Items.getRedItem());
            }

            if(!Data.getInstance().blueTeamActive()) {
                p.getInventory().setItem(2, Items.getGrayItem2());
            } else {
                p.getInventory().setItem(2, Items.getBlueItem());
            }

            if(!Data.getInstance().greenTeamActive()) {
                p.getInventory().setItem(3, Items.getGrayItem3());
            } else {
                p.getInventory().setItem(3, Items.getGreenItem());
            }

            if(!Data.getInstance().yellowTeamActive()) {
                p.getInventory().setItem(4, Items.getGrayItem4());
            } else {
                p.getInventory().setItem(4, Items.getYellowItem());
            }

            p.getInventory().setItem(5, Items.getSpawnerItem());

            p.getInventory().setItem(6, Items.getShopItem());

            mainMenuOpened.put(p.getUniqueId(), true);
            subMenuOpened.put(p.getUniqueId(), "none");
        }
    }

    private void toggleredmenu(Player event) {
        if(!subMenuOpened.containsKey(event.getPlayer().getUniqueId())) subMenuOpened.put(event.getPlayer().getUniqueId(), "none");
        if(subMenuOpened.get(event.getPlayer().getUniqueId()).equals("none")) {
            ItemStack[] itst = Items.getRedBuildMenu();
            for (int i = 1; i < 9; i++) {
                if(itst[i-1] != null) {
                    event.getPlayer().getInventory().setItem(i, itst[i - 1]);
                } else {
                    event.getPlayer().getInventory().setItem(i, new ItemStack(Material.AIR));
                }
            }
            mainMenuOpened.put(event.getPlayer().getUniqueId(), false);
            subMenuOpened.put(event.getPlayer().getUniqueId(), "red");
        } else {
            toggleMainMenu(event);
            subMenuOpened.put(event.getPlayer().getUniqueId(), "none");
        }
    }

    private void togglebluemenu(Player event) {
        if(!subMenuOpened.containsKey(event.getPlayer().getUniqueId())) subMenuOpened.put(event.getPlayer().getUniqueId(), "none");
        if(subMenuOpened.get(event.getPlayer().getUniqueId()).equals("none")) {
            ItemStack[] itst = Items.getBlueBuildMenu();
            for (int i = 1; i < 9; i++) {
                if(itst[i-1] != null) {
                    event.getPlayer().getInventory().setItem(i, itst[i - 1]);
                } else {
                    event.getPlayer().getInventory().setItem(i, new ItemStack(Material.AIR));
                }
            }
            mainMenuOpened.put(event.getPlayer().getUniqueId(), false);
            subMenuOpened.put(event.getPlayer().getUniqueId(), "blue");
        } else {
            toggleMainMenu(event);
            subMenuOpened.put(event.getPlayer().getUniqueId(), "none");
        }
    }

    private void togglegreenemenu(Player event) {
        if(!subMenuOpened.containsKey(event.getPlayer().getUniqueId())) subMenuOpened.put(event.getPlayer().getUniqueId(), "none");
        if(subMenuOpened.get(event.getPlayer().getUniqueId()).equals("none")) {
            ItemStack[] itst = Items.getGreenBuildMenu();
            for (int i = 1; i < 9; i++) {
                if(itst[i-1] != null) {
                    event.getPlayer().getInventory().setItem(i, itst[i - 1]);
                } else {
                    event.getPlayer().getInventory().setItem(i, new ItemStack(Material.AIR));
                }
            }
            mainMenuOpened.put(event.getPlayer().getUniqueId(), false);
            subMenuOpened.put(event.getPlayer().getUniqueId(), "green");
        } else {
            toggleMainMenu(event);
            subMenuOpened.put(event.getPlayer().getUniqueId(), "none");
        }
    }

    private void toggleyellowmenu(Player event) {
        if(!subMenuOpened.containsKey(event.getPlayer().getUniqueId())) subMenuOpened.put(event.getPlayer().getUniqueId(), "none");
        if(subMenuOpened.get(event.getPlayer().getUniqueId()).equals("none")) {
            ItemStack[] itst = Items.getYellowBuildMenu();
            for (int i = 1; i < 9; i++) {

                if(itst[i-1] !=null) {
                    event.getPlayer().getInventory().setItem(i, itst[i - 1]);
                } else {
                    event.getPlayer().getInventory().setItem(i, new ItemStack(Material.AIR));
                }

            }
            mainMenuOpened.put(event.getPlayer().getUniqueId(), false);
            subMenuOpened.put(event.getPlayer().getUniqueId(), "yellow");
        } else {
            toggleMainMenu(event);
            subMenuOpened.put(event.getPlayer().getUniqueId(), "none");
        }
    }

    private void togglespawnermenu(Player event) {
        if (!subMenuOpened.containsKey(event.getPlayer().getUniqueId()))
            subMenuOpened.put(event.getPlayer().getUniqueId(), "none");
        if (subMenuOpened.get(event.getPlayer().getUniqueId()).equals("none")) {
            ItemStack[] itst = Items.getSpawnerBuildMenu();
            for (int i = 1; i < 9; i++) {

                if (itst[i - 1] != null) {
                    event.getPlayer().getInventory().setItem(i, itst[i - 1]);
                } else {
                    event.getPlayer().getInventory().setItem(i, new ItemStack(Material.AIR));
                }

            }
            mainMenuOpened.put(event.getPlayer().getUniqueId(), false);
            subMenuOpened.put(event.getPlayer().getUniqueId(), "spawners");
        } else {
            toggleMainMenu(event);
            subMenuOpened.put(event.getPlayer().getUniqueId(), "none");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player && isBuilder((Player)event.getDamager()) && event.getEntity().getType().equals(EntityType.ARMOR_STAND)) {
            event.setCancelled(false);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(event.getEntity().getType().equals(EntityType.ARMOR_STAND) && event.getEntity().getCustomName().equals(R.NAMES_SHOP)) {
            event.setCancelled(true);
        }
    }
}
