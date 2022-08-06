package com.ogsupersand;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

/**
 * Hello world!
 *
 */
public class App 
{  
    private static final String BOT_TOKEN_NAME = "ACCESS_TOKEN";

    public static void main(String[] args)
    {
        try {
            String token = System.getenv(BOT_TOKEN_NAME);
            JDA jda = JDABuilder.createDefault(token)
                    .addEventListeners(new MineSweeperListener())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
