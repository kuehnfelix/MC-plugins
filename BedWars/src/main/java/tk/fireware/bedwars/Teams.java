package tk.fireware.bedwars;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Iterator;

@SuppressWarnings("deprecation")
public class Teams {
    private static Teams instance;

    private int teamSize = Data.getInstance().getMaxPlayers()/getTeamCount();


    private ScoreboardManager manager;
    private Scoreboard board;
    private Team red = null;
    private Team blue = null;
    private Team green = null;
    private Team yellow = null;

    ArrayList<Team> teams = new ArrayList<Team>();


    private Teams() {
        manager= Bukkit.getScoreboardManager();
        board=manager.getNewScoreboard();
        if(Data.getInstance().redTeamActive()) {
            red = board.registerNewTeam(R.NAMES_RED_TEAM);
            red.setPrefix(ChatColor.RED+"");
            teams.add(red);
        }
        if(Data.getInstance().blueTeamActive()) {
            blue = board.registerNewTeam(R.NAMES_BLUE_TEAM);
            blue.setPrefix(ChatColor.BLUE+"");// #TODO Player Prefixes
            teams.add(blue);
        }
        if(Data.getInstance().greenTeamActive()) {
            green = board.registerNewTeam(R.NAMES_GREEN_TEAM);
            green.setPrefix(ChatColor.GREEN+"");
            teams.add(green);
        }
        if(Data.getInstance().yellowTeamActive()) {
            yellow = board.registerNewTeam(R.NAMES_YELLOW_TEAM);
            yellow.setPrefix(ChatColor.YELLOW+"");
            teams.add(yellow);
        }
    }

    public Team getRed() {
        return red;
    }

    public Team getBlue() {
        return blue;
    }

    public Team getGreen() {
        return green;
    }

    public Team getYellow() {
        return yellow;
    }

    public static Teams getInstance() {
        if(instance==null) instance = new Teams();
        return instance;
    }

    private int getTeamCount() {
        int i = 0;
        if(Data.getInstance().redTeamActive()) i++;
        if(Data.getInstance().blueTeamActive()) i++;
        if(Data.getInstance().greenTeamActive()) i++;
        if(Data.getInstance().yellowTeamActive()) i++;
        if(i==0) i=-1;
        return i;
    }

    public void removePlayer(Player p) {
        if(red!=null && red.hasPlayer(p)) red.removePlayer(p);
        if(green!=null && green.hasPlayer(p)) green.removePlayer(p);
        if(blue!=null && blue.hasPlayer(p)) blue.removePlayer(p);
        if(yellow!=null && yellow.hasPlayer(p)) yellow.removePlayer(p);
    }

    public void addToRed(Player p) {
        if(red==null) {
            return;
        }
        removePlayer(p);
        red.addPlayer(p);
        p.sendMessage(R.MESSAGES_TEAM_JOINED.replaceAll("<team>", R.NAMES_RED_TEAM));
    }

    public void addToBlue(Player p) {
        if(blue==null) {
            return;
        }
        removePlayer(p);
        blue.addPlayer(p);
        p.sendMessage(R.MESSAGES_TEAM_JOINED.replaceAll("<team>", R.NAMES_BLUE_TEAM));
    }

    public void addToGreen(Player p) {
        if(green==null) {
            return;
        }
        removePlayer(p);
        green.addPlayer(p);
        p.sendMessage(R.MESSAGES_TEAM_JOINED.replaceAll("<team>", R.NAMES_GREEN_TEAM));
    }

    public void addToYellow(Player p) {
        if(yellow==null) {
            return;
        }
        removePlayer(p);
        yellow.addPlayer(p);
        p.sendMessage(R.MESSAGES_TEAM_JOINED.replaceAll("<team>", R.NAMES_YELLOW_TEAM));
    }

    public void addToSmallestTeam(Player p) {


        Team smallesteam = null;
        for(Team t : teams) {
            if(smallesteam==null || t.getSize()<=smallesteam.getSize()) {
                smallesteam = t;
            }
        }

        removePlayer(p);
        smallesteam.addPlayer(p);
        p.sendMessage(R.MESSAGES_TEAM_JOINED.replaceAll("<team>", smallesteam.getDisplayName()));

    }

    public void addRest() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            Boolean b = false;
            for(Team t : teams) {
                 b = t.hasPlayer(p) ? true : false;
                 if(b)  break;
            }
            if(b) {
                continue;
            }
            addToSmallestTeam(p);

        }
    }

    public Location getSpawnLocation(Player p) {
        if(red!=null && red.hasPlayer(p)) {
            return Data.getInstance().getRedSpawn();
        } else if(blue!=null && blue.hasPlayer(p)) {
            return Data.getInstance().getBlueSpawn();
        } else if(green!=null && green.hasPlayer(p)) {
            return Data.getInstance().getGreenSpawn();
        } else if(yellow!=null && yellow.hasPlayer(p)) {
            return Data.getInstance().getYellowSpawn();
        }
        return Data.getInstance().getLobbySpawn();
    }
}
