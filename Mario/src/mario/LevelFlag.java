/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mario;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

/**
 *
 * @author gusta
 */
public class LevelFlag extends Collider implements Runnable { // ritar endast ut en flagga i slutet av banan

    private int x, y, width, height;

    public LevelFlag(int x) {
        this.x = x * Game.BLOCK_SIZE;
        this.y = Game.BLOCK_SIZE * 2;
        this.width = (int) (Game.BLOCK_SIZE * 0.2);
        this.height = Game.BLOCK_SIZE * 9;
    }

    public boolean isCollided(int x, int y, int width, int height) {
        return super.isCollided(x, (int) (this.x + Game.BLOCK_SIZE * 0.4), y, this.y, width, this.width, height, this.height);
    }

    public void draw(Graphics g) {
        // ritar olika rekanglar och polygoner för att det ska se ut som en flagga
        g.setColor(new Color(124, 202, 40));
        g.fillRect(
                (int) (x + Game.scrollX + Game.BLOCK_SIZE * 0.4),
                y,
                width,
                height
        );

        int[] flagXPoints = {
            (int) (x + Game.scrollX + Game.BLOCK_SIZE * 0.4),
            (int) (x + Game.scrollX - Game.BLOCK_SIZE * 0.6),
            (int) (x + Game.scrollX + Game.BLOCK_SIZE * 0.4)
        };
        int[] flagYPoints = {
            Game.BLOCK_SIZE * 2,
            Game.BLOCK_SIZE * 2,
            Game.BLOCK_SIZE * 3
        };

        g.setColor(Color.white);
        g.fillPolygon(flagXPoints, flagYPoints, 3);

        g.setColor(Color.black);
        g.fillOval(
                (int) (x + Game.scrollX + Game.BLOCK_SIZE * 0.25),
                (int) (Game.BLOCK_SIZE * 1.5),
                (int) (Game.BLOCK_SIZE * 0.5),
                (int) (Game.BLOCK_SIZE * 0.5)
        );

        g.setColor(new Color(0, 168, 0));
        g.fillOval(
                (int) (x + Game.scrollX + Game.BLOCK_SIZE * 0.3),
                (int) (Game.BLOCK_SIZE * 1.55),
                (int) (Game.BLOCK_SIZE * 0.4),
                (int) (Game.BLOCK_SIZE * 0.4)
        );
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
