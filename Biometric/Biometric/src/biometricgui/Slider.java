/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricgui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 * This class read the given file and plot graph on panels
 */
public class Slider implements Runnable {

    /**
     * @param sliderRef reference to slider from main window
     */
    public Slider(javax.swing.JSlider sliderRef) {
        slider = sliderRef;
        sharedData = SharedData.getSharedDataInstance();
        sliderMinVal = 25201;
        sliderMaxVal = sharedData.getMaxFileLength();
    }

    public void initSlider(int minVal, int maxVal)
    { 
        sliderMinVal = minVal;
        sliderMaxVal = maxVal;
    }

    @Override
    public void run() {

        Hashtable labelTable = new Hashtable();
        
        slider.setMinimum(sliderMinVal);
        slider.setMaximum(sliderMaxVal);
        slider.setValue(sliderMinVal);

        SimpleDateFormat d = new SimpleDateFormat("hh:mm:ss");
        
        labelTable.put( sliderMinVal, new JLabel("00:00:00") );
        labelTable.put( sliderMaxVal, new JLabel(d.format(new Date((sliderMaxVal) * 1000L))) );
        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);
        sharedData.set(sliderMinVal);

        while ( !sharedData.isStopEverything() ) {
            
            try {
                while (slider.getValue() < sliderMaxVal && sharedData.getSliderStatus()) {
                    slider.setValue(slider.getValue() + 1);
                    sharedData.set(slider.getValue());
                    Thread.sleep(1000);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Slider.class.getName()).log(Level.SEVERE, null, ex);
            }

            //sharedData.setSliderStatus(false);
        }
        slider.setValue(sliderMinVal);
        slider.setPaintLabels(false);
    }

    private javax.swing.JSlider slider;
    private SharedData sharedData;
    private Integer sliderMaxVal;
    private Integer sliderMinVal;
}
