package alfredo.gfx;

import alfredo.geom.Vector;
import java.awt.image.BufferedImage;

/**
 * A Graphic object that is completely blank.
 * @author TheMonsterOfTheDeep
 */
public class NullGraphic extends Graphic {
    @Override
    public BufferedImage getRender() {
        return null;
    } 

    @Override
    public Vector getPivot() {
        return new Vector();
    }

    @Override
    public void setPivot(Vector v) { }
}
