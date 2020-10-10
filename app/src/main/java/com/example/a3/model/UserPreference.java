package com.example.a3.model;

public class UserPreference {
    private int row;
    private int col;
    private int mine;

    private static UserPreference instance;

    private UserPreference(int row, int col, int mine) {
        this.row = row;
        this.col = col;
        this.mine = mine;
    }

    public static UserPreference getInstance() {
        if (instance == null) {
            instance = new UserPreference(4, 8, 6);
        }
        return instance;
    }
    
    public void resetTable(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void resetMine(int mine) {this.mine = mine;}

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public int getMine() {
        return mine;
    }
}