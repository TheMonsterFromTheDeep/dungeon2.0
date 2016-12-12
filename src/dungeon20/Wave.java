/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeon20;

import alfredo.geom.Vector;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public abstract class Wave {
    int slimeRate;
    int skelRate;
    int snailRate;
    
    private int nextSlime;
    private int nextSkel;
    private int nextSnail;
    
    float slimeProb = 0;
    float skelProb = 0;
    float snailProb = 0;
    
    int tick;
    
    public boolean over = false;
    
    public Wave() {
        
    }
    
    public abstract void init();
    public abstract void update();
    
    public final void begin() {        
        init();
        tick = 0;
        
        over = false;
        
        nextSlime = slimeRate;
        nextSkel = skelRate;
        nextSnail = snailRate;
        
        TextBubble.newBubble(new Vector(30, 40), TextBubble.TEXT_WAVE, true);
    }
    
    public final void tick() {
        --nextSlime;
        --nextSkel;
        --nextSnail;
        
        if(nextSlime <= 0 && slimeRate != 0) {
            Slime.newSlime(Math.random() < slimeProb); //Boss spawning is up to the individual wave (sorta)
            nextSlime = (int) (slimeRate + Math.random() * 5);
        }
        
        if(nextSkel <= 0 && skelRate != 0) {
            Skeleton.newSkeleton(Math.random() < skelProb);
            nextSkel = (int) (skelRate + Math.random() * 5);
        }
        
        if(nextSnail <= 0 && snailRate != 0) {
            Firesnail.newFiresnail(Math.random() < snailProb);
            nextSnail = (int) (snailRate + Math.random() * 5);
        }
        
        update();
        
        ++tick;
    }
}
