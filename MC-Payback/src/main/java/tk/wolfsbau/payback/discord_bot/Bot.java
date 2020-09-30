package tk.wolfsbau.payback.discord_bot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;

public class Bot {
    public static JDA api;

    public static void main() {
        try {
            api = new JDABuilder(AccountType.BOT).setToken(Ref.TOKEN).buildBlocking();
            api.getPresence().setGame(Game.of(Game.GameType.WATCHING, "Payback"));
            api.addEventListener(new MessageListener());
        } catch (LoginException | InterruptedException e) {
            //e.printStackTrace();
        }
    }

    public static void sendMessageBotChannel(String msg) {
        try {
            api.getTextChannelById("485850149642436608").sendMessage(msg).queue();
        } catch(Exception e) {

        }
    }

    public static void chatLog(String msg) {
        try {
            api.getTextChannelById("485850821284593665").sendMessage(msg).queue();
        } catch(Exception e) {

        }
    }

    public static void sendStrikeChannel(String msg) {
        try {
            api.getTextChannelById("479364864129892362").sendMessage(msg).queue();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }



}
