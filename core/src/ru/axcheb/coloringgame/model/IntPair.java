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

    public IntPair north() {
        return new IntPair(x, y + 1);
    }

    public IntPair northEast() {
        return new IntPair(x + 1, y + 1);
    }

    public IntPair east() {
        return new IntPair(x + 1, y);
    }

    public IntPair southEast() {
        return new IntPair(x + 1, y - 1);
    }

    public IntPair south() {
        return new IntPair(x, y - 1);
    }

    public IntPair southWest() {
        return new IntPair(x - 1, y -1);
    }

    public IntPair west() {
        return new IntPair(x - 1, y);
    }

    public IntPair northWest() {
        return new IntPair(x - 1, y + 1);
    }

    public IntPair[] nearby() {
        return new IntPair[] {north(), northEast(), east(), southEast(), south(), southWest(), west(), northWest()};
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
