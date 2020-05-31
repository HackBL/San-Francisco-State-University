package TankGame.application;

import javax.swing.*;

public class GameFrame extends JFrame {

  public GameFrame( AnimationPanel panel ) {
        setTitle( "Tanks Wars" );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setResizable( false );
        add( panel );
        pack();

        Thread thread = new Thread( panel );
        thread.start();
        setVisible( true );
    }
}