package tk.fireware.bwsigns;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;


public class ServerSign {
    private Server server;
    private Location location;
    private String[] text = new String[4];

    public ServerSign(Location location, Server server) {
        setServer(server);
        setLocation(location);
    }

    public ServerSign(String serialized) {
        String[] s = serialized.split(";");
        server  = new Server(s[1]);
        String[] s2 = s[0].split(":");
        this.setLocation(new Location(Bukkit.getWorld(s2[0]), Integer.valueOf(s2[1]), Integer.valueOf(s2[2]), Integer.valueOf(s2[3])));
    }

    public boolean update() {
        Sign sign = null;
        if(server.fetchData()) {

            String[] motd = server.getMotd().split(";");

            try {
                text[0] = motd[0];
                text[1] = motd[1];
                text[2] = motd[2];
                text[3] = motd[3];
            } catch (Exception e) {
                text[0] = text[1] = "unsupported";
            }
        } else {
            String[] motd = server.getMotd().split(";");

            try {
                text[0] = motd[0];
                text[1] = ChatColor.RED+"OFFLINE";
                text[2] = motd[2];
                text[3] = "";
            } catch (Exception e) {
                text[0] = text[1] = "unsupported";
            }
        }



        try {
            sign = (Sign) Bukkit.getWorlds().get(0).getBlockAt(location).getState();
        } catch (Exception e) {
            BwSigns.instance.getLogger().info(Constraints.MESSAGE_SIGN_BROKEN);
            return false;
        }

        sign.setLine(0,text[0]+"");
        sign.setLine(1,text[1]+"");
        sign.setLine(2,text[2]+"");
        sign.setLine(3,text[3]+"");
        sign.update();

        return true;
    }



    public String serialize() {
        return location.getWorld().getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ() + ";" + server.serialize();
    }

    public void deserialize(String serialized) {
        String[] s = serialized.split(";");
        server.deserialize(s[1]);
        String[] s2 = s[0].split(":");
        this.setLocation(new Location(Bukkit.getWorld(s2[0]), Integer.valueOf(s2[1]), Integer.valueOf(s2[2]), Integer.valueOf(s2[3])));
    }



    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
