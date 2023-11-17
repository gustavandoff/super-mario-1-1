/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mario;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author gustav.andoff
 */
public class LevelBrick extends LevelBlock {

    private boolean existing;

    public LevelBrick(int x, int y) {
        super(x, y);
        existing = true;
    }

    public LevelBrick(int x, int y, int item) {
        super(x, y, item);
        existing = true;
    }

    public void breakBlock() {
        existing = false;
    }

    @Override
    public void punched(boolean playerIsBig) {
        if (playerIsBig) { // om spelaren hoppar in i blocket underifrån ska det gå sönder om spelaren är stor, annars ska det bara studsa
            breakBlock();
        }
        super.punched(playerIsBig);
    }

    @Override
    public boolean isCollided(int x, int y, int width, int height) {
        if (!existing) {
            return false;
        }
        return super.isCollided(x, y, width, height);
    }

    public void draw(Graphics g) {
        if (existing) {
            g.setColor(new Color(200, 76, 12));
            g.fillRect(x + Game.scrollX, shownY, Game.BLOCK_SIZE, Game.BLOCK_SIZE);
        }
    }
}
