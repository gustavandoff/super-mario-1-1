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
public class LevelItemBrick extends LevelBlock {
    
    private Color color = new Color(200, 76, 12);
    private boolean hit; // om spelaren har hoppat in i blocket underifrån eller inte
    
    public LevelItemBrick(int x, int y, int item) {
        super(x, y, item);
        hit = false;
    }
    
    @Override
    public void punched(boolean playerIsBig) {
        if (!hit) { // om spelaren inte redan har hoppat in i blocket
            hit = true;
            this.playerIsBig = playerIsBig;
            super.punched(playerIsBig); // ska punched-metoden anropas hos blocket
            color = Color.LIGHT_GRAY;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x + Game.scrollX, shownY, Game.BLOCK_SIZE, Game.BLOCK_SIZE);
    }
}
