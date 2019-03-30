package ru.axcheb.coloringgame.model;

import java.util.Objects;

public class IntPair {

    private int x;
    private int y;

    public IntPair(int x, int y) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntPair intPair = (IntPair) o;
        return x == intPair.x &&
                y == intPair.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
