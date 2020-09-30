package tk.wolfsbau.payback.util;

import org.bukkit.Bukkit;

import java.lang.reflect.Field;

public final class SetSlots {
    public static final String BASE_NAMESPACE = "net.minecraft.server";
    private static final String MCSERVER_NAMESPACE = BASE_NAMESPACE + "." + getNamespaceVersion() + ".MinecraftServer";

    private static Field cache = null;;
    private static Object playerList = null;

    public static final String getNamespaceVersion(){
        String name = Bukkit.getServer().getClass().getName();
        String[] namespaces = name.split("\\.");
        return namespaces[3];
    }

    public static final int setSlots(int slots){
        if(cache == null || playerList == null){
            try {
                Class<?> mcServerClass = Class.forName(MCSERVER_NAMESPACE);
                Object mcServer = mcServerClass.getDeclaredMethod("getServer").invoke(null);
                Object playerList = mcServerClass.getDeclaredMethod("getPlayerList").invoke(mcServer);
                SetSlots.playerList = playerList;
                SetSlots.cache = playerList.getClass().getSuperclass().getDeclaredField("maxPlayers");
                cache.setAccessible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        try {
            cache.setInt(playerList, slots);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return Bukkit.getServer().getMaxPlayers();
    }
}
