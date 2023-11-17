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
public class ItemBlockCoin extends Item implements Runnable {

    public ItemBlockCoin(double x, double y) {
        super(x + 0.4, y, (Game.BLOCK_SIZE * 0.2), (Game.BLOCK_SIZE * 0.5));
    }

    @Override
    public void start() {
        existing = true;
        
        t = new Thread(this);
        t.start();
    }

    @Override
    public void blockHit(int foreignX, int foreignWidth) {
    }

    /**
     * när detta item skapas ska de flyga upp i luften och falla ner igen
     */
    @Override
    public void run() {
        double ySpeed = 12; // mynten får en ursprungs hastighet i y-led
        int finishY = y - (int)(Game.BLOCK_SIZE); // det y-värde dit myntet ska falla ner till

        while (!Thread.interrupted()) {
            try {
                ySpeed += Game.GRAVITY; // hastigheten minskar med gravitationen
                y -= ySpeed; // myntets y-värde förändras med hastigheten

                if (ySpeed < 0 && y > finishY) { // när myntet har fallit ner till y-värdet finishY ska det försvinna
                    y = finishY;
                    t.interrupt();
                }

                Thread.sleep(Game.THREAD_INTERVAL);
            } catch (InterruptedException ex) {
                existing = false;
                break;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        if (existing) {
            g.setColor(Color.orange);
            g.fillRect(x + Game.scrollX, y, width, height);
        }
    }
}
