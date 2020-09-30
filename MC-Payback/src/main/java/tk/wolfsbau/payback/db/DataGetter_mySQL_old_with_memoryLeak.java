package tk.wolfsbau.payback.db;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class DataGetter_mySQL_old_with_memoryLeak implements IDataGetter {

    MySQL mySQL = new MySQL("localhost", "3306", "payback", "plugin", "pLugin");
    Connection c = null;

    private ResultSet abfrage(String sql) {
        PreparedStatement statement = null;
        ResultSet res=null;

        try {
            c = mySQL.openConnection();
            statement = c.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            res = statement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return res;
    }


    private void update(String sql) {
        PreparedStatement statement = null;
        ResultSet res = null;

        try {
            c = mySQL.openConnection();
            statement = c.prepareStatement(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public String getTeam(String p) {
        ResultSet res = abfrage("SELECT * FROM players WHERE name='" + p + "'");
        try {
            res.next();
            String s = res.getString("team");
            mySQL.closeConnection();
            return s;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }


    @Override
    public ArrayList<String> getTeamPlayers(String name) {
        ResultSet res = abfrage("SELECT * FROM players WHERE team='" + name + "'");
        ArrayList<String> names = new ArrayList<>();
        try {
            while (res.next()) {
                names.add(res.getString("name"));
            }
            mySQL.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return names;
    }


    @Override
    public void setTeam(String name, ArrayList<String> players) {
        String s = "";
        for(String p : players) {
            ResultSet res = abfrage("SELECT * FROM players WHERE name='" + p + "';");
            try {
                res.next();
                String team = res.getString("team");
                update("DELETE FROM players WHERE team='" + team + "'");
            } catch (SQLException e) {}

        }
        update("DELETE FROM players WHERE team='" + name + "'");

        for(String p : players) {
            update("INSERT INTO `players` (`name`, `team`, `folgen`, `location`) VALUES ('" + p + "', '" + name + "', '0', 'world,10,64,10');" );
        }

    }


    @Override
    public void removeTeam(String name) {
        update("DELETE FROM players WHERE team='" + name + "';");
    }


    @Override
    public ArrayList<String> getTeamList() {
        ResultSet res = abfrage("SELECT * FROM players;");
        TeamList teams = new TeamList();
        try {
            while (res.next()) {//add all players to the teamlist object
                teams.addPlayer(res.getString("name"), res.getString("team"));
            }
            return teams.getList();
        } catch(Exception e) {e.printStackTrace();}
        return null;
    }


    @Override
    public HashMap<String, ArrayList<String>> getPlayerList() {
        ResultSet res = abfrage("SELECT * FROM players;");
        TeamList teams = new TeamList();
        try {
            while (res.next()) {
                teams.addPlayer(res.getString("name"), res.getString("team"));
            }
            return teams.getTeamPlayersList();
        } catch(Exception e) {e.printStackTrace();}
        return null;

    }


    @Override
    public Long getOption(Option o) {
        ResultSet res = abfrage("SELECT * FROM config WHERE opt='" + o.text() + "';");
        try {
            while(res.next()) {
                Long l = res.getLong("value");
                return l;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void setOption(Option option, Long value) {
        update("UPDATE config SET value='" + value + "' WHERE opt='" + option.text() + "';");
    }


    @Override
    public ArrayList<String> getOptionList() {
        ResultSet res = abfrage("SELECT * FROM config;");
        ArrayList<String> options = new ArrayList<>();
        try {
            while(res.next()) {
                options.add(ChatColor.AQUA + res.getString("opt") + ChatColor.BLACK + " -> " + ChatColor.WHITE + res.getString("value"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return options;
    }


    @Override
    public Location getChestLocation(String team) {
        ResultSet res = abfrage("SELECT * FROM chests WHERE team ='" + team + "';");
        int x=0;
        int y=0;
        int z=0;
        try {
            res.next();
            x = res.getInt("x");
            y = res.getInt("y");
            z = res.getInt("z");
        } catch (SQLException e) {
            return null;
        }

        return new Location(Bukkit.getWorld("world"), x, y, z);
    }


    @Override
    public void setChestLocation(String team, Location loc) {
        update("DELETE FROM chests WHERE team='" + team + "';");
        if (loc != null) {
            update("INSERT INTO `chests` (`team`, `x`, `y`, `z`) VALUES ('" + team + "', '" + loc.getBlockX() + "', '" + loc.getBlockY() + "', '" + loc.getBlockZ() + "');");
        }
    }


    @Override
    public int getFolge(String player) {
        ResultSet res = abfrage("SELECT * FROM players WHERE name='" + player + "';");
        try {
            res.next();
            int folge = res.getInt("folgen");

            return folge;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public void setFolge(String player, int folge) {
        update("UPDATE players SET folgen=" + folge + " WHERE name ='" + player + "';");
    }


    @Override
    public Location getPlayerLocation(String player) {
        ResultSet res = abfrage("SELECT * FROM players WHERE name='" + player + "';");
        try {
            res.next();
            String[] locString = res.getString("location").split(",");
            if(locString.length!=4) return null;
            return new Location(Bukkit.getWorld(locString[0]),Integer.valueOf(locString[1]),Integer.valueOf(locString[2]),Integer.valueOf(locString[3]));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public double getBorderRadius() {
        int startSize = (int)(long)getOption(Option.BORDERSTARTSIZE);
        int minimumSize = (int)(long)getOption(Option.BORDERMINIMUMSIZE);
        int shrinkDay = (int)(long)getOption(Option.BORDERSTARTSHRINKDAY);
        int shrinkAmount = (int)(long)getOption(Option.BORDERSHRINKAMOUNT);

        ResultSet res = abfrage("SELECT * FROM config WHERE opt='startTime'");
        try {
            res.next();
            long startTime = res.getLong("value");
            long timePassed = System.currentTimeMillis()-(startTime);
            long dayinmillis = TimeUnit.DAYS.toMillis(1);
            long millistostart = TimeUnit.DAYS.toMillis(getOption(Option.BORDERSTARTSHRINKDAY));

            if(millistostart<timePassed) {
                long shrinkmillis = timePassed-millistostart;
                double shrinkdays = ((double)(shrinkmillis)/5000) /  (double)(dayinmillis/5000);

                double borderSize = (startSize - (shrinkdays*shrinkAmount));

                if(borderSize<minimumSize) {
                    return minimumSize;
                }
                return (startSize - (shrinkdays*shrinkAmount));
            }

            return startSize;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 2000;
    }


    @Override
    public boolean isTeamAlive(String team) {
        ResultSet res = abfrage("SELECT * FROM players WHERE team='" + team + "';");
        try {
            while(res.next()) {
                if(res.getInt("alive")==1) {//? kann das stimmen?
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public ArrayList<String> getTeamListAlive() {
        ResultSet res = abfrage("SELECT * FROM players;");
        TeamList teams = new TeamList();
        try {
            while (res.next()) {
                if(res.getInt("alive")!=0) {
                    teams.addPlayer(res.getString("name"), res.getString("team"));
                }
            }

            return teams.getList();
        } catch(Exception e) {e.printStackTrace();}
        return null;
    }


    @Override
    public int getStrikes(String player) {
        ResultSet res  = abfrage("SELECT * FROM players WHERE name='" + player + "';");
        try {
            res.next();
            return (int)res.getLong("strikes");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public void setStrikes(String player, int strikes) {
        update("UPDATE players SET strikes=" + strikes + " WHERE name='" + player + "';");
    }


    @Override
    public boolean isClearInv(String player) {
        ResultSet res = abfrage("SELECT * FROM players WHERE name='" + player + "';");

        try {
            res.next();
            if(res.getInt("clearInv")==0) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void setClearInv(String player, boolean clearInv) {
        update("UPDATE players SET clearInv=" + (clearInv == true ? 1 : 0) + " WHERE name='" + player + "';");
    }


    @Override
    public ArrayList<String> getPlayers() {
        ResultSet res = abfrage("SELECT * FROM players");
        ArrayList<String> players = new ArrayList<>();
        try {
            while(!res.isLast()) {
                res.next();
                players.add(res.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return players;
    }


    @Override
    public void setPlayerLocation(String player, Location location) {
        String locString=location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
        update("UPDATE players SET location='" + locString + "' WHERE name='" + player + "';");
    }


    @Override
    public int getPlayerCount() {
        ResultSet res = abfrage("SELECT COUNT(*) AS rowcount FROM players;");
        try {
            res.next();
            return res.getInt("rowcount");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public boolean isWhiteListed(String player) {
        ResultSet res = abfrage("SELECT * FROM players;");
        try {
            while(res.next()) {
                if(res.getString("name").equalsIgnoreCase(player)) return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void setAlive(String player, boolean isAlive) {
        update("UPDATE players SET alive=" + (isAlive==true?1:0) + " WHERE name='" + player + "';");
    }


    @Override
    public boolean isAlive(String player) {
        ResultSet res = abfrage("SELECT * FROM players WHERE name='" + player + "';");
        try {
            res.next();
            return res.getInt("alive") == 0 ? false : true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public int getMaxFolge() {
        ResultSet res = abfrage("SELECT * FROM config WHERE opt='maxvorproduziert'");
        try {
            res.next();
            System.out.println("Tag: " + getDay());
            System.out.println("FolgeMax: " + (getDay()+res.getInt("value")));
            return res.getInt("value")+getDay();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Errot");
        return -1;
    }

    @Override
    public int getDay() {
        ResultSet res = abfrage("SELECT * FROM config WHERE opt='startTime'");
        try {
            res.next();
            long startTime = res.getLong("value");
            long timePassed = System.currentTimeMillis()-(startTime);
            return (int)TimeUnit.MILLISECONDS.toDays(timePassed)+1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int getPlayerCountAlive() {
        ResultSet res = abfrage("SELECT COUNT(*) AS rowcount FROM players WHERE alive=1;");
        try {
            res.next();
            return res.getInt("rowcount");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getAliveTeamCount() {
        ResultSet res = abfrage("SELECT COUNT(DISTINCT team) AS rowcount FROM players WHERE alive=1");
        try {
            res.next();
            return res.getInt("rowcount");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
