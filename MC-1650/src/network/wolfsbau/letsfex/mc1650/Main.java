package network.wolfsbau.letsfex.mc1650;

import com.mojang.brigadier.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends JavaPlugin implements Listener {

    private static Main plugin;

    private final String[] pirates = new String[]{"Wolf_PlayzYT", "QuotenNic", "DerKoPe", "MultiDuuude", "7abianation"};

    final String PREFIX = ChatColor.GRAY + "[" + ChatColor.AQUA + "SAIL" + ChatColor.GRAY + "] " + ChatColor.YELLOW;
    final boolean debug = true;

    private final int MAX_SHIP_BLOCKS = 10000;

    private static final BlockFace[] faces = {
            BlockFace.DOWN,
            BlockFace.UP,
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST
    };

    private void debug(String message) {
        if(debug) {
            Bukkit.broadcastMessage(PREFIX + message);
        }
    }

    @Override
    public void onEnable() {
        plugin = this;
        Bukkit.getPluginManager().registerEvents(this,this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(Arrays.asList(pirates).contains(event.getPlayer().getName())) {
            event.getPlayer().setDisplayName("[Pirat] " +event.getPlayer().getName() );
        } else {
            event.getPlayer().setDisplayName("[Rotrock] " +event.getPlayer().getName() );
        }
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getClickedBlock().getType().name().contains("SIGN")) {
            Sign sign = (Sign) event.getClickedBlock().getState();
            if(sign.getLine(0).equals("[SAIL]")) {
                String[] coords = sign.getLine(1).split(" ");
                teleportBoat(event.getClickedBlock(), new Location(sign.getLocation().getWorld(), Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2])));
            }

        }
    }

    /**
     * Teleports a boat to a specified location
     * @param from One Block of the Boat
     * @param to Location where the from Block will go
     */
    private void teleportBoat(Block from, Location to) {
        debug("-----------------------------");
        debug("Sailing from " + from.getLocation().getBlockX() + " "  + from.getLocation().getBlockY() + " " + from.getLocation().getBlockZ() + " to " + to.getBlockX() + " " + to.getBlockY() + " " + to.getBlockZ());

        List<Block> blocks = null;

        try {
            blocks = getShipBlocks(from);
        } catch(Exception e) {
            debug("Das Schiff ist zu groß!");
            debug("-----------------------------");
            return;
        }

        debug("Added " + blocks.size() + " Shipblocks!");

        Location vector = to.subtract(from.getLocation());

        //get all players on the boat
        List<Player> players = new ArrayList<>();
        for(Block b : blocks) {
            for(Player p : Bukkit.getOnlinePlayers()) {
                if(p.getLocation().getBlock().getLocation().equals(b.getLocation().add(0,1,0))) {
                    players.add(p);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 255));
                    debug("added " + p.getDisplayName() + " to list");
                }
            }
        }

        //copy all the boat's blocks to the new location
        for(Block b : blocks) {
            b.getWorld().getBlockAt(b.getLocation().add(vector)).setType(b.getType());
            b.getWorld().getBlockAt(b.getLocation().add(vector)).setBlockData(b.getBlockData());
        }
        debug("copied ship");

        //write the coordinates back on the sign
        Sign sign = (Sign)from.getWorld().getBlockAt(from.getLocation().add(vector)).getState();
        sign.setLine(0, "[SAIL]");
        sign.setLine(1, from.getLocation().getBlockX() + " " + from.getLocation().getBlockY() + " " + from.getLocation().getBlockZ());
        sign.update();
        debug("created new sign");

        List<Block> finalBlocks = blocks;
        new BukkitRunnable() {
            @Override
            public void run() {


                //teleport players to new location
                for(Player p : players) {
                    p.teleport(p.getLocation().add(vector));
                    debug("teleported " + p.getDisplayName());
                    p.setFlying(true);
                }

                //remove the old Blocks
                for(Block b : finalBlocks) {
                    b.setType(Material.AIR);
                }
                debug("removed old ship");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for(Player p : players) {
                            p.setFlying(false);
                            debug("-----------------------------");
                        }
                    }
                }.runTaskLater(plugin, 10);
            }
        }.runTaskLater(plugin, 10);


    }

    /**
     *
      * @param clickedBlock The initial Block to start the search for all boat blocks from
     * @return Returns a List of all Blocks connected to the boat (not water and not air)
     */
    private List<Block> getShipBlocks(Block clickedBlock) throws Exception {
        //List to store all blocks that are part of the boat
        List<Block> confirmed = new ArrayList<>();
        //List for all blocks that need to be checked if they are part of the boat
        List<Block> toCheck = new ArrayList<>();

        //add the initial block to the checklist
        toCheck.add(clickedBlock);

        //look for more boat blocks until toCheck is empty(all blocks checked) or the maximum allowed amount of blocks is reached
        while(!toCheck.isEmpty() && confirmed.size()<=MAX_SHIP_BLOCKS) {
            Block block = toCheck.get(0);
            toCheck.remove(0);

            //if the block is not water or air (-> it is part of the boat) add it to the confirmed list and add it's neighbours to the checklist
            if(block.getType() != Material.WATER && block.getType()!=Material.AIR) {
                confirmed.add(block);

                for(BlockFace face : faces) {
                    if(!confirmed.contains(block.getRelative(face)) && !toCheck.contains(block.getRelative(face))) {
                        toCheck.add(block.getRelative(face));
                    }
                }

            }
        }
        if(confirmed.size()>=MAX_SHIP_BLOCKS-3) {
            throw new Exception("Too many Blocks in ship");
        }
        return confirmed;
    }
}
