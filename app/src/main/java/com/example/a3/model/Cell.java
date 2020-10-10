package com.example.a3.model;

import androidx.annotation.Nullable;

public class Cell {
    private int x;
    private int y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Cell theObj = (Cell) obj;
        if (x != theObj.x)
            return false;
        if (y != theObj.y)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return x * 103 + y * 103;
    }
}
