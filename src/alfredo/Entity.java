package alfredo;

import alfredo.geom.Vector;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class Entity {
    private static final ArrayList<Entity> entities = new ArrayList();
    
    public final Vector position;
    public double direction;
    private final ArrayList<Component> components;
    
    public int tag = 0;
    
    public static <T extends Entity> T create(Vector position, double direction) {
        T newEntity = (T)new Entity(position, direction);
        newEntity.init();
        
        entities.add(newEntity);
        
        return newEntity;
    }
    
    public static <T extends Entity> T create(Vector position) {
        return create(position, 0);
    }
    
    public static <T extends Entity> T create(float x, float y) {
        return create(new Vector(x, y));
    }
    
    public static <T extends Entity> T insert(float x, float y) {
        T newEntity = (T)new Entity(new Vector(x, y), 0);
        newEntity.init();
        
        entities.add(0, newEntity);
        
        return newEntity;
    }
    
    public static <T extends Entity> T create(Entity old) {
        return create(old.position, old.direction);
    }
    
    public static <T extends Entity> T create() {
        return create(new Vector());
    }
    
    public static void clear() {
        entities.clear();
    }
    
    protected Entity(Vector position, double direction) {
        this.position = new Vector(position);
        this.direction = direction;
        components = new ArrayList();
    }
    
    protected void init() { }
    
    public <T extends Component> T getComponent(Class<T> type) {
        for(Component c : components) {
            if(c.getClass() == type) {
                return (T)c;
            }
        }
        return null;
    }
    
    public <T extends Component> T addComponent(T c) {
        components.add(c);
        c.parent = this;
        
        c.ready();
        
        return c;
    }
    
    public Entity chain(Component c) {
        components.add(c);
        c.parent = this;
        
        c.ready();
        
        return this;
    }
    
    public Entity tag(int tag) {
        this.tag = tag;
        return this;
    }
    
    public <T extends Component> T removeComponent(Class<T> type) {
        Component c = null;
        for(Iterator<Component> i = components.iterator(); i.hasNext(); c = i.next()) {
            if(c == null) { continue; }
            if(c.getClass() == type) {
                i.remove();
                return (T)c;
            }
        }
        return null;
    }
    
    public final void move(float distance) {
        position.add((float)(distance * Math.cos(Math.toRadians(direction))), (float)(distance * Math.sin(Math.toRadians(direction))));
    }
    
    public final Component[] getComponents() {
        return components.toArray(new Component[0]);
    }
    
    public final void destroy() {
        for(Component c : components) {
            c.destroy();
        }
        entities.remove(this);
    }
    
    public static Entity[] getAllEntities() {
        return entities.toArray(new Entity[0]);
    }
    
    public static <T extends Component> Entity getWithComponent(Class<T> type) {
        for(Entity e : entities) {
            if(e.getComponent(type) != null) { return e; }
        }
        return null;
    }
}
