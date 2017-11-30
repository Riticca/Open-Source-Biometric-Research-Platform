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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
public final class VideoPlotter implements Runnable {

    /**
     * Initialize required stuff for plotting video
     *
     * @param videoPanel the place where to draw graph
     * @param fileToOpen absolute path of file to be read
     * @param eyeTrackingFilePath path for eye tracking data file
     * @param mainWin Main Window object which is required by overlay
     */
    public VideoPlotter(javax.swing.JPanel videoPanel, String fileToOpen,
            String eyeTrackingFilePath, MainWindow mainWin) {

        panel = videoPanel;
        filePath = fileToOpen;
        sharedData = SharedData.getSharedDataInstance();
        mainWindow = mainWin;
        eyeTrackingDataPath = eyeTrackingFilePath;

        /* Initialize date time formatter */
        df = new SimpleDateFormat("HH:mm:ss");
        
        /* Initialize time to signal map */
        timeToXYMap =  new HashMap<>();
        
        if (eyeTrackingDataPath != null)
            initTimeToXYMap();
        
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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(VideoPlotter.class.getName()).log(Level.SEVERE, null, ex);
        }
        //mediaPlayer.pause();
        maxVideoLen = 25201 + (int) (mediaPlayer.getLength()/1000);
        sharedData.setFileLength(maxVideoLen);
    }

    public void initTimeToXYMap()
    {
        String line;
        String[] xyVals;
        Date date = null;
        try {
            fileReader = new BufferedReader(new FileReader(eyeTrackingDataPath));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VideoPlotter.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            while ( (line = fileReader.readLine()) != null ) {
                xyVals = line.split(",");
                try {
                    date = df.parse(xyVals[0]);
                } catch (ParseException ex) {
                    Logger.getLogger(VideoPlotter.class.getName()).log(Level.SEVERE, null, ex);
                }
                Integer xyVal[] = new Integer[]{
                                    Integer.parseInt(xyVals[1]),
                                    Integer.parseInt(xyVals[2])};
                timeToXYMap.put((int)(date.getTime()/1000), xyVal);
            }
        } catch (IOException | NullPointerException ex) {
            Logger.getLogger(GraphPlotter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {

        Overlay overlay;
        String line;

        mediaPlayer.start();
        overlay = new Overlay(mainWindow);
        mediaPlayer.setOverlay(overlay);
        mediaPlayer.enableOverlay(true);

        if (eyeTrackingDataPath != null)
            while ( !sharedData.isStopEverything() ) {

                int oldValue = sharedData.get();
                Integer xyVals[];

                while ( (oldValue == sharedData.get() || !sharedData.getSliderStatus()
                        || !mediaPlayer.isPlaying() ) && !sharedData.isStopEverything() ) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GraphPlotter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                oldValue = sharedData.get();
                int currValue = oldValue;
                try {
                    xyVals = timeToXYMap.get(currValue);
                    overlay.paintEyeTracking(overlay.getGraphics(), xyVals[0], xyVals[1]);
                } catch (NullPointerException ex) {
                    System.out.println("Entry not found for " + currValue + " in timeToXYMap");
                }
            }       
    }

    /* Run media from given value */
    public void setMediaValue(int value) {
        
        /* If media is not running then start it */
        if ( ! mediaPlayer.isPlaying() )
            mediaPlayer.play();

        /* mediaPlayer needs time in milli-sec */
        mediaPlayer.setTime( (value - 25201) * 1000);
    }

    /**
     * Pause video
     */
    public void pauseVideo() {
        mediaPlayer.pause();
    }

    /**
     * Stop video
     */
    public void stopVideo() {
        mediaPlayer.stop();
        mediaPlayer.enableOverlay(false);
        mediaPlayer.release();
        panel.removeAll();
        panel.repaint();
        panel.validate();
    }

    /**
     * Resume video
     */
    public void resumeVideo() {
        mediaPlayer.start();
        //mediaPlayer.play();
    }

    private BufferedReader fileReader;
    private javax.swing.JPanel panel;
    private String filePath;
    private SharedData sharedData;
    private EmbeddedMediaPlayer mediaPlayer;
    private CanvasVideoSurface videoSurface;
    private MediaPlayerFactory mediaPlayerFactory;
    private MainWindow mainWindow;
    private final String eyeTrackingDataPath;
    private Map<Integer, Integer[]> timeToXYMap;
    private SimpleDateFormat df;
    private int maxVideoLen;
}
