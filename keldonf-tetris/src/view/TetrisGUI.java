/*
 * TCSS 305 - Fall 2016
 * Assignment 6 - Tetris
 *
 */
package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import model.Board;

/**
 * 
 * @author Keldon Fischer
 * @version 12/9/2016
 *
 * Here is the APA citation for the song I use in the background.
 * 
 * Lost Woods Dubstep Remix [Advertisement]. (2011, April 10). YouTube. 
 * Retrieved December 8, 2016, from https://www.youtube.com/watch?v=NU75uz0b8EU
 */
public class TetrisGUI extends JFrame implements Observer {
    
    /**
     * This is the level/Timer lever
     * There is also one in TetrisGUI, if you want it to work properly
     * you will need to change it in both classes.
     */
    public static final int LEVEL_AND_TIMER = 5;
    
    /**
     * Change this to increase/decrease the speed.
     * (Speed = Delay / LEVEL_UP_SPEED)
     */
    public static final double LEVEL_UP_SPEED = 1.5;
    
    /**
     * Initializes the delay of the timer.
     */
    public static final int STARTING_DELAY = 1000;
    
    /**
     * Initializes the timer's initial delay at the beginning of the game.
     */
    public static final int INITIAL_DELAY = 100;
    
    /**
     * Initializes the Serial Version UID for this class.
     */
    private static final long serialVersionUID = -7732622655645516733L;
    
    /**
     * Initializes the timer's delay.
     */
    private int myMoveDelay;
    
    /**
     * Initializes the Board.
     */
    private Board myBoard = new Board();
    
    /**
     * Initializes a boolean that tells if its game over or not.
     */
    private boolean myIsGameOver;
    
    /**
     * Initializes myOption, this is used for my show confirm dialog.
     */
    private int myOption;
    
    /**
     * Initializes myPanel, this is the panel that I draw my Tetris game.
     */
    private final DrawingPanel myPanel = new DrawingPanel();
    
    /**
     * Initializes myNextPiecePanel, this is the panel the shows the
     * next piece and the controls too.
     */
    private NextPiecePanel myNextPiecePanel;
    
    /**
     * Initializes the Timer.
     */
    private final Timer myTimer;
    
    /**
     * Initializes lines cleared.
     */
    private int myLinesCleared;
    
    /**
     * Initializes the a boolean that says if grid is on or off.
     */
    private boolean myGridOn;
    
    /**
     * Initializes a boolean for the secret feature.
     */
    private boolean mySecretFeatureOne;
    
    
    /**
     * This is the default constructor for the TetrisGUI.
     */
    public TetrisGUI() {
        super();
        myIsGameOver = false;
        myMoveDelay = STARTING_DELAY;
        myTimer = new Timer(myMoveDelay, new TimerListener());
        myTimer.setInitialDelay(INITIAL_DELAY);
        myNextPiecePanel = new NextPiecePanel();
        myLinesCleared = 0;
        myGridOn = false;
        mySecretFeatureOne = false;

        
    }

    /**
     * This method starts the GUI.
     */
    public void start() {
        
//This is where I add music.
//        try {
//            final Clip clip = AudioSystem.getClip();
//            final AudioInputStream ais = 
//                            AudioSystem.getAudioInputStream(new File("audio/LostWoods.wav"));
//            clip.open(ais);
//            clip.loop(Clip.LOOP_CONTINUOUSLY);
//        } catch (final LineUnavailableException 
//                        | UnsupportedAudioFileException 
//                        | IOException e) {
//            e.printStackTrace();
//        }
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        add(myPanel , BorderLayout.CENTER);
        myNextPiecePanel = new NextPiecePanel();
        
        
        myBoard.addObserver(this);
        myBoard.addObserver(myPanel);
        myBoard.addObserver(myNextPiecePanel);
        myBoard.newGame();
        this.addKeyListener(new Controller());
        this.addKeyListener(new AdvanceController());
        this.addPropertyChangeListener(myPanel);
        this.addPropertyChangeListener(myNextPiecePanel);
        this.setMinimumSize(this.getSize());
        this.setResizable(false);
        pack();
        setVisible(true);
    }
    
    /**
     * This is sort of like my start method but just resets the 
     * game, it doesn't reload the panels.
     */
    public void startOver() {
        firePropertyChange("GameIsGoing", false, true);
        myBoard.newGame();
        add(myNextPiecePanel, BorderLayout.EAST);
        pack();
        myTimer.setInitialDelay(INITIAL_DELAY);
        myTimer.setDelay(STARTING_DELAY);
        myLinesCleared = 0;
        myTimer.start();
    }
    
    
    @Override
    public void update(final Observable theObservable, final Object theObject) {
        myBoard = (Board) theObservable;
        
        if (theObject instanceof Boolean) {
            myIsGameOver = (Boolean) theObject;
        }
        if (theObject instanceof Integer[]) {
            final Integer[] intList = (Integer[]) theObject;
            for (int i = 0; i < intList.length; i++) {
                
                myLinesCleared++;
                if (myLinesCleared == LEVEL_AND_TIMER) {
                    
                    myMoveDelay /= LEVEL_UP_SPEED * intList.length;
                    myTimer.setDelay(myMoveDelay);
                    myLinesCleared = 0;
                }
            }
        }
        repaint();

    }
    
