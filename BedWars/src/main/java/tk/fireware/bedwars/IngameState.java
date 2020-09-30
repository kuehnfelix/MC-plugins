package tk.fireware.bedwars;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class IngameState extends GameStates {

    enum DeathReason {
        VOID, PLAYER, FALLDAMAGE
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        event.getBlock().setMetadata("placed", new FixedMetadataValue(Main.plugin, "player"));
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if(!event.getBlock().hasMetadata("placed")) {
            event.setCancelled(true);
        }
    }
    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        if(event.getPlayer().getLocation().getY()<1) die(event.getPlayer(), DeathReason.VOID);
    }

    @Override
    public void onPlayerDamage(EntityDamageByEntityEvent event) {

    }

    @Override
    public void onPlayerDamage(EntityDamageEvent event) {

    }

    @Override
    public void onEntityInteractEntity(PlayerInteractAtEntityEvent event) {
        if(event.getRightClicked().getCustomName().equals(R.NAMES_SHOP)) {
            openShop(event.getPlayer());
            event.setCancelled(true);
        }
    }

    @Override
    public void onLogin(PlayerLoginEvent event) {

    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        //TODO
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {

    }

    @Override
    public void trigger() {

    }

    private void die(Player p, DeathReason dr) {
        if(dr.equals(DeathReason.VOID)) {
            p.setFireTicks(0);
            p.setHealth(20);
            p.setFallDistance(0);
            p.setSaturation(20);
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1, 255));
            p.teleport(Teams.getInstance().getSpawnLocation(p));
            return;
        }

        p.setFireTicks(0);
        p.setHealth(20);
        p.setFallDistance(0);
        p.setSaturation(20);
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1, 255));
        p.teleport(Teams.getInstance().getSpawnLocation(p));
    }




    private void openShop(Player player) {

    }
}
