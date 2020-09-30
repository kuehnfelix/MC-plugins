package tk.fireware.bwsigns;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;


public final class BwSigns extends JavaPlugin implements Listener, PluginMessageListener {

    public static BwSigns instance;

    private ArrayList<ServerSign> serverSigns;

    private ServerSign lastSign;

    private File signfile;
    private FileConfiguration signscfg;


    @Override
    public void onEnable() {
        instance = this;
        signfile = new File(getDataFolder().getAbsolutePath(), "signs.yml");
        signscfg = YamlConfiguration.loadConfiguration(signfile);
        try {
            signscfg.save(signfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverSigns = new ArrayList<ServerSign>();
        deserialize();


        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

        Bukkit.getPluginManager().registerEvents(this, this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new BukkitRunnable() {
            @Override
            public void run() {
                updateSigns();
            }
            private void updateSigns() {
                ArrayList<ServerSign> toRemove = new ArrayList<ServerSign>();
                for(ServerSign serverSign : serverSigns) {
                    if(serverSign.update()) continue;
                    toRemove.add(serverSign);
                    serialize();
                }
                serverSigns.removeAll(toRemove);
            }

        }, 0L, 5L);
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            for(ServerSign serverSign : serverSigns) {
                if(serverSign.getLocation().equals(event.getClickedBlock().getLocation())) {
                    sendPlayer(event.getPlayer(), serverSign.getServer().getServerName());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSignBreak(BlockBreakEvent event) {
        if(!event.getPlayer().isSneaking()&&!event.getBlock().getType().equals(Material.SIGN)) {
            event.setCancelled(true);
        }
    }

    public void sendPlayer(Player p, String server){
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try{
            out.writeUTF("Connect");
            out.writeUTF(server);
        }catch(IOException e){
            e.printStackTrace();
        }
        p.sendPluginMessage(BwSigns.instance, "BungeeCord", b.toByteArray());
    }

    private void deserialize() {
        for(String serializedServer : signscfg.getStringList("SIGNS")) {
            serverSigns.add(new ServerSign(serializedServer));
        }
    }

    @EventHandler
    public void onSignPlace(SignChangeEvent event) {

        Sign sign = (Sign)event.getBlock().getState();
        String[] lines = event.getLines();
        if(!(lines[0].toUpperCase().equals(Constraints.SIGN_NAME))) return;
        String name = lines[1];


        lastSign = new ServerSign(event.getBlock().getLocation(), new Server(lines[1], "localhost", 25565));
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try{
            out.writeUTF("ServerIP");
            out.writeUTF(name);
        }catch(IOException e){
            e.printStackTrace();
        }
        event.getPlayer().sendPluginMessage(BwSigns.instance, "BungeeCord", b.toByteArray());
        serverSigns.add(lastSign);
        serialize();
    }

    private void serialize() {
        ArrayList<String> strings = new ArrayList<String>();
        for(ServerSign serverSign : serverSigns) {
            strings.add(serverSign.serialize());
        }
        signscfg.set("SIGNS", strings);
        try {
            signscfg.save(signfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        serialize();
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("ServerIP")) {
            String serverName = in.readUTF();
            String ip = in.readUTF();
            short port = in.readShort();
            Bukkit.broadcastMessage(ip + "   " + port);
            serverSigns.remove(lastSign);
            //lastSign.getServer().setAddress(InetSocketAddress.createUnresolved(ip, port));
            serverSigns.add(new ServerSign(lastSign.getLocation(), new Server(serverName, ip, port)));
            serialize();
        }
    }
}
