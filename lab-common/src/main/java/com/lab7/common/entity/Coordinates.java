package com.lab7.common.entity;

import java.io.Serializable;

/**
 * Coordinates of Route represented by x, y coordinates
 */
public class Coordinates implements Serializable {
    private final int x; //Максимальное значение поля: 412
    private final Long y; //Поле не может быть null

    public Coordinates(int x, Long y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * @return y coordinate
     */
    public Long getY() {
        return y;
    }

    /**
     * @return coordinates represented by beautiful string
     */
    @Override
    public String toString() {
        return "{" + x + ", " + y + "}";
    }
}