    /**
     * This method sets pause to either true or false.
     */
    public void setPause() {
        final String pause = "Pause";
        if (myTimer.isRunning()) {
            myTimer.stop();
            firePropertyChange(pause, true, false);
        } else {
            myTimer.start();
            firePropertyChange(pause, false, true);
        }
    }
    

    /**
     * This method sets grid to either true or false.
     * @param theGridOn grabs myGridOn to see what the boolean is already set to.
     */
    public void setGrid(final boolean theGridOn) {
        final String grid = "Grid";
        if (theGridOn) {
            firePropertyChange(grid, true, false);
            myGridOn = false;
        } else {
            firePropertyChange(grid, false, true);
            myGridOn = true;
        }
    }
    
    /**
     * This controller has all of my part B key's
     * Also the complexity was to high so I separated my controller into
     * to parts.
     * 
     * @author Keldon Fischer
     * @version 12/9/2016
     */
    public class AdvanceController extends KeyAdapter {

        @Override
        public void keyPressed(final KeyEvent theEvent) {
            if (theEvent.getKeyCode() == KeyEvent.VK_P) {
                setPause();
            }
            if (theEvent.getKeyCode() == KeyEvent.VK_N) {
                startOver();
            }
            if (theEvent.getKeyCode() == KeyEvent.VK_G) {
                setGrid(myGridOn);
            }
            if (theEvent.getKeyCode() == KeyEvent.VK_1) {
                final String secret = "Secret";
                if (mySecretFeatureOne) {
                    firePropertyChange(secret, true, false);
                    mySecretFeatureOne = false;
                } else {
                    firePropertyChange(secret, false, true);
                    mySecretFeatureOne = true;
                }
            }
            
        }
        
    }
    
    /**
     * Controller is where I perform all of my key listeners for Part A.
     * @author Keldon Fischer
     * @version 12/2/2016
     *
     */
    public class Controller extends KeyAdapter {

        @Override
        public void keyPressed(final KeyEvent theEvent) {
            // --- Basic Control ----
            if (canIMovePieces()) {
                
                if (movingLeft(theEvent)) {
                    myBoard.left();
                }
                if (movingRight(theEvent)) {
                    myBoard.right();
                }
                if (movingCCW(theEvent)) {
                    myBoard.rotateCCW();
                }
                if (movingCW(theEvent)) {
                    myBoard.rotateCW();
                }
                if (theEvent.getKeyCode() == KeyEvent.VK_CONTROL) {
                    myBoard.step();
                }
                if (theEvent.getKeyCode() == KeyEvent.VK_SPACE) {
                    myBoard.drop();
                }
            }
        }
        
        /**
         * Checks to see if I can move a piece.
         * @return true if I can move a piece.
         */
        public boolean canIMovePieces() {
            return myTimer.isRunning() && !myIsGameOver;   
        }
        
        
        /**
         * Moves the Tetris piece left.
         * @param theEvent is the KeyListener.
         * @return Left Key either "A" or "Left Arrow".
         */
        public Boolean movingLeft(final KeyEvent theEvent) {
            return theEvent.getKeyCode() == KeyEvent.VK_A 
                                || theEvent.getKeyCode() == KeyEvent.VK_LEFT;
            
        }
        
        /**
         * Moves the Tetris piece right.
         * @param theEvent is the KeyListener.
         * @return Left Key either "D" or "Right Arrow".
         */
        public Boolean movingRight(final KeyEvent theEvent) {
            return theEvent.getKeyCode() == KeyEvent.VK_D 
                                || theEvent.getKeyCode() == KeyEvent.VK_RIGHT;
            
        }
        
        /**
         * Moves the Tetris piece CCW.
         * @param theEvent is the KeyListener.
         * @return Left Key either "W" or "Up Arrow".
         */
        public Boolean movingCCW(final KeyEvent theEvent) {
            return theEvent.getKeyCode() == KeyEvent.VK_W 
                                || theEvent.getKeyCode() == KeyEvent.VK_UP;
            
        }
        
        /**
         * Moves the Tetris piece CW.
         * @param theEvent is the KeyListener.
         * @return Left Key either "S" or "Down Arrow".
         */
        public Boolean movingCW(final KeyEvent theEvent) {
            return theEvent.getKeyCode() == KeyEvent.VK_S 
                                || theEvent.getKeyCode() == KeyEvent.VK_DOWN;
            
        }
        
        
    } //end of KeyListener
    
    /**
     * This is my timer listener.
     * @author Keldon Fischer
     * @version 12/2/2016
     *
     */
    private class TimerListener implements ActionListener {
        
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            myBoard.step();
            if (myIsGameOver) {
                myTimer.stop();
                myOption = JOptionPane.showConfirmDialog(null,
                                                       "GameOver!!!\n"
                                                       + "Press \"OK\" to start new game.",
                                                       "I'm sorry you lose.",
                                                       JOptionPane.PLAIN_MESSAGE);
                if (myOption == JOptionPane.OK_OPTION) {
                    myIsGameOver = false;
                    startOver();

                }
            }
        }
    } // end of TimerListener


    
}
