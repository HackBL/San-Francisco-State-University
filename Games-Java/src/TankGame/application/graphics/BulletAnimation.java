package TankGame.application.graphics;

import TankGame.application.AnimationPanel;
import TankGame.application.Players;

import java.awt.*;
import java.io.IOException;

public class BulletAnimation extends Animation{
    private final int speed = 20;
    private int index;
    private int x, y;
    private int[] angle;
    private Sprite explosion;
    private Sprite bulletDirection;
    private TankObject tank, anotherTank;
    private WallObject wall;
    private AnimationPanel animationPanel;
    private Players player1;
    private Players player2;

    public BulletAnimation(AnimationPanel animationPanel, TankObject tank, TankObject anotherTank, Players player1, Players player2,
                           WallObject wall, Sprite bulletDirection, int x, int y, int frameDelay, boolean loop) {
        super(bulletDirection, x, y, frameDelay, loop);
        this.animationPanel = animationPanel;
        this.player1 = player1;
        this.player2 = player2;
        this.bulletDirection = bulletDirection;
        this.tank = tank;
        this.anotherTank = anotherTank;
        this.wall = wall;
        this.angle = tank.getAngle();
        this.index = tank.getIndex();
        this.x = x;
        this.y = y;

        try {
            explosion = new Sprite("resources/Explosion_small_strip6.png", 24);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean collisionToWall() {
        try {
            wall.setArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Rectangle bullets = new Rectangle(x, y, 10, 10);

        for (int row = 0; row < 40; row++) {
            for (int col = 0; col < 39; col++) {
                if ( wall.getArray()[row][col].equals("1") || wall.getArray()[row][col].equals("2")){
                    Rectangle walls = new Rectangle(col * wall.getWidth(), row * wall.getHeight(),
                                         wall.getWidth(), wall.getHeight());

                    if (bullets.intersects(walls)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public void attackWall() {
        try {
            wall.setArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Rectangle bullets = new Rectangle(x, y, 10, 10);

        for (int row = 0; row < 40; row++) {
            for (int col = 0; col < 39; col++) {
                if (wall.getArray()[row][col].equals("2")) {
                    Rectangle walls = new Rectangle(col * wall.getWidth(), row * wall.getHeight(),
                                            wall.getWidth(), wall.getHeight());

                    if (bullets.intersects(walls)) {
                        wall.getArray()[row][col] = " ";
                        wall.setNewArray(wall.getArray());
                    }
                }
            }
        }
    }

    public boolean collisionToTank() {
        Rectangle bullets = new Rectangle(x, y, 5, 5);
        Rectangle tankRec = new Rectangle(tank.getX(), tank.getY(), tank.getWidth()-10, tank.getHeight()-10);
        Rectangle anotherTankRec = new Rectangle(anotherTank.getX(), anotherTank.getY(), anotherTank.getWidth()-10, anotherTank.getHeight()-10);

        return (bullets.intersects(tankRec) || bullets.intersects(anotherTankRec));
    }

    public void attackTank() {
        Rectangle bullets = new Rectangle(x, y, 5, 5);
        Rectangle tankRec = new Rectangle(tank.getX(), tank.getY(), tank.getWidth()-10, tank.getHeight()-10);
        Rectangle anotherTankRec = new Rectangle(anotherTank.getX(), anotherTank.getY(), anotherTank.getWidth()-10, anotherTank.getHeight()-10);

        if (bullets.intersects(tankRec)) {
            player1.hitTank();
            if (player1.die()) {
                player2.earnPoint();
                explosion(tank);
                player1.setInitPosition();
                tank.setIndex(0);
            }
        }

        if (bullets.intersects(anotherTankRec)) {
            player2.hitTank();
            if (player2.die()) {
                player1.earnPoint();
                explosion(anotherTank);
                player2.setInitPosition();
                anotherTank.setIndex(0);
            }
        }
    }

    public void explosion(TankObject diedTank) {
        animationPanel.addAnimation(new Animation(this.explosion, diedTank.getX(), diedTank.getY(), 5, false));
    }

    public void forwardMove() {
        x += (int) (Math.cos(Math.toRadians(angle[index])) * speed);
        y -= (int) (Math.sin(Math.toRadians(angle[index])) * speed);
    }

    @Override
    public void repaint( Graphics graphics ) {
        if( !stop ) {
            forwardMove();
            graphics.drawImage(bulletDirection.getFrame(index), x, y, null);
            if (collisionToWall()) {
                attackWall();
                stop = true;
            }

            if (collisionToTank()) {
                attackTank();
                stop = true;
            }
        }
    }
}
