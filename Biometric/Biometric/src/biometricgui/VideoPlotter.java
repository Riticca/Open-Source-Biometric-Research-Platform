/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricgui;

import java.awt.Canvas;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

/**
 * Video Plotter handles video running in Eye Tracking and Camera Panel
 *
 * @author Vikram Wathodkar (vikram.wathodkar@gmail.com)
 */
public class VideoPlotter implements Runnable {

    /**
     * Initialize required stuff for plotting video
     *
     * @param videoPanel the place where to draw graph
     * @param fileToOpen absolute path of file to be read
     * @param mainWin Main Window object which is required by overlay
     */
    public VideoPlotter(javax.swing.JPanel videoPanel, String fileToOpen,
            MainWindow mainWin) {
        
        panel = videoPanel;
        filePath = fileToOpen;
        sharedData = SharedData.getSharedDataInstance();
        mainWindow = mainWin;

        try {
            fileReader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Overlay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {

        Overlay overlay;
        String line;

        /* Make sure VLC is present on machine */
        NativeDiscovery nd = new NativeDiscovery();
        if (!nd.discover()) {
            System.out.println("VLC not found");
            System.exit(-1);
        }

        /* Setup canvas */
        Canvas canvas = new Canvas();
        panel.add(canvas);
        canvas.setSize(panel.getSize());

        /* Setup canvas */
        panel.revalidate();
        panel.repaint();

        mediaPlayerFactory = new MediaPlayerFactory();
        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
        videoSurface = mediaPlayerFactory.newVideoSurface(canvas);
        mediaPlayer.setVideoSurface(videoSurface);
        mediaPlayer.playMedia(filePath);

        overlay = new Overlay(mainWindow);
        mediaPlayer.setOverlay(overlay);
        mediaPlayer.enableOverlay(true);

        
        try {
            while ((line = fileReader.readLine()) != null) {
                String xy[] = line.split(",");
                int x = Integer.parseInt(xy[0]);
                int y = Integer.parseInt(xy[1]);
                overlay.paintEyeTracking(overlay.getGraphics(), x, y);
            }
        } catch (IOException ex) {
            Logger.getLogger(Overlay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /* Run media from given value */
    void setMediaValue(int value) {
        mediaPlayer.setTime(value * 10000);
    }

    /* Pause video */
    void pauseVideo() {
        mediaPlayer.pause();
    }

    /* Stop video */
    void stopVideo() {
        mediaPlayer.stop();
    }

    /* Resume video */
    void resumeVideo() {
        mediaPlayer.start();
    }

    private BufferedReader fileReader;
    private javax.swing.JPanel panel;
    private String filePath;
    private SharedData sharedData;
    private EmbeddedMediaPlayer mediaPlayer;
    private CanvasVideoSurface videoSurface;
    private MediaPlayerFactory mediaPlayerFactory;
    private MainWindow mainWindow;
}
