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
    private static final Set<String> VALID_ACTIONS = Set.of(
            Action.START_GAME.toString(), 
            Action.UNCOVER.toString(),
            Action.FLAG.toString(), 
            Action.UNFLAG.toString(), 
            Action.CHORD.toString());
    private static final String DISCORD_CONFIG_PATH = "config.json";
    private static final String BOT_TOKEN_NAME = "botToken";

    private static MineSweeperGame game;

    public static void main( String[] args ) throws LoginException, FileNotFoundException, IOException, ParseException
    {
        JSONObject config = (JSONObject) new JSONParser().parse(new FileReader(DISCORD_CONFIG_PATH));
        String token = (String) config.get(BOT_TOKEN_NAME);
        JDA jda = JDABuilder.createDefault(token)
                .addEventListeners(new ReadyListener())
                .build();
    }

    private static class ReadyListener extends ListenerAdapter {
        @Override
        public void onMessageReceived(MessageReceivedEvent event) {
            Message msg = event.getMessage();
            String msgString = msg.getContentRaw();
            MessageChannel channel = event.getChannel();
            if (msg.getContentRaw().equals("!ping")) {
                
                long time = System.currentTimeMillis();
                channel.sendMessage("Pong!")
                        .queue(response -> {
                            response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue();
                        });
            }

            if (!msgString.startsWith("!")) {
                return;
            }
            System.out.println(msgString);
            String fullMessage = msgString.substring(1);
            Scanner scan = new Scanner(fullMessage);
            String command = scan.next();
            int x, y;
            if (command.equals("minesweeper"))
            try {
                switch (command) {
                    case "minesweeper":
                        game = new MineSweeperGame();
                        game.startNewGame();
                        sendMessage(channel, game.toString());
                        break;
                    case "dig":
                        x = scan.nextInt();
                        y = scan.nextInt();
                        game.uncover(new Point(x, y));
                        sendMessage(channel, game.toString());
                        break;
                    case "flag":
                        x = scan.nextInt();
                        y = scan.nextInt();
                        game.flag(new Point(x, y));
                        sendMessage(channel, game.toString());
                        break;
                    case "unflag":
                        x = scan.nextInt();
                        y = scan.nextInt();
                        game.unflag(new Point(x, y));
                        sendMessage(channel, game.toString());
                        break;
                }
            } catch (InvalidMoveException e) {
                sendMessage(channel, String.format("Invalid move! %s", e.getMessage()));
                e.printStackTrace();
            } catch (Exception e) {
                sendMessage(channel, 
                        String.format("Couldn't understand request, please choose out of %s\nReceived %s: %s", 
                        VALID_ACTIONS, e.getClass(), e.getMessage()));
                e.printStackTrace();
            }
        }

        private void sendMessage(MessageChannel channel, String s) {
            long time = System.currentTimeMillis();
            channel.sendMessage(s).queue();
        }
    }
}
