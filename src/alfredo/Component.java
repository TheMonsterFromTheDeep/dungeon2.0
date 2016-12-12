package alfredo;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class Component {
    protected Entity parent;
    
    public Component() {
        
    }
    
    public Entity getParent() { return parent; }
    
    public void ready() { }
    public void tick() { }
    public void draw(Canvas c) { }
    
    public void destroy() {}
}
