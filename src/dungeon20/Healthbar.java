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
public class Healthbar extends Component {
    static Graphic bar;
    static Graphic slimed;
    static Graphic frozenbar;
    static Graphic firebar;
    
    public float capacity;
    public float current;
    public float display;
    
    static final int freezeTime = 60;
    
    int frozen = 0;
    int fire = 0;
    
    public Healthbar(float capacity) {
        if(bar == null) {
            bar = ImageGraphic.load("/resrc/img/healthbar.png");
            slimed = ImageGraphic.load("/resrc/img/slimebar.png");
            frozenbar = ImageGraphic.load("/resrc/img/frozen.png"); //Lol bad naming conventions xd
            firebar = ImageGraphic.load("/resrc/img/onfire.png");
        }
        this.display = this.current = this.capacity = capacity;
    }
    
    @Override
    public void ready() {
        
    }
    
    public void changeBy(float delta) {
        current += delta;
        
        HealthBubble.newBubble(parent.position, delta);
        
        if(parent.getComponent(Healer.class) != null) {
            parent.getComponent(Healer.class).damageCooldown = 60;
        }
        
        if(parent.getComponent(EnemySound.class) != null) {
            if(current > 0 && delta < 0) {
                parent.getComponent(EnemySound.class).hurt();
            }
        }
    }
    
