package com.ogsupersand;

public enum Action {
    START_GAME("minesweeper"),
    UNCOVER("dig"),
    FLAG("flag"),
    UNFLAG("unflag"),
    CHORD("chord");

    private String action;

    private Action(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return action;
    }
}
