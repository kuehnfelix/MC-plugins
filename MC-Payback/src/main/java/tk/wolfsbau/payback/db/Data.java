package tk.wolfsbau.payback.db;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;

public class Data {
    private static Data instance;
    private IDataGetter data;


    private Data() {
        data = new DataGetter_mySQL_new();
    }

    public static Data getInstance() {
        if(instance == null) instance = new Data();
        return instance;
    }

    public String getTeam(String p) {
        return data.getTeam(p);
    }
    public ArrayList<String> getTeamPlayers(String name) {
        return data.getTeamPlayers(name);
    }
    public void setTeam(String name, ArrayList<String> players) {
        data.setTeam(name, players);
    }
    public void removeTeam(String name) {
        data.removeTeam(name);
    }
    public ArrayList<String> getTeamList() {
        return data.getTeamList();
    }
    public ArrayList<String> getTeamListAlive() {
        return data.getTeamListAlive();
    }
    public HashMap<String, ArrayList<String>> getPlayerList() {
        return data.getPlayerList();
    }

    public int getStrikes(String player) {return data.getStrikes(player);}
    public void setStrikes(String player, int strikes) {data.setStrikes(player, strikes);}
    public boolean isClearInv(String player) {return data.isClearInv(player);}
    public void setClearInv(String player, boolean clearInv) {data.setClearInv(player, clearInv);}

    public int getAliveTeamCount() {return data.getAliveTeamCount();}
    public int getPlayerCount() {
        return data.getPlayerCount();
    }
    public boolean isWhiteListed(String player) {
        return data.isWhiteListed(player);
    }

    public boolean isTeamAlive(String team) {return data.isTeamAlive(team);}

    public boolean isAlive(String player) {return data.isAlive(player);}
    public void setAlive(String player, boolean isAlive) {data.setAlive(player, isAlive);}

    public int getFolge(String player) {
        return data.getFolge(player);
    }
    public void setFolge(String player, int folge) {
        data.setFolge(player, folge);
    }
    public int getMaxFolge() {return data.getMaxFolge();}

    public int getDay() {return data.getDay();}

    public Location getPlayerLocation(String player) {
        return data.getPlayerLocation(player);
    }
    public void setPlayerLocation(String player, Location location) {
        data.setPlayerLocation(player, location);
    }

    public Location getChestLocation(String team) {
        return data.getChestLocation(team);
    }
    public void setChestLocation(String team, Location location){
        data.setChestLocation(team, location);
    }

    public Long getFolgeMax() {
        return (long)data.getMaxFolge();
    }

    public Location getMitte() {
        return new Location(Bukkit.getServer().getWorlds().get(0), getOption(Option.MITTEX),getOption(Option.MITTEZ),Bukkit.getServer().getWorlds().get(0).getHighestBlockYAt((int)(long)getOption(Option.MITTEX),(int)(long)getOption(Option.MITTEZ)));
    }

    public Long getOption(Option o) {
        return data.getOption(o);
    }
    public void setOption(Option option, Long value) {
        data.setOption(option, value);
    }
    public ArrayList<String> getOptionList() {
        return data.getOptionList();
    }

    public int getPlayerCountAlive() {return data.getPlayerCountAlive();}
    public double getBorderRadius() {return data.getBorderRadius();}

    public ArrayList<String> getPlayers() {return data.getPlayers();
    }
}
