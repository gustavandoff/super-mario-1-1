/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mario;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author gustav.andoff
 */
public class Window extends JFrame {

    private Game game;

    private static SidePanel sidePanel;

    public Window() {
        game = new Game();
        sidePanel = new SidePanel(game);
        initComponents();
    }

    public static void setLblTimer(String time) {
        sidePanel.setTimer(time);
    }

    private void initComponents() {
        GridBagLayout m;
        GridBagConstraints con;
                
        this.setPreferredSize(new Dimension((int)(Game.BLOCK_SIZE * 17 + Game.BLOCK_SIZE * 10), (int) (Game.BLOCK_SIZE * 14.5)));
        this.setResizable(false);
        
        game.setBackground(new Color(92, 148, 252));

        m = new GridBagLayout();
        this.setLayout(m);

        con = new GridBagConstraints();
        con.gridx = 0;
        con.gridy = 0;
        con.weighty = 1;
        con.weightx = 2;
        con.fill = GridBagConstraints.BOTH;
        m.setConstraints(game, con);
        add(game);
        
        con = new GridBagConstraints();
        con.gridx = 1;
        con.gridy = 0;
        con.gridheight = 1;
        con.weightx = 1;
        con.fill = GridBagConstraints.BOTH;
        m.setConstraints(sidePanel, con);
        add(sidePanel);

        pack();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
