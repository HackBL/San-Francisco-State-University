package TankGame.application.graphics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class WallObject extends GameObject{
    private static BufferedReader source;
    private BufferedImage unbreakableWall;
    private String[][] wallArray = new String[40][39];

    public WallObject(String WallSource) throws IOException{
        super(WallSource);
        source = new BufferedReader(new FileReader("resources/level"));
        unbreakableWall = ImageIO.read(new File("resources/Blue_wall1.png"));
    }

    public void setArray() throws IOException{
        int row = 0;
        String line;

        while ((line = source.readLine()) != null) {
            for (int col = 0; col < wallArray[row].length; col++) {
                wallArray[row][col] = String.valueOf(line.charAt(col));
            }
            row++;
        }
    }

    public void setNewArray(String[][] newArray) {
        this.wallArray = newArray;
    }

    public String[][] getArray() {
        return wallArray;
    }

    @Override
    public void repaint( Graphics graphics ) {
        for (x = 0; x < 39; x++) {
            for (y = 0; y < 40; y++) {
                if (wallArray[y][x].equals("1")) {
                    graphics.drawImage( unbreakableWall, x*unbreakableWall.getWidth(), y*unbreakableWall.getHeight(), observer );
                } else if (wallArray[y][x].equals("2")) {
                    graphics.drawImage( image, x*image.getWidth(), y*image.getHeight(), observer );
                }
            }
        }
    }
}
