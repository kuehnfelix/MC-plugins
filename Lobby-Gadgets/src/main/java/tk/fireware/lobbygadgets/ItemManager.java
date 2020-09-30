package tk.fireware.lobbygadgets;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ItemManager implements Listener {
    public ItemManager() {
        Bukkit.getPluginManager().registerEvents(this, Plugin.instance);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        //p.getInventory().clear();
        Data.instance.setVisibility(p,true);
        ItemStack[] its = MenuItems.instance.getMainMenu(p);
        for(int i = 0 ; i<9 ; i++) {
            p.getInventory().setItem(i, its[i]);
        }
        event.setJoinMessage("");

        for(Player pl : Bukkit.getOnlinePlayers()){
            if(Data.instance.getPlayersVisible(pl)==false){
                pl.hidePlayer(p);
                p.hidePlayer(pl);
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        event.setQuitMessage("");
    }

    @EventHandler
    public void openMenu(PlayerInteractEvent event) {
        if(event.getPlayer().getItemInHand()!=null && event.getPlayer().getItemInHand().getItemMeta()!=null && event.getPlayer().getItemInHand().getItemMeta().getDisplayName()!=null) {
            if(event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(Constraints.ITEM_TELEPORTER)) {
                openTeleporter(event.getPlayer());
            }else {
                PlayerInventory inventory = event.getPlayer().getInventory();
                if(event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(Constraints.ITEM_PLAYERVISIBLE)) {
                    for(Player p : Bukkit.getOnlinePlayers()){
                        event.getPlayer().hidePlayer(p);
                        p.hidePlayer(event.getPlayer());
                    }
                    Data.instance.setVisibility(event.getPlayer(),false);
                    //inventory.clear();
                    for(int i = 0; i < 3; i++) {
                        event.getPlayer().getInventory().setItem(i, MenuItems.instance.getMainMenu(event.getPlayer())[i]);
                    }
                    event.getPlayer().updateInventory();
                }else if(event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(Constraints.ITEM_PLAYERINVISIBLE)) {
                    for(Player p : Bukkit.getOnlinePlayers()){
                        event.getPlayer().showPlayer(p);
                        p.showPlayer(event.getPlayer());
                    }
                    Data.instance.setVisibility(event.getPlayer(),true);
                    //inventory.clear();
                    for(int i = 0; i < 3; i++) {
                        event.getPlayer().getInventory().setItem(i, MenuItems.instance.getMainMenu(event.getPlayer())[i]);
                    }
                    event.getPlayer().updateInventory();
                }
            }
        }
    }


    @EventHandler
    public void onInventroyClick(InventoryClickEvent event) {
        if(!(event.getCurrentItem()!=null && event.getCurrentItem().getItemMeta()!=null && event.getCurrentItem().getItemMeta().getDisplayName()!=null)) return; //Prevent NPE
        if(event.getCurrentItem().getItemMeta().getDisplayName().equals(Constraints.ITEM_SURVIVALGAMES)) {
            event.getWhoClicked().teleport(Data.instance.getWarp("sg"));
            return;
        }
        if(event.getCurrentItem().getItemMeta().getDisplayName().equals(Constraints.ITEM_KNOCK)) {
            event.getWhoClicked().teleport(Data.instance.getWarp("knock"));
            return;
        }
        if(event.getCurrentItem().getItemMeta().getDisplayName().equals(Constraints.ITEM_SKYPVP)) {
            event.getWhoClicked().teleport(Data.instance.getWarp("skypvp"));
            return;
        }
        if(event.getCurrentItem().getItemMeta().getDisplayName().equals(Constraints.ITEM_SPAWN)) {
            event.getWhoClicked().teleport(Data.instance.getWarp("spawn"));
            return;
        }
        if(event.getCurrentItem().getItemMeta().getDisplayName().equals(Constraints.ITEM_SIGHT)) {
            event.getWhoClicked().teleport(Data.instance.getWarp("aussicht"));
            return;
        }
    }

    private void openTeleporter(Player player) {
        player.openInventory(MenuItems.instance.getTeleporterMenu(player));
    }

    @EventHandler
    public void onInventroyEvednt(InventoryInteractEvent event) {
        if(!event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE))event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventroyEvent(InventoryClickEvent event) {
        if(!event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE))event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventroyEvent(InventoryDragEvent event) {
        if(!event.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) {
            event.setCancelled(true);
        }
    }



    @EventHandler(priority = EventPriority.LOW)
    public void onInventroyEvent(PlayerDropItemEvent event) {
        if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInventroyEvent(PlayerPickupItemEvent event) {
        //if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlace(BlockPlaceEvent event) {
        if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent event) {
        if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) event.setCancelled(true);
        else event.setCancelled(false);
    }

}
