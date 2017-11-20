/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricgui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Shared data class contains data shared between different objects in GUI
 *
 * @author Vikram Wathodkar (vikram.wathodkar@gmail.com)
 */
public class SharedData {

    public SharedData() {

        /* Slider status true indicates it is running */
        fileLengths = new ArrayList<>();
    }

    public static SharedData getSharedDataInstance() {

        /* Make SharedData singleton */
        if (instance == null) {
            instance = new SharedData();
        }
        return instance;
    }

    public synchronized void setFileLength(int x) {
        fileLengths.add(x);
    }
    
    public synchronized int getNoThreadsCalculatedFileLength() {
        return fileLengths.size();
    }
    
    public synchronized int getMaxFileLength() {
        return Collections.max(fileLengths);
    }
    
    public synchronized void set(int newVal) {
        sliderValue = newVal;
    }

    public synchronized int get() {
        return sliderValue;
    }

    public synchronized boolean getSliderStatus() {
        return sliderStatus;
    }

    public synchronized void setSliderStatus(boolean status) {
        sliderStatus = status;
    }

    public synchronized boolean isStopEverything() {
        return stopEveryThing;
    }
    
    public synchronized void stopEverything() {
        fileLengths = new ArrayList<>();
        stopEveryThing = true;
        sliderStatus = false;
    }
    
    public synchronized void startEverything() {
        sliderStatus = true;
        stopEveryThing = false;
    }

    private int sliderValue;
    private static SharedData instance;
    private boolean sliderStatus;
    private List<Integer> fileLengths;
    private boolean stopEveryThing;
}
