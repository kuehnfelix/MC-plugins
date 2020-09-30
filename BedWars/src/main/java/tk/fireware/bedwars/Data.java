package tk.fireware.bedwars;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Data implements Serializable {
    private static Data data;

    private Data() {}

    static Data getInstance() {
        if(data==null) {
            data = new Data();
        }
        return data;
    }

    //bed-locations
    Location redBED = null;
    Location blueBED = null;
    Location greenBED = null;
    Location yellowBED = null;
    private Location redBED2 = null;
    private Location blueBED2 = null;
    private Location greenBED2 = null;
    private Location yellowBED2 = null;
    //spawn-locations
    Location redSpawn = null;
    Location blueSpawn = null;
    Location greenSpawn = null;
    Location yellowSpawn = null;
    //Lobby location
    private Location lobbySpawn = new Location(Bukkit.getWorld(R.WORLDNAME),0.5,32,0.5);

    int maxPlayers = 8;
    int minPlayers = 2;

    private List<Location> spawners_bronze = new ArrayList<>();
    private List<Location> spawners_silver = new ArrayList<>();
    private List<Location> spawners_gold   = new ArrayList<>();

    public List<Location> getShops() {
        return shops;
    }

    public void setShops(List<Location> shops) {
        this.shops = shops;
    }

    public void addShop(Location loc) {
        shops.add(loc);
    }

    public void removeShop(Location loc) {
        shops.remove(loc);
    }

    private List<Location> shops   = new ArrayList<>();


    //getters&setters

    List<Location> getSpawners_bronze() {
        return spawners_bronze;
    }
    public void setSpawners_bronze(List<Location> spawners_bronze) {
        this.spawners_bronze = spawners_bronze;
    }
    List<Location> getSpawners_silver() {
        return spawners_silver;
    }
    public void setSpawners_silver(List<Location> spawners_silver) {
        this.spawners_silver = spawners_silver;
    }
    List<Location> getSpawners_gold() {
        return spawners_gold;
    }
    public void setSpawners_gold(List<Location> spawners_gold) {
        this.spawners_gold = spawners_gold;
    }
    void addSpawners_bronze(Location loc) {
        spawners_bronze.add(loc);
    }
    void addSpawners_silver(Location loc) {
        spawners_silver.add(loc);
    }
    void addSpawners_gold(Location loc) {
        spawners_gold.add(loc);
    }
    void removeSpawners_bronze(Location loc) {
        spawners_bronze.remove(loc);
    }
    void removeSpawners_silver(Location loc) {
        spawners_silver.remove(loc);
    }
    void removeSpawners_gold(Location loc) {
        spawners_gold.remove(loc);
    }

    Location getRedBED() {
        return redBED;
    }

    Location getRedBED2() {
        return redBED2;
    }

    Location getBlueBED2() {
        return blueBED2;
    }

    Location getGreenBED2() {
        return greenBED2;
    }

    Location getYellowBED2() {
        return yellowBED2;
    }

    Location getLobbySpawn() {
        return lobbySpawn;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    Location getBlueBED() {
        return blueBED;
    }
    Location getGreenBED() {
        return greenBED;
    }
    void setGreenBED(Location greenBED) {
        this.greenBED = greenBED;
    }
    Location getYellowBED() {
        return yellowBED;
    }
    void setYellowBED(Location yellowBED) {
        this.yellowBED = yellowBED;
    }
    void setRedBED2(Location redBED2) {
        this.redBED2 = redBED2;
    }
    void setBlueBED2(Location blueBED2) {
        this.blueBED2 = blueBED2;
    }
    void setGreenBED2(Location greenBED2) {
        this.greenBED2 = greenBED2;
    }
    void setYellowBED2(Location yellowBED2) {
        this.yellowBED2 = yellowBED2;
    }
    Location getRedSpawn() {
        return redSpawn;
    }
    void setRedSpawn(Location redSpawn) {
        this.redSpawn = redSpawn;
    }
    Location getBlueSpawn() {
        return blueSpawn;
    }
    void setBlueSpawn(Location blueSpawn) {
        this.blueSpawn = blueSpawn;
    }
    Location getGreenSpawn() {
        return greenSpawn;
    }
    void setGreenSpawn(Location greenSpawn) {
        this.greenSpawn = greenSpawn;
    }
    Location getYellowSpawn() {
        return yellowSpawn;
    }
    void setYellowSpawn(Location yellowSpawn) {
        this.yellowSpawn = yellowSpawn;
    }
    void setRedBED(Location loc) {
        this.redBED = loc;
    }
    void setBlueBED(Location loc) {
        this.blueBED = loc;
    }

    //get if team is active
    boolean redTeamActive() {
        return !(redBED==null || redSpawn==null);
    }
    boolean blueTeamActive() {
        return !(blueBED==null || blueSpawn==null);
    }
    boolean greenTeamActive() {
        return !(greenBED==null || greenSpawn==null);
    }
    boolean yellowTeamActive() {
        return !(yellowBED==null || yellowSpawn==null);
    }


    void serialize() {
        File file = new File(Bukkit.getWorld(R.WORLDNAME).getWorldFolder().getAbsolutePath()+"/bwdata.ser");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        String sep = ",";
        //save BedLocations
        if(redBED!=null) cfg.set("redbed", redBED.getWorld().getName() + sep + redBED.getBlockX() + sep + redBED.getBlockY() + sep + redBED.getBlockZ());
        if(blueBED!=null)cfg.set("bluebed", blueBED.getWorld().getName() + sep + blueBED.getBlockX()+sep+blueBED.getBlockY()+sep+blueBED.getBlockZ());
        if(greenBED!=null)cfg.set("greenbed", greenBED.getWorld().getName()+sep+greenBED.getBlockX()+sep+greenBED.getBlockY()+sep+greenBED.getBlockZ());
        if(yellowBED!=null)cfg.set("yellowbed", yellowBED.getWorld().getName()+sep+yellowBED.getBlockX()+sep+yellowBED.getBlockY()+sep+yellowBED.getBlockZ());

        if(redBED2!=null)cfg.set("redbed2", redBED2.getWorld().getName()+sep+redBED2.getBlockX()+sep+redBED2.getBlockY()+sep+redBED2.getBlockZ());
        if(blueBED2!=null)cfg.set("bluebed2", blueBED2.getWorld().getName()+sep+blueBED2.getBlockX()+sep+blueBED2.getBlockY()+sep+blueBED2.getBlockZ());
        if(greenBED2!=null)cfg.set("greenbed2", greenBED2.getWorld().getName()+sep+greenBED2.getBlockX()+sep+greenBED2.getBlockY()+sep+greenBED2.getBlockZ());
        if(yellowBED2!=null)cfg.set("yellowbed2", yellowBED2.getWorld().getName()+sep+yellowBED2.getBlockX()+sep+yellowBED2.getBlockY()+sep+yellowBED2.getBlockZ());

        //save spawn-locations
        if(redSpawn!=null)cfg.set("redspawn", redSpawn.getWorld().getName()+sep+redSpawn.getBlockX()+sep+redSpawn.getBlockY()+sep+redSpawn.getBlockZ());
        if(blueSpawn!=null)cfg.set("bluespawn", blueSpawn.getWorld().getName()+sep+blueSpawn.getBlockX()+sep+blueSpawn.getBlockY()+sep+blueSpawn.getBlockZ());
        if(greenSpawn!=null)cfg.set("greenspawn", greenSpawn.getWorld().getName()+sep+greenSpawn.getBlockX()+sep+greenSpawn.getBlockY()+sep+greenSpawn.getBlockZ());
        if(yellowSpawn!=null)cfg.set("yellowspawn", yellowSpawn.getWorld().getName()+sep+yellowSpawn.getBlockX()+sep+yellowSpawn.getBlockY()+sep+yellowSpawn.getBlockZ());

        //save lobbySpawnLocation
        if(lobbySpawn!=null) cfg.set("lobby-spawn", lobbySpawn.getWorld().getName()+sep+lobbySpawn.getX()+sep+lobbySpawn.getY()+sep+lobbySpawn.getZ());

        //save Playercounts
        cfg.set("minplayers", minPlayers);
        cfg.set("maxplayers", maxPlayers);

        //save Spawner-Locations
        ArrayList<String> list = new ArrayList<>();
        for(Location loc : spawners_bronze) {
            list.add(loc.getWorld().getName()+sep+loc.getBlockX()+sep+loc.getBlockY()+sep+loc.getBlockZ());
        }
        cfg.set("spawners_bronze", list.toArray());
        list.clear();
        for(Location loc : spawners_silver) {
            list.add(loc.getWorld().getName()+sep+loc.getBlockX()+sep+loc.getBlockY()+sep+loc.getBlockZ());
        }
        cfg.set("spawners_silver", list.toArray());
        list.clear();
        for(Location loc : spawners_gold) {
            list.add(loc.getWorld().getName()+sep+loc.getBlockX()+sep+loc.getBlockY()+sep+loc.getBlockZ());
        }

        cfg.set("spawners_gold", list.toArray());

        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    void deserialize() {
        File file = new File(Bukkit.getWorld(R.WORLDNAME).getWorldFolder().getAbsolutePath()+"/bwdata.ser");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        //get BedLocations
        String sep = ",";
        String[] loc;
        if(cfg.getString("redbed", null)!=null) {
            loc = cfg.getString("redbed", null).split(sep);
            redBED = new Location(Bukkit.getWorld(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]),Integer.parseInt( loc[3]));
        }
        if(cfg.getString("bluebed", null)!=null) {
            loc = cfg.getString("bluebed", null).split(sep);
            blueBED = new Location(Bukkit.getWorld(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]), Integer.parseInt(loc[2]));
        }
        if(cfg.getString("greenbed", null)!=null) {
            loc = cfg.getString("greenbed", null).split(sep);
            greenBED = new Location(Bukkit.getWorld(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]), Integer.parseInt(loc[3]));
        }
        if(cfg.getString("yellowbed", null)!=null) {
            loc = cfg.getString("yellowbed", null).split(sep);
            yellowBED = new Location(Bukkit.getWorld(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]), Integer.parseInt(loc[3]));
        }
        if(cfg.getString("redbed2", null)!=null) {
            loc = cfg.getString("redbed2", null).split(sep);
            redBED2 = new Location(Bukkit.getWorld(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]), Integer.parseInt(loc[3]));
        }
        if(cfg.getString("bluebed2", null)!=null) {
            loc = cfg.getString("bluebed2", null).split(sep);
            blueBED2 = new Location(Bukkit.getWorld(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]), Integer.parseInt(loc[3]));
        }
        if(cfg.getString("greenbed2", null)!=null) {
            loc = cfg.getString("greenbed2", null).split(sep);
            greenBED2 = new Location(Bukkit.getWorld(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]), Integer.parseInt(loc[3]));
        }
        if(cfg.getString("yellowbed2", null)!=null) {
            loc = cfg.getString("yellowbed2", null).split(sep);
            yellowBED2 = new Location(Bukkit.getWorld(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]), Integer.parseInt(loc[3]));
        }


        //get spawn-locations

        if(cfg.getString("redspawn", null)!=null) {
            loc = cfg.getString("redspawn", null).split(sep);
            redSpawn = new Location(Bukkit.getWorld(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]),Integer.parseInt( loc[3]));
        }
        if(cfg.getString("bluespawn", null)!=null) {
            loc = cfg.getString("bluespawn", null).split(sep);
            blueSpawn = new Location(Bukkit.getWorld(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]),Integer.parseInt( loc[3]));
        }
        if(cfg.getString("greenspawn", null)!=null) {
            loc = cfg.getString("greenspawn", null).split(sep);
            greenSpawn = new Location(Bukkit.getWorld(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]),Integer.parseInt( loc[3]));
        }
        if(cfg.getString("yellowspawn", null)!=null) {
            loc = cfg.getString("yellowspawn", null).split(sep);
            yellowSpawn = new Location(Bukkit.getWorld(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]),Integer.parseInt( loc[3]));
        }


        //get lobbySpawnLocation
        if(cfg.getString("lobby-spawn", null)!=null) {
            loc = cfg.getString("lobby-spawn", R.WORLDNAME + " ,0.5,32,0.5").split(sep);
            lobbySpawn = new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3]));
        }

        //get Playercounts
        minPlayers = cfg.getInt("minplayers", 4);
        maxPlayers = cfg.getInt("maxplayers", 8);

        //get Spawner-Locations
        ArrayList<String> list = (ArrayList) cfg.getStringList("spawners_bronze");
        for(String s : list) {
            String[] st = s.split(sep);
            spawners_bronze.add(new Location(Bukkit.getWorld(st[0]), Integer.parseInt(st[1]), Integer.parseInt(st[2]), Integer.parseInt(st[3])));
        }

        list = (ArrayList) cfg.getStringList("spawners_silver");
        for(String s : list) {
            String[] st = s.split(sep);
            spawners_silver.add(new Location(Bukkit.getWorld(st[0]), Integer.parseInt(st[1]), Integer.parseInt(st[2]), Integer.parseInt(st[3])));
        }

        list = (ArrayList) cfg.getStringList("spawners_gold");
        for(String s : list) {
            String[] st = s.split(sep);
            spawners_gold.add(new Location(Bukkit.getWorld(st[0]), Integer.parseInt(st[1]), Integer.parseInt(st[2]), Integer.parseInt(st[3])));
        }

    }

}
