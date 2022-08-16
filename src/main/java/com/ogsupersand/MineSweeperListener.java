package com.ogsupersand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Strings;

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

    private Map<MessageChannel, MineSweeperGame> gameMap;
    
    public MineSweeperListener() {
        gameMap = new HashMap<>();
    }

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
        String command = scan.next().toLowerCase();
        int x, y;
        if (command.equals(Action.START_GAME.toString())) {
            MineSweeperGame game = new MineSweeperGame();
            game.startNewGame();
            gameMap.put(channel, game);
        } else {
            MineSweeperGame game;
            if (!gameMap.containsKey(channel)) {
                sendMessage(channel, 
                        String.format("Game not started in the current channel. Please enter %s%s to start a game.", COMMAND_TOKEN, Action.START_GAME.toString()));
                if (scan != null) {
                    scan.close();
                }
                return;
            } else {
                game = gameMap.get(channel);
            }
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
                    if (game.chord(p)) {
                        sendMessage(channel, 
                                String.format("You lose! Play again with command %s", 
                                        String.format("%s%s", COMMAND_TOKEN, Action.START_GAME)));
                    } else if (game.isGameWon()) {
                        sendMessage(channel, 
                                String.format("You win! Play again with command %s", 
                                        String.format("%s%s", COMMAND_TOKEN, Action.START_GAME)));
                    }
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
        sendMessage(channel, getMineSweeperBoardWithCoordinates(channel));//game.toString());
        if (scan != null) {
            scan.close();
        }
    }

    private void sendMessage(MessageChannel channel, String s) {
        channel.sendMessage(s).queue();
    }

    /**
     * IN-PROGRESS: I'm only adding channel to test sending messages through here.
     *  IMPORTANT NOTE: There seems to be an emoji cap to Discord messages.
     *  Example board: 12x15 shows that it starts truncating the coords. 
     *  We will keep the board 9x9 then.
     *  FOLLOW-UP: Wrapping the board in code seems to work (```)
     * 
     * @param channel
     * @return
     */
    private String getMineSweeperBoardWithCoordinates(MessageChannel channel) {
        MineSweeperGame game = gameMap.get(channel);
        String board = reverseBoard(channel);//game.toString();
        String separator = "⬛";
        String result = "";

        Scanner lineScan = new Scanner(board);
        int index = 0;//game.HEIGHT - 1;

        // y-axis
        List<String> yAxis = new ArrayList<>();
        for (int i = game.HEIGHT - 1; i >= 0; i--) yAxis.add("" + i);
        int maxYCoordDigits = ((game.HEIGHT - 1) + "").length();
        for (int i = 0; i < game.HEIGHT; i++) yAxis.set(i, Strings.repeat("0", maxYCoordDigits - yAxis.get(i).length()) + (yAxis.size()-1 - i));
        System.out.println(yAxis);
        while (lineScan.hasNext()) {
            String number = "";
            for (char c : yAxis.get(index).toCharArray()) {
                number += emojiMap.get(Integer.parseInt("" + c));
            }
            String prepend = String.format("%s%s", number, separator);
            String newLine = prepend + lineScan.nextLine() + "\n";
            result += newLine;
            index++;
        }
        lineScan.close();

        // x-axis
        // axis line
        int offset = maxYCoordDigits + 1;
        result += Strings.repeat(separator, game.WIDTH + offset) + "\n";

        List<String> xAxis = new ArrayList<>();
        for (int i = 0; i < game.WIDTH; i++) xAxis.add("" + i);
        int maxXCoordDigits = ((game.WIDTH - 1) + "").length();
        for (int i = 0; i < game.WIDTH; i++) xAxis.set(i, Strings.repeat("0", maxXCoordDigits - xAxis.get(i).length()) + i);
        for (int i = 0; i < maxXCoordDigits; i++) {
            String nextLine = Strings.repeat(separator, offset);
            for (String num : xAxis) {
                nextLine += "" + emojiMap.get(Integer.parseInt("" + num.charAt(i)));
            }
            result += nextLine + "\n";
        }
        // There seems to be an emoji limit per message which is cutting
        // off large boards. We can circumvent this by encasing our board
        // in code with "```".
        return String.format("```%n%s%n```", result);
    }

    private String reverseBoard(MessageChannel channel) {
        MineSweeperGame game = gameMap.get(channel);
        String reverse = "";
        Scanner lineScan = new Scanner(game.toString());
        while (lineScan.hasNext()) {
            reverse = String.format("%n%s%s", lineScan.nextLine(), reverse);
        }
        return reverse.trim();
    }

    private String getEmojiRepresentation(int n) {
        String numberAsString = "" + n;
        List<String> numberCharactersList = new ArrayList<>();
        for (char c : numberAsString.toCharArray()) numberCharactersList.add("" + c);
        List<String> numberAsEmojiList = numberCharactersList.stream()
                .map(Integer::parseInt)
                .map(integer -> emojiMap.get(integer))
                .collect(Collectors.toList());
        String result = "";
        for (String s : numberAsEmojiList) {
            result += s;
        }
        return result;
    }

    private Map<Integer, String> emojiMap = Map.of(
        0, "0️⃣",
        1, "1️⃣",
        2, "2️⃣",
        3, "3️⃣",
        4, "4️⃣",
        5, "5️⃣",
        6, "6️⃣",
        7, "7️⃣",
        8, "8️⃣",
        9, "9️⃣"
    );
}
