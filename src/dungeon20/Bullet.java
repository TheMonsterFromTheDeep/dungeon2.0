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
public class Bullet extends Component {

    public static final int TYPE_BASIC = 0;
    public static final int TYPE_SLIME = 1;
    public static final int TYPE_BOLT = 2;
    public static final int TYPE_BONE = 3;
    public static final int TYPE_LIGHTNING = 4;
    public static final int TYPE_NOVA = 5;
    public static final int TYPE_FIRE = 6;
    public static final int TYPE_ICE = 7;
    
    public float damage;
    public int type = TYPE_BASIC;
    
    static Graphic[] bullets = null;
    static Renderer[] bulletRenderers = null;
    
    int ticksAlive = 0;
    
    static void init() {
        if(bullets != null) { return; }
        
        bullets = new Graphic[8];
        bullets[TYPE_BASIC] = ImageGraphic.load("/resrc/img/bullet/basic.png");
        bullets[TYPE_SLIME] = ImageGraphic.load("/resrc/img/bullet/slime.png");
        bullets[TYPE_BOLT] = ImageGraphic.load("/resrc/img/bullet/bolt.png");
        bullets[TYPE_BONE] = ImageGraphic.load("/resrc/img/bullet/bone.png");
        bullets[TYPE_LIGHTNING] = ImageGraphic.load("/resrc/img/bullet/lightning.png");
        bullets[TYPE_NOVA] = ImageGraphic.load("/resrc/img/bullet/nova.png");
        bullets[TYPE_FIRE] = ImageGraphic.load("/resrc/img/bullet/fire.png");
        bullets[TYPE_ICE] = ImageGraphic.load("/resrc/img/bullet/ice.png");
        
        bulletRenderers = new Renderer[bullets.length];
        for(int i = 0; i < bullets.length; ++i) {
            bulletRenderers[i] = new Renderer(bullets[i]);
        }
    }
    
    public static Entity newBullet(Vector position, double direction, float damage, int type) {
        init();
        Entity bullet = Entity.create(position, direction).chain(new Bullet(damage, type));
        if(type != TYPE_LIGHTNING) {
            bullet.chain(bulletRenderers[type]);
        }
        return bullet;
    }
    
    public Bullet() {
        damage = 1;
    }
    
    public Bullet(float damage, int type) {
        this.damage = damage;
        this.type = type;
    }
    
    @Override
    public void ready() {
        if(type == TYPE_LIGHTNING) {
            Vector position = new Vector();
            switch((int)parent.direction) {
                case 0:
                    position.set(240, parent.position.y);
                    break;
                case 90:
                    position.set(parent.position.x, 180);
                    break;
                case 180:
                    position.set(0, parent.position.y);
                    break;
                case 270:
                    position.set(parent.position.x, 0);
                    break;
            }
            Explosion.populate(position, Explosion.EXPLOSION_LIGHTNING);
        }
    }
    
    @Override
    public void draw(Canvas c) {
        if(type == TYPE_LIGHTNING) {
            int dx = 0, dy = 0;
            switch((int)parent.direction) {
                case 0:
                    dx = 4; dy = 0;
                    break;
                case 90:
                    dx = 0; dy = 4;
                    break;
                case 180:
                    dx = -4; dy = 0;
                    break;
                case 270:
                    dx = 0; dy = -4;
                    break;
            }
            float x = parent.position.x;
            float y = parent.position.y;
            while(x > 0 && x < 240 && y > 0 && y < 180) {
                c.draw(bullets[TYPE_LIGHTNING], x, y, parent.direction, 1 / (ticksAlive + 1));
                x += dx;
                y += dy;
            }
        }
    }
    
    @Override
    public void destroy() {
        if(type == TYPE_BOLT) {
            Explosion.populate(parent.position, Explosion.EXPLOSION_BOLT);
        }
        if(type == TYPE_NOVA) {
            Explosion.populate(parent.position, Explosion.EXPLOSION_NOVA);
        }
        if(type == TYPE_ICE) {
            Explosion.populate(parent.position, Explosion.EXPLOSION_ICE);
        }
        if(type == TYPE_BASIC) {
            Explosion.populate(parent.position, Explosion.EXPLOSION_ARROW);
        }
        if(type == TYPE_SLIME) {
            Explosion.populate(parent.position, Explosion.EXPLOSION_SLIME);
        }
        if(type == TYPE_BONE) {
            Explosion.populate(parent.position, Explosion.EXPLOSION_BONE);
        }
        if(type == TYPE_FIRE) {
            Explosion.populate(parent.position, Explosion.EXPLOSION_FLAME);
        }
    }
    
    @Override
    public void tick() {
        if(type == TYPE_LIGHTNING) {
            Entity[] entities = Entity.getAllEntities();
            for(Entity e : entities) {
                if(e.tag == Main.TAG_ENEMY) {
                    float daxis = 0;
                    float dnormal = 20; //False by default
                    switch((int)parent.direction) {
                        case 0:
                            daxis = e.position.x - parent.position.x;
                            dnormal = e.position.y - parent.position.y;
                            break;
                        case 90:
                            daxis = e.position.y - parent.position.y;
                            dnormal = e.position.x - parent.position.x;
                            break;
                        case 180:
                            daxis = parent.position.x - e.position.x;
                            dnormal = e.position.y - parent.position.y;
                            break;
                        case 270:
                            daxis = parent.position.y - e.position.y;
                            dnormal = e.position.x - parent.position.x;
                            break;
                    }
                    if(daxis >= 0 && Math.abs(dnormal) <= 10) {
                        Healthbar b = e.getComponent(Healthbar.class);
                        b.changeBy(-damage / (ticksAlive + 1));
                    }
                }
            }
            if(ticksAlive >= 20) {
                parent.destroy();
            }
        }
        else {
            parent.move(type == TYPE_BONE ? 10 : type == TYPE_FIRE ? 3 : 5);

            if(parent.position.x > 240 || parent.position.x < 0 || parent.position.y > 180 || parent.position.y < 0) {
                parent.destroy();
            }
        }
        ++ticksAlive;
    }
    
}
