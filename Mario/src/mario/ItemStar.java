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
public class ItemStar extends Item {

    private double ySpeed;
    private int xSpeed;
    private int xDirection;
    private int jumpSpeed;

    protected boolean grounded;

    public ItemStar(double x, double y) {
        super(x, y, (Game.BLOCK_SIZE * 0.5), (Game.BLOCK_SIZE * 0.5));
        xDirection = 1;
        xSpeed = 2;
        jumpSpeed = 7;
        grounded = true;
    }

    @Override
    public void start() {
        existing = true;
        y -= Game.BLOCK_SIZE;
    }

    @Override
    public void blockHit(int foreignX, int foreignWidth) {
    }

    private boolean isColliding(int x, int y) {
        for (LevelBlock block : Game.getLevelBlocks()) {
            if (block instanceof LevelGroundBlock && block.isCollided(x, y, width, height)) {
                return true;
            }
        }

        return false;
    }

    public void update() {
        x += xDirection * xSpeed;
        if (isColliding(x, y)) {// om stjärnan kolliderat i x-led
            while (isColliding(x, y)) { // stjärnan förflyttas i motsatt rikning den går tills att den inte längre kolliderar
                x -= xDirection; // stjärnan förflyttas i motsatt rikning den går tills att den inte längre kolliderar
            }
            xDirection *= -1; // skiftar mellan positivt och negativt
            x += xDirection * xSpeed; // flyttas ut ett steg från vägen annars teleporteras den höst upp på det den kolliderade med
        }

        if (grounded) { // om den inte står på marken
            ySpeed = jumpSpeed;
        } else {
            ySpeed += Game.GRAVITY * 0.6; // ökar hastigheten med gravitationen
            y -= ySpeed;
        }

        if (isColliding(x, y)) { // om stjärnan kolliderat i y-led
            while (isColliding(x, y)) { // stjärnan förflyttas i motsatt rikning den rör sig tills att den inte längre kolliderar
                y += ySpeed / Math.abs(ySpeed);
            }
            grounded = ySpeed <= 0;
            ySpeed = 0;
        } else {
            grounded = false;
        }
    }

    @Override
    public void draw(Graphics g) {
        if (existing) {
            g.setColor(Color.yellow);
            g.fillRect(x + width / 2 + Game.scrollX, y, width, height);
        }
    }
}
