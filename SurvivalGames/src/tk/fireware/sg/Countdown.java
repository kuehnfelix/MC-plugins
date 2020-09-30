package tk.fireware.sg;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import tk.fireware.sg.gamestates.GameState;
import tk.fireware.sg.gamestates.GameStateManager;
import tk.fireware.sg.gamestates.WaitState;

public class Countdown {

    int sch1 = -1;

    private static int lobbycd = R.VALUES_LOBBY_TIMER;

    private boolean forcestart = false;
    private static boolean pauseCountdown=false;

    public static Countdown instance;

    public Countdown() {
        instance = this;
    }

    public void startGame() {
        lobbycd=3;
        forcestart = true;
    }

    
    
    @SuppressWarnings("deprecation")
	public void startLobbyCountdown() {
    	final int sec=15;
        sch1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new BukkitRunnable() {
        	@Override
            public void run() {
        		if(pauseCountdown)return;
                if(lobbycd<1) {
                    if(Bukkit.getOnlinePlayers().size()<Data.getInstance().minPlayers&&!forcestart) {
                        Bukkit.broadcastMessage(R.MESSAGES_NOT_ENOUGH_PLAYERS);
                        lobbycd = 20;
                    } else {
                        GameStateManager.instance.setState(GameState.INGAME);


                        Bukkit.getScheduler().cancelTask(sch1);
                        return;
                    }
                }
                if(Bukkit.getOnlinePlayers().size()<Data.getInstance().minPlayers&&lobbycd==(sec+5)) {
                    Bukkit.broadcastMessage(R.MESSAGES_NOT_ENOUGH_PLAYERS);
                    lobbycd = 100;
                }
                if(lobbycd==120||lobbycd==90||lobbycd==60||lobbycd==30||lobbycd==15||lobbycd==10||lobbycd<=5) {
                    String msg = R.MESSAGES_GAME_STARTING.replaceAll("<time>", String.valueOf(lobbycd));
                    Bukkit.broadcastMessage(msg);
                }
                if((lobbycd<sec || forcestart )){
                	if(!(GameStateManager.instance.getActualState() instanceof WaitState)){
                	teleportPlayers();
                	GameStateManager.instance.setState(GameState.WAIT);}
                }
                
                lobbycd--;
            }
        }, 0, 20L);
    }

    private void teleportPlayers() {
    	Iterator<Location> i=Data.getInstance().getSpawnpoints().iterator();
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(i.hasNext())p.teleport(i.next().add(0.5, 0.5, 0.5));
        }
    }
    
    public static void setCountdown(int i){
    	lobbycd=i;
    }
    
    public static void pauseCountdown(boolean vb){
    	pauseCountdown=vb;
    }
}
