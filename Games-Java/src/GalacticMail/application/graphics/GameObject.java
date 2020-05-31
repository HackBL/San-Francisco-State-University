package GalacticMail.application.graphics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class GameObject {
    protected int x;
    protected int y;

    protected BufferedImage image;
    protected ImageObserver observer;

    public GameObject(String resourceLocation ) throws IOException {
        this( resourceLocation, null );
    }

    public GameObject(String resourceLocation, ImageObserver observer ) throws IOException {
        x = 0;
        y = 0;


        image = ImageIO.read( new File( resourceLocation ));
        this.observer = observer;
    }

    public void setX( int x ) {
        this.x = x;
    }

    public void setY( int y ) {
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.image.getWidth();
    }

    public int getHeight() {
        return this.image.getHeight();
    }

    public void repaint( Graphics graphics ) {
        graphics.drawImage( image, x, y, observer );
    }


}
