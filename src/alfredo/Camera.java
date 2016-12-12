package alfredo;

import alfredo.geom.Vector;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public abstract class Camera {
    public static final class Static extends Camera {
        private final Vector viewport = new Vector();
        
        @Override
        public void resize(int width, int height) {
            viewport.set(width, height);
        }
        
        @Override
        public float viewportX(float x) {
            return x;
        }
        
        @Override
        public float viewportY(float y) {
            return y;
        }

        @Override
        public float getScale() {
            return 1;
        }   

        @Override
        public Vector getViewport() {
            return viewport;
        }

        @Override
        public void clip(Canvas c) {}
    }
    
    public static final class Fit extends Camera {
        private final int baseWidth, baseHeight;
        private final float baseScaleW, baseScaleH;
        private float scale;
        private float xoffset, yoffset;
        
        private final Vector viewport;
        
        @Override
        public void resize(int width, int height) {
            if((float)width / baseWidth > (float)height / baseHeight) {
                xoffset = (width - (height * baseScaleW)) / 2;
                yoffset = 0;
                scale = (float)height / baseHeight;
            }
            else {
                yoffset = (height - (width * baseScaleH)) / 2;
                xoffset = 0;
                scale = (float)width / baseWidth;
            }
        }
        
        public Fit(int baseWidth, int baseHeight) {
            this.baseWidth = baseWidth;
            this.baseHeight = baseHeight;
            
            baseScaleW = (float)baseWidth / baseHeight;
            baseScaleH = (float)baseHeight / baseWidth;
            
            resize(baseWidth, baseHeight);
            
            viewport = new Vector(baseWidth, baseHeight);
        }

        @Override
        public float viewportX(float x) {
            return xoffset + scale * x;
        }
        
        @Override
        public float viewportY(float y) {
            return yoffset + scale * y;
        }

        @Override
        public float getScale() {
            return scale;
        }

        @Override
        public Vector getViewport() {
            return viewport;
        }

        @Override
        public void clip(Canvas c) {
            c.clip(Math.round(xoffset), Math.round(yoffset), Math.round(baseWidth * scale), Math.round(baseHeight * scale));
        }
    }
    
    private static Camera main;
    
    public static void setMain(Camera c) {
        main = c;
        Game.updateCamera();
    }
    
    public static Camera getMain() {
        return main;
    }
    
    public final Vector position;
    
    public Camera() {
        position = new Vector();
    }
    
    public abstract float viewportX(float x);
    public abstract float viewportY(float y);
    
    public final float screenX(float x) {
        return viewportX(x) + position.x;
    }
    public final float screenY(float y) {
        return viewportY(y) + position.y;
    }
    public abstract float getScale();
    
    public void resize(int width, int height) { }
    
    public abstract Vector getViewport();
    
    public abstract void clip(Canvas c);
}
