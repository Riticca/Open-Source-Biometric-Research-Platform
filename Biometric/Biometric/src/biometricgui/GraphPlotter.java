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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
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
     *
     * @param panel the place where to draw graph
     * @param filePath absolute path of file to be read
     * @param signals which defines the signals switched on for that graph
     */
    private TimeSeries series;
    private Second current;
    private BufferedReader fileReader;
    private XYDataset dataset;
    private JFreeChart chart;
    private javax.swing.JPanel panel;
    private ChartPanel chartPanel;
    private SharedData sharedData;
    private Map<Integer, Boolean> signals = new HashMap<>();

    public GraphPlotter(javax.swing.JPanel panel, String filePath,
                        boolean s1, boolean s2, boolean s3, boolean s4) {

        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
        System.out.println(s4);
        this.panel = panel;
        sharedData = SharedData.getSharedDataInstance();

        series = new TimeSeries("Biometric Data");
        current = new Second();

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

        String biometricValue;
        Double valueRead = 0.0;
        int endOfFileFlag = 0;
        int oldValue = sharedData.get();

        while (true) {
            while (oldValue == sharedData.get() || !sharedData.getSliderStatus()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GraphPlotter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            oldValue = sharedData.get();
            series.clear();

            for (int i = 0; i < 50; i++) {
                try {
                    biometricValue = fileReader.readLine();
                    for (Entry<Integer, Boolean> entry : signals.entrySet()) {
                        if (entry.getValue().equals(true)) {
                            valueRead = Double.parseDouble(biometricValue.split(",")[entry.getKey()]);

                        }

                    }
                    //System.out.println("It is reading "+ valueRead+"\n");
                    series.add(current, valueRead);
                    current = (Second) current.next();

                } catch (SeriesException e) {
                    System.err.println("Error adding to series");
                } catch (IOException ex) {
                    /* Everything that has a beginning, has an ending */
                    endOfFileFlag = 1;
                    break;
                }
            }

            /* Exit on EOF */
            if (endOfFileFlag == 1) {
                break;
            }

            /* Draw the graph */
            dataset = new TimeSeriesCollection(series);
            chart = ChartFactory.createTimeSeriesChart(this.panel.getName(),
                    "Seconds", this.panel.getName().split(" ")[0] + " values", dataset,
                    false, false, false);

            chartPanel = new ChartPanel(chart);
            chartPanel.setSize(panel.getSize());
            chartPanel.setRefreshBuffer(true);
            panel.add(chartPanel);
            panel.revalidate();
            panel.repaint();

        }

    }

}
