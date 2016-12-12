package alfredo.gfx;

import alfredo.geom.Vector;
import java.awt.image.BufferedImage;

/**
 * A Graphic represents anything that can be rendered on a Canvas.
 * @author TheMonsterOfTheDeep
 */
public abstract class Graphic {
    public static enum Pivot {
        TopLeft,
        TopRight,
        BottomLeft,
        BottomRight
    }
    
    /**
     * Returns a BufferedImage that represents the current state of this
     * Graphic. A return value of "null" means that the image is blank.
     * @return Rendered data the Canvas can draw.
     */
    public abstract BufferedImage getRender();
    
    public abstract Vector getPivot();
    
    public abstract void setPivot(Vector v);
    
    public final Renderer toRenderer() {
        return new Renderer(this);
    }
}
