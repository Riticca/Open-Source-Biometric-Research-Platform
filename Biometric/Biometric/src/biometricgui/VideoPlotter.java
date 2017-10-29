/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricgui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYSeries; 
import org.jfree.chart.plot.PlotOrientation; 
import org.jfree.data.xy.XYSeriesCollection; 
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

public class VideoPlotter implements Runnable {

    /**
     * Initialize required stuff
     * @param panel the place where to draw graph
     * @param filePath absolute path of file to be read
     */
    public VideoPlotter(javax.swing.JPanel panel, String filePath) {
        this.panel = panel;
        this.filePath = filePath;
        /*
        try {
            fileReader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GraphPlotter.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        */
    }

    @Override
    public void run() {
        
        NativeDiscovery nd = new NativeDiscovery();
        if (!nd.discover()) {
           System.out.println("VLC not found");
           System.exit(-1);
        }
        
        Canvas canvas = new Canvas();
        panel.add(canvas);
        canvas.setSize(panel.getSize());
        
        panel.revalidate();
        panel.repaint();
        
        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
        EmbeddedMediaPlayer mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
        CanvasVideoSurface videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
        mediaPlayer.setVideoSurface(videoSurface);
        mediaPlayer.playMedia(filePath);
        
        /*
        series = new XYSeries("Eye Tracking Data");      
        String line = null;
         
        
        
        while( true )
        {
            try {
            
                
                line = fileReader.readLine();
                String comma=",";
                int location=line.indexOf(comma);
                String x_value=line.substring(0,location);
                String y_value=line.substring(location+1);
                long xValue=Integer.valueOf(x_value);
                long yValue=Integer.valueOf(y_value);
                series.add(xValue, yValue);
                
                
            }
             catch (Exception ex) {
                 Logger.getLogger(VideoPlotter.class.getName()).log(Level.INFO, null, ex);
                 break;
            }
        }
            
            /* Draw the graph 
            dataset = new XYSeriesCollection(series);
            JFreeChart xyChart = ChartFactory.createScatterPlot(
                    "ChartTitle" ,
                    "X-Axis" ,
                    "Y-Axis" ,
                    dataset ,
                    PlotOrientation.VERTICAL ,
                    true,true,false);
            chartPanel = new ChartPanel(xyChart);
            chartPanel.setSize(this.panel.getSize());
            chartPanel.setRefreshBuffer(true);
            this.panel.setOpaque(false);
            this.panel.add(chartPanel);
            this.panel.revalidate();
            this.panel.repaint();
            
           
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(VideoPlotter.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
            */
        
    }
    
    private XYSeries series;
    private BufferedReader fileReader;
    private XYSeriesCollection dataset;
    private javax.swing.JPanel panel;
    private ChartPanel chartPanel;
    private String filePath;
}
