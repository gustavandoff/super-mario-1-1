/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mario;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author gustav.andoff
 */
public class LevelQuestion extends LevelBlock {
    
    private Color color = new Color(252,152,56);
    private boolean hit; // om spelaren har hoppat in i blocket underifrån eller inte
    
    public LevelQuestion(int x, int y, int item) {
        super(x, y, item);
        hit = false;
    }
    
    public LevelQuestion(int x, int y) {
        super(x, y, Game.COIN);
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
