package alfredo.inpt;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class Keys {    
    private static final Key[] keys = new Key[256];
    
    public static final KeyListener listener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
            
        }

        @Override
        public void keyPressed(KeyEvent e) {
            for(Key k : keys) {
                if(k.id == e.getKeyCode()) {
                    if(!k.pressed) {
                        k.performDown();
                    }
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            for(Key k : keys) {
                if(k.id == e.getKeyCode()) {
                    if(k.pressed) {
                        k.performUp();
                    }
                }
            }
        }
    };
    
    static {
        for(int i = 0; i < 256; ++i) {
            keys[i] = new Key(i);
        }
    }
    
    public static final Key SHIFT = keys[KeyEvent.VK_SHIFT];
    public static final Key A = keys[KeyEvent.VK_A];
    public static final Key B = keys[KeyEvent.VK_B];
    public static final Key C = keys[KeyEvent.VK_C];
    public static final Key D = keys[KeyEvent.VK_D];
    public static final Key E = keys[KeyEvent.VK_E];
    public static final Key F = keys[KeyEvent.VK_F];
    public static final Key G = keys[KeyEvent.VK_G];
    public static final Key H = keys[KeyEvent.VK_H];
    public static final Key I = keys[KeyEvent.VK_I];
    public static final Key J = keys[KeyEvent.VK_J];
    public static final Key K = keys[KeyEvent.VK_K];
    public static final Key L = keys[KeyEvent.VK_L];
    public static final Key M = keys[KeyEvent.VK_M];
    public static final Key N = keys[KeyEvent.VK_N];
    public static final Key O = keys[KeyEvent.VK_O];
    public static final Key P = keys[KeyEvent.VK_P];
    public static final Key Q = keys[KeyEvent.VK_Q];
    public static final Key R = keys[KeyEvent.VK_R];
    public static final Key S = keys[KeyEvent.VK_S];
    public static final Key T = keys[KeyEvent.VK_T];
    public static final Key U = keys[KeyEvent.VK_U];
    public static final Key V = keys[KeyEvent.VK_V];
    public static final Key W = keys[KeyEvent.VK_W];
    public static final Key X = keys[KeyEvent.VK_X];
    public static final Key Y = keys[KeyEvent.VK_Y];
    public static final Key Z = keys[KeyEvent.VK_Z];
    public static final Key SPACE = keys[KeyEvent.VK_SPACE];
    public static final Key ROW_0 = keys[KeyEvent.VK_0];
    public static final Key ROW_1 = keys[KeyEvent.VK_1];
    public static final Key ROW_2 = keys[KeyEvent.VK_2];
    public static final Key ROW_3 = keys[KeyEvent.VK_3];
    public static final Key ROW_4 = keys[KeyEvent.VK_4];
    public static final Key ROW_5 = keys[KeyEvent.VK_5];
    public static final Key ROW_6 = keys[KeyEvent.VK_6];
    public static final Key ROW_7 = keys[KeyEvent.VK_7];
    public static final Key ROW_8 = keys[KeyEvent.VK_8];
    public static final Key ROW_9 = keys[KeyEvent.VK_9];
    public static final Key PAD_0 = keys[KeyEvent.VK_NUMPAD0];   
    public static final Key PAD_1 = keys[KeyEvent.VK_NUMPAD1];
    public static final Key PAD_2 = keys[KeyEvent.VK_NUMPAD2];
    public static final Key PAD_3 = keys[KeyEvent.VK_NUMPAD3];
    public static final Key PAD_4 = keys[KeyEvent.VK_NUMPAD4];
    public static final Key PAD_5 = keys[KeyEvent.VK_NUMPAD5];
    public static final Key PAD_6 = keys[KeyEvent.VK_NUMPAD6];
    public static final Key PAD_7 = keys[KeyEvent.VK_NUMPAD7];
    public static final Key PAD_8 = keys[KeyEvent.VK_NUMPAD8];
    public static final Key PAD_9 = keys[KeyEvent.VK_NUMPAD9];
}
