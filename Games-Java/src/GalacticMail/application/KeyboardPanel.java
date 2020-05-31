package GalacticMail.application;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardPanel extends AnimationPanel implements KeyListener {
    private boolean  leftPress, rightPress, spacePress, enterPress;


    public KeyboardPanel() {
        super();
        this.addKeyListener(this);
        this.setFocusable(true);
        new Thread(this).start();
    }

    @Override
    public void run() {
        ship.setDegree();

        while ( true ) {
            if ( start ) {
                if ( leftPress ) {
                    ship.rotateLeft();
                }

                if ( rightPress ) {
                    ship.rotateRight();
                }

                if ( spacePress ) {
                    ship.setLanded( false );
                }
            }

            if ( enterPress ) {
                initPosition = true;
                start = true;
                died = false;
            }

            this.repaint();

            try {
                Thread.sleep( 100 );
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void keyPressed( KeyEvent e ) {

        if ( e.getKeyCode() == KeyEvent.VK_LEFT ) {
            leftPress = true;
        }
        if ( e.getKeyCode() == KeyEvent.VK_RIGHT ) {
            rightPress = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            spacePress = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {

            enterPress = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPress = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPress = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            spacePress = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            enterPress = false;
        }
    }


    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

}
