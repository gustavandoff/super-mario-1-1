/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mario;

import java.awt.Graphics;
import java.time.Instant;

/**
 *
 * @author gusta
 */
public abstract class Enemy extends Entity {

    protected double ySpeed;
    protected double xSpeed;
    protected int xDirection;

    protected boolean grounded;
    protected boolean dead;
    protected boolean damagePlayer;

    private Enemy companion; // på vissa ställen finns två stycken fiender bredvid varandra och de ska spawna tillsammans

    public Enemy(double x, double y, double width, double height) {
        super(x, y, width, height);
        xDirection = -1;
        xSpeed = 1.2;
        grounded = true;
        dead = false;
        damagePlayer = true;
    }

    public Enemy(double x, double y, double width, double height, Enemy companion) {
        super(x, y, width, height);
        this.companion = companion;
        xDirection = -1;
        xSpeed = 1.2;
        grounded = true;
        dead = false;
        damagePlayer = true;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean canDamagePlayer() {
        return damagePlayer;
    }

    public void start() {
        existing = true;
        if (companion != null) {
            companion.start();
        }
    }

    public void changeDirection() { // skiftar mellan positivt och negativt
        xDirection *= -1;
    }

    protected boolean isColliding(int x, int y) {
        for (LevelBlock block : Game.getLevelBlocks()) {
            if (block.isCollided(x, y, width, height)) {
                return true;
            }
        }

        for (Enemy enemy : Game.getEnemies()) {
            if (!enemy.equals(this) && enemy.isExisting() && enemy.isCollided(x, y, width, height)) {
                enemy.changeDirection(); // när en fiende har gått in i en annan fiende ska den byta riktning
                return true;
            }
        }

        return false;
    }

    @Override
    public void blockHit(int blockX, int blockWidth) {
        if (!dead) {
            int thisDistanceToXCenter = x - width / 2;
            int blockDistanceToXCenter = blockX - blockWidth / 2;

            int normalizedDistance = (thisDistanceToXCenter - blockDistanceToXCenter != 0)
                    ? Math.abs(thisDistanceToXCenter - blockDistanceToXCenter) / (thisDistanceToXCenter - blockDistanceToXCenter)
                    : xDirection;

            // 1 om enemyn är på höger sida om foreign. -1 om till vänster
            int blockSide = normalizedDistance;
            xDirection = blockSide; // om man slår ett block ska enemyn åka åt det håll som är närmast blockets kant
            ySpeed = 10;
            dead = true;
        }

    }

    public void update() {
        if (!dead && existing) { // normala tillståndet
            x += xDirection * xSpeed;
            if (isColliding(x, y)) { // om den kolliderat i x-led
                while (isColliding(x, y)) { //förflyttas i motsatt rikning den går tills att den inte längre kolliderar
                    x -= xDirection; // förflyttas i motsatt rikning den går tills att den inte längre kolliderar
                }
                changeDirection();
                x += 2 * xDirection * xSpeed; // flyttas ut ett steg från vägen annars teleporteras den höst upp på det den kolliderade med
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
        } else if (!dead && !existing && -Game.scrollX + Game.BLOCK_SIZE * 16 >= this.x) { // om den precis kommer innanför skärmen
            start();
        } else if (dead && existing) { // om den precis dött men fortfarande ska synas på skärmen
            ySpeed += Game.GRAVITY; // ökar hastigheten med gravitationen
            y -= ySpeed;
            x += xDirection * xSpeed;
        }

        if (y > (int) (Game.BLOCK_SIZE * 14.5) || x < -width) { // om den ramlar ner från kartan eller går för långt åt vänster
            existing = false;
        }
    }

    public abstract void stomped(int playerX, int playerWidth);

    public abstract void die();

    public abstract void draw(Graphics g);
}
