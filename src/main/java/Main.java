import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends ListenerAdapter
{
    public static void main(String[] args) {
        System.out.println("(c) 2022 Aariy.NET");
        JDABuilder jda = JDABuilder.createDefault(System.getProperty("token"));
        jda
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new Main())
                .build();
    }

    public void onMessageReceived(MessageReceivedEvent e) {
        if(e.isFromGuild() && !e.getAuthor().isBot() && e.getChannel().getId().equals(System.getProperty("ch"))) {
            String[] mes = e.getMessage().getContentRaw().split("\n");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.hh.mm");
            try {
                Date d = sdf.parse(mes[0].replaceAll("\s", ""));
                if(d.before(new Date()))
                    throw new Exception();

                TimerTask[] task = {
                        new TimerTask() {
                            @SuppressWarnings("ConstantConditions")
                            public void run() {
                                if(e.getChannel().retrieveMessageById(e.getMessage().getId()).complete() == null) return;
                                MessageCreateBuilder mcb = new MessageCreateBuilder().setContent(e.getMessage().getContentRaw().substring(mes[0].length()+mes[1].length()+2));
                                GuildMessageChannel gmc = (GuildMessageChannel)e.getJDA().getGuildChannelById(mes[1].replaceAll("[<#>\s]", ""));
                                gmc.sendMessage(mcb.build()).queue();
                                e.getMessage().getAttachments().forEach(attachment -> gmc.sendMessage(attachment.getUrl()).queue());

                            }
                        }
                };
                Timer timer = new Timer();
                timer.schedule(task[0], d);
                e.getMessage().addReaction(Emoji.fromFormatted(":white_check_mark:")).queue();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
