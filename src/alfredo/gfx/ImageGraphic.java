package alfredo.gfx;

import alfredo.geom.Vector;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * A Graphic that represents a single image.
 * @author TheMonsterOfTheDeep
 */
public class ImageGraphic extends Graphic {

    private final BufferedImage image;
    public Vector pivot;
    
    public static ImageGraphic createRectangle(int color, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.setColor(new Color(color));
        g.fillRect(0, 0, width, height);
        return new ImageGraphic(image, new Vector());
    }
    
    public static Graphic load(String path, Vector pivot) {
        try {
            BufferedImage image = ImageIO.read(ImageGraphic.class.getResourceAsStream(path));
            return new ImageGraphic(image, pivot);
        } catch (IOException ex) {
            System.err.println("Error loading image: " + ex.getLocalizedMessage());
            return new NullGraphic();
        }
    }
    
    public static Graphic load(String path) {
        return load(path, new Vector());
    }
    
    public static Graphic load(String path, Pivot p) {
        try {
            BufferedImage image = ImageIO.read(ImageGraphic.class.getResourceAsStream(path));
            float w = image.getWidth() / 2f;
            float h = image.getHeight() / 2f;
            Vector pivot = 
                    p == Pivot.TopLeft    ? new Vector(-w, -h) :
                    p == Pivot.TopRight   ? new Vector( w, -h) :
                    p == Pivot.BottomLeft ? new Vector(-w,  h) :
                                            new Vector( w,  h);
            return new ImageGraphic(image, pivot);
        } catch (IOException ex) {
            System.err.println("Error loading image: " + ex.getLocalizedMessage());
            return new NullGraphic();
        }
    }
    
    public ImageGraphic(BufferedImage image, Vector pivot) {
        this.image = image;
        this.pivot = pivot;
    }
    
    @Override
    public BufferedImage getRender() {
        return image;
    }

    @Override
    public Vector getPivot() {
        return pivot;
    }

    @Override
    public void setPivot(Vector v) {
        pivot = v;
    }
}
