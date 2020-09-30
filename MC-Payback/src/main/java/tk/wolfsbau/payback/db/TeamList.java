package tk.wolfsbau.payback.db;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Datentyp zum Speichern der Teams in einer Liste
 * */
public class TeamList {
    private HashMap<String, ArrayList<String>> teams;

    public TeamList() {
        teams = new HashMap<>();
    }

    public void addPlayer(String p, String team) {
        if(teams.containsKey(team)) {
            ArrayList<String> players = teams.get(team);
            players.add(p);
            teams.remove(team);
            teams.put(team, players);
        } else {
            ArrayList<String> player = new ArrayList<>();
            player.add(p);
            teams.put(team, player);
        }
    }

    public ArrayList<String> getList() {
        ArrayList<String> list = new ArrayList<>();
        for(String key : teams.keySet()) {
            String s = (ChatColor.BLUE + key + ChatColor.WHITE);
            s+=" -> ";
            for(String p : teams.get(key)) {
                if(Data.getInstance().isAlive(p)) {
                    s+=ChatColor.GREEN;
                } else {
                    s+=ChatColor.STRIKETHROUGH + "" + ChatColor.RED;
                }
                s += (p + "   ");
            }
            list.add(s);
        }

        return list;
    }

    public HashMap<String, ArrayList<String>> getTeamPlayersList() {
        return teams;
    }
}
