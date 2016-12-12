package alfredo;

import alfredo.gfx.Graphic;
import alfredo.inpt.Keys;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class Game {
    public static final int DEFAULT_WIDTH = 600;
    public static final int DEFAULT_HEIGHT = 400;
    
    private static JFrame frame = null;
    private static JPanel panel = null;
    private static Timer timer = null;
    private static Canvas canvas = null;
    
    private static long tick = 0;
    
    private static boolean init(int width, int height) {
        if(frame != null) { return false; }
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        canvas = new Canvas(width, height);
        
        panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                long start = System.currentTimeMillis();
                
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
                
                Scene.getCurrent().render(canvas);
                
                g.drawImage(canvas.getRender(), 0, 0, null);
               
                //System.out.println("Time to paint: " + (System.currentTimeMillis() - start));
                
                repaint();
            }
        };
        
        frame.addKeyListener(Keys.listener);
        
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                canvas.resize(panel.getWidth(), panel.getHeight());
            }
        });
        
        panel.setPreferredSize(new Dimension(width, height));
        frame.add(panel);
        frame.pack();
        
        timer = new Timer(33, (ActionEvent e) -> {
            long start = System.currentTimeMillis();
            Scene.getCurrent().loop();
            
            ++tick;
            //System.out.println("Time to tick: " + (System.currentTimeMillis() - start));
        });
        
        return true;
    }
    
    private static boolean init() {
        return init(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    public static void setTitle(String title) {
        init();
        frame.setTitle(title);
    }
    
    public static void setSize(int width, int height) {
        if(!init(width, height)) {
            panel.setPreferredSize(new Dimension(width, height));
            frame.pack();
            
            canvas.resize(width, height);
        }
    }
    
    public static void play() {
        init();
        frame.setVisible(true);
        timer.start();
    }
    
    public static void setDelay(int ms) {
        init();
        timer.setDelay(ms);
    }
    
    public static int getDelay() {
        init();
        return timer.getDelay();
    }
    
    public static void saveScreenshot(String path) {
        try {
            ImageIO.write(canvas.getRender(), "PNG", new File(path + tick + ".png"));
        } catch (IOException ex) {
            System.err.println("Could not save screenshot: " + ex.getLocalizedMessage());
        }
    }
    
    public static void setIcon(Graphic icon) {
        init();
        BufferedImage b = icon.getRender();
        if(b != null) {
            frame.setIconImage(b);
        }
    }
    
    public static long getTick() {
        return tick;
    }
    
    static void updateCamera() {
        init();
        canvas.camera = Camera.getMain();
        Camera.getMain().clip(canvas);
    }
}
