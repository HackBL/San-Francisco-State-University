package GalacticMail.application.graphics;

import java.awt.*;

public class AsteroidAnimation extends Animation{
    private int frameCount;
    private int frameDelay;
    private int currentFrame;




    public AsteroidAnimation( Sprite sprite,  int width, int height, int frameDelay, boolean loop) {
        super(sprite, width, height, 0, 0, frameDelay, loop);

        this.frameCount = 0;
        this.frameDelay = frameDelay;
        this.currentFrame = 0;
        this.loop = loop;
        this.stop = false;



        setAngle();
        setInitposition();
        this.x = startX;
        this.y = startY;
    }

    @Override
    public void repaint(Graphics graphics) {
        move();
        outOfBound();

        frameCount++;

        if ( frameCount > frameDelay ) {
            frameCount = 0;
            currentFrame = ( currentFrame + 1 ) % sprite.frameCount();
        }

        graphics.drawImage(sprite.getFrame(currentFrame), x, y, null);
    }
}
