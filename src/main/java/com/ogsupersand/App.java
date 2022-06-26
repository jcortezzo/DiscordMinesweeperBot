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
    private static final String COMMAND_TOKEN = "!";
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

            if (!msgString.startsWith(COMMAND_TOKEN)) {
                return;
            }
            System.out.println(msgString);
            String fullMessage = msgString.substring(COMMAND_TOKEN.length());
            Scanner scan = new Scanner(fullMessage);
            String command = scan.next();
            int x, y;
            if (command.equals("minesweeper")) {
                game = new MineSweeperGame();
                game.startNewGame();
            } else {
                try {
                    x = scan.nextInt();
                    y = scan.nextInt();
                    Point p = new Point(x, y);
                    if (command.equals(Action.UNCOVER.toString())) {
                        if (game.uncover(p)) {
                            sendMessage(channel, 
                                    String.format("You lose! Play again with command %s", 
                                            String.format("%s%s", COMMAND_TOKEN, Action.START_GAME)));
                        } else if (game.isGameWon()) {
                            sendMessage(channel, 
                                    String.format("You win! Play again with command %s", 
                                            String.format("%s%s", COMMAND_TOKEN, Action.START_GAME)));
                        }
                    } else if (command.equals(Action.FLAG.toString())) {
                        game.flag(p);
                    } else if (command.equals(Action.UNFLAG.toString())) {
                        game.unflag(p);
                    } else if (command.equals(Action.CHORD.toString())) {
                        game.chord(p);
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
            sendMessage(channel, game.toString());
            if (scan != null) {
                scan.close();
            }
        }

        private void sendMessage(MessageChannel channel, String s) {
            channel.sendMessage(s).queue();
        }
    }
}
