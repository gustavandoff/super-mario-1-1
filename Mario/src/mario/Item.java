/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mario;

import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author gustav.andoff
 */
public abstract class Item extends Entity {

    public Item(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    /**
     * När ett item plockas upp ska det försvinna
     */
    public void collect() {
        existing = false;
    }

    public abstract void start();

    public abstract void draw(Graphics g);
}
