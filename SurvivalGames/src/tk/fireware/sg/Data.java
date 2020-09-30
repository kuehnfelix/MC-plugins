package tk.fireware.sg;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Data implements Serializable {
	private static final long serialVersionUID = -6178996490122033000L;

	private static Data data;

    private Data() {}

    public static Data getInstance() {
        if(data==null) {
            data = new Data();
        }
        return data;
    }

//    private Location lobbySpawn = new Location(Bukkit.getWorld(R.WORLDNAME),0.5,32,0.5);

    public int maxPlayers = 8;
    double minPlayers = 2;

    private List<Location> spawnpoints = new ArrayList<Location>();

	public static String MAPNAME;

    //getters&setters

    public List<Location> getSpawnpoints() {
        return spawnpoints;
    }
    public void setSpawnpoints(List<Location> spawners_bronze) {
        this.spawnpoints = spawners_bronze;
    }
    public void addSpawnpoint(Location loc) {
        spawnpoints.add(loc);
    }
    public void removeSpawnpoint(Location loc) {
        spawnpoints.remove(loc);
    }

//    public Location getLobbySpawn() {
//        return lobbySpawn;
//    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public double getMinPlayers() {
        return minPlayers;
    }

    public void serialize() {
        File file = new File(Bukkit.getWorld(R.WORLDNAME).getWorldFolder().getAbsolutePath()+"/sgdata.ser");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        String sep = ",";

        //save spawn-locations
        
        cfg.set("mapname",MAPNAME);
        
        //save lobbySpawnLocation
//        if(lobbySpawn!=null) cfg.set("lobby-spawn", lobbySpawn.getWorld().getName()+sep+lobbySpawn.getX()+sep+lobbySpawn.getY()+sep+lobbySpawn.getZ());

        //save Playercounts
        cfg.set("minplayers", minPlayers);
//        cfg.set("maxplayers", maxPlayers);

        //save Spawner-Locations
        ArrayList<String> list = new ArrayList<String>();
        for(Location loc : spawnpoints) {
            list.add(loc.getWorld().getName()+sep+loc.getBlockX()+sep+loc.getBlockY()+sep+loc.getBlockZ());
        }
        cfg.set("spawnpoints", list.toArray());

        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void deserialize() {
        File file = new File(Bukkit.getWorld(R.WORLDNAME).getWorldFolder().getAbsolutePath()+"/sgdata.ser");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        String sep = ",";
//        String[] loc;

        //get spawn-locations

        MAPNAME=cfg.getString("mapname","SG-Map");
        
//        //get lobbySpawnLocation
//        if(cfg.getString("lobby-spawn", null)!=null) {
//            loc = cfg.getString("lobby-spawn", R.WORLDNAME + ",0.5,32,0.5").split(sep);
//            lobbySpawn = new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3]));
//        }

        //get Spawner-Locations
        ArrayList<String> list = (ArrayList) cfg.getStringList("spawnpoints");
        for(String s : list) {
            String[] st = s.split(sep);
            spawnpoints.add(new Location(Bukkit.getWorld(st[0]), Integer.parseInt(st[1]), Integer.parseInt(st[2]), Integer.parseInt(st[3])));
        }
        
        //get Playercounts
        minPlayers = list.size()*0.7;
//        maxPlayers = cfg.getInt("maxplayers", 8);
        maxPlayers=list.size();
    }

}
