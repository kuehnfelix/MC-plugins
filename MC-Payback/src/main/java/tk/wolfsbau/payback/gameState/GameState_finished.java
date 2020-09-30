package tk.wolfsbau.payback.gameState;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.meta.FireworkMeta;
import tk.wolfsbau.payback.db.Data;
import tk.wolfsbau.payback.db.Option;
import tk.wolfsbau.payback.discord_bot.Bot;

import java.util.Random;

public class GameState_finished implements IGameState {
    int counter_firework=0;
    @Override
    public void trigger() {
        if(counter_firework%3!=0)return;
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(Data.getInstance().isAlive(p.getName())) {
                //Spawn the Firework, get the FireworkMeta.
                Firework fw = (Firework) p.getWorld().spawnEntity(p.getLocation().add(0,2,0), EntityType.FIREWORK);
                FireworkMeta fwm = fw.getFireworkMeta();

                //Our random generator
                Random r = new Random();

                //Get the type
                int rt = r.nextInt(5) + 1;
                FireworkEffect.Type type = FireworkEffect.Type.BALL;
                if (rt == 1) type = FireworkEffect.Type.BALL;
                if (rt == 2) type = FireworkEffect.Type.BALL_LARGE;
                if (rt == 3) type = FireworkEffect.Type.BURST;
                if (rt == 4) type = FireworkEffect.Type.CREEPER;
                if (rt == 5) type = FireworkEffect.Type.STAR;

                //Get our random colours
                int r1i = r.nextInt(17) + 1;
                int r2i = r.nextInt(17) + 1;
                Color c1 = Color.fromBGR(r.nextInt(256), 255,0);
                Color c2 = Color.fromBGR(0, r.nextInt(256), 255);

                //Create our effect with this
                FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();

                //Then apply the effect to the meta
                fwm.addEffect(effect);

                //Generate some random power and set it
                int rp = r.nextInt(2) + 1;
                fwm.setPower(rp);

                //Then apply this to our rocket
                fw.setFireworkMeta(fwm);
            }
        }
    }

    @Override
    public void onItemDrop(PlayerDropItemEvent e) {

    }

    @Override
    public void onEntityExplode(EntityExplodeEvent event) {

    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(!Data.getInstance().isWhiteListed(event.getPlayer().getName())) {
            event.getPlayer().kickPlayer("Du stehst leider nicht auf der Gästeliste!");
            return;
        }
        int x = (int)(long)Data.getInstance().getOption(Option.MITTEX);
        int z = (int)(long)Data.getInstance().getOption(Option.MITTEZ);
        event.getPlayer().teleport(Bukkit.getWorlds().get(0).getHighestBlockAt(x,z).getLocation().add(0,20,0));
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {

    }

    @Override
    public void onPreLogin(PlayerLoginEvent event) {

    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event) {

    }

    @Override
    public void onRespawn(PlayerRespawnEvent event) {

    }

    @Override
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {

    }

    @Override
    public void onChat(AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        event.setFormat(ChatColor.GRAY + "[" + ChatColor.DARK_AQUA + Data.getInstance().getTeam(p.getName()) + ChatColor.GRAY + "] " + ChatColor.AQUA + p.getName() + ChatColor.GRAY + ": " + ChatColor.WHITE + event.getMessage());
        Bot.chatLog("[**"+Data.getInstance().getTeam(p.getName()) + "**] **"+p.getName() + "**: " + event.getMessage());
    }

    @Override
    public void craftItem(PrepareItemCraftEvent e) {

    }

    @Override
    public void onBrewEvent(BrewEvent event) {

    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {

    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {

    }

    @Override
    public void onSignPlace(SignChangeEvent event) {

    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {

    }

    @Override
    public void onHopper(InventoryMoveItemEvent event) {

    }
}
