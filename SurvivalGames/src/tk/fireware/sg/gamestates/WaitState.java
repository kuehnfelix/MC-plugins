package tk.fireware.sg.gamestates;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;

import tk.fireware.sg.BuildManager;
import tk.fireware.sg.Data;
import tk.fireware.sg.Items;
import tk.fireware.sg.Main;
import tk.fireware.sg.R;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class WaitState extends GameStates {

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
	    	if(event.getFrom().getBlockX() != event.getTo().getBlockX() 
	    			|| event.getFrom().getBlockZ() != event.getTo().getBlockZ()){
	    		float yaw = event.getFrom().getYaw();
	    		float pitch = event.getFrom().getPitch();
	    		Location loc = event.getFrom().getBlock().getLocation().add(0.5,0,0.5);
	    		loc.setPitch(pitch); loc.setYaw(yaw);
	    		event.getPlayer().teleport(loc);
	    	}
//	    	event.setCancelled(true);
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
	    	
	    }

	    @Override
	    public void onPlayerJoin(PlayerJoinEvent event) {
	        ItemStack[] itemStacks=Items.getLobbyMenu();
	        event.getPlayer().setFallDistance(0);
	        event.getPlayer().setGameMode(GameMode.SPECTATOR);
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
	        }

	    }

	    @Override
	    public void trigger() {
	        
	    }

		@Override
		public void onPlayerOpenInventory(InventoryOpenEvent event) {
			
		}

	@Override
	public void onInvClick(InventoryClickEvent event) {

	}

	@Override
		public void init() {
	        for(Player p : Bukkit.getServer().getOnlinePlayers()){
	        	p.closeInventory();
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
			event.setMotd(ChatColor.DARK_GRAY+"["+ChatColor.DARK_GREEN+"SG-1"+ChatColor.DARK_GRAY+"];"+ChatColor.YELLOW+"STARTING;"+ChatColor.BLUE+Data.MAPNAME+";"+event.getNumPlayers()+"/"+Data.getInstance().maxPlayers);
		}

		@Override
		public void onLeave(PlayerQuitEvent event) {
			event.getPlayer().setGameMode(GameMode.SURVIVAL);
		}

	@Override
	public void onLaunch(ProjectileLaunchEvent event) {

	}


}
