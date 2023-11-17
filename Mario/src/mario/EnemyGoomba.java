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
public class EnemyGoomba extends Enemy {

    public EnemyGoomba(double x, double y) {
        super(x, y, Game.BLOCK_SIZE, Game.BLOCK_SIZE);
    }
    
    public EnemyGoomba(double x, double y, Enemy companion) {
        super(x, y, Game.BLOCK_SIZE, Game.BLOCK_SIZE, companion);
    }

    /**
     * Det som sker när spelaren hoppar på goomban
     * @param playerX spelarens x-värde
     * @param playerWidth spelarens bredd
     */
    @Override
    public void stomped(int playerX, int playerWidth) {
        dead = true;
        existing = false;
    }

    @Override
    public void die() {
        dead = true;
        existing = false;
    }

    @Override
    public void draw(Graphics g) {
        if (existing) {
            g.setColor(new Color(200, 76, 12));
            g.fillRect(x + Game.scrollX, y, width, height);
        }
    }
}
