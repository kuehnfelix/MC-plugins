package tk.wolfsbau.payback.db;

import org.bukkit.ChatColor;

public class Messages {
    private static Messages instance;

    public final String NOPERMISSION = ChatColor.RED + "Das darfst du nicht!";

    private Messages() {}

    public static Messages getInstance() {
        if(instance == null) instance = new Messages();
        return instance;
    }
}
