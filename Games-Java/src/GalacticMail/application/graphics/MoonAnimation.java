package GalacticMail.application.graphics;

import java.awt.*;
import java.util.Random;


public class MoonAnimation extends Animation {
    private int index;

    public MoonAnimation(Sprite sprite, int width, int height) {
        super(sprite, width, height, 0, 0, 0,false);
        this.index = new Random().nextInt(7);

        setAngle();
        setInitposition();
        this.x = startX;
        this.y = startY;
    }

    @Override
    public void repaint(Graphics graphics) {
        move();
        outOfBound();

        graphics.drawImage(sprite.getFrame(index), x, y, null);
    }
}
