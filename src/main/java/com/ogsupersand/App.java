package com.ogsupersand;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

import javax.security.auth.login.LoginException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Hello world!
 *
 */
public class App 
{  
    private static final String DISCORD_CONFIG_PATH = "config.json";
    private static final String BOT_TOKEN_NAME = "botToken";

    public static void main(String[] args)
    {
        try {
            JSONObject config = (JSONObject) new JSONParser().parse(new FileReader(DISCORD_CONFIG_PATH));
            String token = (String) config.get(BOT_TOKEN_NAME);
            JDA jda = JDABuilder.createDefault(token)
                    .addEventListeners(new MineSweeperListener())
                    .build();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
