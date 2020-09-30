package tk.fireware.bedwars;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

public abstract class GameStates {
    public abstract void onBlockPlace(BlockPlaceEvent event);
    public abstract void onBlockBreak(BlockBreakEvent event);

    public abstract void onPlayerMove(PlayerMoveEvent event);
    public abstract void onPlayerDamage(EntityDamageByEntityEvent event);
    public abstract void onPlayerDamage(EntityDamageEvent event);

    public abstract void onEntityInteractEntity(PlayerInteractAtEntityEvent event);
    public abstract void onLogin(PlayerLoginEvent event);
    public abstract void onPlayerJoin(PlayerJoinEvent event);
    public abstract void onPlayerInteract(PlayerInteractEvent event);


    public abstract void trigger();

}
