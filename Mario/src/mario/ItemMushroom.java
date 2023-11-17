/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mario;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author gustav.andoff
 */
public abstract class ItemMushroom extends Item {  // superklassen till de två olika svampar som finns i spelet

    protected double ySpeed;
    protected int xSpeed;
    protected int xDirection;
    
    private Color color;

    protected boolean grounded;

    public ItemMushroom(double x, double y, Color color) {
        super(x, y, Game.BLOCK_SIZE * 0.7, Game.BLOCK_SIZE);
        this.color = color;
        xDirection = 1;
        xSpeed = 2;
        grounded = true;
    }

    @Override
    public void start() {
        existing = true;
        y -= Game.BLOCK_SIZE;
    }

    @Override
    public void blockHit(int blockX, int blockWidth) {
        int thisDistanceToXCenter = x - width / 2;
        int blockDistanceToXCenter = blockX - blockWidth / 2;

        int normalizedDistance = (thisDistanceToXCenter - blockDistanceToXCenter != 0)
                ? Math.abs(thisDistanceToXCenter - blockDistanceToXCenter) / (thisDistanceToXCenter - blockDistanceToXCenter)
                : xDirection;

        // 1 om svampen är på höger sida om foreign. -1 om till vänster
        int blockSide = normalizedDistance;
        xDirection = blockSide; // om man slår ett block ska svampen åka åt det håll som är närmast blockets kant
        ySpeed = 10;
    }

    private boolean isColliding(int x, int y) {
        for (LevelBlock block : Game.getLevelBlocks()) {
            if (block.isCollided(x, y, width, height)) {
                return true;
            }
        }

        return false;
    }

    public void update() {
        x += xDirection * xSpeed;
        if (isColliding(x, y)) {// om den kolliderat i x-led
            while (isColliding(x, y)) { // förflyttas i motsatt rikning den går tills att den inte längre kolliderar
                x -= xDirection; // förflyttas i motsatt rikning den går tills att den inte längre kolliderar
            }
            xDirection *= -1; // skiftar mellan positivt och negativt
            x += xDirection * xSpeed; // flyttas ut ett steg från vägen annars teleporteras den höst upp på det den kolliderade med
        }

        if (!grounded) { // om den inte står på marken
            ySpeed += Game.GRAVITY; // ökar hastigheten med gravitationen
            y -= ySpeed;
        }
        
        if (isColliding(x, y)) { // om den kolliderat i y-led
            while (isColliding(x, y)) { // förflyttas i motsatt rikning den rör sig tills att den inte längre kolliderar
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
            g.setColor(color);
            g.fillRect(x + Game.scrollX, y, width, height);
        }
    }
}
