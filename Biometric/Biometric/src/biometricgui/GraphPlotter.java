/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricgui;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;

/**
 *
 * @author vikram
 */
public class GraphPlotter implements Runnable {

    public GraphPlotter(javax.swing.JPanel panel, String filePath) {
        
        PlotTheGraphs gr = new PlotTheGraphs();
        final XYDataset dataset = gr.series(filePath);
        final JFreeChart chart = ChartFactory.createTimeSeriesChart("EMG Readings","Seconds","EMG value captured",dataset,false,false,false);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setSize(panel.getSize());
        panel.add(chartPanel);
        panel.revalidate();
        panel.repaint();
    }

    @Override
    public void run() {
        System.out.println("New thread is created");
    }
    
    
}
