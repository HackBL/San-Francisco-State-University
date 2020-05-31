package TankGame.application.graphics;

import java.awt.*;
import java.io.IOException;

public class TankObject extends GameObject{
    private int[] angle = new int[60];
    private int STEP = 10;
    private int nextX = 0;
    private int nextY = 0;
    private int degree = 0;
    private int index = 0;
    private Sprite tankDirection;

    public TankObject(String resourceLocation) throws IOException {
        super(resourceLocation);
        tankDirection = new Sprite("resources/Tank_blue_basic_strip60.png", 64);
    }

    public void setDegree() {
        for (int row = 0; row < angle.length; row++) {
            angle[row] = degree;
            degree += 6;
        }
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean collisionToWall(WallObject wall) {
        try {
            wall.setArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Rectangle tank = new Rectangle(nextX,nextY,getWidth()-STEP,getHeight()-STEP);
        for (int row = 0; row < 40; row++) {

            for (int col = 0; col < 39; col++) {
                if (wall.getArray()[row][col].equals("1") || wall.getArray()[row][col].equals("2")) {

                    Rectangle walls = new Rectangle(col * wall.getWidth(), row * wall.getHeight(),
                                    wall.getWidth()-STEP, wall.getHeight()-STEP);

                    if (tank.intersects(walls)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean collisionToTank(TankObject anotherTank) {
        Rectangle tank = new Rectangle(nextX,nextY,getWidth()-STEP,getHeight()-STEP);
        Rectangle secondTank = new Rectangle(anotherTank.getX(), anotherTank.getY(),
                            anotherTank.getWidth()-STEP, anotherTank.getHeight()-STEP);

        return tank.intersects(secondTank);
    }

    public void rotateLeft() {
        if (index < 59) {
            index++;
        } else {
            index = 0;
        }
        image = tankDirection.getFrame(index);
    }

    public void rotateRight() {
        if (index > 0) {
            index--;
        } else {
            index = 59;
        }
        image = tankDirection.getFrame(index);
    }

    public void forwardMove() {
        this.nextX = this.getX() + (int) (Math.cos(Math.toRadians(angle[index])) * STEP);
        this.nextY = this.getY() - (int) (Math.sin(Math.toRadians(angle[index])) * STEP);
    }

    public void reverseMove() {
        nextX = this.getX() - (int) (Math.cos(Math.toRadians(angle[index])) * STEP);
        nextY = this.getY() + (int) (Math.sin(Math.toRadians(angle[index])) * STEP);
    }

    public int fireStartX() {
        return this.x + this.getWidth() / 2
                + (int) ((int) (this.getWidth() / 2 * (Math.cos(Math.toRadians(50))))
                * 2 * (Math.cos(Math.toRadians(this.angle[index]))));
    }

    public int fireStartY() {
        return this.y + this.getHeight() / 2
                - (int) ((int) (this.getHeight() / 2 * (Math.cos(Math.toRadians(50))))
                * 2 * (Math.sin(Math.toRadians(this.angle[index]))));
    }

    public int getNextX() {
        return this.nextX;
    }

    public int getNextY() {
        return this.nextY;
    }

    public int getIndex() {
        return index;
    }

    public int[] getAngle() {
        return angle;
    }

    @Override
    public void repaint( Graphics graphics ) {
        graphics.drawImage( tankDirection.getFrame(index), x, y, observer );
    }
}
