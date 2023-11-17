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
public class ItemOneUpMushroom extends ItemMushroom {

    public ItemOneUpMushroom(double x, double y) {
        super(x, y, new Color(0, 168, 0));
        xDirection = 1;
        xSpeed = 2;
        grounded = true;
    }
}
