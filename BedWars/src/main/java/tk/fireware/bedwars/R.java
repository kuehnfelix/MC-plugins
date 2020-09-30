package tk.fireware.bedwars;

import org.bukkit.ChatColor;



class R {
    static final String PREFIX = ChatColor.GRAY + "[" + ChatColor.BLUE + "BedWars" + ChatColor.GRAY + "] " + ChatColor.YELLOW;

    static final String WORLDNAME = "world";
    static final String BACKUP_WORLDNAME = "backup";

    static final String NAMES_BUILDING_MENU = "BUILDING-MENU";
    static final String NAMES_RED_ITEM = "RED-TEAM";
    static final String NAMES_BLUE_ITEM = "BLUE-TEAM";
    static final String NAMES_GREEN_ITEM = "GREEN-TEAM";
    static final String NAMES_YELLOW_ITEM = "YELLOW-TEAM";
    static final String NAMES_SPAWNER_ITEM = "ITEM-SPAWNERS";
    static final String NAMES_BRONZE_SPAWNER = "BRONZE-SPAWNER";
    static final String NAMES_SILVER_SPAWNER = "SILVER-SPAWNER";
    static final String NAMES_GOLD_SPAWNER = "GOLD-SPAWNER";
    static final String NAMES_SHOP = "STORE";

    static final String NAMES_RED_BED = "RED-BED";
    static final String NAMES_BLUE_BED = "BLUE-BED";
    static final String NAMES_GREEN_BED = "GREEN-BED";
    static final String NAMES_YELLOW_BED = "YELLOW-BED";

    static final String NAMES_RED_SPAWN = "RED-SPAWN";
    static final String NAMES_BLUE_SPAWN = "BLUE-SPAWN";
    static final String NAMES_GREEN_SPAWN = "GREEN-SPAWN";
    static final String NAMES_YELLOW_SPAWN = "YELLOW-SPAWN";

    static final String NAMES_RED_TEAM = ""+ChatColor.RED+"RED-TEAM"+ChatColor.YELLOW+"";
    static final String NAMES_BLUE_TEAM = ""+ChatColor.BLUE+"BLUE-TEAM"+ChatColor.YELLOW+"";
    static final String NAMES_GREEN_TEAM = ""+ChatColor.GREEN+"GREEN-TEAM"+ChatColor.YELLOW+"";
    static final String NAMES_YELLOW_TEAM = ""+ChatColor.YELLOW+"YELLOW-TEAM"+ChatColor.YELLOW+"";

    static final String NAMES_EXIT_ITEM="RAUS HIER!";


    static final String MESSAGES_BUILDINGMODE_ENABLED = PREFIX + "Building-Mode enabled!";
    static final String MESSAGES_BUILDINGMODE_DISABLED = PREFIX + "Building-Mode disabled!";
    static final String MESSAGES_GAME_STARTING = PREFIX + "Game starts in <time> seconds";
    static final String MESSAGES_PLAYERS_ONLINE = PREFIX + "There are <count> Players online!";
    static final String MESSAGES_NOT_ENOUGH_PLAYERS = PREFIX + "There are not enough Players online... waiting for more Players!";

    static final String MESSAGES_SET_BED = PREFIX + "The bed of <team> has ben set to <location>";
    static final String MESSAGES_SET_BPAWN = PREFIX + "The spawn of <team> has ben set to <location>";
    static final String MESSAGES_BUILDER_SET_ITEMSPAWNER = PREFIX + "A <spawner> has been placed at <location>!";
    static final String MESSAGES_BUILDER_SET_SHOP = PREFIX + "A " + NAMES_SHOP +" has been placed at <location>!";
    static final String MESSAGES_BUILDER_BREAK_BED = PREFIX + "The bed of <team> has ben broken!";
    static final String MESSAGES_BUILDER_BREAK_SPAWN = PREFIX + "The spawnpoint of <team> has ben broken!";
    static final String MESSAGES_BUILDER_BREAK_ITEMSPAWNER = PREFIX + "The <spawner> at <location> has been broken!";
    static final String MESSAGES_BUILDER_BREAK_SHOP = PREFIX + "A " + NAMES_SHOP +" has been deleted at <location>!";

    static final String MESSAGES_SHOP_CLOSED = PREFIX + "Sorry this " + NAMES_SHOP + " is closed at the moment!";

    static final String MESSAGES_TEAM_JOINED = PREFIX + "You are now in <team>";

    static final String MESSAGES_SERVER_FULL = PREFIX + "The server is full!";
    static final String MESSAGES_KICKED_BY_PREMIUM = PREFIX + "You were kicked to make place for a Premium player!";

    static final String MESSAGES_LEAVE = ChatColor.RED+"That´s not true! Don´t believe this! DON`T PUSH THE BUTTON BELOW!!";



    static final int VALUES_LOBBY_TIMER = 30;

    static final String[] BLOCKED_ITEMS = new String[] {
            NAMES_BUILDING_MENU,
            NAMES_BLUE_ITEM, NAMES_RED_ITEM, NAMES_GREEN_ITEM, NAMES_YELLOW_ITEM,

            NAMES_RED_BED, NAMES_RED_BED + ChatColor.GREEN + " (YES)",NAMES_RED_BED + ChatColor.RED +" (NO)" ,
            NAMES_BLUE_BED, NAMES_BLUE_BED + ChatColor.GREEN + " (YES)",NAMES_BLUE_BED + ChatColor.RED +" (NO)" ,
            NAMES_GREEN_BED, NAMES_GREEN_BED + ChatColor.GREEN + " (YES)",NAMES_GREEN_BED + ChatColor.RED +" (NO)" ,
            NAMES_YELLOW_BED, NAMES_YELLOW_BED + ChatColor.GREEN + " (YES)",NAMES_YELLOW_BED + ChatColor.RED +" (NO)" ,

            NAMES_RED_SPAWN, NAMES_RED_SPAWN + ChatColor.GREEN + " (YES)",NAMES_RED_SPAWN + ChatColor.RED +" (NO)" ,
            NAMES_BLUE_SPAWN, NAMES_BLUE_SPAWN + ChatColor.GREEN + " (YES)",NAMES_BLUE_SPAWN + ChatColor.RED +" (NO)" ,
            NAMES_GREEN_SPAWN, NAMES_GREEN_SPAWN + ChatColor.GREEN + " (YES)",NAMES_GREEN_SPAWN + ChatColor.RED +" (NO)" ,
            NAMES_YELLOW_SPAWN, NAMES_YELLOW_SPAWN + ChatColor.GREEN + " (YES)",NAMES_YELLOW_SPAWN + ChatColor.RED +" (NO)" ,
    };

}
