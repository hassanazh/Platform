package com.platform.service;

import java.util.ArrayList;

public class Position {
    private int row;
    private int column;
    private ArrayList<String> path = new ArrayList<>();

    Position(int column, int row) {
        this.column = column;
        this.row = row;
        path.add(this.toString());
    }

    int getRow() {
        return row;
    }

    void setRow(int row) {
        this.row = row;
    }

    int getColumn() {
        return column;
    }

    void setColumn(int column) {
        this.column = column;
    }

    ArrayList<String> getPath() {
        return path;
    }

    void addPosition() {
        path.add(this.toString());
    }

    @Override
    public String toString() {
        return "" +
                column +
                row;
    }
}
