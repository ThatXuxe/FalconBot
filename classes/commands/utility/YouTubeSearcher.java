package com.xuxe.octaveBot.commands.utility;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import java.io.FileReader;
import java.util.Properties;
import java.util.logging.Logger;

public class YouTubeSearcher extends Command {

    private static final Logger logger = Logger.getGlobal();
    public YouTubeSearcher()
    {
        this.name = "youtube";
        this.aliases = new String[]{"yt","tube"};
        this.help = "Searches YouTube for the given search term";
        this.cooldownScope = CooldownScope.USER;
        this.cooldown = 5;
        this.category = new Category("Utility");
        //this.longhelp = "";
    }

    @Override
    protected void execute(CommandEvent commandEvent)
    {
        logger.info(name+" command used by "+commandEvent.getAuthor().getId()+" in "+commandEvent.getGuild().getId());
        final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
        final JsonFactory JSON_FACTORY = new JacksonFactory();
        try
        {
            YouTube youTube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, httpRequest -> {}).setApplicationName("FalconBot").build();

            String query = commandEvent.getArgs();

            YouTube.Search.List search = youTube.search().list("id,snippet");
                Properties properties = new Properties();
                FileReader fileReader = new FileReader("config.properties");
                properties.load(fileReader);
                search.setKey(properties.getProperty("YTkey"));
                System.out.println(properties.getProperty("YTkey"));
                fileReader.close();
                properties.clear();

            search.setQ(query);
            search.setType("video");
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(Long.parseLong("1"));
            SearchListResponse searchResponse = search.execute();

            SearchResult searchResult = searchResponse.getItems().get(0);
            if (searchResult.getId().getKind().equals("youtube#video"))
            {
                commandEvent.reply("Top search result: https://www.youtube.com/watch?v="+searchResult.getId().getVideoId()+"\n"+commandEvent.getAuthor().getAsMention());
            }
            else
            {
                commandEvent.reply("Something went wrong");//add loop if this is true
            }

        }
        catch (Exception e)
        {
            commandEvent.reply("Something went wrong.");
            e.printStackTrace();
        }
    }
}
