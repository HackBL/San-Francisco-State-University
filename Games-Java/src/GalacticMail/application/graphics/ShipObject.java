package GalacticMail.application.graphics;

import java.awt.*;
import java.io.IOException;

public class ShipObject extends GameObject {
    protected final int WIDTH = 600;
    protected final int HEIGHT = 600;
    private int[] angle = new int[72];
    private int nextX = 0;
    private int nextY = 0;
    private int degree = 0;
    private int index = 0;
    private int step = 10;
    private boolean landed = false;

    private Sprite flyingShip;
    private Sprite landedShip;

    public ShipObject() throws IOException {
        super("resources/Flying_strip72.png");
        flyingShip = new Sprite("resources/Flying_strip72.png", 48);
        landedShip = new Sprite("resources/Landed_strip72.png", 48);
    }



    public void rotateLeft() {
        if (index < 71) {
            index++;
        } else {
            index = 0;
        }
        image = flyingShip.getFrame(index);
    }

    public void rotateRight() {
        if (index > 0) {
            index--;
        } else {
            index = 71;
        }
        image = flyingShip.getFrame(index);
    }

    public void forwardMove() {
        this.nextX = this.x + (int) (Math.cos(Math.toRadians(angle[index])) * step);
        this.nextY = this.y - (int) (Math.sin(Math.toRadians(angle[index])) * step);
    }

    public void outOfBound() {
        if ((this.x + image.getWidth()) < 0) {
            this.setX(WIDTH);
        }

        if (this.x > WIDTH) {
            this.setX(0 - image.getWidth());
        }

        if (this.y + image.getHeight() < 0) {
            this.setY(HEIGHT);
        }

        if (this.y > HEIGHT) {
            this.setY(0 - image.getHeight());
        }
    }

    public void setDegree() {
        for (int row = 0; row < angle.length; row++) {
            angle[row] = degree;
            degree += 5;
        }
    }

    public void setInitStep() {
        this.step = 10;
    }


    public void setStep(int step) {
        this.step += step;
    }

    public void setLanded(boolean landed) {
        this.landed = landed;
    }

    public boolean getLanded() {
        return landed;
    }

    public int getNextX() {
        return this.nextX;
    }

    public int getNextY() {
        return this.nextY;
    }

    public Rectangle getRectangle() {
        return new Rectangle(nextX, nextY, image.getWidth(), image.getHeight());
    }

    @Override
    public void repaint( Graphics graphics ) {
        if (landed) {
            graphics.drawImage( landedShip.getFrame(index), x, y, observer );
        } else {
            graphics.drawImage( flyingShip.getFrame(index), x, y, observer );
        }
    }

}
