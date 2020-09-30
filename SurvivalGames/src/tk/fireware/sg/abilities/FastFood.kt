@file:Suppress("unused")

package tk.fireware.sg.abilities

import org.bukkit.Material

fun isEatable(m : Material):Boolean{
    when(m){
        Material.BAKED_POTATO, Material.APPLE,Material.CARROT,Material.BREAD,Material.COOKED_BEEF,Material.COOKED_CHICKEN,
                Material.COOKED_FISH,Material.COOKED_MUTTON,Material.COOKED_RABBIT,Material.COOKIE,Material.RABBIT,
                Material.MUTTON,Material.RAW_FISH,Material.RAW_BEEF,Material.POTATO_ITEM,Material.PUMPKIN_PIE,Material.MELON,
                Material.RABBIT_STEW,Material.GOLDEN_APPLE,Material.GOLDEN_CARROT,Material.SPECKLED_MELON -> return true
        else ->  return false
    }
}