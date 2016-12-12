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

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class Explosion extends Component {
    public static final int EXPLOSION_FIRE = 0;
    public static final int EXPLOSION_BOLT = 1;
    public static final int EXPLOSION_NOVA = 2;
    public static final int EXPLOSION_LIGHTNING = 3;
    public static final int EXPLOSION_ICE = 4;
    public static final int EXPLOSION_ARROW = 5;
    public static final int EXPLOSION_SLIME = 6;
    public static final int EXPLOSION_BONE = 7;
    public static final int EXPLOSION_FLAME = 8;
    
    static Graphic[] explosions = null;
    
    public static void populate(Vector position, int number) {
        if(explosions == null) {
            explosions = new Graphic[9];
            explosions[0] = ImageGraphic.load("/resrc/img/explosion/fire.png");
            explosions[1] = ImageGraphic.load("/resrc/img/explosion/bolt.png");
            explosions[2] = ImageGraphic.load("/resrc/img/explosion/nova.png");
            explosions[3] = ImageGraphic.load("/resrc/img/explosion/lightning.png");
            explosions[4] = ImageGraphic.load("/resrc/img/explosion/ice.png");
            explosions[5] = ImageGraphic.load("/resrc/img/explosion/arrow.png");
            explosions[6] = ImageGraphic.load("/resrc/img/explosion/slime.png");
            explosions[7] = ImageGraphic.load("/resrc/img/explosion/bone.png");
            explosions[8] = ImageGraphic.load("/resrc/img/explosion/flamearrow.png");
        }
        
        for(int i = 0; i < 20; ++i) {
            Entity.create(position).chain(new Explosion(number));
        }
    }
    
    int lifetime = 0;
    
    final int number;
    
    final Vector delta;
    
    double angle;
    
    public Explosion(int number) {
        delta = Vector.fromDirection(2 + (float)(Math.random() * 5), (float)(Math.random() * 360));
        this.number = number;
    }
    
    @Override
    public void draw(Canvas c) {
        c.draw(explosions[number], parent.position.x, parent.position.y, delta.getDirection(), 1f / (4 * lifetime + 1));
    }
    
    @Override
    public void tick() {
        parent.position.add(delta);
        
        ++lifetime;
        ++angle;
        
        if(lifetime > 5) { parent.destroy(); }
    }
}
