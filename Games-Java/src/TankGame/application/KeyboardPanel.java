package TankGame.application;

import TankGame.application.graphics.Animation;
import TankGame.application.graphics.BulletAnimation;
import TankGame.application.graphics.Sound;
import TankGame.application.graphics.Sprite;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class KeyboardPanel extends AnimationPanel implements KeyListener {
    private Sprite explosion;
    private Sprite bullet;
    private Sound sound;
    private boolean upPress, downPress, leftPress, rightPress, enterPress;
    private boolean wPress, sPress, aPress, dPress, spacePress;

    public KeyboardPanel() {
        super();
        this.addKeyListener( this );
        this.setFocusable( true );
        new Thread( this ).start();

        try {
            this.explosion = new Sprite( "resources/Explosion_small_strip6.png", 32 );
            this.bullet = new Sprite( "resources/Shell_basic_strip60.png", 24 );
            this.sound = new Sound("resources/turret.wav");
        } catch ( IOException exception ) {
            exception.printStackTrace();
        }
    }

    @Override
    public void run() {
        tank1.setDegree();
        tank2.setDegree();

        do {
            if ( ! ( player1.endGame() || player2.endGame() ) ) {

                    if ( leftPress ) {
                    tank2.rotateLeft();
                }

                if ( rightPress ) {
                    tank2.rotateRight();
                }

                if ( upPress ) {
                    tank2.forwardMove();
                    if ( !( ( tank2.collisionToWall( breakableWall ) ) || ( tank2.collisionToTank( tank1 ) ) ) ) {
                        tank2.setX( tank2.getNextX() );
                        tank2.setY( tank2.getNextY() );
                    }
                }

                if ( downPress ) {
                    tank2.reverseMove();
                    if ( !( ( tank2.collisionToWall( breakableWall ) ) || ( tank2.collisionToTank( tank1 ) ) ) ) {
                        tank2.setX( tank2.getNextX() );
                        tank2.setY( tank2.getNextY() );
                    }
                }

                if ( enterPress ) {
//                    addAnimation( new Animation( this.explosion, tank2.fireStartX(), tank2.fireStartY(),
//                            1, false ) );
                    addAnimation( new BulletAnimation( this, tank2, tank1, player2, player1,
                            breakableWall, this.bullet, tank2.fireStartX(), tank2.fireStartY(), 5, false ) );

                    sound.play();
                }

                if ( aPress ) {
                    tank1.rotateLeft();
                }

                if ( dPress ) {
                    tank1.rotateRight();
                }

                if ( wPress ) {
                    tank1.forwardMove();
                    if ( !( ( tank1.collisionToWall( breakableWall ) ) || ( tank1.collisionToTank( tank2 ) ) ) ) {
                        tank1.setX( tank1.getNextX() );
                        tank1.setY( tank1.getNextY() );
                    }
                }

                if ( sPress ) {
                    tank1.reverseMove();

                    if ( !( ( tank1.collisionToWall( breakableWall ) ) || ( tank1.collisionToTank( tank2 ) ) ) ) {
                        tank1.setX( tank1.getNextX() );
                        tank1.setY( tank1.getNextY() );
                    }
                }

                if ( spacePress ) {
//                    addAnimation( new Animation( this.explosion, tank1.fireStartX(), tank1.fireStartY(),
//                            1, false ) );
                    addAnimation( new BulletAnimation( this, tank1, tank2, player1, player2,
                            breakableWall, this.bullet, tank1.fireStartX(), tank1.fireStartY(), 5, false ) );

                    sound.play();
                }
            }

            this.repaint();

            try {
                Thread.sleep( 100 );
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }

        } while ( true );

    }

    @Override
    public void keyPressed( KeyEvent e ) {
        if ( e.getKeyCode() == KeyEvent.VK_LEFT ) {
            leftPress = true;
        }
        if ( e.getKeyCode() == KeyEvent.VK_RIGHT ) {
            rightPress = true;
        }
        if ( e.getKeyCode() == KeyEvent.VK_UP ) {
            upPress = true;
        }
        if ( e.getKeyCode() == KeyEvent.VK_DOWN ) {
            downPress = true;
        }
        if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
            enterPress = true;
        }
        if ( e.getKeyCode() == KeyEvent.VK_A ) {
            aPress = true;
        }
        if ( e.getKeyCode() == KeyEvent.VK_D ) {
            dPress = true;
        }
        if ( e.getKeyCode() == KeyEvent.VK_W ) {
            wPress = true;
        }
        if ( e.getKeyCode() == KeyEvent.VK_S ) {
            sPress = true;
        }
        if ( e.getKeyCode() == KeyEvent.VK_SPACE ) {
            spacePress = true;
        }
    }

    @Override
    public void keyTyped( KeyEvent e ) {}

    @Override
    public void keyReleased( KeyEvent e ) {
        if ( e.getKeyCode() == KeyEvent.VK_LEFT ) {
            leftPress = false;
        }
        if ( e.getKeyCode() == KeyEvent.VK_RIGHT ) {
            rightPress = false;
        }
        if ( e.getKeyCode() == KeyEvent.VK_UP ) {
            upPress = false;
        }
        if ( e.getKeyCode() == KeyEvent.VK_DOWN ) {
            downPress = false;
        }
        if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
            enterPress = false;
        }
        if ( e.getKeyCode() == KeyEvent.VK_A ) {
            aPress = false;
        }
        if ( e.getKeyCode() == KeyEvent.VK_D ) {
            dPress = false;
        }
        if ( e.getKeyCode() == KeyEvent.VK_W ) {
            wPress = false;
        }
        if ( e.getKeyCode() == KeyEvent.VK_S ) {
            sPress = false;
        }
        if ( e.getKeyCode() == KeyEvent.VK_SPACE ) {
            spacePress = false;
        }
    }
}
