package tk.fireware.sg.gamestates;

import net.minecraft.server.v1_8_R3.ItemFood;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import tk.fireware.sg.ChestItems;
import tk.fireware.sg.Data;
import tk.fireware.sg.Main;
import tk.fireware.sg.R;
import tk.fireware.sg.abilities.Ability;
import tk.fireware.sg.abilities.FastFoodKt;
import tk.fireware.sg.abilities.RAbilitiesKt;
import tk.fireware.sg.coins.Money;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class IngameState extends GameStates {

	private ArrayList<Chest> chestsFilled=new ArrayList<Chest>();
	private ArrayList<Player> ingamePlayers=new ArrayList<Player>();
	private ArrayList<Player> spectators=new ArrayList<Player>();
	
	private HashMap<Block, Inventory> ecInv=new HashMap<Block,Inventory>();
	
	private HashMap<Player,Damage> map = new HashMap<>();

	private HashMap<Player,Long> time= new HashMap<>();

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
		if(LobbyState.gekaufteItems.containsKey(event.getPlayer())){
			//Sprinter
			if(LobbyState.gekaufteItems.get(event.getPlayer()).equals(RAbilitiesKt.getSprinter())){
				event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,1,1));
			}
			//Feder
			if(LobbyState.gekaufteItems.get(event.getPlayer()).equals(RAbilitiesKt.getFeder())){
				event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP,1,1));
			}
		}
	}

    @Override
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
    	if(event.getEntity() instanceof  Player && event.getDamager() instanceof Player) {
			if(LobbyState.gekaufteItems.containsKey(event.getDamager())){
				//Krümelmonster
				if(LobbyState.gekaufteItems.get(event.getDamager()).equals(RAbilitiesKt.getKrümelmonster())){
					if(((Player) event.getEntity()).getInventory().contains(Material.COOKIE)){
						double d = 3*event.getDamage();
						event.setDamage(d);
					}
				}
				//Feigling
				if(LobbyState.gekaufteItems.get(event.getDamager()).equals(RAbilitiesKt.getFeigling())){
					if(((Player) event.getEntity()).getHealth()/((Player) event.getEntity()).getMaxHealth() < 0.4){
						((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SPEED,20,3));
					}
				}
			}

    		Player d = (Player) event.getDamager();
    		Player p = (Player) event.getEntity();
    		map.put(p, new Damage(d,System.currentTimeMillis()));
		}
    }

    @Override
    public void onPlayerDamage(EntityDamageEvent event) {
    	
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().setGameMode(GameMode.SPECTATOR);
    }

    @SuppressWarnings("deprecation")
	@Override
    public void onPlayerInteract(PlayerInteractEvent e) {
    	/*if(LobbyState.gekaufteItems.containsKey(e.getPlayer())){
    		FastFood Handling
    		if(LobbyState.gekaufteItems.get(e.getPlayer()).equals(RAbilitiesKt.getFastFood())){
    			if(e.getItem() != null){
    				if(FastFoodKt.isEatable(e.getItem().getType())){
						((ItemFood)e.getItem()).getNutrition();
					}
				}
			}
		}*/
		if(LobbyState.gekaufteItems.containsKey(e.getPlayer())){
			//Sprinter
			if(LobbyState.gekaufteItems.get(e.getPlayer()).equals(RAbilitiesKt.getBillNewton())){
				if(e.getItem().getType().equals(RAbilitiesKt.getBillNewton().getDisplayMaterial())){
					if(!time.containsKey(e.getPlayer())||System.currentTimeMillis() - time.get(e.getPlayer())>8000){
						Vector v = e.getPlayer().getVelocity();
						v.add(new Vector(0,2,0));
						e.getPlayer().setVelocity(v);
						time.put(e.getPlayer(),System.currentTimeMillis());
					}
				}
			}
		}
    	if(e.getClickedBlock()==null){
    		return;
    	}
    	if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)&&e.getClickedBlock().getType().equals(Material.ENDER_CHEST)){
    		if(!ecInv.containsKey(e.getClickedBlock())){
        		ecInv.put(e.getClickedBlock(), Bukkit.getServer().createInventory(null, 27));
        		fillChest(ecInv.get(e.getClickedBlock()),3);
        	}
        	
        	final Inventory inv=ecInv.get((e.getClickedBlock()));
        	final Player p=e.getPlayer();
        	p.openInventory(inv);
			p.updateInventory();
        	Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new BukkitRunnable() {
				
				@Override
				public void run() {
					p.openInventory(inv);
					p.updateInventory();
				}
			},2L);
        	
    	}
    }

    @Override
	public void onLaunch(ProjectileLaunchEvent e){
    	//BogenschützeAbility
    	if(e.getEntity().getShooter() instanceof Player && LobbyState.gekaufteItems.containsKey(e.getEntity().getShooter())){
			if(LobbyState.gekaufteItems.get(e.getEntity().getShooter()).equals(RAbilitiesKt.getBogenschütze())){
				Vector v = e.getEntity().getVelocity();
				Location l = e.getEntity().getLocation().add(v);
				Bukkit.getServer().getWorlds().get(0).spawnArrow(l,v,0.6f,12);
			}
		}
	}
    
    @Override
    public void onPlayerOpenInventory(InventoryOpenEvent e){
    	
        if (e.getInventory().getHolder() instanceof Chest){
        	Chest ch=((Chest)e.getInventory().getHolder());
        	if(ch.getData().getItemType().equals(Material.TRAPPED_CHEST)){
	        	if(!filled(((Chest)e.getInventory().getHolder()))){
	        		//Trapped chest
	        		Chest c=((Chest)e.getInventory().getHolder());
	            	fillChest(e.getInventory(),2);
	            	chestsFilled.add(c);
	            }
        	}else if(ch.getData().getItemType().equals(Material.CHEST)){
        		if(!filled(((Chest)e.getInventory().getHolder()))){
	            	//chest
	        		Chest c=((Chest)e.getInventory().getHolder());
	            	fillChest(e.getInventory(),0);
	            	chestsFilled.add(c);
	            }
        	}
        }
        else if(e.getInventory().getHolder() instanceof DoubleChest){
        	if(!filled((Chest) ((DoubleChest)e.getInventory().getHolder()).getLeftSide())){
        		Chest c=((Chest) ((DoubleChest)e.getInventory().getHolder()).getLeftSide());
            	fillChest(e.getInventory(),1);
            	chestsFilled.add(c);
            }
        }
    }

	@Override
	public void onInvClick(InventoryClickEvent event) {

	}

	private void fillChest(Inventory inv,int itemGueteklasse) {
    	for(int i=0;i<inv.getSize();i++){
			if(inv.getItem(i)==null && fillProbability()){
				inv.setItem(i, ChestItems.getRanItem(itemGueteklasse));
			}
    	}
	}

	private boolean fillProbability() {
		double d=new Random().nextDouble();
		if(d>R.limit)return true;
		return false;
	}

	private boolean filled(Chest chest) {
		return chestsFilled.contains(chest);
	}

	private int i=0;
	
	@Override
    public void trigger() {
		spielendePruefen();
		
		i++;
		if(i>R.chestRefillSec){
			refillChests();
			Bukkit.broadcastMessage(R.MESSAGE_CHESTREFILL);
			i=0;
		}
    }

	@Override
	public void init() {
		for(Player p : Bukkit.getServer().getOnlinePlayers()){
			ingamePlayers.add(p);
			p.getInventory().clear();
			p.setFallDistance(0);
			p.setFireTicks(0);
			p.setSaturation(6);
			p.setExhaustion(20);
			p.setExp(-1);
			if(LobbyState.gekaufteItems.containsKey(p)){
				Ability ability = LobbyState.gekaufteItems.get(p);
				//Rind
				if(ability.equals(RAbilitiesKt.getRind())){
					double d = p.getMaxHealth()*1.2;
					p.setMaxHealth(d);
				}
				for(ItemStack s : ability.getStartItems()){
					if(ability.equals(RAbilitiesKt.getSchwimmer()) && s.getType().equals(Material.LEATHER_BOOTS)){
						s.addEnchantment(Enchantment.DEPTH_STRIDER,3);
					}
					p.getInventory().addItem(s);
				}
			}
		}
	}

	@Override
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();
		if(map.containsKey(p)){
			if(System.currentTimeMillis()-map.get(p).getLastDamage()<7000){
				event.setDeathMessage(R.MESSAGE_KILLEDBY.replaceAll("<player>", p.getName()).replaceAll("<killer>", map.get(p).getP().getName()));
				Money.getInstance().addMoney(map.get(p).getP(), 1);
				map.get(p).getP().sendMessage(R.BANKPREFIX+" +1 Knochen");
			}
		}else{
			event.setDeathMessage(R.MESSAGE_DEATH.replaceAll("<player>", p.getName()));
		}
		
		p.setGameMode(GameMode.SPECTATOR);
		p.sendMessage(R.MESSAGE_SPECTATE);
		spectators.add(p);
		ingamePlayers.remove(p);
	}

	private boolean spielendePruefen() {
		if(ingamePlayers.size()<2){
			Bukkit.broadcastMessage(R.MESSAGE_WIN.replaceAll("<player>",ingamePlayers.get(0).getDisplayName()));
			GameStateManager.instance.setState(GameState.RESTART);
			return true;
		}
		return false;
	}
	
	private void refillChests(){
		chestsFilled=new ArrayList<Chest>();
		ecInv=new HashMap<Block,Inventory>();
	}

	@Override
	public void onPlayerHunger(FoodLevelChangeEvent event) {
		
	}

	@Override
	public void onServerPing(ServerListPingEvent event) {
		Data.getInstance();
		event.setMotd(ChatColor.DARK_GRAY+"["+ChatColor.DARK_GREEN+"SG-1"+ChatColor.DARK_GRAY+"];"+ChatColor.YELLOW+"INGAME;"+ChatColor.BLUE+Data.MAPNAME+";"+event.getNumPlayers()+"/"+Data.getInstance().maxPlayers);
	}

	@Override
	public void onLogin(PlayerLoginEvent event) {

	}

	@Override
	public void onLeave(PlayerQuitEvent event) {
		ingamePlayers.remove(event.getPlayer());
		spectators.remove(event.getPlayer());
		event.getPlayer().setGameMode(GameMode.SURVIVAL);
	}
}
