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
import static dungeon20.Slime.hurt;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class Skeleton extends Component {
    static Animation skeleton = null;
    static Animation boss = null;
    
    static Sound hurt, kill;
    
    public static Entity newSkeleton(boolean isBoss) {
        if(skeleton == null) {
            skeleton = Animation.load("/resrc/img/enemy/skeleton.png", 10);
            boss = Animation.load("/resrc/img/enemy/skeleton_boss.png", 10);
            
            hurt = Sound.load("/resrc/snd/skeleton_hurt.wav");
            kill = Sound.load("/resrc/snd/skeleton.wav");
        }
        
        if(isBoss) {
            TextBubble.announceBoss();
        }
        
        float health = (float)(Player.lifetime / 500) + (isBoss ? 100 : 10 + (float)(Math.random() * 5));
        
        Animation anim = isBoss ? boss.getInstance() : skeleton.getInstance();
        
        return Entity.create(Math.random() > 0.5 ? 0 : 240, 40 + (float)(Math.random() * 112))
                .chain(new Renderer(anim))
                .chain(new Skeleton(anim))
                .chain(new Healthbar(health))
                .chain(new Enemy(isBoss ? 6 : 2))
                .chain(new Drops(4)
                        .add(new Drops.Pair(Drop.TYPE_BONE, 5 + (health / 20), 10 + (health / 20), 1))
                        .add(new Drops.Pair(Drop.TYPE_ARROW, isBoss ? 15 : 3, isBoss ? 20 : 5, isBoss ? 0.8f : 0.6f))
                        .add(new Drops.Pair(Drop.TYPE_BONE_ARROW, isBoss ? 10 : 2, isBoss ? 14 : 5, isBoss ? 1 : 0.7f))
                        .add(new Drops.Pair(Drop.TYPE_CROWN, 1, 1, isBoss ? 1 : 0))
                )
                .chain(new EnemySound(hurt, kill))
                .tag(Main.TAG_ENEMY);
    }
    
    public Skeleton(Animation anim) {
        target = new Vector();
        delta = new Vector();
        velocity = new Vector();
        this.anim = anim;
    }
    
    final Animation anim;
    
    final Vector target;
    final Vector delta;
    
    final Vector velocity;
    
    public int frozen = 0;
    
    @Override
    public void ready() {
        setup();
    }
    
    @Override
    public void destroy() {
        Cinder.populate(parent.position, Cinder.CINDER_BONE);
    }
    
    private void setup() {
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
            target.set(parent.position);
            if(anim.getStart() == 1 || anim.getStart() == 4) {
                anim.setStart(anim.getStart() - 1);
            }
            anim.setLength(0);
            
            return;
        }
        float dx = parent.position.x - targetEntity.position.x;
        float dy = parent.position.y - targetEntity.position.y;
        
        if(Math.abs(dx) > Math.abs(dy)) {
            target.set(targetEntity.position.x, parent.position.y);
            if(target.x > 238) { target.x = 238; }
            if(target.x < 2) { target.x = 2; }
            delta.set(dx > 0 ? -1 : 1, 0);
            
            anim.setRange(dx > 0 ? 8 : 6, dx > 0 ? 9 : 7);
        }
        else {
            target.set(parent.position.x, targetEntity.position.y);
            if(target.y > 168) { target.y = 168; }
            if(target.y < 32) { target.y = 32; }
            delta.set(0, dy > 0 ? -1 : 1);
            
            anim.setRange(dy > 0 ? 4 : 1, dy > 0 ? 5 : 2);
        }
        
        velocity.set(delta);
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
        
        float dx = parent.position.x - target.x;
        float dy = parent.position.y - target.y;
        if(dx * delta.x >= 0 && dy * delta.y >= 0) {
            setup();
        }
        
        velocity.x += (delta.x - velocity.x) / 5;
        velocity.y += (delta.y - velocity.y) / 5;
        
        anim.tick();
    }
}
