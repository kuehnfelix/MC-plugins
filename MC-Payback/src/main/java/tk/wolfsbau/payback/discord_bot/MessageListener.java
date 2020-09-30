package tk.wolfsbau.payback.discord_bot;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import tk.wolfsbau.payback.cmd.Cmd_strike;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] msg = event.getMessage().getContentRaw().split(" ");
        System.out.println(msg);
        if(msg[0].equals("/strike")) {
            if(msg.length==2) {
                Cmd_strike.strike(msg[1], "Regelverstoß");
            }
        }
    }
}