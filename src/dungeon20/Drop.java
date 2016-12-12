/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeon20;

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
public class Drop extends Component {
    public static int TYPE_SLIME = 0;
    public static int TYPE_ARROW = 1;
    public static int TYPE_BONE = 2;
    public static int TYPE_CROWN = 3;
    public static int TYPE_SLIME_ARROW = 4;
    public static int TYPE_BONE_ARROW = 5;
    public static int TYPE_SNAIL = 6;
    public static int TYPE_FIRE_ARROW = 7;
    
    public static int DROP_COUNT = 8;
    
    static Graphic[] drops = null;
    static Renderer[] renderers = null;
    
    static void init() {
        if(drops != null) { return; }
        
        drops = new Graphic[DROP_COUNT];
        drops[0] = ImageGraphic.load("/resrc/img/drops/slime.png");
        drops[1] = ImageGraphic.load("/resrc/img/drops/arrow.png");
        drops[2] = ImageGraphic.load("/resrc/img/drops/bone.png");
        drops[3] = ImageGraphic.load("/resrc/img/drops/crown.png");
        drops[4] = ImageGraphic.load("/resrc/img/drops/slime_arrow.png");
        drops[5] = ImageGraphic.load("/resrc/img/drops/bone_arrow.png");
        drops[6] = ImageGraphic.load("/resrc/img/drops/snail.png");
        drops[7] = ImageGraphic.load("/resrc/img/drops/fire_arrow.png");
        
        renderers = new Renderer[drops.length];
        for(int i = 0; i < drops.length; ++i) {
            renderers[i] = new Renderer(drops[i]);
        }
    }
    
    public static Entity newDrop(float x, float y, int type) {
        init();
        return Entity.insert(x, y).chain(renderers[type]).chain(new Drop(type));
    }
    
    int type;
    
    public Drop(int type) {
        this.type = type;
    }
    
    
    
    @Override
    public void tick() {
        Entity player = Entity.getWithComponent(Player.class);
        if(player == null) { return; }
        Vector pos = new Vector(player.position);
        pos.subtract(parent.position);
        if(pos.getMagnitude() < 10) {
            ++player.getComponent(Player.class).items[type];
            parent.destroy();
        }
    }
}
