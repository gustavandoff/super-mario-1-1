/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mario;

import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author gustav.andoff
 */
public abstract class LevelBlock extends Collider implements Runnable {

    protected int width, height, x, y, initX, initY, shownY;
    protected int containedItem;

    protected boolean playerIsBig;

    protected Thread t;

    public LevelBlock(int x, int y) {
        this.x = x * Game.BLOCK_SIZE;
        this.y = Game.BLOCK_SIZE * (13 - y);
        shownY = this.y;
        this.width = Game.BLOCK_SIZE;
        this.height = Game.BLOCK_SIZE;

        this.initX = x * Game.BLOCK_SIZE;
        this.initY = y * Game.BLOCK_SIZE;
    }

    public LevelBlock(int x, int y, int item) {
        this.x = x * Game.BLOCK_SIZE;
        this.y = Game.BLOCK_SIZE * (13 - y);
        shownY = this.y;
        this.width = Game.BLOCK_SIZE;
        this.height = Game.BLOCK_SIZE;
        containedItem = item;

        this.initX = x - Game.scrollX;
        this.initY = y;
    }

    /**
     * Om en spelare hoppar på ett block underifrån
     * @param playerIsBig true eller false beroende på om spelaren är stor eller liten
     */
    public void punched(boolean playerIsBig) {
        t = new Thread(this);
        t.start();
    }

    /**
     * Kolla om ett block kolliderar med en fiende eller item
     * @param x blockets x-värde
     * @param y blockets y-värde
     */
    protected void isCollidingWithEntity(int x, int y) {
        ArrayList<Entity> entities = new ArrayList();

        entities.addAll(Game.getItems());
        entities.addAll(Game.getEnemies());

        for (Entity entity : entities) {
            if (entity.isCollided(x, y, width, height)) {
                entity.blockHit(x, width);
            }
        }
    }

    public boolean isCollided(int x, int y, int width, int height) {
        return super.isCollided(x, this.x, y, this.y, width, this.width, height, this.height);
    }

    /**
     * Det item som blocket innehåller skapas
     */
    protected void spawnItem() {
        Item item = new ItemBlockCoin(initX, initY); // som standard innehåller blocket en peng
        switch (containedItem) { // om containedItem innehåller något sätts variabeln item om till det itemet
            case (Game.UPGRADE):
                if (playerIsBig) {
                    item = new ItemFireFlower(initX, initY);
                } else {
                    item = new ItemMagicMushroom(initX, initY);
                }
                break;
            case (Game.ONE_UP_MUSHROOM):
                item = new ItemOneUpMushroom(initX, initY);
                break;
            case (Game.STAR):
                item = new ItemStar(initX, initY);
                break;
        }
        item.start();
        Game.addItem(item); // itemet läggs till i spelet för att det ska ritas ut och kunn kollideras med
    }

    @Override
    public void run() { // när tråden startas har spelaren hoppat på blocket underifrån och det ska studsa
        double ySpeed = 5; // blocket får en hastighet i y-led
        isCollidingWithEntity(x, (int) (y - Game.BLOCK_SIZE * 0.25)); // om blocket studsar upp i en fiende eller item ska de interagera 

        while (!Thread.interrupted()) {
            try {
                ySpeed += Game.GRAVITY; // hastigheten minsar med gravitationen
                shownY -= ySpeed; // blockets y värde som det visas som ändras med hastigheten

                if (shownY > y) { // när det y-värdet vid studsen är till baka till det y-värdet var från början ska tråden sluta
                    shownY = y;
                    t.interrupt();
                }

                Thread.sleep(Game.THREAD_INTERVAL);
            } catch (InterruptedException ex) {
                spawnItem(); // efter blocket studsat ska blockets item skapas
                break;
            }
        }
    }

    public abstract void draw(Graphics g);
}
