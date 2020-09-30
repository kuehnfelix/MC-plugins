package tk.fireware.sg.gamestates;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;

public abstract class GameStates {
    public abstract void onBlockPlace(BlockPlaceEvent event);
    public abstract void onBlockBreak(BlockBreakEvent event);

    public abstract void onPlayerMove(PlayerMoveEvent event);
    public abstract void onPlayerDamage(EntityDamageByEntityEvent event);
    public abstract void onPlayerDamage(EntityDamageEvent event);
    public abstract void onPlayerDeath(PlayerDeathEvent event);

    public abstract void onLogin(PlayerLoginEvent event);
    public abstract void onPlayerJoin(PlayerJoinEvent event);
    public abstract void onPlayerInteract(PlayerInteractEvent event);
    public abstract void onPlayerHunger(FoodLevelChangeEvent event);
    
    public abstract void onPlayerOpenInventory(InventoryOpenEvent event);
    public abstract void onInvClick(InventoryClickEvent event);

    public abstract void trigger();
	public abstract void init();
	public abstract void onServerPing(ServerListPingEvent event);
	public abstract void onLeave(PlayerQuitEvent event);
	public abstract void onLaunch(ProjectileLaunchEvent event);

}
