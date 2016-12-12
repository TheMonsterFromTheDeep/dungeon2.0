package dungeon20;

import alfredo.Canvas;
import alfredo.Component;
import alfredo.Entity;
import alfredo.Game;
import alfredo.Scene;
import alfredo.Sound;
import alfredo.geom.Vector;
import alfredo.gfx.Animation;
import alfredo.gfx.Graphic;
import alfredo.gfx.ImageGraphic;
import alfredo.gfx.Renderer;
import alfredo.inpt.Keys;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class Player extends Component {
    Animation normal;
    Animation crown;
    Animation current;
    
    double fireDirection = 90;
    long lastFire = -100;
    int bonus = 0;
    
    int[] items;
    float[] charges;
    
    Sound spell, bow, lightning;
    
    final Attack[] attacks;
    
    int currentAttack = 0;
    
    int invincibleTick;
    
    public int totalExp = 35;
    
    boolean dead = false;
    
    public static int lifetime;
    
    public Player() {
        //up = Animation.load("/resrc/img/character/up.png", 3);
        //down = Animation.load("/resrc/img/character/down.png", 3);
        //left = Animation.load("/resrc/img/character/left.png", 2);
        //right = Animation.load("/resrc/img/character/right.png", 2);
        normal = Animation.load("/resrc/img/character.png", 10);
        crown = Animation.load("/resrc/img/character_crown.png", 10);
        
        spell = Sound.load("/resrc/snd/magic.wav");
        bow = Sound.load("/resrc/snd/bow.wav");
        lightning = Sound.load("/resrc/snd/lightning.wav");
        
        items = new int[Drop.DROP_COUNT];
        //up.setStep(0.01f);
        //down.setStep(0.01f);
        //left.setStep(0.01f);
        //right.setStep(0.01f);
        
        current = normal;
        current.setRange(0, 0);
        
        attacks = new Attack[8];
        
        lifetime = 0;
        
        /*attacks[0] = new Attack(0.7f, 50, 10, Bullet.TYPE_BOLT);
        attacks[1] = new Attack(0.2f, 50, 5, Bullet.TYPE_NOVA);
        attacks[2] = new Attack(0.5f, 5, 10, Bullet.TYPE_LIGHTNING);
        attacks[3] = new Attack(0.2f, 50, 5, Bullet.TYPE_NOVA);
        attacks[4] = new Attack(3f, 300, 30, Bullet.TYPE_BASIC);
        attacks[5] = new Attack(5f, 20f, 2, Bullet.TYPE_SLIME);
        attacks[6] = new Attack(8f, 300, 30, Bullet.TYPE_BONE);
        attacks[7] = new Attack(0.2f, 50, 5, Bullet.TYPE_NOVA);*/
        
        attacks[0] = new Attack(0.7f, 0.6f, Bullet.TYPE_BOLT);
        attacks[1] = new Attack(0.2f, 0.4f, Bullet.TYPE_ICE);
        attacks[2] = new Attack(0.5f, 0.3f, Bullet.TYPE_LIGHTNING);
        attacks[3] = new Attack(0.2f, 0.4f, Bullet.TYPE_NOVA);
        attacks[4] = new Attack(3f, 2f, Bullet.TYPE_BASIC);
        attacks[5] = new Attack(2f, 0.8f, Bullet.TYPE_SLIME);
        attacks[6] = new Attack(2f, 0.6f, Bullet.TYPE_BONE);
        attacks[7] = new Attack(2f, 1.2f, Bullet.TYPE_FIRE);
        
        attacks[0].unlocked = true;
        attacks[4].unlocked = true;
        
        /*for(int i = 0; i < attacks.length; ++i) {
            attacks[i].unlocked = true;
        }*/
        
        charges = new float[4];
        for(int i = 0; i < charges.length; ++i) {
            charges[i] = 1;
        }
        
        Keys.N.onDown(() -> {
            do {
                --currentAttack;
                if(currentAttack < 0) { currentAttack = 7; }
            } while(!attacks[currentAttack].unlocked);
        });
        
        Keys.M.onDown(() -> {
            do {
                currentAttack = (currentAttack + 1) % 8;
            } while(!attacks[currentAttack].unlocked);
        });
    }
    
    @Override
    public void tick() {        
        ++lifetime;
        
        Renderer r = parent.getComponent(Renderer.class);
        
        current = items[Drop.TYPE_CROWN] > 0 ? crown : normal;
        
        if(Keys.A.isPressed()) {
            //current = right;
            current.setRange(8, 9);
            parent.position.x -= 2;
            fireDirection = 180;
        }
        else if(Keys.D.isPressed()) {
            //current = left;
            current.setRange(6, 7);
            parent.position.x += 2;
            fireDirection = 0;
        }
        else if(Keys.W.isPressed()) {
            //current = up;
            current.setRange(4, 5);
            parent.position.y -= 2;
            fireDirection = 270;
        }
        else if(Keys.S.isPressed()) {
            //current = down;
            current.setRange(1, 2);
            parent.position.y += 2;
            fireDirection = 90;
        }
        else {
            if(current.getStart() == 1 || current.getStart() == 4) {
                current.setStart(current.getStart() - 1);
            }
            current.setLength(0);
        }
        current.tick();
        
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
        
        if(Keys.ROW_1.isPressed() && attacks[0].unlocked) {
            currentAttack = 0;
        }
        if(Keys.ROW_2.isPressed() && attacks[1].unlocked) {
            currentAttack = 1;
        }
        if(Keys.ROW_3.isPressed() && attacks[2].unlocked) {
            currentAttack = 2;
        }
        if(Keys.ROW_4.isPressed() && attacks[3].unlocked) {
            currentAttack = 3;
        }
        if(Keys.ROW_5.isPressed() && attacks[4].unlocked) {
            currentAttack = 4;
        }
        if(Keys.ROW_6.isPressed() && attacks[5].unlocked) {
            currentAttack = 5;
        }
        if(Keys.ROW_7.isPressed() && attacks[6].unlocked) {
            currentAttack = 6;
        }
        if(Keys.ROW_8.isPressed() && attacks[6].unlocked) {
            currentAttack = 7;
        }
        
        if(charges[0] < 1) {
            charges[0] += 0.6f;
        }
        if(charges[0] > 1) { charges[0] = 1; }
        
        if(charges[1] < 1) {
            charges[1] += 0.02f;
        }
        if(charges[1] > 1) { charges[1] = 1; }
        
        if(charges[2] < 1) {
            charges[2] += 0.05f;
        }
        if(charges[2] > 1) { charges[2] = 1; }
        
        if(charges[3] < 1) {
            charges[3] += 0.05f;
        }
        if(charges[3] > 1) { charges[3] = 1; }
        
        if(Keys.SPACE.isPressed()) {
            if(Game.getTick() - lastFire > 6) {
                int finalAttack = currentAttack;
                if(currentAttack == 4) {
                    if(items[Drop.TYPE_ARROW] < 1) {
                        finalAttack = 0;
                    }
                    else {
                        --items[Drop.TYPE_ARROW];
                    }
                }
                 if(currentAttack == 5) {
                    if(items[Drop.TYPE_SLIME_ARROW] < 1) {
                        finalAttack = 0;
                    }
                    else {
                        --items[Drop.TYPE_SLIME_ARROW];
                    }
                }
                if(currentAttack == 6) {
                    if(items[Drop.TYPE_BONE_ARROW] < 1) {
                        finalAttack = 0;
                    }
                    else {
                        --items[Drop.TYPE_BONE_ARROW];
                    }
                }
                if(currentAttack == 7) {
                    if(items[Drop.TYPE_FIRE_ARROW] < 1) {
                        finalAttack = 0;
                    }
                    else {
                        --items[Drop.TYPE_FIRE_ARROW];
                    }
                }
                if(currentAttack == 2) {
                    if(charges[2] < 1) {
                        finalAttack = 0;
                    }
                    else {
                        charges[2] = 0;
                    }
                }
                if(currentAttack == 3) {
                    if(charges[3] < 1) {
                        finalAttack = 0;
                    }
                    else {
                        charges[3] = 0;
                    }
                }
                if(currentAttack == 1) {
                    if(charges[1] < 1) {
                        finalAttack = 0;
                    }
                    else {
                        charges[1] = 0;
                    }
                }
                if(finalAttack == 0) {
                    charges[0] = 0;
                }
                
                Entity newBullet = attacks[finalAttack].spawn(parent.position, fireDirection);
                if(finalAttack < 4) {
                    (finalAttack == 2 ? lightning : spell).play();
                }
                else {
                    bow.play();
                }
                //Entity newBullet = Bullet.newBullet(parent.position, fireDirection, items[Drop.TYPE_ARROW] > 0 ? 3f : 0.7f, items[Drop.TYPE_ARROW] > 0 ? Bullet.TYPE_BASIC : Bullet.TYPE_BOLT);
                //if(items[Drop.TYPE_ARROW] > 0) { --items[Drop.TYPE_ARROW]; }
                
                if(newBullet != null) { newBullet.move(9); }
                lastFire = Game.getTick();
                bonus = 4;
            }
        }
        else {
            lastFire -= bonus;
            bonus = 0;
        }
        
        if(invincibleTick > 0) {
            parent.getComponent(Renderer.class).active = invincibleTick % 2 != 0;
        }
        
        r.graphic = current;
        
        if(items[Drop.TYPE_SLIME] + items[Drop.TYPE_BONE] + items[Drop.TYPE_SNAIL] >= totalExp) {
            items[Drop.TYPE_SLIME] = 0;
            items[Drop.TYPE_BONE] = 0;
            items[Drop.TYPE_SNAIL] = 0;
            totalExp += totalExp / 3;
            Scene.get(Main.class).levelUp = true;
        }
    }
    
    @Override
    public void destroy() {    
        if(!dead) {
            dead = true;
            Healthbar h;

            for(Entity e : Entity.getAllEntities()) {
                if((h = e.getComponent(Healthbar.class)) != null && e != parent) {
                    if(h.current >= 0) {
                        h.changeBy(-h.current); //Kill all remaning things
                    }
                }
            }

            Main main = Scene.get(Main.class);
            
            Waves waves = main.waves;
            waves.current = waves.waves.length;
            
            main.reset = 100;

            TextBubble.newBubble(new Vector(120, 90), TextBubble.TEXT_LOSE, true);
        }
    }
}
