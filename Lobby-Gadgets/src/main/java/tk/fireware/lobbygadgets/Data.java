package tk.fireware.lobbygadgets;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Data {
    static Data instance = new Data().deserialize();
    private HashMap<Player,Boolean> visibility=new HashMap<Player, Boolean>();
    private HashMap<String, Location> locations = new HashMap<>();
    public boolean getPlayersVisible(Player p) {
        if(visibility==null) {

        }
        return visibility.get(p);
    }

    public HashMap<Player, Boolean> getVisibility() {
        return visibility;
    }

    public void setVisibility(Player player, boolean isVisible) {
        visibility.remove(player);
        visibility.put(player,isVisible);
    }

    public boolean setWarp(String name, Location location) {
        if(locations.containsKey(name)) {
            locations.remove(name);
            locations.put(name, location);
            serialize();
            return true;
        } else if(!name.equalsIgnoreCase("set")) {
            locations.put(name, location);
            serialize();
            return true;
        }
        return false;
    }

    public Location getWarp(String name) {
        if(locations.containsKey(name)) {
            return locations.get(name);
        }
        return null;
    }


    public Data serialize() {
        File file = new File(Constraints.FILE);
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);

        ArrayList<String> locString = new ArrayList<>();
        for(String name : locations.keySet()) {
            Location loc = locations.get(name);
            locString.add(name + " " + loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " " + loc.getYaw() + " " + loc.getPitch());
        }
        data.set("LOCATIONS", locString);

        try {
            data.save(file);
        } catch(Exception e) { e.printStackTrace();}
        return this;
    }

    public Data deserialize() {
        File file = new File(Constraints.FILE);
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);

        for(String string : data.getStringList("LOCATIONS")) {
            String[] substring = string.split(" ");
            Location loc = new Location(Bukkit.getServer().getWorld(substring[1]), Double.valueOf(substring[2]), Double.valueOf(substring[3]), Double.valueOf(substring[4]), Float.parseFloat(substring[5]), Float.parseFloat(substring[6]));
            locations.put(substring[0], loc);
        }

        try {
            data.save(file);
        } catch(Exception e) { e.printStackTrace();}
        return this;
    }

}
