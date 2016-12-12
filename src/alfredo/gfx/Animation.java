package alfredo.gfx;

import alfredo.geom.Vector;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class Animation extends Graphic {
    private final BufferedImage[] frames;
    private int start = 0;
    private int end = 0;
    private int length = 0;
    private int direction = 1;
    private float current = 0;
    private float step = 0.25f;
    
    public final Vector pivot;
    
    public static Animation load(String path, int frameCount, Vector pivot) {
        try {
            BufferedImage image = ImageIO.read(ImageGraphic.class.getResourceAsStream(path));
            return new Animation(image, frameCount, pivot);
        } catch (IOException ex) {
            System.err.println("Error loading animation: " + ex.getLocalizedMessage());
            return new Animation(pivot);
        }
    }
    
    public static Animation load(String path, int frameCount) {
        return load(path, frameCount, new Vector());
    }
    
    private Animation(BufferedImage[] frames, Vector pivot, float step) {
        this.frames = frames;
        length = frames.length;
        end = length - 1;
        this.pivot = pivot;
        this.step = step;
    }
    
    public Animation(Vector pivot) {
        this.frames = new BufferedImage[] { null };
        this.pivot = pivot;
    }
    
    public Animation(BufferedImage base, int frameCount, Vector pivot) {
        if(base.getWidth() % frameCount != 0) {
            throw new IllegalArgumentException("Image must have an integer number of frames.");
        }
        
        frames = new BufferedImage[frameCount];
        int width = base.getWidth() / frameCount;
        for(int i = 0; i < frames.length; ++i) {
            frames[i] = base.getSubimage(i * width, 0, width, base.getHeight());
        }
        
        this.pivot = pivot;
        
        length = frames.length;
        end = frames.length - 1;
    }

    @Override
    public BufferedImage getRender() {
        return frames[(int)current];
    }

    @Override
    public Vector getPivot() {
        return pivot;
    }

    @Override
    public void setPivot(Vector v) {
        pivot.set(v);
    }
    
    public void tick() {
        current += step * direction;
        if(current - start >= length) {
            current -= length;
        }
    }
    
    public void next() {
        ++current;
        if(current - start >= length) {
            current -= length;
        }
    }
    
    public void setFrame(int frame) {
        if(frame < 0 || frame >= frames.length) {
            throw new IllegalArgumentException("Frame out of frame range");
        }
        current = frame;
    }
    
    public void setRange(int start, int end) {
        if(start < 0 || start >= frames.length) {
            throw new IllegalArgumentException("Start out of frame range");
        }
        if(end < 0 || end >= frames.length) {
            throw new IllegalArgumentException("End out of frame range");
        }
        if(end < start) {
            throw new IllegalArgumentException("Alfredo does not support reverse animations yet.");
        }
        if(current < start || current > end) {
            current = start;
        }
        this.start = start;
        this.end = end;
        length = end - start + 1;
        direction = 1;
    }
    
    public int getStart() { return start; }
    public int getEnd() { return end; }
    
    public void setStart(int start) {
        setRange(start, this.end);
    }
    
    public void setEnd(int end) {
        setRange(this.start, end);
    }
    
    public void setLength(int length) {
        setRange(this.start, this.start + length);
    }
    
    public void setStep(float step) {
        if(step < 0) {
            throw new IllegalArgumentException("Alfredo does not support reverse animations yet.");
        }
        this.step = step;
    }
    
    public Animation getInstance() {
        return new Animation(frames, pivot, step);
    }
}
