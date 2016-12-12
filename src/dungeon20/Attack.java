/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeon20;

import alfredo.Entity;
import alfredo.geom.Vector;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class Attack {
    float damage;
    float variance;
    int bullet;
    
    final float baseDamage, /*max,*/ degree;
    
    int level = 0;
    
    public boolean unlocked = false;
    
    private void calculate() {
        variance = degree;
        damage = baseDamage + level * variance;
    }
    
    /*public Attack(float baseDamage, float max, float degree, int bullet) {
        this.baseDamage = this.damage = baseDamage;
        this.max = max;
        this.degree = degree;
        this.bullet = bullet;
        calculate();
    }*/
    
    public Attack(float baseDamage, float degree, int bullet) {
        this.baseDamage = this.damage = baseDamage;
        this.degree = degree;
        this.bullet = bullet;
        calculate();
    }
    
    public void levelUp() {
        ++level;
        calculate();
    }
    
    public Entity spawn(Vector position, double direction) {
        if(bullet == Bullet.TYPE_NOVA) {
            for(int i = 0; i < 36; ++i) {
                Vector offset = new Vector(position);
                offset.move(10, i * 10);
                Bullet.newBullet(offset, i * 10, damage + (float)(Math.random() * variance), bullet);
            }
            return null;
        }
        else {
            return Bullet.newBullet(position, direction, damage + (float)(Math.random() *  variance), bullet);
        }
    }
}
