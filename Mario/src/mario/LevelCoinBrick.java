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
public class LevelCoinBrick extends LevelBlock {

    private boolean empty = false;
    private Timer coinTimer;

    public LevelCoinBrick(int x, int y) {
        super(x, y, Game.COIN);

        coinTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                empty = true; // 2000 millisekunder efter att spelaren hoppat in i blocket ska blocket bli tomt på pengar
            }
        });
    }

    @Override
    public void punched(boolean playerIsBig) {
        coinTimer.start(); // när spelaren hoppar in i detta block ska en timer startas
        if (!empty) { // tills att timern är klar kommer spelaren kunna fortsätta hoppa på blocket och få en peng varje gång
            super.punched(playerIsBig);
        }
    }

    @Override
    public boolean isCollided(int x, int y, int width, int height) {
        return super.isCollided(x, y, width, height);
    }

    public void draw(Graphics g) {
        if (!empty) {
            g.setColor(new Color(200, 76, 12));
            g.fillRect(x + Game.scrollX, shownY, Game.BLOCK_SIZE, Game.BLOCK_SIZE);
        } else {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(x + Game.scrollX, shownY, Game.BLOCK_SIZE, Game.BLOCK_SIZE);
        }
    }
}
