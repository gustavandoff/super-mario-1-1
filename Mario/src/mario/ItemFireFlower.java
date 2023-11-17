/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mario;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author gusta
 */
public class ItemFireFlower extends Item { // en eldblomma som spelaren kan plocka upp. I denna version kan man inte kasta eldbollar

    public ItemFireFlower(double x, double y) {
        super(x, y, (Game.BLOCK_SIZE * 0.5), (Game.BLOCK_SIZE * 0.5));
    }

    @Override
    public void start() {
        existing = true;
        y -= Game.BLOCK_SIZE;
    }

    @Override
    public void blockHit(int foreignX, int foreignWidth) {
    }

    @Override
    public void draw(Graphics g) {
        if (existing) {
            g.setColor(Color.green);
            g.fillRect(x + width / 2 + Game.scrollX, y, width, height);
        }
    }
}
