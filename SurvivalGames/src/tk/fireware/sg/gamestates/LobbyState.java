package tk.fireware.sg.gamestates;


import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tk.fireware.sg.*;
import tk.fireware.sg.abilities.Ability;
import tk.fireware.sg.abilities.RAbilitiesKt;
import tk.fireware.sg.coins.Money;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class LobbyState extends GameStates {


    public static HashMap<Player, Ability> gekaufteItems = new HashMap<>();

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
        		event.getPlayer().setFallDistance(0);
                event.getPlayer().teleport (event.getPlayer().getWorld().getSpawnLocation());
        }
    }

    @Override
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
    	if(!(event.getEntity() instanceof Player)){
    		event.setDamage(0);
    		return;
    	}
        if(BuildManager.getInstance().isBuilder((Player)event.getEntity())) {
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
    public void onLogin(PlayerLoginEvent event) {
        if(Bukkit.getOnlinePlayers().size()>=Data.getInstance().getMaxPlayers()) {
        	if(event.getPlayer().hasPermission("sg.joinfull")) {
                if(Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if(!player.hasPermission("sg.joinfull")) {
                            player.kickPlayer("Du wurdest lokal transferiert, um einem Premium-Spieler das Betreten des Servers zu ermöglichen!");
                            event.allow();
                            return;
                        }
                    }
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Error 402 ~ PAYMENT REQUIERED. Spende jetzt 200€ um den Server wieder zu betreten.");
                }
            } else {
                if(Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()) {
                    event.disallow(PlayerLoginEvent.Result.KICK_FULL, org.bukkit.ChatColor.RED+"Error 412 ~ PRECONDITION FAILED! Exception at tk.fireware.payment.buypremium!");
                } else {
                    event.allow();
                }
            }
        }
        Player p=event.getPlayer();
        p.setFlying(false);
        p.setGameMode(GameMode.SURVIVAL);
        p.setFoodLevel(20);
        p.setSaturation(6);
    }
    
    

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        ItemStack[] itemStacks=Items.getLobbyMenu();
        event.getPlayer().setFallDistance(0);
        event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
        for(int i=0; i<9;i++){
            event.getPlayer().getInventory().setItem(i,itemStacks[i]);
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
    	if(event.getItem()==null || event.getItem().getItemMeta()==null|| event.getItem().getItemMeta().getDisplayName()==null){
    		return;
    	}
        String name=event.getItem().getItemMeta().getDisplayName();

        if(name.equals(R.NAMES_EXIT_ITEM)){
        	Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(Main.plugin, "BungeeCord");
    		ByteArrayDataOutput out = ByteStreams.newDataOutput();
    		out.writeUTF("Connect");
    		out.writeUTF("lobby");
    		
    		event.getPlayer().sendPluginMessage(Main.plugin, "BungeeCord", out.toByteArray());
            return;
        }else if(name.equals(R.SHOP)){
            Inventory i = Bukkit.getServer().createInventory(null, InventoryType.CHEST, R.SHOP);
            ArrayList<Ability> abilities = RAbilitiesKt.getAbilities();

            for(Ability a : abilities){
                ItemStack s = new ItemStack(a.getDisplayMaterial());
                ItemMeta m = s.getItemMeta();
                m.setDisplayName(a.getName());
                int ffd = a.getPreis();
                m.setLore(Arrays.asList(new String[]{" ~ "+ffd+" Knochen"}));
                s.setItemMeta(m);
                i.addItem(s);
            }

            event.getPlayer().openInventory(i);
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

	@Override
	public void onPlayerOpenInventory(InventoryOpenEvent event) {
		
	}

    @Override
    public void onInvClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null || event.getCurrentItem().getItemMeta().getDisplayName() == null)
            return;
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem().clone();
            if(gekaufteItems.containsKey(event.getWhoClicked())){
                event.getWhoClicked().sendMessage(R.MESSAGE_NOTATB);
                return;
            }
            String s = item.getItemMeta().getDisplayName();
            Bukkit.broadcastMessage(s);
            Ability a = RAbilitiesKt.getAbility(s);
            int hjf = a.getPreis();
            if(Money.getInstance().getMoney((Player) event.getWhoClicked()) >= hjf){
                Money.getInstance().addMoney((Player) event.getWhoClicked(), RAbilitiesKt.getAbility(event.getCurrentItem().getItemMeta().getDisplayName()).getPreis()*-1);
                event.getWhoClicked().getInventory().addItem(item);
                Ability ability= RAbilitiesKt.getAbility(event.getCurrentItem().getItemMeta().getDisplayName());
                gekaufteItems.put((Player) event.getWhoClicked(), ability);
                event.getWhoClicked().sendMessage(R.MESSAGE_PURCHASED);
            }else{
                event.getWhoClicked().sendMessage(R.MESSAGE_NOTEM);
            }
    }

    @Override
	public void init() {
        Bukkit.broadcastMessage(R.MESSAGE_PLUGINBY);
        for(Player p : Bukkit.getOnlinePlayers()){
            ItemStack[] itemStacks=Items.getLobbyMenu();
            p.setFallDistance(0);
            p.teleport(p.getWorld().getSpawnLocation());
            for(int i=0; i<9;i++){
                p.getInventory().setItem(i,itemStacks[i]);
            }
        }
	}

	@Override
	public void onPlayerDeath(PlayerDeathEvent event) {
		
	}

	@Override
	public void onPlayerHunger(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onServerPing(ServerListPingEvent event) {
		Data.getInstance();
		event.setMotd(ChatColor.DARK_GRAY+"["+ChatColor.DARK_GREEN+"SG-1"+ChatColor.DARK_GRAY+"];"+ChatColor.GREEN+"LOBBY;"+ChatColor.BLUE+Data.MAPNAME+";"+event.getNumPlayers()+"/"+Data.getInstance().maxPlayers);
	}

	@Override
	public void onLeave(PlayerQuitEvent event) {
        event.getPlayer().setGameMode(GameMode.SURVIVAL);
        if(gekaufteItems.containsKey(event.getPlayer())){
            int price = gekaufteItems.get(event.getPlayer()).getPreis();
            gekaufteItems.remove(event.getPlayer());
            Money.getInstance().addMoney(event.getPlayer(), price);
        }
	}

    @Override
    public void onLaunch(ProjectileLaunchEvent event) {

    }
}
