package tk.fireware.sg.gamestates;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.scheduler.BukkitRunnable;
import tk.fireware.sg.Main;

public class GameStateManager implements Listener {
    public static GameStateManager instance;

    private GameStates actualState;
    private LobbyState lobbyState;
    private IngameState ingameState;
	private RestartState restartState;
	private WaitState waitState;

    public GameStateManager() {
        instance = this;

        lobbyState = new LobbyState();
        ingameState = new IngameState();
        restartState=new RestartState();
        waitState=new WaitState();
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

    public void setState(GameState gamestate) {
        if(gamestate.equals(GameState.LOBBY)) {
            actualState = lobbyState;
        } else if(gamestate.equals(GameState.INGAME)) {
            actualState = ingameState;
        }else if(gamestate.equals(GameState.RESTART)){
        	actualState=restartState;
        }else if(gamestate.equals(GameState.WAIT)){
        	actualState=waitState;
        }
        
        actualState.init();
    }
    public GameStates getActualState(){
    	return actualState;
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
    public void onLogin(PlayerLoginEvent event) {
        actualState.onLogin(event);
    }
    
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        actualState.onPlayerOpenInventory(event);
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        actualState.onPlayerDeath(event);
    }
    
    @EventHandler
    public void onPlayerHunger(FoodLevelChangeEvent event) {
        actualState.onPlayerHunger(event);
    }
    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
    	actualState.onServerPing(event);
    }
    @EventHandler
    public void  onPlayerDamage(EntityDamageEvent event){
        actualState.onPlayerDamage(event);
    }
    @EventHandler
    public void  onPlayerLeave(PlayerQuitEvent event){
        actualState.onLeave(event);
    }
    @EventHandler
    public void  onInvClick(InventoryClickEvent event){
        actualState.onInvClick(event);
    }

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event){
        actualState.onLaunch(event);
    }
}
