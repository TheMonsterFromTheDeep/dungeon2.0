package alfredo;

import alfredo.geom.Vector;
import alfredo.gfx.Graphic;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class Canvas {
    private BufferedImage buffer;
    private Graphics2D graphics;
    
    public Camera camera;
    
    private void createBuffer(int width, int height) {
        BufferedImage old = buffer;
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        Shape oldClip = null;
        
        if(graphics != null) {
            oldClip = graphics.getClip();
            graphics.dispose();
        }
        
        graphics = buffer.createGraphics();
        
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        if(oldClip != null) {
            graphics.setClip(oldClip);
        }
        
        if(old != null) {
            graphics.drawImage(old, 0, 0, null);
            old.flush();
        }
    }
    
    public Canvas(int width, int height, Camera c) {
        createBuffer(width, height);
        
        this.camera = c;
    }
    
    public Canvas(int width, int height) {
        this(width, height, new Camera.Static());
    }
    
    public void clear() {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
    }
    
    public void resize(int width, int height) {
        createBuffer(width, height);
        camera.resize(width, height);
        camera.clip(this);
        
        Shape clip = graphics.getClip();
        graphics.setClip(null);
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, width, height);
        graphics.setClip(clip);
    }
    
    public void draw(Graphic g, float x, float y, double angle, float opacity) {     
        BufferedImage image = g.getRender();
        
        if(image == null) { return; }
        
        float scale = camera.getScale();
        
        float w = image.getWidth() / 2f;
        float h = image.getHeight() / 2f;
        
        Vector pivot = g.getPivot();
        
        x = camera.screenX(x);
        y = camera.screenY(y);
        
        AffineTransform transform = new AffineTransform(); 

        transform.translate(x, y);
        transform.rotate(Math.toRadians(angle), 0, 0);
        
        transform.scale(scale, scale);
        transform.translate(-w - pivot.x, -h - pivot.y);
        
        Composite old = graphics.getComposite();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        graphics.drawImage(image, transform, null);
        graphics.setComposite(old);
    }
    
    public void draw(Graphic g, float x, float y, double angle) {
        draw(g, x, y, angle, 1);
    }
    
    public void draw(Graphic g, Vector position, double angle) {
        draw(g, position.x, position.y, angle);
    }
    
    public void fill(int color, float x, float y, float width, float height) {
        
        AffineTransform old = graphics.getTransform();
        
        x = camera.screenX(x);
        y = camera.screenY(y);
        float scale = camera.getScale();
        
        graphics.translate(x, y);
        graphics.scale(width * scale, height * scale);
        
        graphics.setColor(new Color(color));
        graphics.fillRect(0, 0, 1, 1);
        
        graphics.setTransform(old);
    }
    
    public void fillRaw(int color, float x, float y, float width, float height) {
        
        AffineTransform old = graphics.getTransform();
        
        x = camera.screenX(x);
        y = camera.screenY(y);
        float scale = camera.getScale();
        
        graphics.translate(x, y);
        graphics.scale(width * scale, height * scale);
        
        graphics.setColor(new Color(color, true));
        graphics.fillRect(0, 0, 1, 1);
        
        graphics.setTransform(old);
    }
    
    public void fill(int color, float x, float y, Vector size) {
        fill(color, x, y, size.x, size.y);
    }
    
    public void fill(int color, Vector position, float width, float height) {
        fill(color, position.x, position.y, width, height);
    }
    
    public void fill(int color, Vector position, Vector size) {
        fill(color, position.x, position.y, size.x, size.y);
    }
    
    public void draw(Graphic g, float x, float y) {
        draw(g, x, y, 0);
    }
    
    public BufferedImage getRender() {
        return buffer;
    }
    
    public void clip(int x, int y, int width, int height) {
        graphics.setClip(x, y, width, height);
    }
    
    public int getWidth() { return buffer.getWidth(); }
    public int getHeight() { return buffer.getHeight(); }
}
