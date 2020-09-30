package tk.fireware.sg;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ChestItems {
	
	public static ItemStack getRanItem(int itemGueteklasse){
		Random r=new Random();
		
		HashMap<ItemStack, Integer> items=null;
		
		switch(itemGueteklasse){
		case 0:
			items=getItemsLevel0();
			break;
		case 1:
			items=getItemsLevel1();
			break;
		case 2:
			items=getItemsLevel2();
			break;
		case 3:
			items=getItemsLevel3();
			break;
		default:
			items=getItemsLevel0();
		}
		
		Set<ItemStack> keyset=items.keySet();
		int w=0;
		
		for(ItemStack item : keyset){
			w+=items.get(item);
		}
		
		int ranint=r.nextInt(w-1)+1;
		
		Iterator<ItemStack> it=keyset.iterator();
		
		ItemStack itemStack=null;
		
		for(int i=0;i<ranint;){
			itemStack=it.next();
			i+=items.get(itemStack);
		}
		
		return itemStack;
	}
    static HashMap<ItemStack,Integer> getItemsLevel0() {
    	HashMap<ItemStack, Integer> Items=new HashMap<ItemStack,Integer>();
      
    	Items.putAll(itemsLvl0);
    	
        return Items;
    }
    
    static HashMap<ItemStack, Integer> getItemsLevel1() {
    	HashMap<ItemStack,Integer> Items=new HashMap<ItemStack,Integer>();
    	Items.putAll((Map<? extends ItemStack, ? extends Integer>) getItemsLevel0());
    	
    	Items.putAll(itemsLvl1);
    	
        return Items;
    }
    
	static HashMap<ItemStack,Integer> getItemsLevel2() {
    	HashMap<ItemStack,Integer> Items=new HashMap<ItemStack,Integer>();
    	
    	Items.putAll((Map<? extends ItemStack, ? extends Integer>) getItemsLevel0());
    	Items.putAll((Map<? extends ItemStack, ? extends Integer>) getItemsLevel1());
    	
    	Items.putAll(itemsLvl2);
    	
        return Items;
    }
    
    static HashMap<ItemStack,Integer> getItemsLevel3() {
    	HashMap<ItemStack,Integer> Items=new HashMap<ItemStack,Integer>();
    	
    	Items.putAll((Map<? extends ItemStack, ? extends Integer>) getItemsLevel0());
    	Items.putAll((Map<? extends ItemStack, ? extends Integer>) getItemsLevel1());
    	Items.putAll((Map<? extends ItemStack, ? extends Integer>) getItemsLevel2());
    	
    	Items.putAll(itemsLvl3);
    	
        return Items;
    }
    
    private static HashMap<ItemStack, Integer> itemsLvl0=new HashMap<ItemStack,Integer>();
    private static HashMap<ItemStack, Integer> itemsLvl1=new HashMap<ItemStack,Integer>();
    private static HashMap<ItemStack, Integer> itemsLvl2=new HashMap<ItemStack,Integer>();
    private static HashMap<ItemStack, Integer> itemsLvl3=new HashMap<ItemStack,Integer>();
    
	public static void init(){
		File file=new File(Main.plugin.getDataFolder().getAbsolutePath(),"items.yml");
		YamlConfiguration itemsConfig=YamlConfiguration.loadConfiguration(file);
		
		final String sep=R.sep;
		final String sepV=R.sepV;
		final String sepVsub=R.sepVsub;
		
		ArrayList<String> items=(ArrayList<String>)itemsConfig.getStringList("ItemsLvl0");

		for(String s : items){
			try{
			String[] substring = s.split(sep);

			Material m = Material.getMaterial(substring[1]);
			ItemStack itemStack = new ItemStack(m);
			itemStack.setAmount(Integer.valueOf(substring[2]));
			ItemMeta meta = itemStack.getItemMeta();
			
			if(substring.length > 3 && substring[3]!="")meta.setDisplayName(substring[3]);
			
			if(substring.length > 4 && substring[4]!=""){
				String[] verzauberungen = substring[4].split(sepV);
				for(String str : verzauberungen){
					String[] verzauberung = str.split(sepVsub);
					meta.addEnchant(Enchantment.getByName(verzauberung[0]), Integer.valueOf(verzauberung[1]), true);
				}
			}
			
			itemStack.setItemMeta(meta);
			
			itemsLvl0.put(itemStack,Integer.valueOf(substring[0]));
			}catch(Exception e){e.printStackTrace(); System.out.println(R.CONFIGHELP);}
		}
		
		ArrayList<String> items1=(ArrayList<String>)itemsConfig.getStringList("ItemsLvl1");
		for(String s : items1){
			try{
			String[] substring = s.split(sep);

			Material m = Material.getMaterial(substring[1]);
			ItemStack itemStack = new ItemStack(m);
			itemStack.setAmount(Integer.valueOf(substring[2]));
			ItemMeta meta = itemStack.getItemMeta();
			
			if(substring.length > 3 && substring[3]!="")meta.setDisplayName(substring[3]);
			
			if(substring.length > 4 && substring[4]!=""){
				String[] verzauberungen = substring[4].split(sepV);
				for(String str : verzauberungen){
					String[] verzauberung = str.split(sepVsub);
					meta.addEnchant(Enchantment.getByName(verzauberung[0]), Integer.valueOf(verzauberung[1]), true);
				}
			}
			
			itemStack.setItemMeta(meta);
			
			itemsLvl1.put(itemStack,Integer.valueOf(substring[0]));
			}catch(Exception e){e.printStackTrace(); System.out.println(R.CONFIGHELP);}
		}
		
		ArrayList<String> items2=(ArrayList<String>)itemsConfig.getStringList("ItemsLvl2");
		for(String s : items2){
			try{
			String[] substring = s.split(sep);
			
			Material m = Material.getMaterial(substring[1]);
			ItemStack itemStack = new ItemStack(m);
			itemStack.setAmount(Integer.valueOf(substring[2]));
			ItemMeta meta = itemStack.getItemMeta();
			
			if(substring.length > 3 && substring[3]!="")meta.setDisplayName(substring[3]);
			
			if(substring.length > 4 && substring[4]!=""){
				String[] verzauberungen = substring[4].split(sepV);
				for(String str : verzauberungen){
					String[] verzauberung = str.split(sepVsub);
					meta.addEnchant(Enchantment.getByName(verzauberung[0]), Integer.valueOf(verzauberung[1]), true);
				}
			}
			
			itemStack.setItemMeta(meta);
			
			itemsLvl2.put(itemStack,Integer.valueOf(substring[0]));
			}catch(Exception e){e.printStackTrace(); System.out.println(R.CONFIGHELP);}
		}
		
		ArrayList<String> items3=(ArrayList<String>)itemsConfig.getStringList("ItemsLvl3");
		for(String s : items3){
			try{
			String[] substring = s.split(sep);
			
			Material m = Material.getMaterial(substring[1]);
			ItemStack itemStack = new ItemStack(m);
			itemStack.setAmount(Integer.valueOf(substring[2]));
			ItemMeta meta = itemStack.getItemMeta();
			
			if(substring.length > 3 && substring[3]!="")meta.setDisplayName(substring[3]);
			
			if(substring.length > 4 && substring[4]!=""){
				String[] verzauberungen = substring[4].split(sepV);
				for(String str : verzauberungen){
					String[] verzauberung = str.split(sepVsub);
					meta.addEnchant(Enchantment.getByName(verzauberung[0]), Integer.valueOf(verzauberung[1]), true);
				}
			}
			
			itemStack.setItemMeta(meta);
			
			itemsLvl3.put(itemStack,Integer.valueOf(substring[0]));
			}catch(Exception e){e.printStackTrace(); System.out.println(R.CONFIGHELP);}
		}

//		Enchantment.THORNS
//		Enchantment.FIRE_ASPECT
	}
}
