/*
 * TCSS 305 - Fall 2016
 * Assignment 6 - Tetris
 *
 */
package view;

import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * 
 * @author Keldon Fischer
 * @version 12/2/2016
 */
public final class TetrisMain {

    /**
     * This is the default constructor.
     */
    private TetrisMain() {
        throw new IllegalStateException();

    }

    /**
     * this sets the them for my GUI.
     */
    private static void setLookAndFeel() {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            UIManager.put("swing.boldMetal", Boolean.FALSE);
        } catch (final ClassNotFoundException 
                        | InstantiationException 
                        | IllegalAccessException
                        | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param theArgs the cmd line args.
     * @throws ClassNotFoundException just in case.
     */
    public static void main(final String[] theArgs) throws ClassNotFoundException {
        setLookAndFeel();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                new TetrisGUI().start();
            }
        });
    }

}
