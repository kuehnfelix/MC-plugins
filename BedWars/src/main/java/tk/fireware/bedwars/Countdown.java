package tk.fireware.bedwars;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown {

    int sch1 = -1;

    int lobbycd = R.VALUES_LOBBY_TIMER;

    private boolean forcestart = false;

    public static Countdown instance;

    public Countdown() {
        instance = this;
    }

    public void startGame() {
        lobbycd=3;
        forcestart = true;
    }

    public void startLobbyCountdown() {


        sch1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new BukkitRunnable() {

            @Override
            public void run() {
                if(lobbycd<1) {
                    if(Bukkit.getOnlinePlayers().size()<Data.getInstance().minPlayers&&!forcestart) {
                        Bukkit.broadcastMessage(R.MESSAGES_NOT_ENOUGH_PLAYERS);
                        lobbycd = 20;
                    } else {
                        Teams.getInstance().addRest();
                        teleportPlayers();
                        GameStateManager.instance.setState(GameState.INGAME);


                        Bukkit.getScheduler().cancelTask(sch1);
                        return;
                    }
                }
                if(Bukkit.getOnlinePlayers().size()<Data.getInstance().minPlayers&&lobbycd==5) {
                    Bukkit.broadcastMessage(R.MESSAGES_NOT_ENOUGH_PLAYERS);
                    lobbycd = 20;
                }
                if(lobbycd==120||lobbycd==90||lobbycd==60||lobbycd==30||lobbycd==15||lobbycd==10||lobbycd<=5) {
                    String msg = R.MESSAGES_GAME_STARTING.replaceAll("<time>", String.valueOf(lobbycd));
                    Bukkit.broadcastMessage(msg);
                }


                lobbycd--;
            }
        }, 0, 20L);
    }

    private void teleportPlayers() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(Data.getInstance().redTeamActive() && Teams.getInstance().getRed().hasPlayer(p)) {
                p.teleport(Data.getInstance().getRedSpawn().add(0.5,0.2,0.5));
            } else if(Data.getInstance().blueTeamActive() && Teams.getInstance().getBlue().hasPlayer(p)) {
                p.teleport(Data.getInstance().getBlueSpawn().add(0.5,0.2,0.5));
            } else if(Data.getInstance().greenTeamActive() && Teams.getInstance().getGreen().hasPlayer(p)) {
                p.teleport(Data.getInstance().getGreenSpawn().add(0.5,0.2,0.5));
            } else if(Data.getInstance().yellowTeamActive() && Teams.getInstance().getYellow().hasPlayer(p)) {
                p.teleport(Data.getInstance().getYellowSpawn().add(0.5,0.2,0.5));
            }
        }
    }

}
