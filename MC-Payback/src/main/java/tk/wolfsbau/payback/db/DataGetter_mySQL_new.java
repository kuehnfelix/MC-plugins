package tk.wolfsbau.payback.db;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class DataGetter_mySQL_new implements IDataGetter {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/payback";
    private static final String user = "plugin";
    private static final String passwd = "pLugin";

    private static final Logger logger = Bukkit.getLogger();

    public DataGetter_mySQL_new() {
        try {
            Class.forName(JDBC_DRIVER);
            logger.info("Loaded JDBC driver.");
        } catch (ClassNotFoundException e) {
            logger.severe("Can't load database driver: " + e.getMessage());
        }
    }


    @Override
    public String getTeam(String player) {
        String teamName = null;
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE name='" + player + "'");
                try {
                    statement.execute();
                    ResultSet results = statement.getResultSet();
                    try {
                        while (results.next()) {
                            teamName = results.getString("team");
                        }
                    } finally {
                        results.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
        return teamName;
    }


    @Override
    public ArrayList<String> getTeamPlayers(String name) {
        ArrayList<String> teamPlayers = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE team='" + name + "'");
                try {
                    statement.execute();
                    ResultSet res = statement.getResultSet();
                    try {
                        while (res.next()) {
                            teamPlayers.add(res.getString("name"));
                        }
                    } finally {
                        res.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
        return teamPlayers;
    }


    @Override
    public void setTeam(String name, ArrayList<String> players) {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {

                //remove all teams players of the new team are already in
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE name=?;");
                try {
                    for(String player : players) { //loop through all players of the new team
                        statement.setString(1, player);
                        statement.execute();
                        ResultSet res = statement.getResultSet();
                        try {
                            if (res.next()) {//if player already is already in a team this is true -> remove that complete team
                                String team = res.getString("team");
                                PreparedStatement statement2 = connection.prepareStatement("DELETE FROM players WHERE team='" + team + "'"); //remove team the player is already in
                                try {
                                    statement2.executeUpdate();
                                } finally {
                                    statement2.close();
                                }
                            }
                        } finally {
                            res.close();
                        }
                    }
                } finally {
                    statement.close();
                }

                //remove the team from db if it already exists
                PreparedStatement statement3 = connection.prepareStatement("DELETE FROM players WHERE team='" + name + "'");
                try {
                    statement3.executeUpdate();
                } finally {
                    statement3.close();
                }

                //add all the players to the db
                PreparedStatement statement4 = connection.prepareStatement("INSERT INTO `players` (`name`, `team`, `folgen`, `location`) VALUES (?, ?, '0', 'world,10,64,10');");
                try {
                    for(String player : players) { //loop through all players of the new team
                            statement4.setString(1, player);
                            statement4.setString(2, name);
                            statement4.executeUpdate();
                    }
                } finally {
                    statement4.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
    }


    @Override
    public void removeTeam(String name) {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("DELETE FROM players WHERE team='" + name + "';");
                try {
                    statement.executeUpdate();
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
    }


    @Override
    public ArrayList<String> getTeamList() {
        TeamList teams = new TeamList();
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM players;");
                try {
                    statement.execute();
                    ResultSet res = statement.getResultSet();
                    try {
                        while (res.next()) {
                            teams.addPlayer(res.getString("name"), res.getString("team"));
                        }
                    } finally {
                        res.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
        return teams.getList();
    }


    @Override
    public HashMap<String, ArrayList<String>> getPlayerList() {
        TeamList teams = new TeamList();
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM players;");
                try {
                    statement.execute();
                    ResultSet res = statement.getResultSet();
                    try {
                        while (res.next()) {
                            teams.addPlayer(res.getString("name"), res.getString("team"));
                        }
                    } finally {
                        res.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
        return teams.getTeamPlayersList();
    }


    @Override
    public Long getOption(Option o) {
        Long l = null;
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM config WHERE opt='" + o.text() + "';");
                try {
                    statement.execute();
                    ResultSet res = statement.getResultSet();
                    try {
                        if (res.next()) {
                            l = res.getLong("value");
                        }
                    } finally {
                        res.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
        return l;
    }


    @Override
    public void setOption(Option option, Long value)
    {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE config SET value='" + value + "' WHERE opt='" + option.text() + "';");
                try {
                    statement.executeUpdate();
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
    }


    @Override
    public ArrayList<String> getOptionList() {
        ArrayList<String> options = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM config;");
                try {
                    statement.execute();
                    ResultSet res = statement.getResultSet();
                    try {
                        while(res.next()) {
                            options.add(ChatColor.AQUA + res.getString("opt") + ChatColor.BLACK + " -> " + ChatColor.WHITE + res.getString("value"));
                        }
                    } finally {
                        res.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
        return options;
    }


    @Override
    public Location getChestLocation(String team) {
        int x=0;
        int y=0;
        int z=0;
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM chests WHERE team ='" + team + "';");
                try {
                    statement.execute();
                    ResultSet res = statement.getResultSet();
                    try {
                        if (res.next()) {
                            x = res.getInt("x");
                            y = res.getInt("y");
                            z = res.getInt("z");
                        }
                    } finally {
                        res.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
        return new Location(Bukkit.getWorlds().get(0), x, y, z);
    }


    @Override
    public void setChestLocation(String team, Location loc) {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                //remove old chest location
                PreparedStatement statement = connection.prepareStatement("DELETE FROM chests WHERE team='" + team + "';");
                try {
                    statement.executeUpdate();
                } finally {
                    statement.close();
                }
                //remove old chest location
                if(loc != null) {//if null don't set new location in database (null is used to remove chest location from db)
                    PreparedStatement statement2 = connection.prepareStatement("INSERT INTO `chests` (`team`, `x`, `y`, `z`) VALUES ('" + team + "', '" + loc.getBlockX() + "', '" + loc.getBlockY() + "', '" + loc.getBlockZ() + "');");
                    try {
                        statement2.executeUpdate();
                    } finally {
                        statement.close();
                    }
                }

            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }

    }


    @Override
    public int getFolge(String player) {
        int folge = 0;
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE name='" + player + "';");
                try {
                    statement.execute();
                    ResultSet res = statement.getResultSet();
                    try {
                        if (res.next()) {
                             folge = res.getInt("folgen");
                        }
                    } finally {
                        res.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
        return folge;
    }


    @Override
    public void setFolge(String player, int folge) {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE players SET folgen=" + folge + " WHERE name ='" + player + "';");
                try {
                    statement.executeUpdate();
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
    }


    @Override
    public Location getPlayerLocation(String player) {
        String[] locString = null;
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE name='" + player + "';");
                try {
                    statement.execute();
                    ResultSet res = statement.getResultSet();
                    try {
                        if (res.next()) {
                            locString = res.getString("location").split(",");
                        }
                    } finally {
                        res.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
        if(locString.length!=4) return null;
        return new Location(Bukkit.getWorld(locString[0]),Integer.valueOf(locString[1]),Integer.valueOf(locString[2]),Integer.valueOf(locString[3]));
    }


    @Override
    public void setPlayerLocation(String player, Location location) {
        String locString=location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE players SET location='" + locString + "' WHERE name='" + player + "';");
                try {
                    statement.executeUpdate();
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
    }


    @Override
    public int getPlayerCount() {
        int count = 0;
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS rowcount FROM players;");
                try {
                    statement.execute();
                    ResultSet res = statement.getResultSet();
                    try {
                        if (res.next()) {
                            count = res.getInt("rowcount");
                        }
                    } finally {
                        res.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
        return count;
    }


    @Override
    public boolean isWhiteListed(String player) {
        boolean iswhitelisted = false;
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM players;");
                try {
                    statement.execute();
                    ResultSet res = statement.getResultSet();
                    try {
                        while (res.next()) {
                            if(res.getString("name").equalsIgnoreCase(player)) {
                                iswhitelisted = true;
                            }
                        }
                    } finally {
                        res.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
        return iswhitelisted;
    }


    @Override
    public void setAlive(String player, boolean isAlive) {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE players SET alive=" + (isAlive==true?1:0) + " WHERE name='" + player + "';");
                try {
                    statement.executeUpdate();
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }

    }


    @Override
    public boolean isAlive(String player) {
        boolean isAlive = false;
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE name='" + player + "';");
                try {
                    statement.execute();
                    ResultSet res = statement.getResultSet();
                    try {
                        if (res.next()) {
                            isAlive = res.getInt("alive") == 0 ? false : true;
                        }
                    } finally {
                        res.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
        return isAlive;
    }


    @Override
    public int getMaxFolge() {
        int maxFolge = 0;
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM config WHERE opt='maxvorproduziert'");
                try {
                    statement.execute();
                    ResultSet res = statement.getResultSet();
                    try {
                        if (res.next()) {
                            maxFolge = res.getInt("value")+getDay();
                        }
                    } finally {
                        res.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
        return maxFolge;
    }


    @Override
    public int getDay() {
        int day = 0;
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM config WHERE opt='startTime'");
                try {
                    statement.execute();
                    ResultSet res = statement.getResultSet();
                    try {
                        if (res.next()) {
                            long startTime = res.getLong("value");
                            long timePassed = System.currentTimeMillis()-(startTime);
                            day = (int) TimeUnit.MILLISECONDS.toDays(timePassed)+1;
                        }
                    } finally {
                        res.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
        return day;
    }


    @Override
    public int getPlayerCountAlive() {
        int playerCountAlive = 0;
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS rowcount FROM players WHERE alive=1;");
                try {
                    statement.execute();
                    ResultSet res = statement.getResultSet();
                    try {
                        if (res.next()) {
                            playerCountAlive = res.getInt("rowcount");
                        }
                    } finally {
                        res.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
        return playerCountAlive;
    }


    @Override
    public int getAliveTeamCount() {
        int aliveTeamCount = 0;
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT COUNT(DISTINCT team) AS rowcount FROM players WHERE alive=1");
                try {
                    statement.execute();
                    ResultSet res = statement.getResultSet();
                    try {
                        if (res.next()) {
                            aliveTeamCount = res.getInt("rowcount");
                        }
                    } finally {
                        res.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
        return aliveTeamCount;
    }


    @Override
    public double getBorderRadius() {
        double borderRadius = 0;
        int startSize = (int)(long)getOption(Option.BORDERSTARTSIZE);
        int minimumSize = (int)(long)getOption(Option.BORDERMINIMUMSIZE);
        int shrinkDay = (int)(long)getOption(Option.BORDERSTARTSHRINKDAY);
        int shrinkAmount = (int)(long)getOption(Option.BORDERSHRINKAMOUNT);
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM config WHERE opt='startTime'");
                try {
                    statement.execute();
                    ResultSet res = statement.getResultSet();
                    try {
                        if (res.next()) {
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
                                borderRadius = (startSize - (shrinkdays*shrinkAmount));
                            } else {
                                borderRadius = startSize;
                            }
                        }
                    } finally {
                        res.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
        return borderRadius;
    }


    @Override
    public boolean isTeamAlive(String team) {
        boolean isTEamAlive = false;
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE team='" + team + "';");
                try {
                    statement.execute();
                    ResultSet res = statement.getResultSet();
                    try {
                        while (res.next()) {
                            if(res.getInt("alive")==1) {
                                isTEamAlive = true;
                            }
                        }
                    } finally {
                        res.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
        return isTEamAlive;
    }


    @Override
    public ArrayList<String> getTeamListAlive() {
        TeamList teams = new TeamList();
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM players;");
                try {
                    statement.execute();
                    ResultSet res = statement.getResultSet();
                    try {
                        while (res.next()) {
                            if(res.getInt("alive")!=0) {
                                teams.addPlayer(res.getString("name"), res.getString("team"));
                            }
                        }
                    } finally {
                        res.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
        return teams.getList();
    }


    @Override
    public int getStrikes(String player) {
        int strikes = 0;
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE name='" + player + "';");
                try {
                    statement.execute();
                    ResultSet res = statement.getResultSet();
                    try {
                        if (res.next()) {
                            strikes = (int)res.getLong("strikes");
                        }
                    } finally {
                        res.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
        return strikes;
    }


    @Override
    public void setStrikes(String player, int strikes) {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE players SET strikes=" + strikes + " WHERE name='" + player + "';");
                try {
                    statement.executeUpdate();
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }

    }

    @Override
    public boolean isClearInv(String player) {
        boolean isclearinv = false;
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE name='" + player + "';");
                try {
                    statement.execute();
                    ResultSet res = statement.getResultSet();
                    try {
                        if (res.next()) {
                            if(res.getInt("clearInv")!=0) {
                                isclearinv = true;
                            }
                        }
                    } finally {
                        res.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
        return  isclearinv;
    }

    @Override
    public void setClearInv(String player, boolean clearInv) {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE players SET clearInv=" + (clearInv == true ? 1 : 0) + " WHERE name='" + player + "';");
                try {
                    statement.executeUpdate();
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<String> getPlayers() {
        ArrayList<String> players = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, user, passwd);
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM players");
                try {
                    statement.execute();
                    ResultSet res = statement.getResultSet();
                    try {
                        while (res.next()) {
                            players.add(res.getString("name"));
                        }
                    } finally {
                        res.close();
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            logger.severe("Failed to get JDBC connection: " + e.getMessage());
        }
        return players;
    }
}
