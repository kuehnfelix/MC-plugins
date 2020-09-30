package network.wolfsbau.piratebattle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;

public final class Piratebattle extends JavaPlugin implements Listener {

    private ArrayList<String> pirates = new ArrayList<String>(Arrays.asList(new String[]{"Wolf_PlayzYT", "QuotenNic", "DerKoPe", "MultiDuuude", "7abianation", "Brakito"}));
    private Location piratePosition;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this,this);
        piratePosition = new Location(Bukkit.getWorlds().get(0), -44, 71, 0);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(pirates.contains(event.getPlayer().getName())) {
            event.getPlayer().teleport(piratePosition);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        event.getPlayer().setGameMode(GameMode.SPECTATOR);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        boolean piraten = false;
        boolean briten = false;
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(p.equals(event.getEntity())) continue;
            if(p.getGameMode().equals(GameMode.SURVIVAL)) {
                if(pirates.contains(event.getEntity().getName())) {
                    piraten = true;
                } else {
                    briten=true;
                }
            }

        }

        System.out.println("piraten: " + piraten);
        System.out.println("briten: " + briten);

        if(piraten==false && briten==true) {
            for(Player p : Bukkit.getOnlinePlayers()) {
                sendTitle(p, ChatColor.BLACK + "Piraten gewinnen!", "Game Over", 3, 100, 60);
            }
        } else if(piraten==true && briten==false) {
            for(Player p : Bukkit.getOnlinePlayers()) {
                sendTitle(p, ChatColor.RED + "Briten gewinnen!", "Game Over", 3, 100, 60);
            }
        }

    }

    public void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Class<?> getNMSClass(String name) {
        // org.bukkit.craftbukkit.v1_8_R3...
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        try {
            Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
            Object enumSubTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
            Object chat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
            Object subchat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");
            Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
            Object packet = titleConstructor.newInstance(enumTitle, chat, fadeIn, stay, fadeOut);
            Object packet2 = titleConstructor.newInstance(enumSubTitle, subchat, fadeIn, stay, fadeOut);
            sendPacket(player, packet);
            sendPacket(player, packet2);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
