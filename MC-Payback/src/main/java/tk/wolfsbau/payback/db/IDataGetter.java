package tk.wolfsbau.payback.db;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;

public interface IDataGetter {

    public abstract String getTeam(String player);
    public abstract ArrayList<String> getTeamPlayers(String name);
    public abstract void setTeam(String name, ArrayList<String> players);
    public abstract void removeTeam(String name);
    public abstract ArrayList<String> getTeamList();
    public abstract HashMap<String, ArrayList<String>> getPlayerList();


    public abstract Long getOption(Option o);
    public abstract void setOption(Option option, Long value);
    public abstract ArrayList<String> getOptionList();


    public abstract Location getChestLocation(String team);
    public abstract void setChestLocation(String team, Location loc);


    public abstract int getFolge(String player);
    public abstract void setFolge(String player, int folge);


    public abstract Location getPlayerLocation(String player);
    public abstract void setPlayerLocation(String player, Location location);


    public abstract int getPlayerCount();

    public boolean isWhiteListed(String player);

    void setAlive(String player, boolean isAlive);
    boolean isAlive(String player);

    int getMaxFolge();

    int getDay();

    int getPlayerCountAlive();

    int getAliveTeamCount();

    public double getBorderRadius();

    boolean isTeamAlive(String team);

    ArrayList<String> getTeamListAlive();

    int getStrikes(String player);

    void setStrikes(String player, int strikes);

    boolean isClearInv(String player);

    void setClearInv(String player, boolean clearInv);

    ArrayList<String> getPlayers();
}
