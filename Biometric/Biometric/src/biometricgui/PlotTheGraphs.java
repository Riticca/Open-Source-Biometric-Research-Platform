/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricgui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author Gargi
 */
public class PlotTheGraphs extends MainWindow {
    
    public XYDataset series() 
    {
        final TimeSeries series = new TimeSeries( "Biometric Data" );
            Second current = new Second( );
        try {
            
            BufferedReader br;
            br = new BufferedReader(new FileReader("ecginput.txt"));
            
            
            
            for (int i = 0; i < 10000; i++) {
                System.out.println(br.readLine());
                try {
                    String line=br.readLine();
                    
                    series.add(current, new Double( line ) );
                    current = ( Second ) current.next( );
                } catch ( SeriesException e ) {
                    System.err.println("Error adding to series");
                } catch (IOException ex) {
                    Logger.getLogger(PlotTheGraphs.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PlotTheGraphs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PlotTheGraphs.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new TimeSeriesCollection(series);
    }
}
