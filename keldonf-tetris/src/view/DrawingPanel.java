/*
 * TCSS 305 - Fall 2016
 * Assignment 6 - Tetris
 *
 */
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.JPanel;

import model.Block;
import model.Board;

/**
 * This class is use to show the Tetris game.
 * @author Keldon Fischer
 * @version 12/9/2016
 *
 */
public class DrawingPanel extends JPanel implements Observer, PropertyChangeListener {
    
    /**
     * Initializes the Serial Version UID for this class.
     */
    private static final long serialVersionUID = -3464866898872616797L;

    /**
     * Initializes the size.
     */
    private static final int SIZE = 20;
    
    /**
     * Initializes a list of block arrays, I use this to store all
     * of the blocks on the board.
     */
    private final List<Block[]> myBlockList = new ArrayList<Block[]>();

    /**
     * Initializes the board.
     */
    private Board myBoard;
    
    /**
     * Initializes the grid.
     */
    private boolean myGridOn;
    
    /**
     * Initializes if the game is going or not.
     */
    private boolean myGameIsGoing;
    
    /**
     * Initializes if the game is pause or not.
     */
    private boolean myPause;
    
    /**
     * Initializes a boolean for the secret feature.
     */
    private boolean mySecretFeatureOne;

    /**
     * This is the default constructor for DrawingPanel.
     */
    public DrawingPanel() {
        super();
        myBoard = new Board();
        setPreferredSize(new Dimension(myBoard.getWidth() * SIZE, 
                                       myBoard.getHeight() * SIZE));
        setBackground(Color.WHITE);
        myGridOn = false;
        myGameIsGoing = false;
        myPause = false;
        mySecretFeatureOne = false;

    }
    
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2D = (Graphics2D) theGraphics;

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        final double width = myBoard.getWidth() * SIZE;
        final double height = myBoard.getHeight() * SIZE;
        final Rectangle2D gameBorder = new Rectangle2D.Double(0.0, 0.0, width, height);
        g2D.draw(gameBorder); 
        
        for (int boardHeight = 0; boardHeight < myBlockList.size(); boardHeight++) { //columns
            
            final Block[] temp1 = myBlockList.get(boardHeight);
            final int howRounded = 5;

            for (int i = 0; i < temp1.length; i++) { //rows
                // ------ Grid -------------------------
                if (myGridOn) {
                    final Rectangle2D rTemp =
                                    new Rectangle2D.Double(SIZE * i,
                                                           (SIZE - boardHeight) * SIZE - SIZE,
                                                           SIZE, SIZE);
                    g2D.setPaint(Color.BLACK); // adds outline
                    g2D.draw(rTemp);
                }
                //------- Disco ------------------------ 
                if (mySecretFeatureOne) {
                    final Rectangle2D rTemp =
                                    new Rectangle2D.Double(SIZE * i,
                                                           (SIZE - boardHeight) * SIZE - SIZE,
                                                           SIZE, SIZE);
                    g2D.setPaint(Color.BLACK); // adds outline
                    g2D.fill(rTemp);
                }
                //--------------------------------------------
                if (temp1[i] != null) {
                    final Rectangle2D rTemp =
                                    new Rectangle2D.Double(SIZE * i,
                                                           (SIZE - boardHeight) * SIZE - SIZE,
                                                           SIZE, SIZE);
                    g2D.setPaint(enumColor(temp1[i])); // adds color to the pieces
                    g2D.fill(rTemp);
                    g2D.setPaint(Color.BLACK); // adds outline
                    g2D.draw(rTemp);
                    g2D.drawRoundRect(SIZE * i, (SIZE - boardHeight) * SIZE - SIZE, SIZE, SIZE,
                                      howRounded, howRounded);
                    
                }
            }
        }
        
        if (!myPause) {
            final Rectangle2D rTemp = new Rectangle2D.Double(0.0, 0.0, width, height);
            g2D.setPaint(Color.GRAY.brighter()); // adds outline
            g2D.fill(rTemp);
            g2D.setPaint(Color.BLACK);
            final String comicFontStlye = "Comic Sans MS";
            final int sizeOfTitle = 18;
            final int sizeOfText = 15;
            final int indent = 10;
            final int fifty = 50;
            final int oneHundred = 100;
            g2D.setFont(new Font(comicFontStlye, Font.BOLD, sizeOfTitle)); 
            g2D.drawString("Kel's Tetris", indent, fifty);
            g2D.setFont(new Font(comicFontStlye, Font.PLAIN, sizeOfText)); 
            g2D.drawString("Press N: for New Game", indent, oneHundred);
            g2D.drawString("Press P: for Pause", indent, oneHundred + fifty);
            g2D.drawString("Press G: for Grid", indent, oneHundred + oneHundred);
            g2D.drawString("Press 1: for Disco", indent, oneHundred + oneHundred + fifty);
        }
        
    } //end of paint

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
        myBoard = (Board) theObservable;

        if (theObject instanceof List) {
            myBlockList.clear();
            myBlockList.addAll((List<Block[]>) theObject);
        }          
    } //end of update

    
    
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if ("Grid".equals(theEvent.getPropertyName())) {
            myGridOn = (boolean) theEvent.getNewValue();
        }
        if ("GameIsGoing".equals(theEvent.getPropertyName())) {
            myGameIsGoing = (boolean) theEvent.getNewValue();
            myPause = (boolean) theEvent.getNewValue();
            repaint();
        }
        if ("Pause".equals(theEvent.getPropertyName()) && myGameIsGoing) {
            myPause = (boolean) theEvent.getNewValue();
            repaint();
        }
        if ("Secret".equals(theEvent.getPropertyName())) {
            mySecretFeatureOne = (boolean) theEvent.getNewValue();
        }
    }
    
    
} //end of class



