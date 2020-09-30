package tk.fireware.lobbygadgets;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Plugin extends JavaPlugin implements Listener {

    public static Plugin instance;

    @Override
    public void onEnable() {
        instance = this;
        new ItemManager();
        //new Board();
        Bukkit.getPluginManager().registerEvents(this, this);

        Bukkit.getPluginCommand("warp").setExecutor(CommandManager.getInstance());
        Bukkit.getPluginCommand("updateresourcepack").setExecutor(CommandManager.getInstance());

        saveConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }



    @EventHandler
    public void onPlayerDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setDamage(0);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Board.getInstance().updateScoreboard(event.getPlayer());
        event.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());
        event.getPlayer().setFoodLevel(20);
        event.getPlayer().setHealth(20);
        if(!getConfig().getStringList("HASRESOURCEPACK").contains(event.getPlayer().getUniqueId().toString())) {
            event.getPlayer().setResourcePack("pack.zip");
            List<String> list =  getConfig().getStringList("HASRESOURCEPACK");
            list.add(event.getPlayer().getUniqueId().toString());
            getConfig().set("HASRESOURCEPACK", list);
            saveConfig();
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
            event.setFoodLevel(20);
        }
    }

    @EventHandler
    public void onVoid(PlayerMoveEvent event) {
        if (event.getPlayer().getLocation().getY()<20) {
            event.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());       }
    }
}
