package tk.fireware.knockffa;

import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Shop implements Listener {

    private ArrayList<Product> products = new ArrayList<Product>();

    MySQL mySQL = new MySQL("localhost", "3306", "wolfsbau-stats", "plugin", "pLugin");
    Connection c = null;


    public Shop() {
        addProducts();
        try {
            c = mySQL.openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addProducts() {
        products.add(new Product(new ItemStack(Material.SNOW_BALL), 1));
        products.add(new Product(new ItemStack(Material.FISHING_ROD), 5));
        Potion potion = new Potion(PotionType.SPEED);
        products.add(new Product(potion.toItemStack(1), 15));
        Potion potion2 = new Potion(PotionType.SLOWNESS);
        potion2.setSplash(true);
        products.add(new Product(potion2.toItemStack(1), 15));
        products.add(new Product(new ItemStack(Material.ENDER_PEARL), 25));

    }


    private void setMoney(Player player, int money) {
        try {
            if(c.isClosed()) {
                c = mySQL.openConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Statement statement = null;
        try {
            statement = c.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            statement.executeUpdate("UPDATE money SET money = " + money + " WHERE uuid = '" + player.getUniqueId() + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getMoney(Player player) {
        ResultSet res = null;
        int money = 0;
        try {
            res = mySQL.querySQL("SELECT * FROM money WHERE uuid = '" + player.getUniqueId() + "';");
            res.next();
            money = res.getInt("money");

        } catch (SQLException | ClassNotFoundException e) {
            try {
                mySQL.updateSQL("INSERT INTO money (uuid, money) VALUES ('" + player.getUniqueId() + "', 0);");
            } catch (SQLException | ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }
        return money;
    }

    @EventHandler
    public void onShopOpen(PlayerInteractAtEntityEvent event) {
        if(event.getRightClicked() instanceof ArmorStand && !event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            event.setCancelled(true);
            openShop(event.getPlayer());
        }
    }

    @EventHandler
    public void onShop(InventoryClickEvent event) {
        if(event.getClickedInventory().getTitle().contains("SHOP")) {
            event.setCancelled(true);
            Player player = (Player)event.getWhoClicked();
            for(Product p : products) {
                if(p.getProduct().equals(event.getCurrentItem())) {
                    int money = getMoney(player);
                    if(money>=p.getPrice()) {
                        setMoney(player, money-p.getPrice());
                        player.getInventory().addItem(p.getProduct());
                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 100, 0);
                        event.getWhoClicked().sendMessage(ChatColor.GRAY+"[" + ChatColor.GOLD +"Bank" + ChatColor.GRAY + "]" + ChatColor.RED + " -" + p.getPrice());
                    } else {

                    }
                    break;
                }
            }


        }
    }

    private void openShop(Player player) {
        Inventory shop = Bukkit.createInventory(null, products.size()<10 ? 9 : products.size()<19?18:27, "SHOP           Knochen: " + ChatColor.BOLD + getMoney(player));
        for(Product p : products) {
            shop.addItem(p.getProduct());
        }
        player.openInventory(shop);
    }


}
