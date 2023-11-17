/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mario;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;

/**
 *
 * @author gustav.andoff
 */
public class Player {

    private double ySpeed;
    private double xSpeed;
    private double jumpSpeed;
    private double friction;
    private double acceleration;

    private double maxWalkSpeed;
    private double maxRunSpeed;

    private boolean grounded;

    private int width;
    private int height;

    private int x;
    private int y;

    private boolean scrollScreen;

    private boolean acceleratingRight;
    private boolean acceleratingLeft;
    private boolean jumping;
    private boolean holdingDown;
    private boolean running;

    private boolean fire;
    private boolean invincible;
    private boolean superStar;
    private boolean big;

    private boolean checkForPunch; // om man hoppar uppåt ska man slå till blocken om man träffar dem

    private Timer invincibleTimer;
    private Timer jumpTimer;
    private Timer superStarTimer;

    private boolean levelEnd;
    private boolean endWalking;

    private Game game;

    public Player(double x, double y, ArrayList<LevelBlock> levelBlocks, ArrayList<Item> items, boolean scrollScreen, Game game) {
        levelEnd = false;
        endWalking = false;
        ySpeed = 0;
        xSpeed = 0;
        jumpSpeed = 7;
        friction = 0.5;
        acceleration = 0.2;
        maxWalkSpeed = 4.1;
        maxRunSpeed = maxWalkSpeed * 5 / 3;

        this.game = game;

        height = (int) (Game.BLOCK_SIZE * 0.875);
        width = (int) (Game.BLOCK_SIZE * 0.6875);
        big = false;

        this.x = (int) (Game.BLOCK_SIZE * x);
        this.y = (int) (Game.BLOCK_SIZE * y) + height;

        this.scrollScreen = scrollScreen;

        grounded = true;
        checkForPunch = false;

        invincibleTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                invincible = false;
            }
        });

        jumpTimer = new Timer(280, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jumping = false;
            }
        });

        superStarTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loseSuperStar();
            }
        });
    }

    public void setAcceleratingRight(boolean isAcceleratingRight) {
        this.acceleratingRight = isAcceleratingRight;
    }

    public void setAcceleratingLeft(boolean isAcceleratingLeft) {
        this.acceleratingLeft = isAcceleratingLeft;
    }

    /**
     * När hoppknappen trycks ner
     */
    public void startJump() {
        if (grounded) { // om man står på marken
            this.jumping = true; // ska man börja hoppa
            jumpTimer.start(); // tills att denna timer är klar kan hoppknappen fortsättas hållas in för att spelarens y-hastighet ska öka så hoppet blir högre
        }
    }

    /**
     * När hoppknappen slutar vara nertryck
     */
    public void endJump() {
        if (jumpTimer.isRunning()) { // om hopptimern är igång
            jumpTimer.stop(); // ska timern sluta tidigt
            this.jumping = false; // och spelarens y-hastighet slutar att öka, dvs hoppet blir kortare om man släpper hoppknappen innan hopptimern är klar
        }
    }

    public void setRunning(boolean isRunning) {
        this.running = isRunning;
    }

    public void setHoldingDown(boolean holdingDown) {
        this.holdingDown = holdingDown;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    private ArrayList<LevelBlock> isColliding(int x, int y) {
        ArrayList<LevelBlock> collidedBlocks = new ArrayList();

        for (LevelBlock block : game.getLevelBlocks()) {
            if (block.isCollided(x - game.scrollX, y, width, height)) {
                if (checkForPunch) { // när man hoppar uppåt och träffar ett block
                    block.punched(big); // ska blockets punched-metod anropas
                    endJump(); // träffar man ett block ska man stanna och börja falla istället
                    collidedBlocks.add(block); // om man kan slå blocken kan man slå flera stycken alltså måste den fortsätta kolla om man kolliderar
                } else {
                    collidedBlocks.add(block);
                    return collidedBlocks; // om man inte kan slå någonting kan man avsluta metoden direkt eftersom det inte spelar någon roll hur mycket man kolliderar med
                }
            }
        }

        return collidedBlocks;
    }

    private void checkCollidingWithItem(int x, int y) {
        for (Item item : game.getItems()) {
            if (item.isCollided(x - game.scrollX, y, width, height) && item.isExisting()) {
                item.collect(); // har man kolliderat med ett item ska man plocka upp itemet
                switch (item.getClass().getName()) { // beroende på vilket item det är ska olika skaer ske
                    case ("mario.ItemMagicMushroom"):
                        upgrade();
                        break;
                    case ("mario.ItemFireFlower"):
                        upgrade();
                        break;
                    case ("mario.ItemStar"):
                        becomeSuperStar();
                        break;
                    case ("mario.ItemOneUpMushroom"): // detta item har inte effekt i detta spel
                        break;
                    case ("mario.ItemCoin"): // detta item har inte effekt i detta spel
                        break;
                }
            }
        }
    }

    /**
     * Om spelaren har gått in i målflaggan
     * @param x spelarens x-värde
     * @param y spelarens y-värde
     * @return om man gått in i flaggan eller inte 
     */
    private boolean checkCollidingWithFlag(int x, int y) {
        if (game.getFlag().isCollided(x - game.scrollX, y, width, height)) {
            levelEnd = true;
            return true;
        }

        return false;
    }

    /**
     * hämtar en lista med alla fiender man för tillfället kolliderar med
     * @param x spelarens x-värde
     * @param y spelarens y-värde
     * @return en lista med alla fiender man kolliderar med. Om den är tom kolliderar man inte
     */
    private ArrayList<Enemy> getCollidingEnemies(int x, int y) {
        ArrayList<Enemy> collidedEnemies = new ArrayList();

        for (Enemy enemy : game.getEnemies()) {
            if (enemy.isCollided(x - game.scrollX, y, width, height) && !enemy.isDead()) {
                if (!invincible) {
                    collidedEnemies.add(enemy);
                }
                if (superStar) { // om man tagit upp en stjärna ska fienden dö om man kolliderar med den
                    enemy.die();
                }
            }
        }

        return collidedEnemies;
    }

    /**
     * Flyttar antingen kameran eller gubben i x-led
     * @param x så mycket gubben går i x-led
     */
    private void moveX(double x) {
        if (this.x + x < 0) {
            this.x = 0;
            xSpeed = 0;
        } else {
            if (this.x < Game.BLOCK_SIZE * 8 || x < 0 || !scrollScreen) {
                this.x += x;
            } else {
                game.scrollX -= x;
            }
        }

    }

    private void upgrade() {
        if (big) {
            becomeFire();
        } else {
            grow();
        }
    }

    /**
     * Om man tar skada från någon fiende
     */
    private void damaged() {
        if (invincible) { // om man är odödlig (sker om man precis blivit träffad av nån fiende) ska man inte ta skada
            return;
        }
        if (fire) { // om man har tagit en eldbomma ska man förlora den
            loseFire();
            invincible = true;
            invincibleTimer.start();
        } else if (big) { // om man är stor ska man bli liten
            shrink();
            invincible = true;
            invincibleTimer.start();
        } else { // om man varken har eldblomma eller är stor ska man dö
            die();
        }
    }

    /**
     * när man plockat upp en stjärna
     */
    private void becomeSuperStar() {
        superStar = true;
        invincible = true;
        superStarTimer.start();
    }

    /**
     * när man blir av med stjärnan
     */
    private void loseSuperStar() {
        superStar = false;
        invincible = false;
        if (superStarTimer.isRunning()) {
            superStarTimer.stop();
        }
    }

    /**
     * när man plocka upp en eldblomma
     */
    private void becomeFire() {
        fire = true;
    }

    /**
     * när man förlorar eldblomman
     */
    private void loseFire() {
        fire = false;
    }

    /**
     * när man plockat upp en svamp och ska växa
     */
    public void grow() {
        big = true;
        height = (int) (Game.BLOCK_SIZE * 1.5625);
        width = (int) (Game.BLOCK_SIZE * 0.75);
        y -= (int) (Game.BLOCK_SIZE * 0.6875);
    }

    /**
     * när man tar skada och ska krympa
     */
    public void shrink() {
        big = false;
        height = (int) (Game.BLOCK_SIZE * 0.875);
        width = (int) (Game.BLOCK_SIZE * 0.6875);
        y += (int) (Game.BLOCK_SIZE * 0.6875);
    }

    /**
     * när man dör
     */
    private void die() {
        game.restartGame();
    }

    public void draw(Graphics g) {
        if (superStar) {
            g.setColor(Color.yellow);
        } else if (fire) {
            g.setColor(Color.red);
        } else if (invincible) {
            g.setColor(Color.black);
        } else {
            g.setColor(Color.blue);
        }

        g.fillRect(x, y, width, height);
    }

    /**
     * kollar om man ska åka igenom något rör
     * @param blockCollisions alla block man kolliderar med
     * @param directionAxis om man förflyttas i y-led eller x-led
     */
    private void checkPipeTransportation(ArrayList<LevelBlock> blockCollisions, int directionAxis) {
        final int VERTICAL = LevelPipe.UP + LevelPipe.DOWN; // Om röret är riktat upp eller ner är det vertikalt
        final int HORIZONTAL = LevelPipe.RIGHT + LevelPipe.LEFT; // Om röret är riktat åt höger eller vänster är det horisontelt

        int direction = 0; // den mer specifika riktningen som spelaren rör sig mot

        if (directionAxis == VERTICAL) { // veritkalt betyder antingen upp eller ner
            if (ySpeed > 0) {
                direction = LevelPipe.DOWN; // om spelaren rör sig uppåt ska man kunna åka igenom rör som är riktade neråt
            } else if (holdingDown) {
                direction = LevelPipe.UP; // om man håller nerknappen ska man kunna åka igenom rör som är riktade uppåt
            }
        } else if (directionAxis == HORIZONTAL) { // horisontelt betyder antingen höger eller vänster
            if (acceleratingRight) {
                direction = LevelPipe.LEFT; // om man rör sig åt höger ska man kunna åkga igenom rör som är riktade åt vänster
            } else if (acceleratingLeft) {
                direction = LevelPipe.RIGHT; // om man rör sig åt vänster ska man kunna åkga igenom rör som är riktade åt höger
            }
        }

        if (direction == 0) { // om inget av ovanstående stämmer rör man sig inte åt något håll
            return;
        }
        for (LevelBlock block : blockCollisions) {
            if (block instanceof LevelPipe) { // om ett visst block är ett rör
                LevelPipe pipe = ((LevelPipe) block);

                if (pipe.getDirection() == direction && pipe.hasDestination()) { // om rörets riktning är samma som spelarens direction och röret leder någonstans
                    game.pipeTransport(pipe.getDestination(), pipe.getDestinationWorld()); // då ska spelaren transporteras genom röret
                }
            }
        }
    }

    public void update() {
        if (!levelEnd) { // så länge man inte befinner sig i slutet av spelet efter att man nuddat flaggan
            ArrayList<LevelBlock> blockCollisions;
            if (acceleratingRight) { // om man rör sig åt höger
                xSpeed += acceleration; // ökar hastigheten med accelerationskonstanten
                if (running && xSpeed > maxRunSpeed) { // man ska inte kunna passera en viss maxhastighet
                    xSpeed = maxRunSpeed;
                } else if (!running && xSpeed > maxWalkSpeed) { // man ska inte kunna passera en viss maxhastighet
                    xSpeed = maxWalkSpeed;
                }
            }
            if (acceleratingLeft) { // om man rör sig åt vänster
                xSpeed -= acceleration; // ökar hastigheten med accelerationskonstanten
                if (running && xSpeed < -maxRunSpeed) { // man ska inte kunna passera en viss maxhastighet
                    xSpeed = -maxRunSpeed;
                } else if (!running && xSpeed < -maxWalkSpeed) { // man ska inte kunna passera en viss maxhastighet
                    xSpeed = -maxWalkSpeed;
                }
            }
            moveX(xSpeed); // spelaren förflyttas med hastigheten
            checkCollidingWithFlag(x, y); // kollar om man gått in i flaggan
            for (Enemy enemy : getCollidingEnemies(x, y)) { // kollar om man kolliderat med någon fiende
                if (enemy.canDamagePlayer()) {
                    damaged(); // om fienden kan skada spelaren tar man skada
                } else if (enemy instanceof EnemyKoopaTroopa && ((EnemyKoopaTroopa) enemy).isShell() && !((EnemyKoopaTroopa) enemy).isShellGliding() && xSpeed != 0) {
                    System.out.println("player glid");
                    ((EnemyKoopaTroopa) enemy).startGlide((int) (Math.abs(xSpeed) / xSpeed));
                }
            }

            blockCollisions = isColliding(x, y);
            if (!blockCollisions.isEmpty()) { // om det här är true har man kolliderat i x-led

                checkPipeTransportation(blockCollisions, LevelPipe.LEFT + LevelPipe.RIGHT); // kollar efter rör horisontelt

                double normalizedXSpeed = Math.abs(xSpeed) / xSpeed;
                while (!isColliding(x, y).isEmpty()) {
                    moveX(-normalizedXSpeed); // man förflyttas i motsatt rikning man går tills att man inte längre kolliderar
                }
                xSpeed = 0;
            }

            if (jumping) { 
                grounded = false; // om man hoppar står man itne längre på marken
                ySpeed = jumpSpeed; // hastigheten i y-led sätts till hopphastighetskonstanten
            }

            if (grounded) { // om man står på marken

                // applicerar friktion
                if ((xSpeed != 0 && (acceleratingRight == acceleratingLeft) || (xSpeed > 0 && acceleratingLeft) || (xSpeed < 0 && acceleratingRight))) { // om man inte står stilla och inte försöker gå åt något håll eller försöker gå i motsatt riktning man för tillfället går åt
                    int normalizedXSpeed = (int) (Math.abs(xSpeed) / xSpeed); // 1 eller -1 beroende på hastighetens riktning
                    xSpeed -= friction * normalizedXSpeed; // när man står på marken minskar hastigheten med friktionen gånger normerade hastigheten
                    int newNormedXSpeed = (int) (Math.abs(xSpeed) / xSpeed);
                    if (newNormedXSpeed != normalizedXSpeed) { // om hastigheten ändrat riktning
                        xSpeed = 0;
                    }
                }

                for (Enemy enemy : getCollidingEnemies(x, y)) {
                    if (enemy.canDamagePlayer()) {
                        damaged();
                    }
                }

            } else {
                ySpeed += Game.GRAVITY; // ökar hastigheten med gravitationen
                y -= ySpeed;

                if (ySpeed < 0 && !getCollidingEnemies(x, y).isEmpty()) {
                    for (Enemy enemy : getCollidingEnemies(x, y)) {
                        enemy.stomped(x - game.scrollX, width);
                    }
                    y -= 5;
                    ySpeed = 10; // om man landar på en fiende ska man studsa uppåt
                }
            }

            checkForPunch = ySpeed > 0; // man ska bara kunna slå blocken om man hoppat dvs ySpeed är riktad uppåt
            blockCollisions = isColliding(x, y);
            if (!blockCollisions.isEmpty()) { // om det här är true har man kolliderat i y-led

                checkPipeTransportation(blockCollisions, LevelPipe.UP + LevelPipe.DOWN);

                checkForPunch = false;
                double normalizedYSpeed = ySpeed / Math.abs(ySpeed);
                while (!isColliding(x, y).isEmpty()) {
                    y += normalizedYSpeed; // när man har kolliderat ska ens position ändras med 1 eller -1 tills att man inte längre kolliderar
                }
                grounded = ySpeed <= 0; // endast om man landat på ett block är man på marken dvs ySpeed är riktad neråt eller noll
                ySpeed = 0;
            } else {
                checkForPunch = false;

                blockCollisions = isColliding(x, y + 1);
                if (blockCollisions.isEmpty()) { // om man är en pixel ifrån att kollidera med marken ska det räknas som att man står på marken
                    grounded = false;
                } else {
                    checkPipeTransportation(blockCollisions, LevelPipe.UP + LevelPipe.DOWN);
                }
            }

            if (y > (int) (Game.BLOCK_SIZE * 14.5)) {
                die();
            }

            checkCollidingWithItem(x, y);
        } else { // hämnar här i slutet när man ska glida ner för flaggan
            game.endLevel();
            double normalizedXSpeed = Math.abs(xSpeed) / xSpeed;

            while (checkCollidingWithFlag(x, y) && !endWalking) {
                moveX(-normalizedXSpeed); // man förflyttas i motsatt rikning man går tills att man inte längre kolliderar
            }
            if (isColliding(x, y).isEmpty() && !endWalking) {
                y += 2;
            } else {
                endWalking = true;
                scrollScreen = false;
                moveX(2);

                if (!grounded) {
                    ySpeed += Game.GRAVITY; // ökar hastigheten med gravitationen
                    y -= ySpeed;
                }

                ArrayList<LevelBlock> blockCollisions = isColliding(x, y);
                if (!blockCollisions.isEmpty()) { // om det här är true har man kolliderat i y-led

                    double normalizedYSpeed = ySpeed / Math.abs(ySpeed);
                    while (!isColliding(x, y).isEmpty()) {
                        y += normalizedYSpeed; // när man har kolliderat ska ens position ändras med 1 eller -1 tills att man inte längre kolliderar
                    }
                    grounded = ySpeed <= 0; // endast om man landat på ett block är man på marken dvs ySpeed är riktad neråt eller noll
                    ySpeed = 0;
                } else {
                    blockCollisions = isColliding(x, y + 1);
                    if (blockCollisions.isEmpty()) { // om man är en pixel ifrån att kollidera med marken ska det räknas som att man står på marken
                        grounded = false;
                    }
                }
            }

        }

    }
}
