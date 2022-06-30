package com.ogsupersand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.awt.Point;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class MineSweeperGame {
    protected BiMap<Point, Tile> board;
    private Set<Tile> allTiles;
    private Set<Tile> mines;
    private boolean isGameRunning;
    private static final int NUM_MINES = 10;
    protected static final int HEIGHT = 9;
    protected static final int WIDTH = 9;

    private int uncoveredTiles;

    public MineSweeperGame() {

    }

    public void startNewGame() {
        board = HashBiMap.create();
        allTiles = new HashSet<>();
        mines = new HashSet<>();
        isGameRunning = true;
        uncoveredTiles = 0;

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Point p = new Point(x, y);
                Tile t = new Tile();
                allTiles.add(t);
                board.put(p, t);
            }
        }
        // hopefully shallow copy?
        List<Tile> toBeMines = new ArrayList<>(allTiles);
        Collections.shuffle(toBeMines);
        for (int i = 0; i < NUM_MINES; i++) {
            Tile t = toBeMines.get(i);
            Point p = board.inverse().get(t);
            t.setType(TileType.MINE);
            mines.add(t);
            board.put(p, t);
        }

        board.keySet().stream().forEach(point -> reevaluateTile(point));
    }

    public boolean isGameWon() {
        return uncoveredTiles >= HEIGHT * WIDTH - NUM_MINES;
    }

    private void reevaluateTile(Point p) {
        Tile t = board.get(p);
        if (t == null || t.isMine()) return;

        int numSurroundingMines = getNeighboringMines(p).size();
        if (numSurroundingMines > 0) {
            // make this a map later or something...
            switch (numSurroundingMines) {
                case 1: t.setType(TileType.ONE);
                        break;
                case 2: t.setType(TileType.TWO);
                        break;
                case 3: t.setType(TileType.THREE);
                        break;
                case 4: t.setType(TileType.FOUR);
                        break;
                case 5: t.setType(TileType.FIVE);
                        break;
                case 6: t.setType(TileType.SIX);
                        break;
                case 7: t.setType(TileType.SEVEN);
                        break;
                case 8: t.setType(TileType.EIGHT);
                        break;
            }
        }
        board.put(p, t);
    }

    /**
     * Uncovers a coordinate on the board
     * @param p coord to uncover
     * @return whether a mine was uncovered
     */
    public boolean uncover(Point p) {
        if (!isGameRunning()) {
            // throw new exception
        }
        Tile t = board.get(p);
        if (t == null || !t.isCovered() || t.isFlagged()) {
            throw new InvalidMoveException();
        }
        if (t.isMine()) {
            t.setType(TileType.MINE_LOSE);
            lose(p);
            return true;
        }
        uncover(p, new HashSet<>());
        return false;
    }

    private void uncover(Point p, Set<Point> closed) {
        Tile t = board.get(p);
        if (closed.contains(p) || 
            t == null ||
            !t.isCovered ||
            t.isFlagged()) {
            return;
        }
        uncoveredTiles++;
        closed.add(p);
        t.uncover();
        if (t.isEmpty()) {
            getNeighbors(p).stream()
                    .map(point -> board.get(point))
                    .filter(tile -> !tile.isMine() && !tile.isFlagged())
                    .map(tile -> board.inverse().get(tile))
                    .forEach(point -> uncover(point, closed));
        }
    }

    public void chord(Point p) {
        Tile t = board.get(p);
        if (t == null || t.isCovered() || 
            !t.isNumber()) {
               throw new InvalidMoveException();
        }
        Set<Point> neighbors = getNeighbors(p);
        int numFlags = neighbors.stream()
                .map(point -> board.get(point))
                .filter(tile -> tile.isFlagged())
                .collect(Collectors.toList())
                .size();
        if (numFlags == t.getValue()) {
            neighbors.stream()
                    .map(point -> board.get(point))
                    .filter(tile -> !tile.isFlagged() && tile.isCovered())
                    .map(tile -> board.inverse().get(tile))
                    .forEach(point -> uncover(point));
        } else {
            throw new InvalidMoveException(String.format("Cannot chord, the space must touch %s flags!", t.getValue()));
        }
    }

    /**
     * Flags mine
     * @param p point to flag
     */
    public void flag(Point p) {
        Tile t = board.get(p);
        if (t == null || t.isFlagged() || !t.isCovered()) {
            throw new InvalidMoveException(String.format("Cannot flag point %s", p));
        } 
        else
        {
            board.get(p).flag();

        }
    }

    /**
     * Unflags mine
     * @param p point to unflag
     */
    public void unflag(Point p) {
        Tile t = board.get(p);
        if (t == null || !t.isFlagged() || !t.isCovered()) {
            throw new InvalidMoveException();
        } 
        else
        {
            board.get(p).unflag();
        }
    }

    public void lose(Point p) {
        lose(Set.of(p));
    }

    public void lose(Set<Point> p) {
        p.stream()
                .map(point -> board.get(point))
                .forEach(tile -> tile.setType(TileType.MINE_LOSE));
        board.keySet().stream()
                .map(point -> board.get(point))
                .forEach(tile -> tile.uncover());
    }

    @Override
    public String toString() {
        if (board == null || !isGameRunning) {
            return "GAME IS NOT STARTED";
        } else {
            StringBuilder result = new StringBuilder();
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    Point p = new Point(x, y);
                    Tile t = board.get(p);
                    result.append(t.isFlagged() ? "🚩" : t.isCovered() ? "🟩" : t.getToken());
                    //result.append(t == null ? "N" : t.isCovered ? "C" : t.isMine() ? "M" : t.getValue());//t.getToken());
                }
                result.append('\n');
            }
            return result.toString();
        }
    }

    private Set<Point> getNeighbors(Point p) {
        Set<Point> neighbors = new HashSet<>();
        Set<Point> possibleNeighbors = Set.of(
            new Point(p.x, p.y - 1),  // up
            new Point(p.x, p.y + 1),  // down
            new Point(p.x - 1, p.y),  // left
            new Point(p.x + 1, p.y),  // right

            new Point(p.x - 1, p.y - 1), // up left
            new Point(p.x + 1, p.y - 1), // up right
            new Point(p.x - 1, p.y + 1), // down left
            new Point(p.x + 1, p.y + 1)  // up right
        );
        for (Point neighbor : possibleNeighbors) {
            if (board.containsKey(neighbor)) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    private Set<Point> getNeighboringMines(Point p) {
        Set<Point> neighbors = getNeighbors(p);
        return neighbors.stream()
                .map(point -> board.get(point))
                .filter(tile -> tile.isMine())
                .map(tile -> board.inverse().get(tile))
                .collect(Collectors.toSet());
    }

    public boolean isGameRunning() {
        return isGameRunning;
    }

    public class Tile {
        private boolean isCovered;
        private boolean isFlagged;
        private TileType type;

        public Tile() {
            this(TileType.EMPTY);
        }

        public Tile(TileType type) {
            isCovered = true;
            isFlagged = false;
            this.type = type;
        }

        public void uncover() {
            isCovered = false;
            if (type == TileType.MINE) {
                type = TileType.MINE_LOSE;
            }
        }

        public void flag() {
            isFlagged = true;
        }

        public void unflag() {
            isFlagged = false;
        }

        public boolean isFlagged() {
            return isFlagged;
        }

        public boolean isCovered() {
            return isCovered;
        }

        public boolean isMine() {
            return type == TileType.MINE || type == TileType.MINE_LOSE;
        }

        public boolean isNumber() {
            return type.isNumber();
        }

        public boolean isEmpty() {
            return type == TileType.EMPTY;
        }

        public int getValue() {
            return type.value;
        }

        public void setType(TileType type) {
            this.type = type;
        }

        public String getToken() {
            return type.getToken();
        }

        @Override
        public String toString() {
            return getToken();
        }
    }

    public enum TileType {
        EMPTY("⬜", false, 0),
        //ONE("1", true, 1),
        ONE("1️⃣", true, 1),
        TWO("2️⃣", true, 2),
        THREE("3️⃣", true, 3),
        FOUR("4️⃣", true, 4),
        FIVE("5️⃣", true, 5),
        SIX("6️⃣", true, 6),
        SEVEN("7️⃣", true, 7),
        EIGHT("8️⃣", true, 8),
        MINE("\uD83D\uDCA3", false, -1),
        MINE_LOSE("💥", false, -1);

        private String token;
        private boolean isNumber;
        private int value;

        private TileType(String token, boolean isNumber, int value) {
            this.token = token;
            this.isNumber = isNumber;
            this.value = value;
        }

        public String getToken() {
            return token;
        }

        public boolean isNumber() {
            return isNumber;
        }

        public int getValue() {
            return value;
        }
    }
}
