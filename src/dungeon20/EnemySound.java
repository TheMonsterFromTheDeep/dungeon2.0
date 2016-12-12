/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeon20;

import alfredo.Component;
import alfredo.Sound;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class EnemySound extends Component {
    final Sound hurt;
    final Sound kill;
    
    public EnemySound(Sound hurt, Sound kill) {
        this.hurt = hurt;
        this.kill = kill;
    }
    
    public void hurt() {
        hurt.play();
    }
    
    @Override
    public void destroy() {
        kill.play();
    }
}
