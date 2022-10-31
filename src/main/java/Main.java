import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends ListenerAdapter
{
    public static void main(String[] args) {
        System.out.println("© Aariy.NET");
        JDABuilder jda = JDABuilder.createDefault("token");
        jda
                .addEventListeners(new Main())
                .build();
    }
    public void onMessageReceived(MessageReceivedEvent e) {
        if(e.getChannel().getId().equals("id")) {
            String[] mes = e.getMessage().getContentRaw().split("\n");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.hh.mm");
            try
            {
                Date d = sdf.parse(mes[0]);
                TimerTask[] task = {
                        new TimerTask() {
                            @SuppressWarnings("ConstantConditions")
                            public void run() {
                                ((GuildMessageChannel)e.getJDA().getGuildChannelById(mes[1].replaceAll("[<#>]", ""))).sendMessage(e.getMessage().getContentRaw().substring(mes[0].length()+mes[1].length()+2)).queue();
                            }
                        }
                };
                Timer timer = new Timer();
                timer.schedule(task[0], d);
                e.getMessage().addReaction(Emoji.fromUnicode("✅")).queue();
            }
            catch (Exception ex) {
                e.getMessage().addReaction(Emoji.fromUnicode("🚫")).queue();
            }
        }
    }
}
