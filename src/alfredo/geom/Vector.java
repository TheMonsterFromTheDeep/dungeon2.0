package alfredo.geom;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class Vector {
    public float x;
    public float y;
    
    private boolean cached = false;
    private float magnitude;
    private float direction;
    
    public static Vector fromDirection(float length, float direction) {
        return new Vector(length * (float)Math.cos(Math.toRadians(direction)), length * (float)Math.sin(Math.toRadians(direction)));
    }
    
    private void checkCache() {
        if(!cached) {
            magnitude = (float)Math.sqrt(x * x + y * y);
            direction = (float)Math.toDegrees(Math.atan2(y, x));
            cached = true;
        }
    }
    
    public Vector() {
        x = y = 0;
    }
    
    public Vector(float dupli) {
        x = y = dupli;
    }
    
    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector(Vector orig) {
        this.x = orig.x;
        this.y = orig.y;
    }
    
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
        cached = false;
    }
    
    public void set(Vector dupli) {
        x = dupli.x;
        y = dupli.y;
        cached = false;
    }
    
    public void setX(float x) { this.x = x; cached = false; }
    public void setY(float y) { this.y = y; cached = false; }
    
    public void setX(Vector dupli) { this.x = dupli.x; cached = false; }
    public void setY(Vector dupli) { this.y = dupli.y; cached = false; }
    
    public void add(float x, float y) {
        this.x += x;
        this.y += y;
        cached = false;
    }
    
    public void add(Vector dupli) {
        this.x += dupli.x;
        this.y += dupli.y;
        cached = false;
    }
    
    public void addX(float x) { this.x += x; cached = false; }
    public void addY(float y) { this.y += y; cached = false; }
    
    public void addX(Vector dupli) { this.x += dupli.x; cached = false; }
    public void addY(Vector dupli) { this.y += dupli.y; cached = false; }
    
    public void subtract(float x, float y) {
        this.x -= x;
        this.y -= y;
        cached = false;
    }
    
    public void subtract(Vector dupli) {
        this.x -= dupli.x;
        this.y -= dupli.y;
        cached = false;
    }
    
    public void subtractX(float x) { this.x -= x; cached = false; }
    public void subtractY(float y) { this.y -= y; cached = false; }
    
    public void subtractX(Vector dupli) { this.x -= dupli.x; cached = false; }
    public void subtractY(Vector dupli) { this.y -= dupli.y; cached = false; }
    
    public void scale(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        cached = false;
    }
    
    public void rotate(float px, float py, double theta) {
        float dx = x - px;
        float dy = y - py;
        theta = Math.toRadians(theta);
        double cos = Math.cos(theta);
        double sin = Math.sin(theta);
        x = (float)(px + (dx * cos) + (dy * sin));
        y = (float)(py - (dx * sin) + (dy * cos));
        cached = false;
    }
    
    public void move(float distance, double direction) {
        x += distance * Math.cos(Math.toRadians(direction));
        y += distance * Math.sin(Math.toRadians(direction));
    }
    
    public float getMagnitude() {
        checkCache();
        return magnitude;
    }
    
    public float getDirection() {
        checkCache();
        return direction;
    }
    
    public float distance(Vector other) {
        return (float) Math.sqrt( (other.x - x) * (other.x - x) + (other.y - y) * (other.y - y) );
    }
}
