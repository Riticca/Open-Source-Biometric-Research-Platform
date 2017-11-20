/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricgui;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.data.xy.XYDataset;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Month;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/**
 * @author Vikram Wathodkar (vikram.wathodkar@gmail.com) 
 * This class read the given file and plot graph on panels
 */
public final class GraphPlotter implements Runnable {

    /**
     * Initialize required stuff
     *
     * @param panel the place where to draw graph
     * @param filePath absolute path of file to be read
     * @param signal1 Is signal 1 present
     * @param signal2 Is signal 2 present
     * @param signal3 Is signal 3 present
     * @param signal4 Is signal 4 present
     */
    public GraphPlotter(javax.swing.JPanel panel, String filePath,
                        boolean signal1, boolean signal2,
                        boolean signal3, boolean signal4) {

        signalArray = new boolean[] {signal1, signal2, signal3, signal4};
        this.panel = panel;
        sharedData = SharedData.getSharedDataInstance();
        series = new TimeSeries("Biometric Data");

        /* We already made sure that file exists
         * but my IDE wants me to double check it
         */
        try {
            fileReader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GraphPlotter.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        
        /* Initialize date time formatter */
        df = new SimpleDateFormat("HH:mm:ss");

        /* Initialize time to signal map */
        timeToLineMap =  new HashMap<>();

        try {
            /* We will crate map of time to line number for fast lookup */
            initTimeToLineMap();
        } catch (ParseException ex) {
            Logger.getLogger(GraphPlotter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    public void initTimeToLineMap() throws ParseException
    {
        String line;
        String[] bioVals;
        Date date = null;

        try {
            while ( (line = fileReader.readLine()) != null ) {
                bioVals = line.split(",");
                date = df.parse(bioVals[0]);
                Float signals[] = new Float[]{
                                    Float.parseFloat(bioVals[1]),
                                    Float.parseFloat(bioVals[2]),
                                    Float.parseFloat(bioVals[3]),
                                    Float.parseFloat(bioVals[4])};
        
                timeToLineMap.put((int)(date.getTime()/1000), signals);
            }
        } catch (IOException ex) {
            Logger.getLogger(GraphPlotter.class.getName()).log(Level.SEVERE, null, ex);
        }

        /* Store last time in shared data for slider */
        if (date != null)
            sharedData.setFileLength((int) (date.getTime()/1000));
    }
    
    private TimeSeriesCollection createDataset(int currValue, int signal) {
        final TimeSeries set;
        set = new TimeSeries("Random Data" + currValue);
        int i = 0;
        Float currSignals[];
        
        try {
            for(i = 0; i < 10; i++) {
                currSignals = timeToLineMap.get(currValue + i);
                set.add(new Millisecond(new Date((currValue + i) * 1000)), currSignals[signal]);
            }
        } catch (NullPointerException ex) {
            for(; i < 10; i++)
                set.add(new Millisecond(new Date((currValue + i) * 1000)), 5);
        }
        return new TimeSeriesCollection(set);
    }
    
    @Override
    public void run() {

        int currValue;
        int oldValue = sharedData.get();
        int setIndex;

        while ( !sharedData.isStopEverything() ) {
            
            while ((oldValue == sharedData.get() || !sharedData.getSliderStatus())
                        && !sharedData.isStopEverything()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GraphPlotter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            oldValue = sharedData.get();
            currValue = oldValue;
            
            dataset = new TimeSeriesCollection(series); // Dummy dataset
            chart = ChartFactory.createTimeSeriesChart(this.panel.getName(),
                    "Time", this.panel.getName().split(" ")[0] + " values", dataset,
                    false, false, false);
            XYPlot plot = chart.getXYPlot();

            setIndex = 1;
            for (int i = 0; i < 4; i++)
                if (signalArray[i]) {
                    plot.setDataset(setIndex, createDataset(currValue, i));
                    plot.setRenderer(setIndex, new StandardXYItemRenderer());
                    setIndex++;
                }

            /* Draw the graph */
        
            final DateAxis axis = (DateAxis) plot.getDomainAxis();
            axis.setDateFormatOverride(new SimpleDateFormat("HH:mm:ss"));
            
            chartPanel = new ChartPanel(chart);
            chartPanel.setSize(panel.getSize());
            chartPanel.setRefreshBuffer(true);
            panel.removeAll();
            panel.add(chartPanel);
            panel.revalidate();
            panel.repaint();
        }
        
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
    }
    
    private boolean signalArray[];
    private TimeSeries series;
    private BufferedReader fileReader;
    private XYDataset dataset;
    private JFreeChart chart;
    private javax.swing.JPanel panel;
    private ChartPanel chartPanel;
    private SharedData sharedData;
    private Map<Integer, Float[]> timeToLineMap;
    private SimpleDateFormat df;

}