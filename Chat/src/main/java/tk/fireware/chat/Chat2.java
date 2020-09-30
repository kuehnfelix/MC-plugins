package tk.fireware.chat;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.sql.Connection;

public class Chat2 extends JavaPlugin implements Listener  {
    MySQL mySQL = new MySQL("localhost", "3306", "wolfsbau-pex", "pex", "pEx");
    Connection c = null;
    Connection c2 = null;
    ScoreboardManager man;
    Scoreboard sb;


}
