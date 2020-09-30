package tk.fireware.knockffa;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Product {
    private int price;
    private ItemStack product;

    public Product(ItemStack product, int price) {
        this.product = product;
        this.price = price;
        ItemMeta meta = product.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.RED + "Preis: " + price + " Knochen");
        meta.setLore(lore);
        product.setItemMeta(meta);
    }

    public void addEnchantment(Enchantment enchantment, int lvl) {
        ItemMeta meta = product.getItemMeta();
        meta.addEnchant(enchantment, lvl, true);
        product.setItemMeta(meta);
    }

    public void setAmount(int amount) {
        product.setAmount(amount);
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ItemStack getProduct() {
        return product;
    }

    public void setProduct(ItemStack product) {
        this.product = product;
    }
}
