package tk.fireware.knockffa;

import java.util.HashMap;
import java.util.UUID;

public class Data {
    private static Data instance;

    private HashMap<UUID, PlayerData> playerData = new HashMap<UUID, PlayerData>();

    public static Data getInstance() {
        if(instance == null) instance = new Data();
        return instance;
    }

    public void addPlayer(UUID uuid) {
        playerData.put(uuid, new PlayerData());
    }

    public void removePlayer(UUID uuid) {
        playerData.remove(uuid);
    }

    public PlayerData getData(UUID uuid) {
        return playerData.get(uuid);
    }
}
