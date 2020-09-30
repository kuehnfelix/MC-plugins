package tk.fireware.bedwars;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

public class GameStateManager implements Listener {
    private GameState gamestate;

    static GameStateManager instance;

    private GameStates actualState;
    private LobbyState lobbyState;
    private IngameState ingameState;

    GameStateManager(GameState gs) {
        gamestate = gs;
        instance = this;

        lobbyState = new LobbyState();
        ingameState = new IngameState();
        actualState = lobbyState;

        Bukkit.getPluginManager().registerEvents(this, Main.plugin);

        clock();
    }

    @SuppressWarnings("deprecation")
    private void clock() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new BukkitRunnable() {
            @Override
            public void run() {
                actualState.trigger();
            }
        } , 0L, 20L);
    }

    void setState(GameState g) {
        gamestate = g;
        if(gamestate.equals(GameState.LOBBY)) {
            actualState = lobbyState;
        } else if(gamestate.equals(GameState.INGAME)) {
            actualState = ingameState;
        }
    }

    @SuppressWarnings("unused")
    public GameState getState() {
        return gamestate;
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        actualState.onBlockPlace(event);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        actualState.onBlockBreak(event);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(event!=null)
        actualState.onPlayerMove(event);
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        actualState.onPlayerJoin(event);
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        actualState.onPlayerInteract(event);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        actualState.onPlayerDamage(event);
    }

    @EventHandler
    public void onPlyerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        actualState.onEntityInteractEntity(event);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        actualState.onLogin(event);
    }

}
