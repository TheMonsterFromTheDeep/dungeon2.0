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
public class Enemy extends Component {
    public final int level;
    
    public Enemy(int level) {
        this.level = level;
    }
}
