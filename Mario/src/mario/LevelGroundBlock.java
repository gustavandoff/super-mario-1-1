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
public class LevelGroundBlock extends LevelBlock {

    public LevelGroundBlock(int x, int y) {
        super(x, y);
    }

    public void draw(Graphics g) {
        g.setColor(new Color(200, 76, 12));
        g.fillRect(x + Game.scrollX, y, Game.BLOCK_SIZE, Game.BLOCK_SIZE);
        
        g.setColor(Color.black);
        g.drawRect(x + Game.scrollX, y, Game.BLOCK_SIZE, Game.BLOCK_SIZE);

    }

}
