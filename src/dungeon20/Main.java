package dungeon20;

import alfredo.Camera;
import alfredo.Canvas;
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
import alfredo.phx.Physics;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class Main extends Scene {

    Entity player;
    
    final Graphic bg;
    final Graphic lava;
    final Graphic topBlocks;
    float lavaPosition = 240;
    
    public static int TAG_ENEMY = 1;
    public static int TAG_SLIMED = 2;
    
    Graphic selected;
    Animation ammo;
    
    Graphic upgrade;
    Graphic lock;
    
    Waves waves;
    
    int waveDelay = 60;
    
    boolean levelUp = false;
    boolean isMenu = true;
    
    Sound music;
    
    Sound playerHurt;
    
    Graphic menu;
    int reset = 0;
    
    Animation help;
    Animation help2;
    
    
    static final int helpLength = 150;
    int helpCountdown = helpLength;
    
    public Main() {
        bg = ImageGraphic.load("/resrc/img/bg.png", Graphic.Pivot.TopLeft);
        lava = ImageGraphic.load("/resrc/img/lava.png", Graphic.Pivot.BottomRight);
        --lava.getPivot().y;
        topBlocks = ImageGraphic.load("/resrc/img/topblocks.png", Graphic.Pivot.TopLeft);
        
        selected = ImageGraphic.load("/resrc/img/selected.png", Graphic.Pivot.TopLeft);
        upgrade = ImageGraphic.load("/resrc/img/upgrade.png", Graphic.Pivot.TopLeft);
        lock = ImageGraphic.load("/resrc/img/locked.png", Graphic.Pivot.TopLeft);
        menu = ImageGraphic.load("/resrc/img/menu.png", Graphic.Pivot.TopLeft);
        
        ammo = Animation.load("/resrc/img/text/numbers.png", 10);
        
        help = Animation.load("/resrc/img/help.png", 16, new Vector(-60, -(75 / 2f)));
        help2 = help.getInstance();
        help2.setFrame(1);

        music = Sound.load("/resrc/snd/music.wav");
        music.loop();
        
        playerHurt = Sound.load("/resrc/snd/hurt.wav");
        
        waves = new Waves();
        
        this.bgcolor = 0x000b3f;
    }
    
    @Override
    public void backdrop(Canvas c) {
        c.draw(bg, 0, 0);
    }
    
    private void drawAmmo(int value, int position, Canvas c) {
        position += 9;
        if(value > 999) { value = 999; }
        do {
            ammo.setFrame(value % 10);
            c.draw(ammo, position - 0.5f, 11.5f);
            position -= 4;
            value /= 10;
        } while(value > 0);
    }
    
    private void drawCharge(float value, int position, Canvas c) {
        c.fill(0x29fff7, position - 1, 10, value * 10, 3);
    }
    
    @Override
    public void loop() {
        if(!levelUp && !isMenu) {
            super.loop();
        }
        else if(isMenu) {
            if(Keys.SPACE.isPressed()) {
                isMenu = false;
                player = Entity.create(120, 90).chain(new Renderer()).chain(new Player()).chain(new Healthbar(10)).chain(new Healer()).chain(new EnemySound(playerHurt, playerHurt));
            }
            --helpCountdown;
            if(helpCountdown < -20) {
                help.next();
                help2.next();
                
                helpCountdown = helpLength;
            }
        }
        else {
            Player p = player.getComponent(Player.class);
            
            if(Keys.ROW_1.isPressed() && p.attacks[0].unlocked) {
                p.attacks[0].levelUp();
                if(!p.attacks[1].unlocked) {
                    p.attacks[1].unlocked = true;
                    TextBubble.announceUnlock();
                }
                levelUp = false;
            }
            if(Keys.ROW_2.isPressed() && p.attacks[1].unlocked) {
                p.attacks[1].levelUp();
                if(!p.attacks[2].unlocked) {
                    p.attacks[2].unlocked = true;
                    TextBubble.announceUnlock();
                }
                levelUp = false;
            }
            if(Keys.ROW_3.isPressed() && p.attacks[2].unlocked) {
                p.attacks[2].levelUp();
                if(!p.attacks[3].unlocked) {
                    p.attacks[3].unlocked = true;
                    TextBubble.announceUnlock();
                }
                levelUp = false;
            }
            if(Keys.ROW_4.isPressed() && p.attacks[3].unlocked) {
                p.attacks[3].levelUp();
                levelUp = false;
            }
            if(Keys.ROW_5.isPressed() && p.attacks[4].unlocked) {
                p.attacks[4].levelUp();
                if(!p.attacks[5].unlocked) {
                    p.attacks[5].unlocked = true;
                    TextBubble.announceUnlock();
                }
                levelUp = false;
            }
            if(Keys.ROW_6.isPressed() && p.attacks[5].unlocked) {
                p.attacks[5].levelUp();
                if(!p.attacks[6].unlocked) {
                    p.attacks[6].unlocked = true;
                    TextBubble.announceUnlock();
                }
                levelUp = false;
            }
            if(Keys.ROW_7.isPressed() && p.attacks[6].unlocked) {
                p.attacks[6].levelUp();
                if(!p.attacks[7].unlocked) {
                    p.attacks[7].unlocked = true;
                    TextBubble.announceUnlock();
                }
                levelUp = false;
            }
            if(Keys.ROW_8.isPressed() && p.attacks[7].unlocked) {
                p.attacks[7].levelUp();
                levelUp = false;
            }
        }
    }
    
    @Override
    public void draw(Canvas c) {
        c.draw(topBlocks, 0, 0);
        c.draw(lava, lavaPosition, 180);
        
        if(player != null) {
            Player p = player.getComponent(Player.class);

            int selectPosition = 89;
            switch(p.currentAttack) {
                case 0:
                    selectPosition = 91;
                    break;
                case 1:
                    selectPosition = 105;
                    break;
                case 2:
                    selectPosition = 119;
                    break;
                case 3:
                    selectPosition = 133;
                    break;
                case 4:
                    selectPosition = 147;
                    break;
                case 5:
                    selectPosition = 161;
                    break;
                case 6:
                    selectPosition = 175;
                    break;
                case 7:
                    selectPosition = 189;
                    break;
            }
            c.draw(selected, selectPosition, 1);

            drawCharge(p.charges[0], 91, c);
            drawCharge(p.charges[1], 105, c);
            drawCharge(p.charges[2], 119, c);
            drawCharge(p.charges[3], 133, c);

            drawAmmo(p.items[Drop.TYPE_ARROW], 147, c);
            drawAmmo(p.items[Drop.TYPE_SLIME_ARROW], 161, c);
            drawAmmo(p.items[Drop.TYPE_BONE_ARROW], 175, c);
            drawAmmo(p.items[Drop.TYPE_FIRE_ARROW], 189, c);

            float total = 204;
            float add = (float)(p.items[Drop.TYPE_SLIME] * 34) / p.totalExp;
            c.fill(0x498947, total, 2, add, 7);
            total += add;
            add = (float)(p.items[Drop.TYPE_BONE] * 34) / p.totalExp;
            c.fill(0xfaffd3, total, 2, add, 7);
            total += add;
            add = (float)(p.items[Drop.TYPE_SNAIL] * 34) / p.totalExp;
            c.fill(0xa20f00, total, 2, add, 7);


            if(levelUp) {
                c.fillRaw(0xbb4b4b4b, 0, 0, 240, 180);
                c.draw(upgrade, 0, 0);
            }

            if(!p.attacks[0].unlocked) { c.draw(lock, 91, 1); }
            if(!p.attacks[1].unlocked) { c.draw(lock, 105, 1); }
            if(!p.attacks[2].unlocked) { c.draw(lock, 119, 1); }
            if(!p.attacks[3].unlocked) { c.draw(lock, 133, 1); }
            if(!p.attacks[4].unlocked) { c.draw(lock, 147, 1); }
            if(!p.attacks[5].unlocked) { c.draw(lock, 161, 1); }
            if(!p.attacks[6].unlocked) { c.draw(lock, 175, 1); }
            if(!p.attacks[7].unlocked) { c.draw(lock, 189, 1); }
        }
        
        if(isMenu) {
            c.draw(menu, 0, 0);
            
            c.draw(help, 115, 97);
            
            if(helpCountdown <= 0) {
                c.draw(help2, 115, 97, 0, helpCountdown / -20f);
            }
        }
        
        if(reset > 0) {
            c.draw(menu, 0, 0, 0, (100 - reset) / 100f);
        }
    }
    
    @Override
    public void tick() {
        lavaPosition = (lavaPosition + 0.3f) % 240 + 240;
        
        --reset;
        if(reset == 0) {
            isMenu = true;
            help.setFrame(0);
            help2.setFrame(1);
            helpCountdown = helpLength;
            Entity.clear();
            waves.current = 0;
            waveDelay = 60;
        }
        
        if(waveDelay > 0) {
            --waveDelay;
        }
        else if(waveDelay == 0) {
            waves.begin();
            --waveDelay;
        }
        else { 
            if(waves.tick()) {
                TextBubble.newBubble(new Vector(30, 40), TextBubble.TEXT_WAVE_END, true);
                ++waves.current;
                waveDelay = 100;
            }
        }
    }
    
    public static void main(String[] args) {
        Scene.open(Main.class);
        
        Camera.setMain(new Camera.Fit(240, 180));
        
        Physics.gravity.set(0, 0);
        
        Game.setTitle("Dungeon 2.0");
        Game.setIcon(ImageGraphic.load("/resrc/img/icon.png"));
        Game.setSize(240 * 3, 180 * 3);
        Game.play();
    }
    
}
