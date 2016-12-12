/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeon20;

import alfredo.Canvas;
import alfredo.Component;
import alfredo.Entity;
import alfredo.geom.Vector;
import alfredo.gfx.Graphic;
import alfredo.gfx.ImageGraphic;
import alfredo.gfx.Renderer;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class Cinder extends Component {
    public static int CINDER_SLIME = 0;
    public static int CINDER_BONE = 1;
    public static int CINDER_SNAIL = 2;
    
    static Graphic[] cinders = null;
    
    public static void populate(Vector position, int cinder) {
        if(cinders == null) {
            cinders = new Graphic[3];
            cinders[0] = ImageGraphic.load("/resrc/img/cinder/slime.png");
            cinders[1] = ImageGraphic.load("/resrc/img/cinder/bone.png");
            cinders[2] = ImageGraphic.load("/resrc/img/cinder/snail.png");
        }
        
        for(int i = 0; i < 40; ++i) {
            Entity.create(position).chain(new Cinder(cinder));
        }
    }
    
    int lifetime = 0;
    
    final int cinder;
    
    final Vector delta;
    
    public Cinder(int cinder) {
        delta = Vector.fromDirection(2 + (float)(Math.random() * 5), 180 + (float)(Math.random() * 180));
        this.cinder = cinder;
    }
    
    @Override
    public void draw(Canvas c) {
        c.draw(cinders[cinder], parent.position.x, parent.position.y, 0, 1f / (0.5f * lifetime + 1));
    }
    
    @Override
    public void tick() {
        parent.position.add(delta);
        parent.position.y += lifetime;
        
        ++lifetime;
        
        if(lifetime > 20) { parent.destroy(); }
    }
}
