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
import java.util.concurrent.TimeUnit;
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
    
    public XYDataset series(String fileToRead) 
    {
        final TimeSeries series = new TimeSeries( "Biometric Data" );
            Second current = new Second( );
        try {
            String str;
            int numberofDataValues =0;
            String biometicValue;
            
            BufferedReader br,rebr;
            br = new BufferedReader(new FileReader(fileToRead));
          
            System.out.println("The buffered Read is"+br);
             while ((str = br.readLine()) != null) {
                 numberofDataValues ++;
             }
             System.out.println("Number of lines captured in file are\n"+numberofDataValues);
             //br.seek(0);
             rebr=new BufferedReader(new FileReader(fileToRead));
           //for(int j=0;j<(numberofDataValues%100);j++){
                //series.clear();
               // TimeUnit.SECONDS.sleep(5);
            for (int i = 0; i < 100; i++) {
                //System.out.println(br.readLine());
                try {
                    biometicValue=rebr.readLine();
                   System.out.println("Value is "+biometicValue+"\n");
                    series.add(current, new Double( biometicValue ) );
                    current = ( Second ) current.next( );
                } catch ( SeriesException e ) {
                    System.err.println("Error adding to series");
                } catch (IOException ex) {
                    Logger.getLogger(PlotTheGraphs.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
           //}
            System.out.println("The buffered now Read is"+br);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PlotTheGraphs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PlotTheGraphs.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new TimeSeriesCollection(series);
    }
}
