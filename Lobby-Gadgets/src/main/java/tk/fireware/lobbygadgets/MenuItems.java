package tk.fireware.lobbygadgets;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;


public class MenuItems {
    public static final MenuItems instance = new MenuItems();


    public ItemStack[] getMainMenu(Player p) {
        ItemStack[] stacks = new ItemStack[9];

        ItemStack it = new ItemStack(Data.instance.getPlayersVisible(p) ? Material.GLOWSTONE_DUST : Material.REDSTONE);
        ItemMeta meta = it.getItemMeta();
        meta.setDisplayName(Data.instance.getPlayersVisible(p) ? Constraints.ITEM_PLAYERVISIBLE : Constraints.ITEM_PLAYERINVISIBLE);
        it.setItemMeta(meta);
        //stacks[0] = it;

        it = new ItemStack(Material.COMPASS);
        meta = it.getItemMeta();
        meta.setDisplayName(Constraints.ITEM_TELEPORTER);
        it.setItemMeta(meta);
        stacks[1] = it;

        it = new ItemStack(Material.SNOW_BALL,100);
        meta = it.getItemMeta();
        meta.setDisplayName("FLUMMI");
        meta.addEnchant(Enchantment.ARROW_KNOCKBACK,2,true);
        it.setItemMeta(meta);
        stacks[2] = it;

        it = new ItemStack(Material.AIR);

        /*stacks[3] = it;

        stacks[4] = it;


        stacks[5] = it;
        stacks[6] = it;

        stacks[7] = it;
        stacks[8] = it;*/


        return stacks;
    }

    public Inventory getTeleporterMenu(Player p) {
        Inventory inv = Bukkit.createInventory(p, 54, "Teleporter");
        inv.setContents(getTeleportItems());
        return inv;
    }

    public ItemStack[] getTeleportItems() {
        ItemStack[] itst = new ItemStack[54];

        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getData());
        ItemMeta metaet = itemStack.getItemMeta();
        metaet.setDisplayName("-");
        itemStack.setItemMeta(metaet);
        for(int  i = 0; i<itst.length; i++) {
            itst[i] = itemStack;
        }

        ItemStack its = new ItemStack(Material.IRON_HELMET);
        ItemMeta meta = its.getItemMeta();
        meta.setDisplayName(Constraints.ITEM_SURVIVALGAMES);
        meta.setLore(Arrays.asList(new String[]{"Kämpfe gegen deine Feinde", " um als letzter zu überleben!"}));
        its.setItemMeta(meta);
        itst[4] = its;


        its = new ItemStack(Material.STICK);
        meta = its.getItemMeta();
        meta.setDisplayName(Constraints.ITEM_KNOCK);
        meta.addEnchant(Enchantment.KNOCKBACK, 1, false);
        meta.setLore(Arrays.asList(new String[]{ "Mit Knockback und Geschick zum Ziel!", "Minecraft \"KnockFFA\"!"}));
        its.setItemMeta(meta);
        itst[20] = its;


        its = new ItemStack(Material.MAGMA_CREAM);
        meta = its.getItemMeta();
        meta.setDisplayName(Constraints.ITEM_SPAWN);
        meta.addEnchant(Enchantment.KNOCKBACK, 1, false);
        //meta.setLore(Arrays.asList(new String[]{"Sammle dein Equipment" + " und verteidige dich auf einer Inselwelt"}));
        its.setItemMeta(meta);
        itst[22] = its;

        its = new ItemStack(Material.WATCH);
        meta = its.getItemMeta();
        meta.setDisplayName("Sky-PVP");
        meta.setLore(Arrays.asList(new String[]{"Sammle dein Equipment", " und verteidige dich auf einer Inselwelt"}));
        its.setItemMeta(meta);
        itst[24] = its;

        its = new ItemStack(Material.BEACON);
        meta = its.getItemMeta();
        meta.setDisplayName("Aussicht");
        meta.setLore(Arrays.asList(new String[]{"Schau dir die", "Map von oben an"}));
        its.setItemMeta(meta);
        itst[40] = its;

        return itst;
    }
}
