package GalacticMail.application.graphics;

import java.awt.*;
import java.util.Random;

public class Animation {
    protected Sprite sprite;
    protected int[] angle = new int[360];

    protected int[] XCoord;
    protected int[] YCoord;

    protected int WIDTH;
    protected int HEIGHT;

    protected int startX;
    protected int startY;

    protected int x;
    protected int y;

    protected int exploseX;
    protected int exploseY;

    protected int speed = 8;
    protected int randomDegree;


    private int frameCount;
    private int frameDelay;
    private int currentFrame;
    private int duration;
    protected boolean loop;
    protected boolean stop;



    public Animation( Sprite sprite, int width, int height, int x, int y, int frameDelay, boolean loop) {
        this.sprite = sprite;
        this.WIDTH = width;
        this.HEIGHT = height;

        this.startX = 0;
        this.startY = 0;

        this.exploseX = x;
        this.exploseY = y;

        randomDegree = new Random().nextInt(359);

        this.XCoord = new int[] {0, WIDTH - sprite.getWidth()};
        this.YCoord = new int[] {0, HEIGHT - sprite.getHeight()};

        this.frameCount = 0;
        this.frameDelay = frameDelay;
        this.currentFrame = 0;
        this.loop = loop;
        this.stop = false;
        this.duration = 0;

    }


    public void outOfBound() {
        if ((this.x + sprite.getWidth()) < 0) {
            this.setX(WIDTH);
        }

        if (this.x > WIDTH) {
            this.setX(0 - sprite.getWidth());
        }

        if (this.y + sprite.getHeight() < 0) {
            this.setY(HEIGHT);
        }

        if (this.y > HEIGHT) {
            this.setY(0 - sprite.getHeight());
        }
    }

    public void move() {
        this.x += (int) (Math.cos(Math.toRadians(angle[randomDegree])) * speed);
        this.y += (int) (Math.sin(Math.toRadians(angle[randomDegree])) * speed);
    }

    public void setAngle() {
        for (int index = 0; index < 360; index++) {
            angle[index] += index;
        }
    }

    public void setInitposition() {
        this.startX = XCoord[new Random().nextInt(XCoord.length)];
        this.startY = YCoord[new Random().nextInt(YCoord.length)];
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setInitSpeed() {
        this.speed = 8;
    }

    public void setSpeed(int speed) {
        this.speed += speed;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Rectangle getRectangle() {
        return new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());
    }


    public int totalTime() {
        return this.frameDelay * sprite.frameCount();
    }

    public void repaint( Graphics graphics ) {
        if( ! stop ) {
            duration++;
            frameCount++;

            if ( frameCount > frameDelay ) {
                frameCount = 0;
                stop = ( duration > this.totalTime() ) && ! loop;
                currentFrame = ( currentFrame + 1 ) % sprite.frameCount();
            }

            graphics.drawImage(sprite.getFrame( currentFrame ), exploseX, exploseY,null );
        }
    }
}
