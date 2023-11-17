/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mario;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author gusta
 */
public class EnemyKoopaTroopa extends Enemy { // denna fiende blev aldrig klar och finns inte i spelet någonstans

    private boolean shell;
    private boolean shellGliding;

    private double glideSpeed;
    private double walkSpeed;

    private Timer shellTimer;

    public EnemyKoopaTroopa(double x, double y) {
        super(x, y, Game.BLOCK_SIZE * 0.9, Game.BLOCK_SIZE * 0.9);
        glideSpeed = 7;
        walkSpeed = xSpeed;

        shellTimer = new Timer(500000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loseShell();
            }
        });
    }

    public boolean isShell() {
        return shell;
    }
    
    public boolean isShellGliding() {
        return shellGliding;
    }

    private void becomeShell() {
        System.out.println("becomeShell");
        shellTimer.start();
        shell = true;
        xDirection = 0;
        xSpeed = 0;
        shellGliding = false;
        damagePlayer = false;
    }

    private void loseShell() {
        System.out.println("loseShell");
        shell = false;
        damagePlayer = true;
        xDirection = 1;
        xSpeed = walkSpeed;
    }

    public void startGlide(int xDirection) {
        System.out.println("startGlide: " + xDirection);
        shellTimer.stop();
        damagePlayer = true;
        this.xDirection = xDirection;
        xSpeed = glideSpeed;
        shellGliding = true;
    }

    public void stopGlide() {
        System.out.println("stopGlide");
        becomeShell();
    }

    /**
     * Det som sker när spelaren hoppar på koopa troopan
     * @param playerX spelarens x-värde
     * @param playerWidth spelarens bredd
     */
    @Override
    public void stomped(int playerX, int playerWidth) { 
        if (!shell) {
            System.out.println("stomp !shell");
            becomeShell();
        } else if (!shellGliding) {
            System.out.println("stomp !shellGliding");
            int thisCenter = x - width / 2;
            int playerCenter = playerX - playerWidth / 2;

            int normalizedDistance = (thisCenter - playerCenter != 0)
                    ? Math.abs(thisCenter - playerCenter) / (thisCenter - playerCenter)
                    : 1;

            startGlide(normalizedDistance); // om man hoppar på ena sidan av ett skal ska den glida åt motsatt rikning
        } else {
            System.out.println("stomp else");
            stopGlide();
        }

    }

    @Override
    public void die() {
        dead = true;
        existing = false;
    }

    @Override
    protected boolean isColliding(int x, int y) {
        for (LevelBlock block : Game.getLevelBlocks()) {
            if (block.isCollided(x, y, width, height)) {
                if (shellGliding && block instanceof LevelBrick) { // om skalet glider in i en brick ska den gå sönder
                    ((LevelBrick) block).breakBlock();
                }
                return true;
            }
        }

        for (Enemy enemy : Game.getEnemies()) {
            if (!enemy.equals(this) && enemy.isExisting() && enemy.isCollided(x, y, width, height)) {
                if (shellGliding) { // om skalet glider in i en enemy ska den dö
                    enemy.die();
                } else {
                    enemy.changeDirection();
                    return true;
                }
            }
        }

        return false;
    }

    @Override
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

    @Override
    public void draw(Graphics g) {
        if (existing) {
            g.setColor(new Color(0, 168, 0));
            g.fillRect(x + Game.scrollX, y, width, height);
        }
    }
}
