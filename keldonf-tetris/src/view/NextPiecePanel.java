/*
 * TCSS 305 - Fall 2016 Assignment 6 - Tetris
 *
 */

package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.JPanel;

import model.Block;
import model.Point;
import model.TetrisPiece;

/**
 * This class is used to show the next piece and the controls.
 * 
 * @author Keldon Fischer
 * @version 12/9/2016
 *
 */
public class NextPiecePanel extends JPanel implements Observer, PropertyChangeListener {

    /**
     * This is the level/Timer lever
     * There is also one in TetrisGUI, if you want it to work properly
     * you will need to change it in both classes.
     */
    public static final int LEVEL_AND_TIMER = 5;
    
    /**
     * Initializes the Serial Version UID for this class.
     */
    private static final long serialVersionUID = 123378620593940727L;

    /**
     * Initializes the number 10.
     */
    private static final int TEN = 10;

    /**
     * Initializes the number 1.
     */
    private static final int ONE = 1;

    /**
     * Initializes the number 20.
     */
    private static final int TWENTY = 20;
    
    /**
     * Initializes the number 30.
     */
    private static final int THIRTY = 30;
    
    /**
     * Initializes the number 35.
     */
    private static final int THIRTY_FIVE = 35;
    
    /**
     * Initializes the number 310.
     */
    private static final int THREE_TEN = 310;
    
    /**
     * Initializes the number 325.
     */
    private static final int THREE_TWENTY_FIVE = 325;
    
    /**
     * Initializes the number 20.
     */
    private static final int SQUARE_SIZE = 20;

    /**
     * Initializes the number 220.
     */
    private static final int KEY_LOCATION = 260;

    /**
     * Initializes the panel width.
     */
    private static final int PANEL_WIDTH = 130;

    /**
     * Initializes the next piece.
     */
    private TetrisPiece myTP;
    
    /**
     * Initializes lines cleared.
     */
    private int myLinesCleared;
    
    /**
     * Initializes total lines cleared.
     */
    private int myTotalLinesCleared;

    /**
     * Initializes the level number.
     */
    private int myLevelNumber;

    /**
     * Initializes the score.
     */
    private int myHighScore;
    
    /**
     * Initializes a boolean for the secret feature.
     */
    private boolean mySecretFeatureOne;

    /**
     * This is the default constructor for NextPiecePanel.
     */
    public NextPiecePanel() {
        super();
        setPreferredSize(new Dimension(PANEL_WIDTH, ONE));
        setBackground(Color.GRAY);
        myLevelNumber = 1;
        myLinesCleared = 0;
        myTotalLinesCleared = 0;
        myHighScore = 0;
        mySecretFeatureOne = false;
    }

    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2D = (Graphics2D) theGraphics;

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        // ------------- Draws Prev Box -------------
        final Rectangle2D prevBox = new Rectangle2D.Double(5, 5, 120, 100);
        g2D.setPaint(Color.WHITE);
        if (mySecretFeatureOne) {
            g2D.setPaint(Color.BLACK);
        }
        g2D.fill(prevBox);
        g2D.setPaint(Color.BLACK);
        g2D.draw(prevBox);
        // ------------- Tetris Piece ------------------------------
        if (null != myTP) {
            final Point[] block = myTP.getPoints();
            
            final int three = 3;
            final int four = 4;
            final int twentyFive = 25;
            int widthPosition = THIRTY;
            final int howRounded = 5;
            int heightPosition = THREE_TWENTY_FIVE;
            
            
            for (final Point p : block) {
                // not centered
                if (myTP.getWidth() == four) {
                    widthPosition = twentyFive;
                    heightPosition = THREE_TEN;
                }
                if (myTP.getWidth() == three && myTP.getBlock() != Block.O) {
                    widthPosition = THIRTY_FIVE;
                }
                final Rectangle2D tetrisBlock =
                                new Rectangle2D.Double(p.x() * SQUARE_SIZE + widthPosition,
                                                       (SQUARE_SIZE - p.y()) * SQUARE_SIZE
                                                                          - heightPosition,
                                                       SQUARE_SIZE, SQUARE_SIZE);
                
                g2D.setPaint(enumColor(myTP.getBlock())); // adds color to the pieces
                g2D.fill(tetrisBlock);
                g2D.setPaint(Color.BLACK); // adds outline
                g2D.draw(tetrisBlock);
                g2D.drawRoundRect(p.x() * SQUARE_SIZE + widthPosition,
                                  (SQUARE_SIZE - p.y()) * SQUARE_SIZE - heightPosition,
                                  SQUARE_SIZE, SQUARE_SIZE, howRounded, howRounded);
                
            }
        }

        //---------------- Status Box ------------------------------------------
        
        final Rectangle2D statBox = new Rectangle2D.Double(5, 120, 120, 110);
        g2D.setPaint(Color.WHITE);
        g2D.fill(statBox);
        g2D.setPaint(Color.BLACK);
        g2D.draw(statBox);
        g2D.drawString(levelString(), TEN,
                       KEY_LOCATION - TWENTY - TWENTY - TWENTY - TWENTY - TWENTY - TWENTY);
        g2D.drawString("Your Current Score", TEN,
                       KEY_LOCATION - TWENTY - TWENTY - TWENTY - TWENTY - TWENTY);
        g2D.drawString(highScoreString(), TEN,
                       KEY_LOCATION - TWENTY - TWENTY - TWENTY - TWENTY);
        g2D.drawString("Total Lines Cleared", TEN,
                       KEY_LOCATION - TWENTY - TWENTY - TWENTY);
        g2D.drawString(totalLinethatAreCleared(), TEN,
                       KEY_LOCATION - TWENTY - TWENTY);

