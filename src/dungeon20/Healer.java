/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeon20;

import alfredo.Component;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class Healer extends Component {
    public int damageCooldown;
    
    @Override
    public void tick() {
        if(damageCooldown > 0) {
            --damageCooldown;
        }
        else {
            Healthbar h = parent.getComponent(Healthbar.class);
            if(h != null && h.current < h.capacity) {
                h.changeBy((h.capacity - h.current) / 5);
                if(h.capacity - h.current < 0.5) {
                    h.current = h.capacity; //This is mostly so it will stop showing +s
                }
            }
        }
    }
}
