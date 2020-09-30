package tk.fireware.bedwars;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class LobbyState extends GameStates {


    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        if(!BuildManager.getInstance().isBuilder(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        if(!BuildManager.getInstance().isBuilder(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        if(event.getPlayer().getLocation().getY()<5) {
                event.getPlayer().teleport (Data.getInstance().getLobbySpawn()
            );
        }
    }

    @Override
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if(BuildManager.getInstance().isBuilder((Player)event.getDamager())) {
            event.setCancelled(true);
        } else {
            event.setDamage(0);
        }
    }

    @Override
    public void onPlayerDamage(EntityDamageEvent event) {
        event.setDamage(0);
    }

    @Override
    public void onEntityInteractEntity(PlayerInteractAtEntityEvent event) {
        if(event.getRightClicked().getCustomName().equals(R.NAMES_SHOP)) {
            event.getPlayer().sendMessage(R.MESSAGES_SHOP_CLOSED);
            event.setCancelled(true);
        }
    }

    @Override
    public void onLogin(PlayerLoginEvent event) {
        if(Bukkit.getOnlinePlayers().size()>=Data.getInstance().getMaxPlayers()) {
            if(event.getPlayer().hasPermission("bedwars.joinfull")) {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    if(!p.hasPermission("bedwars.joinfull")) {
                        p.kickPlayer(R.MESSAGES_KICKED_BY_PREMIUM);
                        break;
                    }
                }
                if(Bukkit.getOnlinePlayers().size()>=Data.getInstance().getMaxPlayers()) {
                    event.setKickMessage(R.MESSAGES_SERVER_FULL);
                    event.disallow(PlayerLoginEvent.Result.KICK_FULL, R.MESSAGES_SERVER_FULL);
                } else {
                    event.allow();
                }
            } else {
                event.setKickMessage(R.MESSAGES_SERVER_FULL);
                event.disallow(PlayerLoginEvent.Result.KICK_FULL, R.MESSAGES_SERVER_FULL);
            }
        }
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        ItemStack[] itemStacks=Items.getLobbyMenu();
        for(int i=0; i<9;i++){
            event.getPlayer().getInventory().setItem(i,itemStacks[i]);
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        String name=event.getItem().getItemMeta().getDisplayName();

        if(name.equals(R.NAMES_RED_TEAM)){
            Teams.getInstance().addToRed(event.getPlayer());
            return;
        }
        if(name.equals(R.NAMES_BLUE_TEAM)){
            Teams.getInstance().addToBlue(event.getPlayer());
            return;
        }
        if(name.equals(R.NAMES_GREEN_TEAM)){
            Teams.getInstance().addToGreen(event.getPlayer());
            return;
        }
        if(name.equals(R.NAMES_YELLOW_TEAM)){
            Teams.getInstance().addToYellow(event.getPlayer());
            return;
        }
        if(name.equals(R.NAMES_EXIT_ITEM)){
            event.getPlayer().kickPlayer(R.MESSAGES_LEAVE);
            return;
        }

    }

    private int counter=0;
    @Override
    public void trigger() {
        if(counter > 13) {
            Bukkit.broadcastMessage(R.MESSAGES_PLAYERS_ONLINE.replace("<count>", Bukkit.getOnlinePlayers().size() + "/" +Data.getInstance().maxPlayers));
            counter=0;
        }
        counter++;
    }


}
