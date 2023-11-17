/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mario;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 *
 * @author gusta
 */
public class SidePanel extends JPanel {

    private static JButton btnRestart, btnSaveScore, btnUpdateScoreboard;
    private static JTextField txfName;
    private static JLabel lblTimer;
    private static JTextArea txaScoreboard;

    private ServerCommunicator sc;

    private Game game;

    public SidePanel(Game game) {
        this.game = game;
        sc = new ServerCommunicator();

        initComponents();
    }

    private void initComponents() {

        GridBagLayout m = new GridBagLayout();
        GridBagConstraints con;
        setLayout(m);

        con = new GridBagConstraints();
        lblTimer = new JLabel("400");
        lblTimer.setFont(new Font("Arial", Font.PLAIN, 30));
        con.gridx = 0;
        con.gridy = 0;
        con.insets = new Insets(0, 0, 0, 0);
        m.setConstraints(lblTimer, con);
        add(lblTimer);

        con = new GridBagConstraints();
        btnRestart = new JButton("Restart game");
        con.gridx = 0;
        con.gridy = 1;
        con.insets = new Insets(25, 0, 25, 0);
        m.setConstraints(btnRestart, con);
        add(btnRestart);

        con = new GridBagConstraints();
        txfName = new JTextField();
        txfName.setBorder(new TitledBorder("Player name"));
        txfName.setEnabled(false);
        txfName.setFocusable(false);
        txfName.setPreferredSize(new Dimension(100, 40));
        con.gridx = 0;
        con.gridy = 2;
        con.insets = new Insets(25, 0, 5, 0);
        m.setConstraints(txfName, con);
        add(txfName);

        con = new GridBagConstraints();
        btnSaveScore = new JButton("Save score");
        btnSaveScore.setEnabled(false);
        con.gridx = 0;
        con.gridy = 3;
        con.insets = new Insets(5, 0, 25, 0);
        m.setConstraints(btnSaveScore, con);
        add(btnSaveScore);

        con = new GridBagConstraints();
        btnUpdateScoreboard = new JButton("Update scoreboard");
        con.gridx = 0;
        con.gridy = 4;
        con.insets = new Insets(25, 0, 25, 0);
        m.setConstraints(btnUpdateScoreboard, con);
        add(btnUpdateScoreboard);

        con = new GridBagConstraints();
        txaScoreboard = new JTextArea();
        txaScoreboard.setBorder(new TitledBorder("Scoreboard"));
        txaScoreboard.setPreferredSize(new Dimension(150, 165));
        txaScoreboard.setFocusable(false);
        txaScoreboard.setEditable(false);
        con.gridx = 0;
        con.gridy = 5;
        con.insets = new Insets(25, 0, 25, 0);
        m.setConstraints(txaScoreboard, con);
        add(txaScoreboard);

        btnRestart.setFocusable(false);
        btnSaveScore.setFocusable(false);
        btnUpdateScoreboard.setFocusable(false);

        btnRestart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.restartGame();
            }
        });

        btnSaveScore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sc.sendScore(lblTimer.getText(), txfName.getText());
                    btnSaveScore.setEnabled(false);
                    txfName.setEnabled(false);
                    txfName.setFocusable(false);
                } catch (UnknownHostException ex) {
                    System.out.println("Felaktig adress");
                }
            }
        });

        btnUpdateScoreboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sc.getScoreboard();
                } catch (UnknownHostException ex) {
                    System.out.println("Felaktig adress");
                }
            }
        });

        txfName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnSaveScore.setEnabled(txfName.getText().length() > 0);
            }
        });
    }

    public static void setTimer(String time) {
        lblTimer.setText(time);
    }

    public static void setScoreboard(String sb) {
        txaScoreboard.setText(sb);
    }

    public static void setSaveScoreEnabled(boolean enabled) {
        btnSaveScore.setEnabled(enabled);
    }

    public static void setNameFieldEnabled(boolean enabled) {
        txfName.setEnabled(enabled);
        txfName.setFocusable(enabled);
    }
}
