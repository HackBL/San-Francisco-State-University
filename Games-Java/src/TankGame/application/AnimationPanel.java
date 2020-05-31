package TankGame.application;

import TankGame.application.graphics.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public abstract class AnimationPanel extends JPanel implements Runnable {
    protected final int WIDTH = 1250;
    protected final int HEIGHT = 1280;
    protected final int SET_HEIGHT = 960;
    protected final String BACKGROUND_IMAGE = "resources/TankBackground.png";
    protected final String TANK_IMAGE = "resources/Tank_blue_basic_strip60.png";
    protected final String BACKGROUND_MUSIC = "Resources/Fantasy_Game_Background.mp3";

    protected Dimension dimension;
    protected GameObject background;
    protected WallObject breakableWall;
    protected TankObject tank1,tank2;
    protected Players player1;
    protected Players player2;
    protected Sound backgroundBack;
    protected ArrayList< Animation > animations;
    protected BufferedImage miniImage = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
    protected BufferedImage tank1ViewImg = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
    protected BufferedImage tank2ViewImg = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
    protected Graphics miniGraphic ;

    public AnimationPanel() {
        try {
          this.background = new GameObject( BACKGROUND_IMAGE );
          this.breakableWall = new WallObject("resources/Blue_wall2.png");
          this.tank1 = new TankObject( TANK_IMAGE);
          this.tank2 = new TankObject( TANK_IMAGE);
          this.breakableWall.setArray();
          this.backgroundBack = new Sound(BACKGROUND_MUSIC);
          player1 = new Players(tank1,breakableWall,"3");
          player2 = new Players(tank2,breakableWall,"4");
          player1.setInitPosition();
          player2.setInitPosition();
        } catch( IOException exception ) {
          System.err.println( "Failed to load sprite." );
          exception.printStackTrace();
        }
        this.animations = new ArrayList<>();
        this.dimension = new Dimension( WIDTH, SET_HEIGHT );


        backgroundBack.play();
    }

    @Override
    public Dimension getPreferredSize() {
         return this.dimension;
    }

    @Override
    public void paintComponent( Graphics graphics ) {
        super.paintComponent( graphics );
        miniGraphic = miniImage.createGraphics();

        for( int x = 0; x < WIDTH; x += background.getWidth() ) {
            for( int y = 0; y < HEIGHT; y+= background.getHeight() ) {
                background.setX( x );
                background.setY( y );
                background.repaint(miniGraphic);
            }
        }

        Animation animation;
        for( int counter = 0; counter < animations.size(); counter++ ) {
            animation = animations.get( counter );

            if( animation.isStopped() ) {
                animations.remove( counter );
            } else {
                animation.repaint( miniGraphic );
            }
        }

        breakableWall.repaint(miniGraphic);
        tank1.repaint( miniGraphic );
        tank2.repaint( miniGraphic );
        miniGraphic = graphics;

        tank1ViewImg = miniImage.getSubimage(getTankViewX(tank1), getTankViewY(tank1), WIDTH/2, SET_HEIGHT);
        tank2ViewImg = miniImage.getSubimage(getTankViewX(tank2), getTankViewY(tank2), WIDTH/2, SET_HEIGHT);

        miniGraphic.drawImage(tank1ViewImg,0,0,WIDTH/2-1,SET_HEIGHT,this);
        miniGraphic.drawImage(tank2ViewImg,WIDTH/2,0,WIDTH/2,SET_HEIGHT,this);

        miniGraphic.drawImage(miniImage, 480, 550, 300, 300,this);

        player1.paintHealth(miniGraphic, 5, SET_HEIGHT-35);
        player2.paintHealth(miniGraphic, WIDTH/2+10, SET_HEIGHT-35);

        player1.paintLives(miniGraphic, 230, SET_HEIGHT-35);
        player2.paintLives(miniGraphic, WIDTH/2+240, SET_HEIGHT-35 );

        player1.paintPoint(miniGraphic,WIDTH/2-200, SET_HEIGHT-10);
        player2.paintPoint(miniGraphic,WIDTH-200, SET_HEIGHT-10);
    }

    public int getTankViewX(TankObject tanks) {
        int viewX;
        viewX = tanks.getX() - WIDTH/4;

        if (viewX < 0) {
            viewX = 0;
        }

        if (viewX > (WIDTH/2)) {
            viewX = (WIDTH/2);
        }
        return viewX;
    }

    public int getTankViewY(TankObject tanks) {
        int viewY;
        viewY = tanks.getY() - SET_HEIGHT/2;

        if (viewY < 0) {
            viewY = 0;
        }

        if (viewY > (HEIGHT - SET_HEIGHT)) {
            viewY = (HEIGHT - SET_HEIGHT);
        }
        return viewY;
    }

    public void addAnimation( Animation animation ) {
        animations.add( animation );
    }


}
