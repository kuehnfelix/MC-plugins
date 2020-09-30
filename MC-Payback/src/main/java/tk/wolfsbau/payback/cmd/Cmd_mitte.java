package tk.wolfsbau.payback.cmd;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tk.wolfsbau.payback.db.Data;
import tk.wolfsbau.payback.db.Option;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Cmd_mitte implements CommandExecutor, Listener {



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission("payback.mitte")) {
            if(args.length==1) {
                if(sender instanceof Player) {
                    mitteBauen(((Player) sender).getLocation());
                    sender.sendMessage(ChatColor.GREEN + "Mitte wurde gebaut!");
                } else {
                    error(sender);
                }
            } else if(args.length==2) {
                mitteBauen((Bukkit.getWorlds().get(0).getHighestBlockAt(Integer.valueOf(args[0]),Integer.valueOf(args[1]))).getLocation());
                sender.sendMessage(ChatColor.GREEN + "Mitte wurde gebaut!");
            } else {
                error(sender);
            }
        } else {
            CmdMgr.noPermission(sender);
        }

        return true;
    }

    private void mitteBauen(Location location) {
        Data.getInstance().setOption(Option.MITTEX, (long)location.getBlockX());
        Data.getInstance().setOption(Option.MITTEZ, (long)location.getBlockZ());
        int playerCount = Data.getInstance().getPlayerCount();
        int umfang = 6 * playerCount;
        int radius = 2+(int)(umfang/(2*Math.PI));
        Location center = (location.clone().getWorld().getHighestBlockAt(location.getBlockX(), location.getBlockZ())).getLocation();
        int centerX = center.getBlockX();
        int centerZ = center.getBlockZ();
        portalBauen(center);

        HashMap<String, ArrayList<String>> map = Data.getInstance().getPlayerList();
        int i = 0;
        for(String team : map.keySet()) {
            ArrayList<String> players = map.get(team);
            for(String s : players) {
                double bogenmass = ((2*Math.PI)/playerCount)*i;
                int x = (int)(Math.cos(bogenmass)*radius);
                int z = (int)(Math.sin(bogenmass)*radius);
                Data.getInstance().setPlayerLocation(s, lochBauen(center.clone().add(x,0,z)));
                i++;
            }
        }
        umfang = (playerCount);
        radius = 1+(int)(umfang/(2*Math.PI));
        for(i = 0; i<(playerCount/4); i++) {
            double bogenmass = ((2*Math.PI)/(playerCount/4))*i;
            int x = (int)(Math.cos(bogenmass)*radius);
            int z = (int)(Math.sin(bogenmass)*radius);
            kisteBauen(center.clone().add(x,0,z));
        }
    }

    private void kisteBauen(Location loc) {
        Location loc2 = loc.getWorld().getHighestBlockAt(loc.getBlockX(),loc.getBlockZ()).getLocation();
        loc2.getWorld().getBlockAt(loc2).setType(Material.CHEST);
        Chest chest = (Chest)loc2.getWorld().getBlockAt(loc2).getState();
        Inventory inv = chest.getBlockInventory();

        Material[] randomItens = {Material.AIR, Material.AIR,Material.AIR ,Material.AIR,
                                Material.AIR,Material.AIR , Material.AIR,
                                Material.APPLE, Material.STICK, Material.STICK,
                                Material.DEAD_BUSH, Material.DEAD_BUSH, Material.WOOD_HOE,
                                Material.WOOD_HOE, Material.WOOD_AXE, Material.LEATHER_BOOTS,
                                Material.SIGN, Material.SIGN, Material.CARROT_STICK,
                                Material.ROTTEN_FLESH, Material.RAW_BEEF
                                };
        Random rand = new Random();

        for(int i = rand.nextInt(14)+4; i>0;i--) {
            inv.setItem(rand.nextInt(27), new ItemStack(randomItens[rand.nextInt(randomItens.length)]));
        }

    }

    private Location lochBauen(Location loc) {
        Location loc2 = new Location(loc.getWorld(), loc.getBlockX(), loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()),loc.getBlockZ());
        loc2.getWorld().getBlockAt(loc2.add(0,-1,0)).setType(Material.AIR);
        loc2.getWorld().getBlockAt(loc2.clone().add(0,-1,0)).setType(Material.STONE);
        loc2.getWorld().getBlockAt(loc2.clone().add(0,1,1)).setType(Material.WOOD_STEP);
        loc2.getWorld().getBlockAt(loc2.clone().add(0,1,-1)).setType(Material.WOOD_STEP);
        loc2.getWorld().getBlockAt(loc2.clone().add(1,1,0)).setType(Material.WOOD_STEP);
        loc2.getWorld().getBlockAt(loc2.clone().add(-1,1,0)).setType(Material.WOOD_STEP);
        return loc2;
    }

    private void portalBauen(Location loc) {
        Location loc2 = new Location(loc.getWorld(), loc.getBlockX(), loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()),loc.getBlockZ());
        loc2.getWorld().getBlockAt(loc2.add(0,0,0)).setType(Material.OBSIDIAN);
        loc2.getWorld().getBlockAt(loc2.add(1,0,0)).setType(Material.OBSIDIAN);
            loc2.getWorld().getBlockAt(loc2.add(1,1,0)).setType(Material.OBSIDIAN);
            loc2.getWorld().getBlockAt(loc2.add(0,1,0)).setType(Material.OBSIDIAN);
            loc2.getWorld().getBlockAt(loc2.add(0,1,0)).setType(Material.OBSIDIAN);
            loc2.getWorld().getBlockAt(loc2.add(0,1,0)).setType(Material.OBSIDIAN);
        loc2.getWorld().getBlockAt(loc2.add(-1,1,0)).setType(Material.OBSIDIAN);
        loc2.getWorld().getBlockAt(loc2.add(-1,0,0)).setType(Material.OBSIDIAN);
        loc2.getWorld().getBlockAt(loc2.add(-1,0,0)).setType(Material.OBSIDIAN);
            loc2.getWorld().getBlockAt(loc2.add(-1,-1,0)).setType(Material.OBSIDIAN);
            loc2.getWorld().getBlockAt(loc2.add(0,-1,0)).setType(Material.OBSIDIAN);
            loc2.getWorld().getBlockAt(loc2.add(0,-1,0)).setType(Material.OBSIDIAN);
            loc2.getWorld().getBlockAt(loc2.add(0,-1,0)).setType(Material.OBSIDIAN);
        loc2.getWorld().getBlockAt(loc2.add(1,-1,0)).setType(Material.OBSIDIAN);
        loc2.getWorld().getBlockAt(loc2.add(0,1,0)).setType(Material.FIRE);
    }

    public static void error(CommandSender sender) {
        sender.sendMessage(ChatColor.DARK_RED + "Syntaxfehler!");
        sender.sendMessage(ChatColor.RED + "Usage:");
        sender.sendMessage(ChatColor.RED + "Players: /mitte bauen");
        sender.sendMessage(ChatColor.RED + "Console: /mitte <X> <Y>");
    }
}
