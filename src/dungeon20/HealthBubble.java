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

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class HealthBubble extends Component {
    static Animation negative = null;
    static Animation positive = null;
    
    public static Entity newBubble(Vector position, float deltaHealth) {
        if(negative == null) {
            negative = Animation.load("/resrc/img/text/hurts.png", 11);
            positive = Animation.load("/resrc/img/text/healths.png", 11);
        }
        
        return Entity.create(position.x - 8 + (float)(16 * Math.random()), position.y - 11).chain(new HealthBubble(deltaHealth));
    }
    
    final int value;
    final Animation frames;
    
    int lifetime = 0;
    
    double rotation = 0;
    
    double dtheta;
    
    public HealthBubble(float deltaHealth) {
        this.value = Math.round(deltaHealth);
        frames = deltaHealth >= 0 ? positive : negative;
        
        dtheta = 2 * (Math.random() - 0.5);
    }
    
    @Override
    public void draw(Canvas c) {
        int dvalue = value;
        if(dvalue < 0) { dvalue = -dvalue; }
        int xpos = 0;
        
        while(dvalue > 0) {
            frames.setFrame(dvalue % 10);
            c.draw(frames, parent.position.x + xpos, parent.position.y, rotation, 1f / (lifetime + 1));
            dvalue /= 10;
            xpos -= 4;
        }
        frames.setFrame(10);
        c.draw(frames, parent.position.x + xpos, parent.position.y, rotation, 1f / (lifetime + 1));
    }
    
    @Override
    public void tick() {
        ++lifetime;
        --parent.position.y;
        
        rotation += dtheta;
        
        if(lifetime > 20) {
            parent.destroy();
        }
    }
}
