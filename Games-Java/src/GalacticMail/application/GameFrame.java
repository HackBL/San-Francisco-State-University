package GalacticMail.application;

import javax.swing.*;

public class GameFrame extends JFrame {

  public GameFrame(AnimationPanel panel ) {
        setTitle( "Galactic Mail" );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setResizable( false );
        add( panel );
        pack();

        Thread thread = new Thread( panel );
        thread.start();
        setVisible( true );
    }
}