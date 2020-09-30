package tk.fireware.sg.coins;

import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Money {

    private MySQL mySQL;
    private Connection c = null;
    private static Money instance;

    public Money(){
        mySQL = new MySQL("localhost", "3306", "wolfsbau-stats", "plugin", "pLugin");
        instance=this;
        try {
            c = mySQL.openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Money getInstance() {
        return instance;
    }

    private void setMoney(Player player, int money) {
        try {
            c = mySQL.openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Statement statement = null;
        try {
            statement = c.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            statement.executeUpdate("UPDATE money SET money = " + money + " WHERE uuid = '" + player.getUniqueId() + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getMoney(Player player) {
        try {
            c = mySQL.openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        Statement statement = null;
        try {
            statement = c.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet res = null;
        int money = 0;
        try {
            res = statement.executeQuery("SELECT * FROM money WHERE uuid = '" + player.getUniqueId() + "';");
            res.next();
            money = res.getInt("money");

        } catch (SQLException e) {
            try {
                statement.executeUpdate("INSERT INTO money (uuid, money) VALUES ('" + player.getUniqueId() + "', 0);");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        try {
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return money;
    }

    public void addMoney(Player p,int moneyToAdd){
        int m = getMoney(p);
        setMoney(p, m+moneyToAdd);
    }
}
