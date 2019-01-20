package graphics;

import java.awt.*;

import logic.Entity;
import logic.Vector;


public class Wall extends Entity {

    protected int x;
    protected int y;
    protected int side1;
    protected int side2;
    private Color color;

    public Wall(int x, int y, int side1, int side2, Color color) {
        this.center = new Vector(x, y);
        this.side1 = side1;
        this.side2 = side2;
        this.radius = side1;
        this.color = color;
    }
}