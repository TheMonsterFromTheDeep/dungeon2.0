package dungeon20;

import alfredo.Component;
import alfredo.Entity;
import alfredo.Game;
import alfredo.Sound;
import alfredo.geom.Vector;
import alfredo.gfx.Animation;
import alfredo.gfx.Renderer;
import dungeon20.Drops.Pair;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class Slime extends Component {
    static Animation slime = null;
    static Animation boss = null;
    static Animation arrow = null;
    
    static Sound hurt, kill;
    
    public static Entity newSlime(boolean isBoss) {
        if(slime == null) {
            slime = Animation.load("/resrc/img/enemy/slime_green.png", 1);
            boss = Animation.load("/resrc/img/enemy/slime_green_boss.png", 1);
            arrow = Animation.load("/resrc/img/enemy/slime_green_arrow.png", 1);
            
            hurt = Sound.load("/resrc/snd/slime_hurt.wav");
            kill = Sound.load("/resrc/snd/slime.wav");
        }
        
        if(isBoss) {
            TextBubble.announceBoss();
        }
        
        boolean isArrow = isBoss ? false : Math.random() < 0.47;
        
        float health = (float)(Player.lifetime / 500) + (isBoss ? 70 : 2) + (float)(Math.random() * 1.5);
        
        return Entity.create(Math.random() > 0.5 ? 0 : 240, 40 + (float)(Math.random() * 112))
                .chain(new Renderer(isBoss ? boss.getInstance() : isArrow ? arrow.getInstance() : slime.getInstance()))
                .chain(new Slime(isBoss))
                .chain(new Healthbar(health))
                .chain(new Enemy(isBoss ? 3 : 1))
                .chain(new Drops(3)
                        .add(new Pair(Drop.TYPE_SLIME, 5 + (health / 20), 10 + (health / 20), 1))
                        .add(new Pair(Drop.TYPE_ARROW, isBoss ? 15 : 3, isBoss ? 20 : 5, (isBoss || isArrow) ? 1 : 0))
                        .add(new Pair(Drop.TYPE_SLIME_ARROW, isBoss ? 7 : 2, isBoss ? 14 : 5, isBoss ? 0.6f : isArrow ? 0.3f : 0))
                )
                .chain(new EnemySound(hurt, kill))
                .tag(Main.TAG_ENEMY);
    }
    
    public Slime(boolean isBoss) {
        this.isBoss = isBoss;
    }
    
    public int frozen = 0;
    
    boolean isBoss;
    
    int tick;
    int height;
    float base;
    
    int length;
    
    int delay = 0;
    
    int direction;
    int axis;
    
    @Override
    public void ready() {
        setup();
    }
    
    @Override
    public void destroy() {
        Cinder.populate(parent.position, Cinder.CINDER_SLIME);
    }
    
    private void setup() {
        height = 0;
        length = (int)(8 + (Math.random() * (isBoss ? 2 : 4)));
        tick = -length;
        delay = (int)(20 + Math.random() * 20);
        axis = Math.random() > 0.5 ? 1 : 0;
        if(axis == 1) {
            if(parent.position.y <= 48) {
                direction = 1;
            }
            else if(parent.position.y >= 160) {
                direction = -1;
            }
            else {
                direction = Math.random() > 0.5 ? 1 : -1;
            }
        }
        else {
            if(parent.position.x <= 16) {
                direction = 1;
            }
            else if(parent.position.x >= 224) {
                direction = -1;
            }
            else {
                direction = Math.random() > 0.5 ? 1 : -1;
            }
        }
        
        base = parent.position.y;
        
    }
    
    @Override
    public void tick() {
        if(frozen > 0) {
            --frozen;
            return;
        }
        
        if(delay > 0) { 
            --delay;
            return;
        }
       
        height = (-(tick - length) * (tick + length)) / length;
        parent.position.y = base - height + (axis == 1 ? (direction * (tick + 8)) : 0);
        ++tick;
        
        if(axis == 0) {
            parent.position.x += direction;
        }
        
        if(height <= 0 && tick > 0) {
            setup();
        }
    }
    
    /*@Override
    public void destroy() {
        int dropCount = (int)(isBoss ? 20 + Math.random() * 15 : Math.random() * parent.getComponent(Healthbar.class).capacity);
        for (int i = 0; i < dropCount; ++i) {
            Drop.newDrop(parent.position.x + (float)(Math.random() * 5 - 2.5), parent.position.y + (float)(Math.random() * 5 - 2.5), Drop.TYPE_SLIME);
        }
        if(isBoss || isArrow) {
            dropCount = (int)(isBoss ? 20 + Math.random() * 15 : 1 + Math.random() * parent.getComponent(Healthbar.class).capacity);
            for (int i = 0; i < dropCount; ++i) {
                Drop.newDrop(parent.position.x + (float)(Math.random() * 5 - 2.5), parent.position.y + (float)(Math.random() * 5 - 2.5), Drop.TYPE_ARROW);
            }
        }
    }*/
}
