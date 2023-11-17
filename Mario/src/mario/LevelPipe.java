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
public class LevelPipe extends LevelBlock {

    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 4;
    public static final int LEFT = 7;

    private int direction;
    private int length;

    private int pipeNumber; // rörets nummer
    private int destination; // -1 om den inte leder någonstans. 0 om den leder till början. Annars numret på pipen den leder till.
    private int destinationWorld; // den värld dit röret leder

    public LevelPipe(int x, int y, int length, int direction) {
        super(x, y);
        this.direction = direction;
        this.destination = -1;
        this.destinationWorld = 0;
        this.pipeNumber = -1;
        this.length = length;
        init(length);
    }
    
    public LevelPipe(int x, int y, int length, int direction, int pipeNumber) {
        super(x, y);
        this.direction = direction;
        this.destination = -1;
        this.destinationWorld = 0;
        this.pipeNumber = pipeNumber;
        this.length = length;
        init(length);
    }

    public LevelPipe(int x, int y, int length, int direction, int pipeNumber, int destination, int destinationWorld) {
        super(x, y);
        this.direction = direction;
        this.destination = destination;
        this.destinationWorld = destinationWorld;
        this.pipeNumber = pipeNumber;
        this.length = length;
        init(length);
    }

    /**
     * skapar rörets bredd och höjd utifrån dess längd och vilket håll det pekar åt
     * @param length 
     */
    private void init(int length) {
        if (direction == LEFT || direction == RIGHT) {
            this.x += length * Game.BLOCK_SIZE;
            width = length * Game.BLOCK_SIZE;
            height = 2 * Game.BLOCK_SIZE;
        } else {
            this.y -= length * Game.BLOCK_SIZE;
            width = 2 * Game.BLOCK_SIZE;
            height = length * Game.BLOCK_SIZE;
        }
    }

    public int getDirection() {
        return direction;
    }

    public int getDestinationWorld() {
        return destinationWorld;
    }

    public int getDestination() {
        return destination;
    }

    public boolean hasDestination() {
        return destination != -1;
    }

    public int getPipeNumber() {
        return pipeNumber;
    }

    public int getLength() {
        return length;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(0, 168, 0));
        g.fillRect(x + Game.scrollX, y, width, height);
    }

}
