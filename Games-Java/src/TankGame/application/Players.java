package TankGame.application;

import TankGame.application.graphics.TankObject;
import TankGame.application.graphics.WallObject;

import java.awt.*;

public class Players {
    private String tankPosition;
    private int tankX, tankY;
    private int health = 200;
    private int lives = 3;
    private int point = 0;
    private boolean end = false;
    private TankObject tank;
    private WallObject wall;

    public Players(TankObject tank, WallObject wall, String tankPosition) {
        this.tank = tank;
        this.wall = wall;
        this.tankPosition = tankPosition;
    }

    public void setInitPosition() {
        for (int row = 0; row < 40; row++) {
            for (int col = 0; col < 39; col++) {
                if (wall.getArray()[row][col].equals( tankPosition)) {
                    tankX = col * wall.getWidth();
                    tankY = row * wall.getHeight();
                }
            }
        }
        tank.setX(tankX);
        tank.setY(tankY);
    }

    public void paintHealth(Graphics graphics, int x, int y) {
        if (health > 120){
            graphics.setColor(Color.green);
            graphics.fillRect(x, y, health,30);
        } else if (health > 40 && health <= 120) {
            graphics.setColor(Color.yellow);
            graphics.fillRect(x, y, health,30);
        } else if (health > 0 && health <= 40){
            graphics.setColor(Color.red);
            graphics.fillRect(x, y, health,30);
        } else {
            lives -= 1;
            health = 200;
        }
    }

    public void paintLives(Graphics graphics, int x, int y) {
        int pixel = 40;
        graphics.setColor(Color.lightGray);

        for (int life = 0; life < lives-1; life ++) {
            graphics.fillOval(x + life*pixel ,y,25,25);
        }

        if (lives == 0) {
            this.end = true;
            graphics.setColor(Color.red);
            Font font = graphics.getFont().deriveFont( 70.0f );
            graphics.setFont(font);
            graphics.drawString("Nice Game!", 450, 500);

        }
    }

    public void paintPoint(Graphics graphics, int x, int y) {
        graphics.setColor(Color.white);
        Font font = graphics.getFont().deriveFont( 30.0f );
        graphics.setFont(font);
        graphics.drawString(Integer.toString(point),x , y );
    }

    public void hitTank() {
        this.health -= 40;
    }

    public void earnPoint() {
        point += 10;
    }

    public boolean endGame() {
        return end;
    }

    public boolean die() {
        return health == 0;
    }
}



