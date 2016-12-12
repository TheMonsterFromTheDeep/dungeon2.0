package alfredo.phx;

import alfredo.Component;
import alfredo.Entity;
import alfredo.Scene;
import alfredo.geom.Vector;
import java.util.ArrayList;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class Physics {
    public static Vector gravity = new Vector(0, 9.81f);
    
    public static void tick() {
        Entity[] entities = Entity.getAllEntities();
        
        ArrayList<Body> bodies = new ArrayList();
        
        for(Entity e : entities) {
            Body b = e.getComponent(Body.class);
            
            if(b != null) { bodies.add(b); }
        }
        
        for(Body b : bodies) {
            b.acceleration.set(gravity);
        }
        
        Scene.getCurrent().tick();
        
        for(Entity e : entities) {
            for (Component c : e.getComponents()) {
                c.tick();
            }
        }
    }
}
