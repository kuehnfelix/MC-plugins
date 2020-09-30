package tk.fireware.sg;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class Items {

    //Main Build-Menu
    static ItemStack getBuildMenuItem() {
        ItemStack i = new ItemStack(Material.COMPASS);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(R.NAMES_BUILDING_MENU);
        i.setItemMeta(meta);
        return i;
    }
    
    static ItemStack getSpawnpointItem() {
        ItemStack i = new ItemStack(Material.CARPET);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(R.NAMES_SPAWNPOINT_ITEM);
        i.setItemMeta(meta);
        return i;
    }

    public static ItemStack[] getLobbyMenu() {
        ItemStack[] itemStacks=new ItemStack[9];

        ItemStack air=new ItemStack(Material.AIR);
        for(int i=0; i<9;i++){
            itemStacks[i]=air;
        }

        ItemStack x2=new ItemStack(Material.BONE);
        ItemMeta meta2 = x2.getItemMeta();
        meta2.setDisplayName(R.SHOP);
        x2.setItemMeta(meta2);
        itemStacks[2]=x2;

        ItemStack x=new ItemStack(Material.BARRIER);
        ItemMeta meta = x.getItemMeta();
        meta.setDisplayName(R.NAMES_EXIT_ITEM);
        x.setItemMeta(meta);
        itemStacks[8]=x;

        return itemStacks;
    }
}
