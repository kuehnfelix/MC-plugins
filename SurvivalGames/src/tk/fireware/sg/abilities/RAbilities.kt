package tk.fireware.sg.abilities

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

val Bogenschütze = Ability("Bogenschütze", 20,Material.BOW, arrayListOf(ItemStack(Material.BOW),ItemStack(Material.ARROW,8)))
//val FastFood = Ability("FastFood", 23,Material.COOKED_BEEF, arrayListOf(ItemStack(Material.COOKED_BEEF)))
val Krümelmonster = Ability("Krümelmonster", 10,Material.COOKIE, arrayListOf(ItemStack(Material.COOKIE,20)))
val Sprinter = Ability("Sprinter", 14, Material.LEATHER_BOOTS, arrayListOf(ItemStack(Material.COOKED_RABBIT)))
val Schwimmer = Ability("Schwimmer", 52, Material.LEATHER_BOOTS, arrayListOf(ItemStack(Material.LEATHER_BOOTS)))
val Feigling = Ability("Feigling",21, Material.COOKED_RABBIT, arrayListOf(ItemStack(Material.COOKED_RABBIT)))
val Rind = Ability("Rind",21, Material.COOKED_BEEF, arrayListOf(ItemStack(Material.COOKED_BEEF), ItemStack(Material.LEATHER_CHESTPLATE)))
val Feder= Ability("Feder",23, Material.FEATHER, arrayListOf(ItemStack(Material.FEATHER), ItemStack(Material.GOLD_BOOTS)))
val BillNewton= Ability("BillNewton",19, Material.FEATHER, arrayListOf(ItemStack(Material.FEATHER), ItemStack(Material.SPONGE,4)))

val list = arrayListOf<Ability>(Bogenschütze, /*FastFood,*/ Krümelmonster, Sprinter, Schwimmer, Feigling,Rind,Feder, BillNewton)

fun getAbility(name : String): Ability? {
    for(a in list){
        if(a.name.equals(name))return a
    }
    return null
}

fun getAbilities(): ArrayList<Ability> {
    return list
}

fun containsItem(name : String):Boolean{
    for(a in list){
        if(a.name.equals(name))return true
    }
    return false
}