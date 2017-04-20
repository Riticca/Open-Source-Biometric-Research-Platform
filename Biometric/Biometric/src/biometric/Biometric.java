/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometric;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 *
 * @author smurali
 */
public class Biometric {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
//        NativeInterface.open();
//        SwingUtilities.invokeLater(new Runnable() {
//        @Override
//        public void run() {
//            JFrame frame = new JFrame("YouTube Viewer");
//            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//            frame.getContentPane().add(getBrowserPanel(), BorderLayout.CENTER);
//            frame.setSize(800, 600);
//            frame.setLocationByPlatform(true);
//            frame.setVisible(true);
//        }
//
//            private Component getBrowserPanel() {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//    });
//    NativeInterface.runEventPump();
//    // don't forget to properly close native components
//    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
//        @Override
//        public void run() {
//            NativeInterface.close();
//        }
//    }));
    }
    
    public static JPanel getBrowserPanel() {
    JPanel webBrowserPanel = new JPanel(new BorderLayout());
    JWebBrowser webBrowser = new JWebBrowser();
    webBrowserPanel.add(webBrowser, BorderLayout.CENTER);
    webBrowser.setBarsVisible(false);
    webBrowser.navigate("https://www.youtube.com/v/b-Cr0EWwaTk?fs=1");
    return webBrowserPanel;
}
}

