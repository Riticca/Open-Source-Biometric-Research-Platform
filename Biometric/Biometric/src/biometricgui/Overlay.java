/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricgui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Overlay class provides infrastructure to paint on top of canvas
 * @author Vikram Wathodkar (email: vikram.wathodkar@gmail.com)
 */
public class Overlay extends Window {
    
    /**
     * Constructor for Overlay
     * @param owner MainWindow
     */
    public Overlay(Window owner) {
        super(owner);
        setBackground(new Color(0, 0, 0, 0)); 
    }
    
    /**
     * Paint function will get called when we enable overlay
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }
    
    /**
     * paintEyeTracking function draw the small circle on canvas
     * depending upon given x and y position
     * @param graphic Graphic object to paint 
     * @param x x position for eye tracking
     * @param y y position for eye tracking
     */
    public void paintEyeTracking(Graphics graphic, int x, int y) {

        Graphics2D twoDG;
        
        try {
            twoDG = (Graphics2D)graphic;
            twoDG.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            
            /* Draw and fill a circle at position x, y of height and width 10 */
            twoDG.drawOval(x, y, 20, 20);
            twoDG.fillOval(x, y, 20, 20);
            
            /* Put thread to sleep so user can see the circle */
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Overlay.class.getName()).log(Level.WARNING, null, ex);
            }
            
            /* Clear the circle before we exit */
            twoDG.clearRect(x, y, 25, 25);

        } catch (NullPointerException ex) {
            Logger.getLogger(Overlay.class.getName()).log(Level.WARNING, null, ex);
        }

    }
}
