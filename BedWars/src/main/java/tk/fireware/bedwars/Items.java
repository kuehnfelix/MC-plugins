package tk.fireware.bedwars;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Items {

    //Main Build-Menu
    static ItemStack getBuildMenuItem() {
        ItemStack i = new ItemStack(Material.COMPASS);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(R.NAMES_BUILDING_MENU);
        i.setItemMeta(meta);
        return i;
    }
    static ItemStack getGrayItem1() {
        ItemStack i = new ItemStack(Material.WOOL, 1, DyeColor.GRAY.getWoolData());
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(R.NAMES_RED_ITEM);
        i.setItemMeta(meta);
        return i;
    }
    static ItemStack getGrayItem2() {
        ItemStack i = new ItemStack(Material.WOOL, 1, DyeColor.GRAY.getWoolData());
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(R.NAMES_BLUE_ITEM);
        i.setItemMeta(meta);
        return i;
    }
    static ItemStack getGrayItem3() {
        ItemStack i = new ItemStack(Material.WOOL, 1, DyeColor.GRAY.getWoolData());
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(R.NAMES_GREEN_ITEM);
        i.setItemMeta(meta);
        return i;
    }
    static ItemStack getGrayItem4() {
        ItemStack i = new ItemStack(Material.WOOL, 1, DyeColor.GRAY.getWoolData());
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(R.NAMES_YELLOW_ITEM);
        i.setItemMeta(meta);
        return i;
    }
    static ItemStack getRedItem() {
        ItemStack i = new ItemStack(Material.WOOL, 1, DyeColor.RED.getWoolData());
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(R.NAMES_RED_ITEM);
        i.setItemMeta(meta);
        return i;
    }
    static ItemStack getBlueItem() {
        ItemStack i = new ItemStack(Material.WOOL, 1, DyeColor.BLUE.getWoolData());
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(R.NAMES_BLUE_ITEM);
        i.setItemMeta(meta);
        return i;
    }
    static ItemStack getGreenItem() {
        ItemStack i = new ItemStack(Material.WOOL, 1, DyeColor.GREEN.getWoolData());
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(R.NAMES_GREEN_ITEM);
        i.setItemMeta(meta);
        return i;
    }
    static ItemStack getYellowItem() {
        ItemStack i = new ItemStack(Material.WOOL, 1, DyeColor.YELLOW.getWoolData());
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(R.NAMES_YELLOW_ITEM);
        i.setItemMeta(meta);
        return i;
    }
    static ItemStack getSpawnerItem() {

        ItemStack i = new ItemStack(Material.MOB_SPAWNER);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(R.NAMES_SPAWNER_ITEM);
        i.setItemMeta(meta);
        return i;
    }
    public static ItemStack getShopItem() {

        ItemStack i = new ItemStack(Material.ARMOR_STAND);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(R.NAMES_SHOP);
        i.setItemMeta(meta);
        return i;
    }

    static ItemStack[] getLobbyMenu() {
        ItemStack[] itemStacks=new ItemStack[9];

        ItemStack air=new ItemStack(Material.AIR);
        for(int i=0; i<9;i++){
            itemStacks[i]=air;
        }

        if(Data.getInstance().redTeamActive()) {
            ItemStack red = new ItemStack(Material.WOOL, Teams.getInstance().getRed().getSize() == 0 ? 1 : Teams.getInstance().getRed().getSize(), DyeColor.RED.getWoolData());
            ItemMeta meta = red.getItemMeta();
            meta.setDisplayName(R.NAMES_RED_TEAM);
            red.setItemMeta(meta);
            itemStacks[0] = red;
        }

        if(Data.getInstance().blueTeamActive()) {
            ItemStack blue = new ItemStack(Material.WOOL, Teams.getInstance().getBlue().getSize() == 0 ? 1 : Teams.getInstance().getBlue().getSize(), DyeColor.BLUE.getWoolData());
            ItemMeta meta = blue.getItemMeta();
            meta.setDisplayName(R.NAMES_BLUE_TEAM);
            blue.setItemMeta(meta);
            itemStacks[1] = blue;
        }

        if(Data.getInstance().greenTeamActive()) {
            ItemStack green = new ItemStack(Material.WOOL, Teams.getInstance().getGreen().getSize() == 0 ? 1 : Teams.getInstance().getGreen().getSize(), DyeColor.GREEN.getWoolData());
            ItemMeta meta = green.getItemMeta();
            meta.setDisplayName(R.NAMES_GREEN_TEAM);
            green.setItemMeta(meta);
            itemStacks[2] = green;
        }

        if(Data.getInstance().yellowTeamActive()) {
            ItemStack yellow = new ItemStack(Material.WOOL, Teams.getInstance().getYellow().getSize() == 0 ? 1 : Teams.getInstance().getYellow().getSize(), DyeColor.YELLOW.getWoolData());
            ItemMeta meta = yellow.getItemMeta();
            meta.setDisplayName(R.NAMES_YELLOW_TEAM);
            yellow.setItemMeta(meta);
            itemStacks[3] = yellow;
        }

        ItemStack x=new ItemStack(Material.BARRIER);
        ItemMeta meta = x.getItemMeta();
        meta.setDisplayName(R.NAMES_EXIT_ITEM);
        x.setItemMeta(meta);
        itemStacks[8]=x;

        return itemStacks;
    }


    //Red SubMenu
    static ItemStack[] getRedBuildMenu() {
        ItemStack[] itst = new ItemStack[8];
        ItemStack bed;
        if(Bukkit.getVersion().contains("1.7")||Bukkit.getVersion().contains("1.8")||Bukkit.getVersion().contains("1.9")||Bukkit.getVersion().contains("1.10")||Bukkit.getVersion().contains("1.11")) {
            bed = new ItemStack(Material.BED, 1);
        } else {
            bed = new ItemStack(Material.BED, 1, (byte) 14);
        }

        ItemMeta bedmeta = bed.getItemMeta();
        if(Data.getInstance().redBED==null) {
            bedmeta.setDisplayName(R.NAMES_RED_BED + ChatColor.RED + " (NO)");
        } else {
            bedmeta.setDisplayName(R.NAMES_RED_BED + ChatColor.GREEN + " (YES)");
        }
        bed.setItemMeta(bedmeta);

        ItemStack spawn = new ItemStack(Material.CARPET, 1, DyeColor.RED.getWoolData());
        ItemMeta spawnmeta = spawn.getItemMeta();
        if(Data.getInstance().redSpawn==null) {
            spawnmeta.setDisplayName(R.NAMES_RED_SPAWN + ChatColor.RED + " (NO)");
        } else {
            spawnmeta.setDisplayName(R.NAMES_RED_SPAWN + ChatColor.GREEN + " (YES)");
        }
        spawn.setItemMeta(spawnmeta);
        itst[2] = spawn;

        itst[0] = getRedItem();
        itst[1] = bed;

        return itst;
    }

    static ItemStack[] getBlueBuildMenu() {
        ItemStack[] itst = new ItemStack[8];
        ItemStack bed;
        if(Bukkit.getVersion().contains("1.7")||Bukkit.getVersion().contains("1.8")||Bukkit.getVersion().contains("1.9")||Bukkit.getVersion().contains("1.10")||Bukkit.getVersion().contains("1.11")) {
            bed = new ItemStack(Material.BED, 1);
        } else {
            bed = new ItemStack(Material.BED, 1, (byte) 11);
        }
        ItemMeta bedmeta = bed.getItemMeta();
        if(Data.getInstance().blueBED==null) {
            bedmeta.setDisplayName(R.NAMES_BLUE_BED + ChatColor.RED + " (NO)");
        } else {
            bedmeta.setDisplayName(R.NAMES_BLUE_BED + ChatColor.GREEN + " (YES)");
        }
        bed.setItemMeta(bedmeta);

        ItemStack spawn = new ItemStack(Material.CARPET, 1, DyeColor.BLUE.getWoolData());
        ItemMeta spawnmeta = spawn.getItemMeta();
        if(Data.getInstance().blueSpawn==null) {
            spawnmeta.setDisplayName(R.NAMES_BLUE_SPAWN + ChatColor.RED + " (NO)");
        } else {
            spawnmeta.setDisplayName(R.NAMES_BLUE_SPAWN + ChatColor.GREEN + " (YES)");
        }
        spawn.setItemMeta(spawnmeta);


        itst[0] = getBlueItem();
        itst[1] = bed;
        itst[2] = spawn;
        return itst;
    }

    static ItemStack[] getGreenBuildMenu() {
        ItemStack[] itst = new ItemStack[8];
        ItemStack bed;
        if(Bukkit.getVersion().contains("1.7")||Bukkit.getVersion().contains("1.8")||Bukkit.getVersion().contains("1.9")||Bukkit.getVersion().contains("1.10")||Bukkit.getVersion().contains("1.11")) {
            bed = new ItemStack(Material.BED, 1);
        } else {
            bed = new ItemStack(Material.BED, 1, (byte) 13);
        }
        ItemMeta bedmeta = bed.getItemMeta();
        if(Data.getInstance().greenBED==null) {
            bedmeta.setDisplayName(R.NAMES_GREEN_BED + ChatColor.RED + " (NO)");
        } else {
            bedmeta.setDisplayName(R.NAMES_GREEN_BED + ChatColor.GREEN + " (YES)");
        }
        bed.setItemMeta(bedmeta);

        ItemStack spawn = new ItemStack(Material.CARPET, 1, DyeColor.GREEN.getWoolData());
        ItemMeta spawnmeta = spawn.getItemMeta();
        if(Data.getInstance().greenSpawn==null) {
            spawnmeta.setDisplayName(R.NAMES_GREEN_SPAWN + ChatColor.RED + " (NO)");
        } else {
            spawnmeta.setDisplayName(R.NAMES_GREEN_SPAWN + ChatColor.GREEN + " (YES)");
        }
        spawn.setItemMeta(spawnmeta);
        itst[2] = spawn;

        itst[0] = getGreenItem();
        itst[1] = bed;
        return itst;
    }

    static ItemStack[] getYellowBuildMenu() {
        ItemStack[] itst = new ItemStack[8];


        ItemStack bed;
        if (Bukkit.getVersion().contains("1.7") || Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10") || Bukkit.getVersion().contains("1.11")) {
            bed = new ItemStack(Material.BED, 1);
        } else {
            bed = new ItemStack(Material.BED, 1, (byte) 4);
        }
        ItemMeta bedmeta = bed.getItemMeta();
        if (Data.getInstance().yellowBED == null) {
            bedmeta.setDisplayName(R.NAMES_YELLOW_BED + ChatColor.RED + " (NO)");
        } else {
            bedmeta.setDisplayName(R.NAMES_YELLOW_BED + ChatColor.GREEN + " (YES)");
        }
        bed.setItemMeta(bedmeta);

        ItemStack spawn = new ItemStack(Material.CARPET, 1, DyeColor.YELLOW.getWoolData());
        ItemMeta spawnmeta = spawn.getItemMeta();
        if (Data.getInstance().yellowSpawn == null) {
            spawnmeta.setDisplayName(R.NAMES_YELLOW_SPAWN + ChatColor.RED + " (NO)");
        } else {
            spawnmeta.setDisplayName(R.NAMES_YELLOW_SPAWN + ChatColor.GREEN + " (YES)");
        }
        spawn.setItemMeta(spawnmeta);
        itst[2] = spawn;

        itst[0] = getYellowItem();
        itst[1] = bed;

        return itst;


    }

    static ItemStack[] getSpawnerBuildMenu() {
        ItemStack[] itst = new ItemStack[8];

        ItemStack bronze = new ItemStack(Material.HARD_CLAY, Data.getInstance().getSpawners_bronze().size() < 1 ? 1 : Data.getInstance().getSpawners_bronze().size(), DyeColor.ORANGE.getDyeData());
        ItemMeta bronzemeta = bronze.getItemMeta();
        bronzemeta.setDisplayName(R.NAMES_BRONZE_SPAWNER + " (" + Data.getInstance().getSpawners_bronze().size() + ")");
        bronze.setItemMeta(bronzemeta);


        ItemStack silver = new ItemStack(Material.IRON_BLOCK, Data.getInstance().getSpawners_silver().size() < 1 ? 1 : Data.getInstance().getSpawners_silver().size());
        ItemMeta silvermeta = silver.getItemMeta();
        silvermeta.setDisplayName(R.NAMES_SILVER_SPAWNER + " (" + Data.getInstance().getSpawners_silver().size() + ")");
        silver.setItemMeta(silvermeta);


        ItemStack gold = new ItemStack(Material.GOLD_BLOCK,Data.getInstance().getSpawners_gold().size() < 1 ? 1 : Data.getInstance().getSpawners_gold().size());
        ItemMeta goldmeta = gold.getItemMeta();
        goldmeta.setDisplayName(R.NAMES_GOLD_SPAWNER + " (" + Data.getInstance().getSpawners_gold().size() + ")");
        gold.setItemMeta(goldmeta);


        itst[0] = getSpawnerItem();
        itst[1] = bronze;
        itst[2] = silver;
        itst[3] = gold;
        return itst;


    }


}
