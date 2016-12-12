package alfredo.inpt;

import java.util.ArrayList;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class Key {
    public static interface Action {
        void perform();
    }
    
    final int id;
    private final ArrayList<Action> downActions = new ArrayList();
    private final ArrayList<Action> upActions = new ArrayList();
    
    boolean pressed = false;
    
    public Key(int id) {
        this.id = id;
    }
    
    public boolean isPressed() { return pressed; }
    
    void performDown() {
        pressed = true;
        for(Action a : downActions) {
            a.perform();
        }
    }
    
    void performUp() {
        pressed = false;
        for(Action a : upActions) {
            a.perform();
        }
    }
    
    public void onDown(Action action) {
        downActions.add(action);
    }
    
    public void onUp(Action action) {
        upActions.add(action);
    }
}
