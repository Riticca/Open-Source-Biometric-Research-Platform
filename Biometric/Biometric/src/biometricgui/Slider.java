/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricgui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.data.xy.XYDataset;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/**
 * @author Vikram Wathodkar (vikram.wathodkar@gmail.com)
 * This class read the given file and plot graph on panels
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
            while(slider.getValue() < 100 && sharedData.getSliderStatus()) {
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