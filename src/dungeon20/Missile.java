/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeon20;

import alfredo.Component;
import alfredo.Entity;
import alfredo.geom.Vector;
import alfredo.gfx.Animation;
import alfredo.gfx.Renderer;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class Missile extends Component {
    static Animation missile = null;
    static Animation friend = null;
    
    public static Entity newMissile(Vector position, double direction, boolean isFriendly) {
        if(missile == null) {
            missile = Animation.load("/resrc/img/missile.png", 3);
            missile.setStep(1);
            friend = Animation.load("/resrc/img/missile_friendly.png", 3);
            friend.setStep(1);
        }
        
        Animation anim = (isFriendly ? friend : missile).getInstance();
        return Entity.create(position, direction).chain(new Renderer(anim)).chain(new Missile(anim, isFriendly));
    }
    
    final Animation anim;
    final boolean friendly;
    
    public final int damage = 1;
    
    public Missile(Animation anim, boolean friendly) {
        this.anim = anim;
        this.friendly = friendly;
    }
    
    @Override
    public void destroy() {
        Explosion.populate(parent.position, Explosion.EXPLOSION_FIRE);
    }
    
    @Override
    public void tick() {
        parent.move(5);
        
        anim.tick();
        
        if(parent.position.x > 240 || parent.position.x < 0 || parent.position.y > 180 || parent.position.y < 0) {
            parent.destroy();
        }
        
        if(!friendly) {
            Bullet b;
            for(Entity e : Entity.getAllEntities()) {
                if((b = e.getComponent(Bullet.class)) != null) {
                    if(b.type == Bullet.TYPE_LIGHTNING) { continue; }
                    Vector pos = new Vector(e.position);
                    pos.subtract(parent.position);
                    if(pos.getMagnitude() < 10) {
                        e.destroy();
                        parent.destroy();
                    }
                }
            }
        }
    }
}
