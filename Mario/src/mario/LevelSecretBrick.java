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
public class LevelSecretBrick extends LevelBlock {

    private boolean hit;

    public LevelSecretBrick(int x, int y, int item) {
        super(x, y, item);
        hit = false;
    }

    @Override
    public void punched(boolean playerIsBig) {
        if (!hit) { // om spelaren inte redan har hoppat in i blocket
            hit = true;
            super.punched(playerIsBig); // ska punched-metoden anropas hos blocket
        }
    }

    public void draw(Graphics g) {
        if (hit) {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(x + Game.scrollX, shownY, Game.BLOCK_SIZE, Game.BLOCK_SIZE);
        }

    }
}
