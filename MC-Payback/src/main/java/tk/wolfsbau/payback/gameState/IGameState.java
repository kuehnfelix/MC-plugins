package tk.wolfsbau.payback.gameState;

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

public interface IGameState {
    public void trigger();

    void onItemDrop(PlayerDropItemEvent e);

    void onEntityExplode(EntityExplodeEvent event);

    void onEntityDamage(EntityDamageEvent event);

    void onPlayerJoin(PlayerJoinEvent event);

    void onPlayerQuit(PlayerQuitEvent event);

    void onPreLogin(PlayerLoginEvent event);

    void onPlayerDeath(PlayerDeathEvent event);

    void onRespawn(PlayerRespawnEvent event);

    void onEntityDamageEntity(EntityDamageByEntityEvent event);

    void onChat(AsyncPlayerChatEvent event);

    void craftItem(PrepareItemCraftEvent e);

    void onBrewEvent(BrewEvent event);

    void onBlockPlace(BlockPlaceEvent event);

    void onBlockBreak(BlockBreakEvent event);

    void onSignPlace(SignChangeEvent event);

    void onPlayerInteract(PlayerInteractEvent event);

    void onHopper(InventoryMoveItemEvent event);
}
