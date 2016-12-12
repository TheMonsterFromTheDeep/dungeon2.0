package alfredo;

import alfredo.geom.Vector;
import alfredo.gfx.Renderer;
import alfredo.phx.Physics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class Scene {
    
    private static ArrayList<Scene> scenes;
    private static Scene current = null;
    
    protected int bgcolor;
    
    public static <T extends Scene> void open(Class<T> sceneClass) {
        if(scenes == null) {
            scenes = new ArrayList();
        }
        else {
            for(Scene s : scenes) {
                if(s.getClass() == sceneClass) {
                    current = s;
                    return;
                }
            }
        }
        try {
            current = sceneClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            System.out.println("Error setting scene: " + ex);
        }
        scenes.add(current);
    }
    
    public static <T extends Scene> T get(Class<T> sceneClass) {
        if(scenes == null) {
            return null;
        }
        for(Scene s : scenes) {
            if(s.getClass() == sceneClass) {
                return (T) s;
            }
        }
        return null;
    }
    
    public static Scene getCurrent() { return current; }
    
    public Scene() { }
    
    public void render(Canvas c) {
        c.clear();
        c.fill(bgcolor, 0, 0, Camera.getMain().getViewport());
        
        backdrop(c);
        
        Entity[] all = Entity.getAllEntities();
        Renderer r;
        for(Entity e : all) {
            for(Component co : e.getComponents()) {
                co.draw(c);
            }
            r = e.getComponent(Renderer.class);
            if(r == null) { continue; }
            if(!r.active) { continue; }
            c.draw(r.graphic, e.position, e.direction);
        }
        
        draw(c);
    }
    
    public void loop() {
        Physics.tick();
    }
    
    public void tick() { }
    public void backdrop(Canvas c) { }
    public void draw(Canvas c) { }
}