        // -------------------- Control Box --------------
        final Rectangle2D textBox = new Rectangle2D.Double(5, 240, 120, 150);
        g2D.setPaint(Color.WHITE);
        g2D.fill(textBox);
        g2D.setPaint(Color.BLACK);
        g2D.draw(textBox);
        g2D.drawString("Left : A or <", TEN, KEY_LOCATION);
        g2D.drawString("Right : D or >", TEN, KEY_LOCATION + TWENTY);
        g2D.drawString("Rotate CCW : W or ^", TEN, KEY_LOCATION + TWENTY + TWENTY);
        g2D.drawString("Rotate CW : S or v", TEN, KEY_LOCATION + TWENTY + TWENTY + TWENTY);
        g2D.drawString("Down : Ctrl", TEN, KEY_LOCATION + TWENTY + TWENTY + TWENTY + TWENTY);
        g2D.drawString("Drop : Space", TEN,
                       KEY_LOCATION + TWENTY + TWENTY + TWENTY + TWENTY + TWENTY);
        g2D.drawString("Pause : P", TEN,
                       KEY_LOCATION + TWENTY + TWENTY + TWENTY + TWENTY + TWENTY + TWENTY);

    } // end of paint

    /**
     * This string calls the current level.
     * @return a String of what level your on level.
     */
    private String levelString() {
        return "Level: " + myLevelNumber;
    }

    /**
     * This string calls the current Score.
     * @return a String of total score.
     */
    private String highScoreString() {
        final int eight = 8;
        return NumberFormat.getNumberInstance(Locale.US).format(myHighScore - eight);
    }
    
    /**
     * This string calls the total lines currently cleared.
     * @return a String of total lines cleared.
     */
    private String totalLinethatAreCleared() {
        final Integer tlc = myTotalLinesCleared;
        return  tlc.toString();
    }
    
    /**
     * This grabs a enum value and spits out its color.
     * @param theBlock comes from the Block class.
     * @return color, depends on what enum is in the parameter.
     */
    public Color enumColor(final Block theBlock) {
        final Map<Block, Color> colorList = new HashMap<Block, Color>();
        if (mySecretFeatureOne) {
            final int red = new Random().nextInt(255);
            final int green = new Random().nextInt(255);
            final int blue = new Random().nextInt(255);
            final Color randomColor = new Color(red, green, blue);
            colorList.put(Block.I, randomColor.brighter());
            colorList.put(Block.J, randomColor.brighter());
            colorList.put(Block.L, randomColor.brighter());
            colorList.put(Block.O, randomColor.brighter());
            colorList.put(Block.S, randomColor.brighter());
            colorList.put(Block.T, randomColor.brighter());
            colorList.put(Block.Z, randomColor.brighter());
            colorList.put(Block.EMPTY, randomColor.brighter());
            
        } else {
            colorList.put(Block.I, Color.BLUE);
            colorList.put(Block.J, Color.ORANGE);
            colorList.put(Block.L, Color.GREEN.darker());
            colorList.put(Block.O, Color.RED);
            colorList.put(Block.S, Color.PINK);
            colorList.put(Block.T, Color.MAGENTA);
            colorList.put(Block.Z, Color.CYAN);
        }
        return colorList.get(theBlock);
    }

    @Override
    public void update(final Observable theObservable, final Object theObject) {
        final int one = 1;
        final int two = 2;
        final int three = 3;
        final int four = 4;

        if (theObject instanceof TetrisPiece) {
            myTP = (TetrisPiece) theObject;
            myHighScore += four;
            repaint();
        }
        if (theObject instanceof Integer[]) {
            final Integer[] intArray = (Integer[]) theObject;
            final int oneLineCleared = 40;
            final int twoLineCleared = 100;
            final int threeLineCleared = 300;
            final int fourLineCleared = 1200;
            if (intArray.length == one) {
                myHighScore += oneLineCleared * myLevelNumber;
            }
            if (intArray.length == two) {
                myHighScore += twoLineCleared * myLevelNumber;
            }
            if (intArray.length == three) {
                myHighScore += threeLineCleared * myLevelNumber;
            }
            if (intArray.length == four) {
                myHighScore += fourLineCleared * myLevelNumber;
            }
            for (int i = 0; i < intArray.length; i++) {

                myLinesCleared++;
                myTotalLinesCleared++;
                if (myLinesCleared == LEVEL_AND_TIMER) {
                    myLevelNumber++;
                    myLinesCleared = 0;
                }
            }

        }
    }

    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if ("GameIsGoing".equals(theEvent.getPropertyName())) {
            myLinesCleared = 0;
            myTotalLinesCleared = 0;
            myLevelNumber = 1;
            myHighScore = 0;
        }
        if ("Secret".equals(theEvent.getPropertyName())) {
            mySecretFeatureOne = (boolean) theEvent.getNewValue();
        }
        
    }

} // end of class
