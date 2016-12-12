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
import alfredo.gfx.Animation;
import alfredo.gfx.Graphic;
import alfredo.gfx.ImageGraphic;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class TextBubble extends Component {
    public static final int TEXT_CRIT = 0;
    public static final int TEXT_SLIME = 1;
    public static final int TEXT_BETRAY = 2;
    public static final int TEXT_RESURRECT = 3;
    public static final int TEXT_MINIBOSS = 4;
    public static final int TEXT_WAVE = 5;
    public static final int PARTICLE_FIRE = 6; //Not really text, but... oh well
    public static final int TEXT_WAVE_END = 7;
    public static final int TEXT_UNLOCK = 8;
    public static final int TEXT_LOSE = 9;
    
    static Graphic[] bubbles = null;
    
    public static Entity newBubble(Vector position, int bubble, boolean announce) {
        if(bubbles == null) {
            bubbles = new Graphic[10];
            bubbles[0] = ImageGraphic.load("/resrc/img/text/crit.png");
            bubbles[1] = ImageGraphic.load("/resrc/img/text/slimed.png");
            bubbles[2] = ImageGraphic.load("/resrc/img/text/betrayed.png");
            bubbles[3] = ImageGraphic.load("/resrc/img/text/resurrected.png");
            bubbles[4] = ImageGraphic.load("/resrc/img/text/miniboss.png");
            bubbles[5] = ImageGraphic.load("/resrc/img/text/wave.png");
            bubbles[6] = ImageGraphic.load("/resrc/img/fireparticle.png");
            bubbles[7] = ImageGraphic.load("/resrc/img/text/wave_end.png");
            bubbles[8] = ImageGraphic.load("/resrc/img/text/unlocked.png");
            bubbles[9] = ImageGraphic.load("/resrc/img/text/lose.png");
        }
        
        return Entity.create(position.x - 8 + (float)(16 * Math.random()), position.y - 11).chain(new TextBubble(bubble, announce));
    }
    
    public static Entity newBubble(Vector position, int bubble) {
        return newBubble(position, bubble, false);
    }
    
    public static void announceBoss() {
        newBubble(new Vector(180, 40), TEXT_MINIBOSS, true);
    }
    
    public static void announceUnlock() {
        newBubble(new Vector(180, 40), TEXT_UNLOCK, true);
    }
    
    final int graphic;
    
    int lifetime = 0;
    
    double rotation = 0;
    
    double dtheta;
    
    final boolean isAnnouncer;
    
    public TextBubble(int graphic, boolean announce) {
        this.graphic = graphic;
        
        dtheta = (announce ? 1 : 5) * (Math.random() - 0.5);
        isAnnouncer = announce;
    }
    
    @Override
    public void draw(Canvas c) {
        float o = isAnnouncer ? (
                    lifetime < 20 ? 1:
                    1f / ((lifetime - 20) + 1)
                ) :
                1f / (lifetime + 1);
        c.draw(bubbles[graphic], parent.position.x, parent.position.y, rotation, o);
    }
    
    @Override
    public void tick() {
        ++lifetime;
        
        if(!isAnnouncer || lifetime > 20) {
            --parent.position.y;
        }
        
        rotation += dtheta;
        
        
        if(lifetime > (isAnnouncer ? 40 : 20)) {
            parent.destroy();
        }
    }
}
