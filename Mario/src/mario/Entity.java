/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mario;

/**
 *
 * @author gusta
 */
public abstract class Entity extends Collider { // allt "levande" i spelet är ett Entity (förutom player)

    protected int x, y, width, height;
    protected Thread t;
    protected boolean existing;

    public Entity(double x, double y, double width, double height) {
        this.x = (int) (x * Game.BLOCK_SIZE);
        this.y = (int) (Game.BLOCK_SIZE * (13 - y));
        this.width = (int) width;
        this.height = (int) height;
        existing = false;
    }

    public boolean isExisting() {
        return existing;
    }

    public boolean isCollided(int x, int y, int width, int height) {
        return super.isCollided(x, this.x, y, this.y, width, this.width, height, this.height);
    }

    public abstract void blockHit(int blockX, int blockWidth);

}