    @Override
    public void tick() {
        --frozen;
        
        Player p;
        
        float lavaDist = 176 - parent.position.y;
        if(lavaDist < 40) {
            changeBy(-(40 - lavaDist) / 1600);
        }
        if(fire > 0) {
            changeBy(-1);
            TextBubble.newBubble(parent.position, TextBubble.PARTICLE_FIRE);
            --fire;
        }
        if((p = parent.getComponent(Player.class)) != null) {
            Missile m;
            
            if(p.invincibleTick > 0) { --p.invincibleTick; return; }
            for(Entity e : Entity.getAllEntities()) {
                if(e.tag == Main.TAG_ENEMY) {
                    Vector pos = new Vector(e.position);
                    pos.subtract(parent.position);
                    if(pos.getMagnitude() < 10) {
                        Enemy enemy = e.getComponent(Enemy.class);
                        changeBy(enemy == null ? -1 : -enemy.level);
                        if(current <= 0) {
                            parent.destroy();
                        }
                        p.invincibleTick = 20;
                    }
                }
                if((m = e.getComponent(Missile.class)) != null) {
                    if(!m.friendly) {
                        Vector pos = new Vector(e.position);
                        pos.subtract(parent.position);
                        if(pos.getMagnitude() < 10) {
                            changeBy(-m.damage);
                            if(current <= 0) {
                                parent.destroy();
                            }
                            p.invincibleTick = 20;
                            e.destroy();
                        }
                    }
                }
            }
        }
        else if(parent.tag == Main.TAG_ENEMY) {
            Bullet b;
            Missile m;
            
            for(Entity e : Entity.getAllEntities()) {
                if(e.tag == Main.TAG_SLIMED) {
                    Vector pos = new Vector(e.position);
                    pos.subtract(parent.position);
                    if(pos.getMagnitude() < 10) {
                        Healthbar h = e.getComponent(Healthbar.class);
                        double tmp = h.current;
                        if(current >= 0) {
                            h.changeBy(-current);
                        }
                        if(tmp >= 0) {
                            changeBy((float) -tmp);
                        }
                        if(current <= 0) {
                            TextBubble.newBubble(parent.position, TextBubble.TEXT_BETRAY);
                        }
                    }
                }
                else if((b = e.getComponent(Bullet.class)) != null) {
                    if(b.type == Bullet.TYPE_LIGHTNING) { continue; }
                    Vector pos = new Vector(e.position);
                    pos.subtract(parent.position);
                    if(pos.getMagnitude() < 10) {
                        e.destroy();
                        float damage = b.damage;
                        if(b.type != Bullet.TYPE_NOVA) {
                            if(Math.random() <= 0.05) {
                                damage *= 10;
                                Entity crit = TextBubble.newBubble(parent.position, TextBubble.TEXT_CRIT);
                                crit.position.y -= 8;
                            }
                        }
                        changeBy(-damage);
                        if(b.type != Bullet.TYPE_NOVA) {
                            if(parent.getComponent(Skeleton.class) != null) {
                                parent.getComponent(Skeleton.class).velocity.move(3, b.getParent().direction);
                            }
                            if(parent.getComponent(Firesnail.class) != null) {
                                parent.getComponent(Firesnail.class).velocity.move(3, b.getParent().direction);
                            }
                        }
                        if(b.type == Bullet.TYPE_ICE) {
                            if(parent.getComponent(Slime.class) != null) {
                                parent.getComponent(Slime.class).frozen = freezeTime;
                            }
                            if(parent.getComponent(Skeleton.class) != null) {
                                parent.getComponent(Skeleton.class).frozen = freezeTime;
                            }
                            if(parent.getComponent(Firesnail.class) != null) {
                                parent.getComponent(Firesnail.class).frozen = freezeTime;
                            }
                            this.frozen = freezeTime;
                        }
                        if(b.type == Bullet.TYPE_FIRE) {
                            fire = (int) (b.damage);
                        }
                        if(b.type == Bullet.TYPE_SLIME && current <= 0) {
                            TextBubble.newBubble(parent.position, TextBubble.TEXT_SLIME);
                            capacity /= 2;
                            current = capacity;
                            display = capacity;
                            parent.tag = Main.TAG_SLIMED;
                            parent.addComponent(new Healer());
                        }
                    }
                }
                else if((m = e.getComponent(Missile.class)) != null) {
                    if(m.friendly) {
                        Vector pos = new Vector(e.position);
                        pos.subtract(parent.position);
                        if(pos.getMagnitude() < 10) {
                            e.destroy();
                            float damage = m.damage;
                            if(Math.random() <= 0.05) {
                                damage *= 10;
                                Entity crit = TextBubble.newBubble(parent.position, TextBubble.TEXT_CRIT);
                                crit.position.y -= 8;
                            }
                            changeBy(-damage);
                            if(parent.getComponent(Skeleton.class) != null) {
                                parent.getComponent(Skeleton.class).velocity.move(3, m.getParent().direction);
                            }
                            if(parent.getComponent(Firesnail.class) != null) {
                                parent.getComponent(Firesnail.class).velocity.move(3, m.getParent().direction);
                            }
                            if(current <= 0) {
                                TextBubble.newBubble(parent.position, TextBubble.TEXT_BETRAY);
                            }
                        }
                    }
                }
            }
        }
        if(current <= 0) {
            double chance = 1 - Math.exp(capacity / -30); //Asymtotically appraoch an 100% chance of resurrection as x gets bigger
            if(parent.tag == Main.TAG_SLIMED && Math.random() < chance) {
                parent.tag = Main.TAG_ENEMY; //Come back to life as enemy again
                capacity *= 3; //Grow stronger
                current = capacity;
                display = capacity;
                parent.removeComponent(Healer.class);
                TextBubble.newBubble(parent.position, TextBubble.TEXT_RESURRECT);
            }
            else {
                parent.destroy();
            }
        }
        display += (current - display) / 5; //Transition smoothly between healths
    }
    
    @Override
    public void draw(Canvas c) {
        c.fill(frozen > 0 ? 0x4ddbff : 
               fire > 0   ? 0xff9c00 :
                            0xff0000, 
                parent.position.x - 7, parent.position.y - 9, 14 * (display / capacity), 2);
        
        c.draw(
                parent.tag == Main.TAG_SLIMED ? slimed : 
                                frozen > 0 ? frozenbar : 
                                fire > 0   ? firebar   : 
                                             bar, parent.position.x, parent.position.y - 8);
    }
}
