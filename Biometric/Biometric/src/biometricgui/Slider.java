/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricgui;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Vikram Wathodkar (vikram.wathodkar@gmail.com) This class read the
 * given file and plot graph on panels
 */
public class Slider implements Runnable {

    /**
     * @param sliderRef reference to slider from main window
     */
    public Slider(javax.swing.JSlider sliderRef) {
        slider = sliderRef;
        sharedData = SharedData.getSharedDataInstance();
    }

    @Override
    public void run() {

        slider.setValue(0);

        sharedData.set(0);

        while (true) {
            try {
                while (slider.getValue() < 100 && sharedData.getSliderStatus()) {
                    slider.setValue(slider.getValue() + 1);
                    sharedData.set(slider.getValue());
                    Thread.sleep(1000);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Slider.class.getName()).log(Level.SEVERE, null, ex);
            }

            //sharedData.setSliderStatus(false);
        }
    }

    private javax.swing.JSlider slider;
    private SharedData sharedData;
}
