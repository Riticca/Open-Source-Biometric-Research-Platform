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
public class GraphPlotter implements Runnable {

    /**
     * Initialize required stuff
     * @param panel the place where to draw graph
     * @param filePath absolute path of file to be read
     */
    public GraphPlotter(javax.swing.JPanel panel, String filePath) {
        
        this.panel = panel;
        sharedData = SharedData.getSharedDataInstance();
        
        series = new TimeSeries("Biometric Data");
        current = new Second( );
        
        /* We already made sure that file exists
         * but my IDE wants me to double check it
         */
        try {
            fileReader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GraphPlotter.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        
        Double biometricValue;
        int endOfFileFlag = 0;
        int oldValue = sharedData.get();

        while( true )
        {
            while (oldValue == sharedData.get() || !sharedData.getSliderStatus())
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GraphPlotter.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            oldValue = sharedData.get();
            series.clear();
            
            /* Let's plot 50 values at a time */
            for (int i = 0; i < 50; i++) 
                
                try {
                   biometricValue = new Double(fileReader.readLine());
                   series.add(current, biometricValue);
                   current = ( Second ) current.next( ); // TODO: it will come
                                                         // from the file
                } catch ( SeriesException e ) {
                    System.err.println("Error adding to series");
                } catch ( IOException ex ) {
                    /* Everything that has a beginning, has an ending */
                    endOfFileFlag = 1;
                    break;
                }
            
            /* Exit on EOF */
            if (endOfFileFlag == 1)
                break;
            
            /* Draw the graph */
            dataset = new TimeSeriesCollection(series);
            chart = ChartFactory.createTimeSeriesChart(this.panel.getName(),
                    "Seconds", this.panel.getName().split(" ")[0]+" values", dataset,
                    false, false, false);
            
            chartPanel = new ChartPanel(chart);
            chartPanel.setSize(panel.getSize());
            chartPanel.setRefreshBuffer(true);
            panel.add(chartPanel);
            panel.revalidate();
            panel.repaint();
            /*
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(GraphPlotter.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
            */
        }
        
    }
    
    private TimeSeries series;
    private Second current;
    private BufferedReader fileReader;
    private XYDataset dataset;
    private JFreeChart chart;
    private javax.swing.JPanel panel;
    private ChartPanel chartPanel;
    private SharedData sharedData;

}
