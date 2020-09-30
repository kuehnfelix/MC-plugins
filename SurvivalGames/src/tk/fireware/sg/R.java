package tk.fireware.sg;

import org.bukkit.ChatColor;



public class R {
    public static final String PREFIX = ChatColor.GRAY + "[" + ChatColor.BLUE + "SG" + ChatColor.GRAY + "] " + ChatColor.YELLOW;
    public static final String BANKPREFIX = ChatColor.GRAY + "[" + ChatColor.YELLOW + "Bank" + ChatColor.GRAY + "] " + ChatColor.YELLOW;

    static final String WORLDNAME = "world";
    static final String BACKUP_WORLDNAME = "backup";

    static final String NAMES_BUILDING_MENU = "BUILDING-MENU";
    static final String NAMES_SPAWNPOINT_ITEM= "SET SPAWNPOINT";
    public static final String NAMES_EXIT_ITEM="RAUS HIER!";
    public static final String SHOP = "Items einkaufen";
    public static final String SHOPPREFIX = ChatColor.GOLD+""+ChatColor.MAGIC+"|"+ChatColor.RESET+ChatColor.GRAY + " [" + ChatColor.GOLD + "SHOP" + ChatColor.GRAY +"] " +ChatColor.GOLD+""+ChatColor.MAGIC+"|"+ChatColor.RESET+" "+ ChatColor.GOLD;
    public static final String MESSAGE_PURCHASED = SHOPPREFIX+"Du hast ein Item gekauft.";
    public static final String MESSAGE_NOTEM = SHOPPREFIX+ChatColor.RED+"Du hast nicht genug Knochen, um dieses Item zu kaufen.";
    public static final String MESSAGE_NOTATB = SHOPPREFIX+ChatColor.RED+"Du hast bereits ein Item für diese Runde gekauft!";

    static final String MESSAGES_BUILDINGMODE_ENABLED = PREFIX + "Building-Mode enabled!";
    static final String MESSAGES_BUILDINGMODE_DISABLED = PREFIX + "Building-Mode disabled!";
    static final String MESSAGES_GAME_STARTING = PREFIX + "Game starts in <time> seconds";
    public static final String MESSAGES_PLAYERS_ONLINE = PREFIX + "There are <count> Players online!";
    static final String MESSAGES_NOT_ENOUGH_PLAYERS = PREFIX + "There are not enough Players online... waiting for more Players!";

    static final String MESSAGES_BUILDER_SET_SPAWNPOINT = PREFIX + "A Spawnpoint has been placed at <location>! Total Spawnpoints: <amount>";
    static final String MESSAGES_BUILDER_BREAK_SPAWNPOINT = PREFIX + "The Spawnpoint at <location> has been broken! Total Spawnpoints: <amount>";

    public static final String MESSAGES_SERVER_FULL = PREFIX + "The server is full!";
    public static final String MESSAGES_KICKED_BY_PREMIUM = PREFIX + "You were kicked to make place for a Premium player!";
	public static final String MESSAGE_RESTART=PREFIX+"Server restarts in <number> seconds!";
	public static final String MESSAGE_WORLDSAVED=PREFIX+"World saved.";
	public static final String MESSAGE_WORLDSAVEERROR=PREFIX+ChatColor.RED+"World couldn't be saved.";
    
    public static final String MESSAGES_LEAVE = ChatColor.RED+"That´s not true! Don´t believe this! DON`T PUSH THE BUTTON BELOW!!";
    public static final String MESSAGE_KILLEDBY = PREFIX + "<player> wurde von <killer> getötet.";
    public static final String MESSAGE_DEATH = PREFIX + "<player> ist gestorben.";
    public static final String MESSAGE_WIN = PREFIX + ChatColor.GOLD+"<player> hat gewonnen!";
    public static final String MESSAGE_CHESTREFILL=PREFIX+"All Chests have been refilled!";
	public static final String MESSAGE_SPECTATE = PREFIX+"Spectate Players by using the Spectator Menu. (Press 1,2,3... and ENTER)";
    
	public static final double limit=0.7;
	public static final int chestRefillSec=65*7;
	public static final int restartSec=10;
    public static final int VALUES_LOBBY_TIMER = 30;
    
    public static final String MESSAGE_PLUGINBY=PREFIX+" SG-Plugin by ZAO77 aka lfnnx";

    static final String[] BLOCKED_ITEMS = new String[] {
            NAMES_BUILDING_MENU,NAMES_SPAWNPOINT_ITEM,NAMES_EXIT_ITEM
    };
    
    public static final String sep=";";
	public static final String sepV="&";
	public static final String sepVsub=",";
	
	public static final String CONFIGHELP="There are four entries in the items.yml file: ItemsLvl0, ItemsLvl1, ItemsLvl2, ItemsLvl3. Each entry contains Items of this probability class. The Arguments of the Item are separated by '"+sep+"'. arg[0]=probability, arg[1]=Material, arg[2]=Amount, arg[3]=Displayname, arg[4]=Enchantments separated by '"+sepV+"'. Example Item:		7;Material.SNOW_BALL;16;customDisplayname;Enchantment.KNOCKBACK,1&Enchantment.THORNS,2";
}
