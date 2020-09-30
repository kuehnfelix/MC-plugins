package tk.fireware.knockffa;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.UUID;

public class PlayerData {

    private int kills;
    private int deaths;
    private int killStreak;
    private Damage lastDamage;

    public PlayerData() {
        lastDamage = new Damage(null, 0);
        kills = 0;
        deaths = 0;
    }

    public int getKillStreak() {
        return killStreak;
    }

    public void setKillStreak(int killStreak) {
        this.killStreak = killStreak;
    }

    public Damage getLastDamage() {
        return lastDamage;
    }

    public void setLastDamage(Damage lastDamage) {
        this.lastDamage = lastDamage;
    }

    public void hit(UUID uuid) {
        setLastDamage(new Damage(uuid, System.currentTimeMillis()));
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void addKill() {
        kills++;
        killStreak++;

    }

    public void addDeath() {
        deaths++;
        killStreak = 0;
    }

    public String getKDString() {
        DecimalFormat f = new DecimalFormat("#0.00");

        if(deaths==0) return kills + "/" + ChatColor.RED + deaths;
        if(kills==0) return ChatColor.RED + "" + kills + ChatColor.RESET + "/" + deaths;
        return f.format(((double)kills)/deaths);
    }

    public double getKD() {

        if(deaths==0) return kills;
        if(kills==0) return deaths*(-1);
        return (double)(((double)kills)/deaths);
    }
}
