package com.xuxe.octaveBot.main;

import com.xuxe.octaveBot.commands.miscellaneous.*;
import com.xuxe.octaveBot.commands.utility.*;
import com.xuxe.octaveBot.commands.admin.*;
import com.xuxe.octaveBot.commands.music.*;
import com.xuxe.octaveBot.commands.owner.*;
import com.xuxe.octaveBot.listeners.*;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.*;
import java.util.Properties;

/*
    Copyright 2019 ThatXuxe
    Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software distributed under the License is
    distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and limitations under the License.
 */
@SuppressWarnings("unused")
public class Main extends ListenerAdapter {

    public static void main(String[] args) throws Exception
    {

        //extracting bot token and owner ID
        Properties properties = new Properties();
        FileReader fileReader = new FileReader("config.properties");
        properties.load(fileReader);
        String token = properties.getProperty("token");
        String ownerID = properties.getProperty("ownerID");
        fileReader.close();
        properties.clear();
        EventWaiter waiter = new EventWaiter();

        //building commands.
        CommandClientBuilder commandClientBuilder = new CommandClientBuilder();

        commandClientBuilder.setPrefix("!!");
        //Admin
        commandClientBuilder.addCommand(new Kick());
        commandClientBuilder.addCommand(new Ban());
        commandClientBuilder.addCommand(new PurgeMessages());
        //music
        commandClientBuilder.addCommand(new MusicCommand());
        //utility
        commandClientBuilder.addCommand(new Google());
        commandClientBuilder.addCommand(new JoinDate());
        commandClientBuilder.addCommand(new Emotify());
        commandClientBuilder.addCommand(new ServerCreationDate());
        commandClientBuilder.addCommand(new WikiInfo());
        commandClientBuilder.addCommand(new CatImage());
        commandClientBuilder.addCommand(new RandomFact());
        commandClientBuilder.addCommand(new LearnJava());
        commandClientBuilder.addCommand(new Avatar());
        commandClientBuilder.addCommand(new Trivia(waiter));
        commandClientBuilder.addCommand(new SongInfo());
        commandClientBuilder.addCommand(new YouTubeSearcher());
        commandClientBuilder.addCommand(new MathCommand());
        //miscellaneous
        commandClientBuilder.addCommand(new GetBotPing());
        commandClientBuilder.addCommand(new GetRepository());
        commandClientBuilder.addCommand(new UserCount());
        commandClientBuilder.addCommand(new GuildCount());
        commandClientBuilder.addCommand(new EmoteInfo());
        //owner
        commandClientBuilder.addCommand(new Eval());
        commandClientBuilder.addCommand(new Say());
        commandClientBuilder.addCommand(new MemoryCommand());
        commandClientBuilder.addCommand(new ListGuilds());
        //building client
        commandClientBuilder.setOwnerId(ownerID);
        commandClientBuilder.useHelpBuilder(true);
        //commandClientBuilder.setDiscordBotsKey("");

        JDA jda = new JDABuilder(AccountType.BOT).setToken(token).addEventListener(commandClientBuilder.build()).addEventListener(waiter).build().awaitReady();
        jda.getPresence().setGame(Game.listening("Raptor engines (!!help)"));
        //Event Listeners
        commandClientBuilder.setServerInvite("https://discordapp.com/invite/xNyH7y3");

        jda.addEventListener(new MessageReceived());
        jda.addEventListener(new Ready());
    }
}