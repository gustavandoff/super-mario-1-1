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
 * @author gustav.andoff
 */
public class LevelMetalBlock extends LevelBlock {

    public LevelMetalBlock(int x, int y) {
        super(x, y);
    }

    public void draw(Graphics g) {
        g.setColor(new Color(200, 76, 12));
        g.fillRect(x + Game.scrollX, y, Game.BLOCK_SIZE, Game.BLOCK_SIZE);
        
        g.setColor(Color.black);
        g.drawRect(x + Game.scrollX, y, Game.BLOCK_SIZE, Game.BLOCK_SIZE);

    }

}
