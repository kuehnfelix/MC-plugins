package tk.fireware.sg.gamestates;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import tk.fireware.sg.Data;
import tk.fireware.sg.Main;
import tk.fireware.sg.R;

public class RestartState extends GameStates{

	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent event) {
	}

	@Override
	public void onPlayerDamage(EntityDamageByEntityEvent event) {
	}

	@Override
	public void onPlayerDamage(EntityDamageEvent event) {
	}

	@Override
	public void onPlayerDeath(PlayerDeathEvent event) {
	}

	@Override
	public void onLogin(PlayerLoginEvent event) {
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
	}

	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
	}

	@Override
	public void onPlayerOpenInventory(InventoryOpenEvent event) {
	}

	@Override
	public void onInvClick(InventoryClickEvent event) {

	}

	private int sec=0;
	
	@Override
	public void trigger() {
		sec++;
		if(sec>=R.restartSec-5 || sec==R.restartSec-10){
			Bukkit.broadcastMessage(R.MESSAGE_RESTART.replaceAll("<number>", String.valueOf(R.restartSec-sec)));
		}
		if(sec>R.restartSec){
			restart();
		}
		if(sec>R.restartSec+2){
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "restart"); 
		}
	}

	private void restart() {
		Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(Main.plugin, "BungeeCord");
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF("lobby");

		for(Player player : Bukkit.getOnlinePlayers()){
			player.setGameMode(GameMode.SURVIVAL);
			player.sendPluginMessage(Main.plugin, "BungeeCord", out.toByteArray());
		}
	}

	@Override
	public void init() {
		for(Player p : Bukkit.getServer().getOnlinePlayers()){
			p.setGameMode(GameMode.SURVIVAL);
			p.setHealth(20);
			p.setTotalExperience(1);
		}
		Bukkit.broadcastMessage(R.MESSAGE_PLUGINBY);
	}

	@Override
	public void onPlayerHunger(FoodLevelChangeEvent event) {
	}

	@Override
	public void onServerPing(ServerListPingEvent event) {
		Data.getInstance();
		event.setMotd(ChatColor.DARK_GRAY+"["+ChatColor.DARK_GREEN+"SG-1"+ChatColor.DARK_GRAY+"];"+ChatColor.RED+"RESTARTING...;"+ChatColor.BLUE+Data.MAPNAME+";"+event.getNumPlayers()+"/"+Data.getInstance().maxPlayers);
	}

	@Override
	public void onLeave(PlayerQuitEvent event) {
		event.getPlayer().setGameMode(GameMode.SURVIVAL);
	}

	@Override
	public void onLaunch(ProjectileLaunchEvent event) {

	}

}
