package tk.fireware.sg.abilities

import org.bukkit.inventory.ItemStack

data class Ability(val name: String, val preis: Int, val displayMaterial: org.bukkit.Material, val startItems : ArrayList<ItemStack>)