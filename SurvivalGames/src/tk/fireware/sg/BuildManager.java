package tk.fireware.sg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BuildManager implements Listener {
    private static BuildManager instance;

    private List<UUID> builders = new ArrayList<UUID>();

    private BuildManager() {
        Bukkit.getPluginManager().registerEvents(this, Main.plugin);
    }

    public static BuildManager getInstance() {
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
                p.setGameMode(GameMode.CREATIVE);
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

    public List<UUID> getBuilders() {
        return builders;
    }

    public boolean isBuilder(Player p) {
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
        BuildManager bm = BuildManager.getInstance();
        if(bm.isBuilder(event.getPlayer()) && Data.getInstance().getSpawnpoints().contains(event.getBlock().getLocation())) {
            Data.getInstance().removeSpawnpoint(event.getBlock().getLocation());
            event.getPlayer().sendMessage(R.MESSAGES_BUILDER_BREAK_SPAWNPOINT.replaceAll("<location>", event.getBlock().getLocation().getBlockX() + "," + event.getBlock().getLocation().getBlockY() + "," + event.getBlock().getLocation().getBlockZ()).replace("<amount>", String.valueOf(Data.getInstance().getSpawnpoints().size())));
        }
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
    	
    	if(event.getItemInHand()==null || event.getItemInHand().getItemMeta()==null || event.getItemInHand().getItemMeta().getDisplayName()==null) {
            return;
        }
    	
        if (BuildManager.getInstance().isBuilder(event.getPlayer()) && event.getPlayer().getItemInHand().getType().equals(Material.CARPET)) {
            String displayname = event.getPlayer().getItemInHand().getItemMeta().getDisplayName();
            if(displayname.equals(R.NAMES_SPAWNPOINT_ITEM)){
            	Data.getInstance().addSpawnpoint(event.getBlock().getLocation());
            	event.getPlayer().sendMessage(R.MESSAGES_BUILDER_SET_SPAWNPOINT.replaceAll("<location>", event.getBlock().getLocation().getBlockX() + "," + event.getBlock().getLocation().getBlockY() + "," + event.getBlock().getLocation().getBlockZ()).replace("<amount>", String.valueOf(Data.getInstance().getSpawnpoints().size())));
            }
        }
//        if (BuildManager.getInstance().isBuilder(event.getPlayer()) && displayname.contains(R.NAMES_BRONZE_SPAWNER)) {
//            Data.getInstance().addSpawners_bronze(event.getBlock().getLocation());
//            refreshMenu(event.getPlayer());
//            event.getPlayer().sendMessage(R.MESSAGES_BUILDER_SET_ITEMSPAWNER.replaceAll("<spawner>", R.NAMES_BRONZE_SPAWNER).replaceAll("<location>", event.getBlock().getLocation().getBlockX() + "," + event.getBlock().getLocation().getBlockY() + "," + event.getBlock().getLocation().getBlockZ()));
//        }
    }


    @EventHandler
    public void onItemMove(InventoryClickEvent event) {
        if(event.getCurrentItem()!=null && event.getCurrentItem().getItemMeta()!=null && event.getCurrentItem().getItemMeta().getDisplayName()!=null && Arrays.asList(R.BLOCKED_ITEMS).contains(event.getCurrentItem().getItemMeta().getDisplayName())) {
        	event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemMove(PlayerDropItemEvent event) {
        if(event.getItemDrop()!=null && event.getItemDrop().getItemStack().getItemMeta()!=null && event.getItemDrop().getItemStack().getItemMeta().getDisplayName()!=null && Arrays.asList(R.BLOCKED_ITEMS).contains(event.getItemDrop().getItemStack().getItemMeta().getDisplayName())) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand()!=null && event.getPlayer().getItemInHand().getItemMeta()!=null && event.getPlayer().getItemInHand().getItemMeta().getDisplayName()!=null && event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(R.NAMES_BUILDING_MENU)) {
            loadBuildInventory(event.getPlayer());
        }
    }
    
    private void loadBuildInventory(Player p){
    	Inventory i= p.getInventory();
    	i.setItem(2,Items.getSpawnpointItem());
    	p.updateInventory();
    }
}
