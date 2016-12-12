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
public class Drops extends Component {
    public static class Pair {
        int type;
        float probability;
        int min;
        int max;
        
        public Pair(int type, float min, float max, float probability) {
            this.type = type;
            this.min = (int)min;
            this.max = (int)max;
            this.probability = probability;
        }
    }
    
    final Pair[] pairs;
    
    private int next;
    
    public Drops(int count) {
        this.pairs = new Pair[count];
    }
    
    public Drops add(Pair p) {
        if(next < pairs.length) {
            pairs[next++] = p;
        }
        else {
            System.err.println("Warning: attempting to add a drop to an already full drop collection!");
        }
        return this;
    }
    
    @Override
    public void destroy() {
        for(Pair p : pairs) {
            double chance = Math.random();
            if(chance <= p.probability) {
                int dropCount = p.min + (int)(Math.random() * (p.max - p.min));
                for (int i = 0; i < dropCount; ++i) {
                    Drop.newDrop(parent.position.x + (float)(Math.random() * 10 - 5), parent.position.y + (float)(Math.random() * 10 - 5), p.type);
                }
            }
        }
    }
}
