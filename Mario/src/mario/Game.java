/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mario;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author gustav.andoff
 */
public class Game extends JPanel implements Runnable {

    public static final double GRAVITY = -0.55;
    public static final int BLOCK_SIZE = 40;
    public static final int THREAD_INTERVAL = 10;

    public static final int UPGRADE = 1; // svamp eller blomma
    public static final int ONE_UP_MUSHROOM = 2;
    public static final int COIN = 3;
    public static final int STAR = 4;

    private int currentWorld;

    private static ArrayList<LevelBlock> levelBlocks;
    private static ArrayList<Item> items;
    private static ArrayList<Enemy> enemies;
    private static LevelFlag flag;

    public static int scrollX; // kamerans x-värde när skärmen scrollar åt sidan

    private Timer timer;
    private int second, decimalSecond;
    private String ddMilliSecond, ddSecond;
    private DecimalFormat dFormat = new DecimalFormat("00");

    private Thread gameLoop;

    private Player player;

    public Game() {
        gameLoop = new Thread(this);
        decimalSecond = 0;
        second = 0;

        currentWorld = 111;

        scrollX = 0;

        init111(); // skapar världen

        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case 'a':
                    case 'A':
                        player.setAcceleratingLeft(true);
                        break;
                    case 'd':
                    case 'D':
                        player.setAcceleratingRight(true);
                        break;
                    case ' ':
                        player.startJump();
                        break;
                    case 16:
                        player.setRunning(true);
                        break;
                    case 's':
                    case 'S':
                        player.setHoldingDown(true);
                        break;
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case 'a':
                    case 'A':
                        player.setAcceleratingLeft(false);
                        break;
                    case 'd':
                    case 'D':
                        player.setAcceleratingRight(false);
                        break;
                    case 32:
                        player.endJump();
                        break;
                    case 16:
                        player.setRunning(false);
                        break;
                    case 's':
                    case 'S':
                        player.setHoldingDown(false);
                        break;
                }
            }

        });

        gameLoop.start();

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        timer = new Timer(10, new ActionListener() { // spelets timer som håller koll på hur snabbt man klarar banan
            @Override
            public void actionPerformed(ActionEvent e) {
                decimalSecond++; // hundradels sekunden ökar

                ddMilliSecond = dFormat.format(decimalSecond);
                ddSecond = dFormat.format(second);
                Window.setLblTimer(ddSecond + "." + ddMilliSecond);

                if (decimalSecond >= 100) { // när hundradels sekunden är mer än hundra ska istället sekunden öka och hundradelararna återgår till 0
                    decimalSecond = 0;
                    second++;

                    ddMilliSecond = dFormat.format(decimalSecond);
                    ddSecond = dFormat.format(second);
                    Window.setLblTimer(ddSecond + "." + ddMilliSecond);
                }
            }
        });

        timer.start();
    }

    public void endLevel() {
        if (timer.isRunning()) {
            timer.stop();
            SidePanel.setNameFieldEnabled(true); // gör det möjligt att skriva in sitt namn för att skicka sin tid
        }
    }

    /**
     * skapar värld 1
     */
    private void init111() {
        levelBlocks = new ArrayList();
        items = new ArrayList();
        enemies = new ArrayList();
        player = new Player(3.5, 10, levelBlocks, items, true, this);

        // lägger till marken
        for (int i = 0; i < 69; i++) {
            levelBlocks.add(new LevelGroundBlock(i, 0));
        }
        for (int i = 0; i < 69; i++) {
            levelBlocks.add(new LevelGroundBlock(i, 1));
        }

        for (int i = 71; i < 86; i++) {
            levelBlocks.add(new LevelGroundBlock(i, 0));
        }
        for (int i = 71; i < 86; i++) {
            levelBlocks.add(new LevelGroundBlock(i, 1));
        }

        for (int i = 89; i < 152; i++) {
            levelBlocks.add(new LevelGroundBlock(i, 0));
        }
        for (int i = 89; i < 152; i++) {
            levelBlocks.add(new LevelGroundBlock(i, 1));
        }

        for (int i = 154; i < 207; i++) {
            levelBlocks.add(new LevelGroundBlock(i, 0));
        }
        for (int i = 154; i < 207; i++) {
            levelBlocks.add(new LevelGroundBlock(i, 1));
        }

        // lägger till frågetecknena
        levelBlocks.add(new LevelQuestion(16, 5));
        levelBlocks.add(new LevelQuestion(21, 5, UPGRADE));
        levelBlocks.add(new LevelQuestion(22, 9));
        levelBlocks.add(new LevelQuestion(23, 5));
        levelBlocks.add(new LevelQuestion(78, 5, UPGRADE));
        levelBlocks.add(new LevelQuestion(94, 9));
        levelBlocks.add(new LevelQuestion(106, 5));
        levelBlocks.add(new LevelQuestion(109, 5));
        levelBlocks.add(new LevelQuestion(109, 9, UPGRADE));
        levelBlocks.add(new LevelQuestion(112, 5));
        levelBlocks.add(new LevelQuestion(128, 9));
        levelBlocks.add(new LevelQuestion(129, 9));
        levelBlocks.add(new LevelQuestion(169, 5));

        // lägger till tegelstenarna
        levelBlocks.add(new LevelBrick(20, 5));
        levelBlocks.add(new LevelBrick(22, 5));
        levelBlocks.add(new LevelBrick(24, 5));
        levelBlocks.add(new LevelBrick(77, 5));
        levelBlocks.add(new LevelBrick(79, 5));
        levelBlocks.add(new LevelBrick(80, 9));
        levelBlocks.add(new LevelBrick(81, 9));
        levelBlocks.add(new LevelBrick(82, 9));
        levelBlocks.add(new LevelBrick(83, 9));
        levelBlocks.add(new LevelBrick(84, 9));
        levelBlocks.add(new LevelBrick(85, 9));
        levelBlocks.add(new LevelBrick(86, 9));
        levelBlocks.add(new LevelBrick(87, 9));
        levelBlocks.add(new LevelBrick(91, 9));
        levelBlocks.add(new LevelBrick(92, 9));
        levelBlocks.add(new LevelBrick(93, 9));
        levelBlocks.add(new LevelBrick(100, 5));
        levelBlocks.add(new LevelBrick(118, 5));
        levelBlocks.add(new LevelBrick(121, 9));
        levelBlocks.add(new LevelBrick(122, 9));
        levelBlocks.add(new LevelBrick(123, 9));
        levelBlocks.add(new LevelBrick(127, 9));
        levelBlocks.add(new LevelBrick(128, 5));
        levelBlocks.add(new LevelBrick(129, 5));
        levelBlocks.add(new LevelBrick(130, 9));
        levelBlocks.add(new LevelBrick(167, 5));
        levelBlocks.add(new LevelBrick(168, 5));
        levelBlocks.add(new LevelBrick(170, 5));

        // lägger till rören
        levelBlocks.add(new LevelPipe(28, 1, 2, LevelPipe.UP));
        levelBlocks.add(new LevelPipe(38, 1, 3, LevelPipe.UP));
        levelBlocks.add(new LevelPipe(46, 1, 4, LevelPipe.UP));
        levelBlocks.add(new LevelPipe(57, 1, 4, LevelPipe.UP, 1, 0, 112));
        levelBlocks.add(new LevelPipe(162, 1, 2, LevelPipe.UP, 2));
        levelBlocks.add(new LevelPipe(178, 1, 2, LevelPipe.UP));

        // lägger till metallblocken
        levelBlocks.add(new LevelMetalBlock(133, 2));
        levelBlocks.add(new LevelMetalBlock(134, 2));
        levelBlocks.add(new LevelMetalBlock(134, 3));
        levelBlocks.add(new LevelMetalBlock(135, 2));
        levelBlocks.add(new LevelMetalBlock(135, 3));
        levelBlocks.add(new LevelMetalBlock(135, 4));
        levelBlocks.add(new LevelMetalBlock(136, 2));
        levelBlocks.add(new LevelMetalBlock(136, 3));
        levelBlocks.add(new LevelMetalBlock(136, 4));
        levelBlocks.add(new LevelMetalBlock(136, 5));
        levelBlocks.add(new LevelMetalBlock(139, 2));
        levelBlocks.add(new LevelMetalBlock(139, 3));
        levelBlocks.add(new LevelMetalBlock(139, 4));
        levelBlocks.add(new LevelMetalBlock(139, 5));
        levelBlocks.add(new LevelMetalBlock(140, 2));
        levelBlocks.add(new LevelMetalBlock(140, 3));
        levelBlocks.add(new LevelMetalBlock(140, 4));
        levelBlocks.add(new LevelMetalBlock(141, 2));
        levelBlocks.add(new LevelMetalBlock(141, 3));
        levelBlocks.add(new LevelMetalBlock(142, 2));

        levelBlocks.add(new LevelMetalBlock(147, 2));
        levelBlocks.add(new LevelMetalBlock(148, 2));
        levelBlocks.add(new LevelMetalBlock(148, 3));
        levelBlocks.add(new LevelMetalBlock(149, 2));
        levelBlocks.add(new LevelMetalBlock(149, 3));
        levelBlocks.add(new LevelMetalBlock(149, 4));
        levelBlocks.add(new LevelMetalBlock(150, 2));
        levelBlocks.add(new LevelMetalBlock(150, 3));
        levelBlocks.add(new LevelMetalBlock(150, 4));
        levelBlocks.add(new LevelMetalBlock(150, 5));
        levelBlocks.add(new LevelMetalBlock(151, 2));
        levelBlocks.add(new LevelMetalBlock(151, 3));
        levelBlocks.add(new LevelMetalBlock(151, 4));
        levelBlocks.add(new LevelMetalBlock(151, 5));
        levelBlocks.add(new LevelMetalBlock(154, 2));
        levelBlocks.add(new LevelMetalBlock(154, 3));
        levelBlocks.add(new LevelMetalBlock(154, 4));
        levelBlocks.add(new LevelMetalBlock(154, 5));
        levelBlocks.add(new LevelMetalBlock(155, 2));
        levelBlocks.add(new LevelMetalBlock(155, 3));
        levelBlocks.add(new LevelMetalBlock(155, 4));
        levelBlocks.add(new LevelMetalBlock(156, 2));
        levelBlocks.add(new LevelMetalBlock(156, 3));
        levelBlocks.add(new LevelMetalBlock(157, 2));

        levelBlocks.add(new LevelMetalBlock(180, 2));

        levelBlocks.add(new LevelMetalBlock(181, 2));
        levelBlocks.add(new LevelMetalBlock(181, 3));

        levelBlocks.add(new LevelMetalBlock(182, 2));
        levelBlocks.add(new LevelMetalBlock(182, 3));
        levelBlocks.add(new LevelMetalBlock(182, 4));

        levelBlocks.add(new LevelMetalBlock(183, 2));
        levelBlocks.add(new LevelMetalBlock(183, 3));
        levelBlocks.add(new LevelMetalBlock(183, 4));
        levelBlocks.add(new LevelMetalBlock(183, 5));

        levelBlocks.add(new LevelMetalBlock(184, 2));
        levelBlocks.add(new LevelMetalBlock(184, 3));
        levelBlocks.add(new LevelMetalBlock(184, 4));
        levelBlocks.add(new LevelMetalBlock(184, 5));
        levelBlocks.add(new LevelMetalBlock(184, 6));

        levelBlocks.add(new LevelMetalBlock(185, 2));
        levelBlocks.add(new LevelMetalBlock(185, 3));
        levelBlocks.add(new LevelMetalBlock(185, 4));
        levelBlocks.add(new LevelMetalBlock(185, 5));
        levelBlocks.add(new LevelMetalBlock(185, 6));
        levelBlocks.add(new LevelMetalBlock(185, 7));

        levelBlocks.add(new LevelMetalBlock(186, 2));
        levelBlocks.add(new LevelMetalBlock(186, 3));
        levelBlocks.add(new LevelMetalBlock(186, 4));
        levelBlocks.add(new LevelMetalBlock(186, 5));
        levelBlocks.add(new LevelMetalBlock(186, 6));
        levelBlocks.add(new LevelMetalBlock(186, 7));
        levelBlocks.add(new LevelMetalBlock(186, 8));

        levelBlocks.add(new LevelMetalBlock(187, 2));
        levelBlocks.add(new LevelMetalBlock(187, 3));
        levelBlocks.add(new LevelMetalBlock(187, 4));
        levelBlocks.add(new LevelMetalBlock(187, 5));
        levelBlocks.add(new LevelMetalBlock(187, 6));
        levelBlocks.add(new LevelMetalBlock(187, 7));
        levelBlocks.add(new LevelMetalBlock(187, 8));
        levelBlocks.add(new LevelMetalBlock(187, 9));

        levelBlocks.add(new LevelMetalBlock(188, 2));
        levelBlocks.add(new LevelMetalBlock(188, 3));
        levelBlocks.add(new LevelMetalBlock(188, 4));
        levelBlocks.add(new LevelMetalBlock(188, 5));
        levelBlocks.add(new LevelMetalBlock(188, 6));
        levelBlocks.add(new LevelMetalBlock(188, 7));
        levelBlocks.add(new LevelMetalBlock(188, 8));
        levelBlocks.add(new LevelMetalBlock(188, 9));

        levelBlocks.add(new LevelMetalBlock(197, 2));

        // lägger till det osynliga blocket
        levelBlocks.add(new LevelSecretBrick(64, 6, ONE_UP_MUSHROOM));

        // lägger till tegelstenen med pengar
        levelBlocks.add(new LevelCoinBrick(94, 5));

        // lägger till tegelstenne med stjärna
        levelBlocks.add(new LevelItemBrick(101, 5, STAR));

        // lägger till alla fiender
        Enemy companion;
        enemies.add(new EnemyGoomba(22, 2));
        enemies.add(new EnemyGoomba(40, 2));

        companion = new EnemyGoomba(52.5, 2);
        enemies.add(new EnemyGoomba(51, 2, companion));
        enemies.add(companion);

        enemies.add(new EnemyGoomba(80, 10));
        enemies.add(new EnemyGoomba(82, 10));

        companion = new EnemyGoomba(98.5, 2);
        enemies.add(new EnemyGoomba(97, 2, companion));
        enemies.add(companion);

        companion = new EnemyGoomba(115.5, 2);
        enemies.add(new EnemyGoomba(114, 2, companion));
        enemies.add(companion);

        companion = new EnemyGoomba(125.5, 2);
        enemies.add(new EnemyGoomba(124, 2, companion));
        enemies.add(companion);

        companion = new EnemyGoomba(129.5, 2);
        enemies.add(new EnemyGoomba(128, 2, companion));
        enemies.add(companion);

        flag = new LevelFlag(197); // 197
    }

    /**
     * skapar delen under marken i värld 1
     */
    private void init112() {
        scrollX = 0;
        levelBlocks = new ArrayList();
        items = new ArrayList();
        enemies = new ArrayList();
        player = new Player(2, 1, levelBlocks, items, false, this);

        // player startar på x: 1.5, y: 11
        // lägger till marken
        for (int i = 0; i < 16; i++) {
            levelBlocks.add(new LevelGroundBlock(i, 0));
        }
        for (int i = 0; i < 16; i++) {
            levelBlocks.add(new LevelGroundBlock(i, 1));
        }

        // lägger till tegelstenarna
        for (int i = 2; i < 14; i++) {
            levelBlocks.add(new LevelBrick(0, i));
        }
        for (int i = 2; i < 5; i++) {
            for (int j = 4; j < 11; j++) {
                levelBlocks.add(new LevelBrick(j, i));
            }
        }
        for (int i = 4; i < 11; i++) {
            levelBlocks.add(new LevelBrick(i, 13));
        }

        // lägger till rören
        levelBlocks.add(new LevelPipe(15, 1, 13, LevelPipe.UP));
        levelBlocks.add(new LevelPipe(11, 3, 2, LevelPipe.LEFT, 1, 2, 111));

        // lägger till mynten
        items.add(new ItemCoin(6, 7));

    }

    public static ArrayList<Item> getItems() {
        return items;
    }

    public static ArrayList<LevelBlock> getLevelBlocks() {
        return levelBlocks;
    }

    public static ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public static LevelFlag getFlag() {
        return flag;
    }

    public static void addItem(Item item) {
        items.add(item);
    }

    public static void addLevelBlock(LevelBlock block) {
        levelBlocks.add(block);
    }

    public static void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    /**
     * När spelare går genom ett rör
     * @param pipeNumber det röret som spelaren ska till
     * @param worldNumber världen som röret ligger i
     */
    public void pipeTransport(int pipeNumber, int worldNumber) {
        switch (worldNumber) {
            case 111:
                init111();
                break;
            case 112:
                init112();
                break;
        }
        for (LevelBlock block : levelBlocks) {
            if (block instanceof LevelPipe) {
                LevelPipe pipe = (LevelPipe) block;
                if (pipe.getPipeNumber() == pipeNumber) {
                    scrollX = -pipe.getX() + (int) (Game.BLOCK_SIZE * 3.5);
                    player.setY(pipe.getLength() + pipe.getY() - player.getHeight() - 5);
                }
            }
        }
    }

    /**
     * Startar om spelet till början
     */
    public void restartGame() {
        SidePanel.setNameFieldEnabled(false); // gör så man inte längre kan skriva in sitt namn i sidopanelen
        scrollX = 0; // återställer kameran
        timer.stop(); // stoppar timern
        timer.start(); // startar en ny timer
        decimalSecond = 0; // sätter tiden till 0
        second = 0; // sätter tiden till 0
        Window.setLblTimer("00.00"); // sätter tiden till 0
        init111(); //skapar världen
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Item item : items) {
            item.draw(g);
        }
        for (LevelBlock block : levelBlocks) {
            block.draw(g);
        }
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }
        flag.draw(g);

        player.draw(g);
    }
    /**
     * anropar update-metoden hos de klasser som är aktiva i spelet. Ritar ut spelet efter att klasserma har uppdaterats
     */
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                player.update();

                for (Enemy enemy : enemies) {
                    enemy.update();
                }

                for (Item item : items) {
                    if (item instanceof ItemMushroom) {
                        ((ItemMushroom) item).update();
                    } else if (item instanceof ItemStar) {
                        ((ItemStar) item).update();
                    }
                }

                repaint();
                Thread.sleep(THREAD_INTERVAL);
            } catch (InterruptedException ex) {
                System.out.println("Spel avbrutet");
                break;
            }
        }
    }
}
