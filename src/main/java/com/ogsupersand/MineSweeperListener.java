package com.ogsupersand;

import java.util.Scanner;
import java.util.Set;
import java.awt.Point;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MineSweeperListener extends ListenerAdapter {
    private static final Set<String> VALID_ACTIONS = Set.of(
        Action.START_GAME.toString(), 
        Action.UNCOVER.toString(),
        Action.FLAG.toString(), 
        Action.UNFLAG.toString(), 
        Action.CHORD.toString());
    
    private static final String COMMAND_TOKEN = "!";

    private MineSweeperGame game;

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
        if (command.equals(Action.START_GAME.toString())) {
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
