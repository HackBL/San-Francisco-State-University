package GalacticMail.application;

import GalacticMail.application.graphics.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public abstract class AnimationPanel extends JPanel implements Runnable {
    protected final int WIDTH = 600;
    protected final int HEIGHT = 600;
    protected int speedIncreased = 3;
    protected int level = 1;
    protected int score = 1000;
    protected int curScore = 1000;
    protected int moonNum = 0;
    protected int asteroidNum = 0;
    protected int moonCounter = 0;

    protected final String BACKGROUND_IMAGE = "resources/Background.png";
    protected final String TITLE_IMAGE = "resources/Title.png";
    protected final String ASTEROID_IMAGE = "resources/Asteroid_strip180.png";
    protected final String MOON_IMAGE = "resources/Bases_strip8.png";
    protected final String EXPLOSION_IMAGE = "resources/Explosion_strip9.png";
    protected final String BACKGROUND_MUSIC = "resources/Music.mid";
    protected final String LAUNCH_SOUND = "resources/Launch.wav";
    protected final String EXPLOSION_SOUND = "resources/Explosion.wav";


    protected boolean start = false;
    protected boolean initPosition = true;
    protected boolean moonLanded = false;
    protected boolean destroy = false;
    protected boolean died = false;
    protected boolean earnPoints = false;

    protected Sound backgroundSound;
    protected Sound launchSound;
    protected Sound explosionSound;

    protected GameObject title;
    protected GameObject background;
    protected ShipObject ship;
    protected Animation animation;


    protected Sprite moons;
    protected Sprite asteroid;
    protected Sprite explosion;


    protected Dimension dimension;

    protected ArrayList< MoonAnimation > moonAnimations;
    protected ArrayList <AsteroidAnimation> asteroidAnimations;
    protected ArrayList<Integer> scoreDataList;


    public AnimationPanel() {
        try {
            this.background = new GameObject( BACKGROUND_IMAGE );
            this.title = new GameObject( TITLE_IMAGE );
            this.moons = new Sprite(MOON_IMAGE, 64);
            this.asteroid = new Sprite(ASTEROID_IMAGE, 50);
            this.explosion = new Sprite(EXPLOSION_IMAGE, 64);
            this.backgroundSound = new Sound( BACKGROUND_MUSIC );
            this.launchSound = new Sound( LAUNCH_SOUND );
            this.explosionSound = new Sound( EXPLOSION_SOUND );

            this.ship = new ShipObject();

        } catch( IOException exception ) {
            System.err.println( "Failed to load sprite." );
            exception.printStackTrace();
        }


        this.dimension = new Dimension( WIDTH, HEIGHT );

        this.asteroidAnimations = new ArrayList<>();
        this.moonAnimations = new ArrayList<>();
        this.scoreDataList = new ArrayList<>();

        this.backgroundSound.play();

    }

    @Override
    public Dimension getPreferredSize() {
        return this.dimension;
    }

    @Override
    public void paintComponent( Graphics graphics ) {
        super.paintComponent( graphics );
        int counter = 0;

        while (moonNum < 4) {
            moonAddAnimation( new MoonAnimation(moons, WIDTH, HEIGHT) );
            moonNum++;
        }

        while (asteroidNum < 4) {
            asteroidAddAnimation(new AsteroidAnimation(asteroid, WIDTH, HEIGHT, 1, false));
            asteroidNum++;
        }


        if (!start) {
            for ( int x = 0; x < WIDTH; x += title.getWidth() ) {
                for ( int y = 0; y < HEIGHT; y += title.getHeight() ) {
                    title.setX( x );
                    title.setY( y );
                    title.repaint( graphics );
                }
            }

            graphics.setColor( Color.red );
            Font font = graphics.getFont().deriveFont( 30.0f );
            graphics.setFont(font);
            graphics.drawString( "Press Enter", 250,590 );
        }


        if ( start ) {
            for ( int x = 0; x < WIDTH; x += background.getWidth() ) {
                for ( int y = 0; y < HEIGHT; y += background.getHeight() ) {
                    background.setX( x );
                    background.setY( y );
                    background.repaint( graphics );
                }
            }
        }

        if ( initPosition ) {
            moonLanded = true;
            ship.setLanded( true );

            initPosition = false;
        }


        if (moonLanded) {
            ship.setX(moonAnimations.get(moonCounter).getX());
            ship.setY(moonAnimations.get(moonCounter).getY());

            if (start)
                setScore();

            if (!ship.getLanded()) {
                moonAnimations.remove( moonCounter );
                launchSound.play();
                moonLanded = false;
            }
        }


        while (counter < moonAnimations.size()) {
            if (ship.getRectangle().intersects(moonAnimations.get(counter).getRectangle()) && !moonLanded) {
                moonLanded = true;
                ship.setLanded(true);
                moonCounter = counter;
                earnPoints = true;

            }

            moonAnimations.get(counter).repaint(graphics);
            counter++;
        }

        counter = 0;
        AsteroidAnimation asteroidAnimation;

        while ( counter < asteroidAnimations.size() ) {
            asteroidAnimation = asteroidAnimations.get( counter );


            if (ship.getRectangle().intersects(asteroidAnimation.getRectangle() ) && !moonLanded && !ship.getLanded()) {
                this.destroy = true;
            }

            asteroidAnimation.repaint(graphics);
            counter++;
        }

        animation =  new Animation(explosion, WIDTH, HEIGHT, ship.getX(),ship.getY(),10,false);

        if (destroy) {
            animation.repaint( graphics );
            ship.setX(WIDTH);
            ship.setY(HEIGHT);
            explosionSound.play();

            this.died = true;

            scoreDataList.add(curScore);

        }

        if ( died ) {
            this.destroy = false;
            this.initPosition = true;
            this.moonLanded = false;
            this.earnPoints = false;
            this.ship.setLanded( false );

            highScore();
            moonAnimations.clear();
            asteroidAnimations.clear();

            moonNum = 0;
            asteroidNum = 0;
            curScore = 1000;
            level = 1;
            ship.setInitStep();

            for (MoonAnimation moonAnimation : moonAnimations) {
                moonAnimation.setInitSpeed();
            }

            for (AsteroidAnimation asteroidAnimation1 : asteroidAnimations) {
                asteroidAnimation1.setInitSpeed();
            }

        }

        if ( died && start ) {
            int scoreY = 300;
            for (int listSize = 0; listSize < scoreDataList.size(); listSize++) {
                    graphics.setColor(Color.red);
                    Font font = graphics.getFont().deriveFont(20.0f);
                    graphics.setFont(font);
                    graphics.drawString(listSize+1 + "." +
                            " " + Integer.toString(scoreDataList.get(listSize)), 250, scoreY);
                    scoreY = scoreY + 30;
            }

            graphics.setColor( Color.red );
            Font font = graphics.getFont().deriveFont( 30.0f );
            graphics.setFont(font);
            graphics.drawString( "Press Enter", 250,590 );


        }

        if ( moonAnimations.size() == 1 && ship.getLanded()) {
            moonAnimations.get( 0 ).setSpeed(speedIncreased);

            for (int moonNum = 0; moonNum < 3; moonNum++) {
                MoonAnimation moonAnimation = new MoonAnimation(moons, WIDTH, HEIGHT);
                moonAnimation.setSpeed(speedIncreased);
                moonAddAnimation( moonAnimation );
            }

            for (int asteroidNum = 0; asteroidNum < 4; asteroidNum++) {
                asteroidAnimations.get(asteroidNum).setSpeed(speedIncreased);
            }
            ship.setStep(speedIncreased);
            level++;
        }


        graphics.setColor( Color.white );
        Font font = graphics.getFont().deriveFont( 20.0f );
        graphics.setFont(font);
        graphics.drawString( "Level: " + Integer.toString( level ), 250, 20 );
        graphics.drawString( "Score: " + Integer.toString( getScore() ), 10, 20 );

        ship.forwardMove();
        ship.setX( ship.getNextX() );
        ship.setY( ship.getNextY() );
        ship.outOfBound();

        ship.repaint( graphics );

    }


    public void moonAddAnimation( MoonAnimation animation ) {
        moonAnimations.add( animation );
    }

    public void  asteroidAddAnimation( AsteroidAnimation animation ) {
        asteroidAnimations.add( animation );
    }

    private void setScore() {
        if (earnPoints) {
            curScore += score;
            earnPoints = false;
        }
        curScore--;

        if ( curScore < 0 ) {
            start = false;
        }
    }

    public int getScore() {
        return curScore;
    }

    public void highScore() {
        for (int index1 = 0; index1 < scoreDataList.size()-1; index1 ++) {
            for (int index2 = index1+1; index2 < scoreDataList.size(); index2++) {

                if (scoreDataList.get(index2) > scoreDataList.get(index1) ) {
                    Collections.swap(scoreDataList, index2,index1);
                }
            }
        }

        if (scoreDataList.size() >= 5) {
            for (int size = 5; size < scoreDataList.size(); size++ ){
                scoreDataList.remove(size);
            }
        }
    }
}
