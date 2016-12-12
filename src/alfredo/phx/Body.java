package alfredo.phx;

import alfredo.Component;
import alfredo.Entity;
import alfredo.Game;
import alfredo.geom.Vector;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class Body extends Component {
    public static float DEFAULT_MASS = 10;
    
    public float mass;
    public final Vector acceleration, velocity;
    
    public Body(Vector velocity, float mass) {
        this.velocity = velocity;
        this.mass = mass;
        acceleration = new Vector();
    }
    
    public Body(Vector velocity) {
        this(velocity, DEFAULT_MASS);
    }
    
    public Body(float mass) {
        this(new Vector(), mass);
    }
    
    public Body() {
        this(DEFAULT_MASS);
    }
    
    @Override
    public void tick() {
        float delay = Game.getDelay() / 1000f;
        velocity.add(acceleration.x * delay, acceleration.y * delay);
        parent.position.add(velocity.x, velocity.y);
    }
    
    public void addForce(Vector force) {
        force.scale(1 / mass);
        acceleration.add(force);
    }
    
    public void accelerate(Vector acc) {
        acceleration.add(acc);
    }
}
