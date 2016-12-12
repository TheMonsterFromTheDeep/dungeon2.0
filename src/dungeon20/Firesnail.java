/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeon20;

import alfredo.Component;
import alfredo.Entity;
import alfredo.Game;
import alfredo.Sound;
import alfredo.geom.Vector;
import alfredo.gfx.Animation;
import alfredo.gfx.Renderer;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class Firesnail extends Component {
    static Animation firesnail = null;
    static Animation boss = null;
    
    static Sound hurt, kill;
    
    public static Entity newFiresnail(boolean isBoss) {
        if(firesnail == null) {
            firesnail = Animation.load("/resrc/img/enemy/firesnail.png", 16);
            boss = Animation.load("/resrc/img/enemy/firesnail_boss.png", 16);
            
            hurt = Sound.load("/resrc/snd/snail_hurt.wav");
            kill = Sound.load("/resrc/snd/snail.wav");
        }
        
        if(isBoss) {
            TextBubble.announceBoss();
        }
        
        float health = (float)(Player.lifetime / 500) + (isBoss ? 150 : 15 + (float)(Math.random() * 5));
        
        Animation anim = isBoss ? boss.getInstance() : firesnail.getInstance();
        
        return Entity.create(Math.random() > 0.5 ? 0 : 240, 40 + (float)(Math.random() * 112))
                .chain(new Renderer(anim))
                .chain(new Firesnail(anim))
                .chain(new Healthbar(health))
                .chain(new Enemy(isBoss ? 6 : 2))
                .chain(new Drops(3)
                        .add(new Drops.Pair(Drop.TYPE_SNAIL, 5 + (health / 20), 10 + (health / 20), 1))
                        .add(new Drops.Pair(Drop.TYPE_ARROW, isBoss ? 15 : 3, isBoss ? 20 : 5, isBoss ? 0.8f : 0.6f))
                        .add(new Drops.Pair(Drop.TYPE_FIRE_ARROW, isBoss ? 15 : 2, isBoss ? 30 : 5, isBoss ? 1 : 0.7f))
                )
                .chain(new EnemySound(hurt, kill))
                .tag(Main.TAG_ENEMY);
    }
    
    public Firesnail(Animation anim) {
        target = new Vector();
        delta = new Vector();
        velocity = new Vector();
        this.anim = anim;
    }
    
    final Animation anim;
    
    final Vector target;
    final Vector delta;
    
    final Vector velocity;
    
    int cooldown = 0;
    
    float fireDirection = 0;
    
    public int frozen = 0;
    
    @Override
    public void destroy() {
        Cinder.populate(parent.position, Cinder.CINDER_SNAIL);
    }
    
    private void fire() {
        if(cooldown <= 0) {
            Missile.newMissile(parent.position, fireDirection, parent.tag == Main.TAG_SLIMED);
            
            cooldown = 80;
        }
    }
    
    @Override
    public void tick() {
        parent.position.add(velocity);

        if(parent.position.x < 0) {
            parent.position.x = 0;
        }
        else if(parent.position.x > 240) {
            parent.position.x = 240;
        }
        if(parent.position.y < 30) {
            parent.position.y = 30;
        }
        else if(parent.position.y > 170) {
            parent.position.y = 170;
        }
        
        if(frozen > 0) {
            --frozen;
            
            velocity.x += (0 - velocity.x) / 5;
            velocity.y += (0 - velocity.y) / 5;
            
            return;
        }
        
        Entity targetEntity = null;
        if(parent.tag == Main.TAG_SLIMED) {
            for(Entity e : Entity.getAllEntities()) {
                if(e.tag == Main.TAG_ENEMY) {
                    targetEntity = e;
                    break;
                }
            }
        }
        else {
            targetEntity = Entity.getWithComponent(Player.class);
        }
        
        if(targetEntity == null) {
            targetEntity = parent;
        }
        
        delta.x = targetEntity.position.x - parent.position.x;
        delta.y = targetEntity.position.y - parent.position.y;
        
        if(Math.abs(delta.x) < 10) {
            delta.x = 0;
            delta.y = delta.y > 0 ? 0.5f : -0.5f;
            fireDirection = delta.y > 0 ? 90 : 270;
            fire();
        }
        else if(Math.abs(delta.y) < 10) {
            delta.x = delta.x > 0 ? 0.5f : -0.5f;
            delta.y = 0;
            fireDirection = delta.x > 0 ? 0 : 180;
            fire();
        }
        else if(Math.abs(delta.x) < Math.abs(delta.y)) {
            delta.x = delta.x > 0 ? 0.5f : -0.5f;
            delta.y = 0;
        }
        else {
            delta.x = 0;
            delta.y = delta.y > 0 ? 0.5f : -0.5f;
        }
        
        if(delta.x != 0) {
            anim.setRange(delta.x < 0 ? 12 : 8, delta.x < 0 ? 15 : 11);
        }
        else {
            anim.setRange(delta.y < 0 ? 4 : 0, delta.y < 0 ? 7 : 3);
        }
        
        
        
        velocity.x += (delta.x - velocity.x) / 5;
        velocity.y += (delta.y - velocity.y) / 5;
        
        
        anim.tick();
        
        --cooldown;
    }
}
