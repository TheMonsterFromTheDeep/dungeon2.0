/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dungeon20;

/**
 *
 * @author TheMonsterOfTheDeep
 */
public class Waves {
    public Wave[] waves;
    public int current = 0;
    
    int baseCount = 250;
    
    public Waves() {
        waves = new Wave[] {
            new Wave() {
                @Override
                public void init() {
                    slimeRate = 200;
                    skelRate = 300;
                    snailRate = 0;
                    
                    snailProb = 0;
                }

                @Override
                public void update() {
                    if(tick == 300) {
                        slimeRate = 100;
                        skelRate = 250;
                    }
                    
                    if(tick == 600) {
                        slimeRate = 50;
                    }
                    
                    if(tick == 1000) {
                        slimeRate = 150;
                        skelRate = 300;
                        Slime.newSlime(true); //Spawn a boss slime
                    }
                    
                    if(tick == 1600) {
                        slimeRate = 300;
                        skelRate = 400;
                    }
                    
                    if(tick == 1650) {
                        over = true;
                    }
                }
            },
            
            new Wave() {
                @Override
                public void init() {
                    slimeRate = 200;
                    skelRate = 200;
                    snailRate = 0;
                    
                    slimeProb = 0.01f;
                }

                @Override
                public void update() {
                    if(tick == 300) {
                        slimeRate = 100;
                        skelRate = 150;
                    }
                    
                    if(tick == 500) {
                        snailRate = 200;
                        skelRate = 200;
                    }
                    
                    if(tick == 1000) {
                        snailRate = 0;
                        slimeRate = 400;
                        skelRate = 100;
                        slimeProb = 0;
                        Skeleton.newSkeleton(true);
                    }
                    
                    if(tick == 1600) {
                        slimeRate = 300;
                        skelRate = 400;
                    }
                    
                    if(tick == 1650) {
                        over = true;
                    }
                }
            },
            
            new Wave() {
                @Override
                public void init() {
                    slimeRate = 100;
                    skelRate = 150;
                    snailRate = 200;
                }

                @Override
                public void update() {
                    if(tick == 300) {
                        slimeRate = 75;
                    }
                    
                    if(tick == 500) {
                        snailRate = 100;
                        skelRate = 400;
                        slimeRate = 400;
                    }
                    
                    if(tick == 1000) {
                        snailRate = 300;
                        Firesnail.newFiresnail(true);
                    }
                    
                    if(tick == 1600) {
                        slimeRate = 200;
                        skelRate = 200;
                    }
                    
                    if(tick == 2200) {
                        over = true;
                    }
                }
            },
            
            new Wave() {
                @Override
                public void init() {
                    slimeRate = (int) (baseCount - (Math.random() * 50));
                    skelRate = (int) (baseCount - (Math.random() * 50));
                    snailRate = (int) (baseCount - (Math.random() * 50));
                    
                    if(baseCount > 150) {
                        baseCount -= 20;
                    }
                    
                    slimeProb = 0.01f;
                    skelProb = 0.01f;
                    snailProb = 0.01f;
                }

                @Override
                public void update() {
                    if(tick == 300) {
                        slimeRate -= Math.random() * baseCount / 50;
                        skelRate -= Math.random() * baseCount / 50;
                        snailRate -= Math.random() * baseCount / 50;
                    }
                    
                    if(tick == 1000) {
                        switch((int)(Math.random() * 3)) {
                            case 0:
                                slimeRate = 50;
                                break;
                            case 1:
                                skelRate = 50;
                                break;
                            case 2:
                            default:
                                snailRate = 50;
                                break;
                        }
                    }
                    
                    if(tick == 1400) {
                        slimeRate = (int) (baseCount - (Math.random() * 50));
                        skelRate = (int) (baseCount - (Math.random() * 50));
                        snailRate = (int) (baseCount - (Math.random() * 50));
                        for(int i = 0; i < 3 * Math.random(); ++i) {
                            switch((int)(Math.random() * 3)) {
                                case 0:
                                    Slime.newSlime(true);
                                    break;
                                case 1:
                                    Skeleton.newSkeleton(true);
                                    break;
                                case 2:
                                default:
                                    Firesnail.newFiresnail(true);
                                    break;
                            }
                        }
                    }
                    
                    if(tick == 1700) {
                        over = true;
                    }
                }
            },
        };
    }
    
    public void begin() {
        if(current >= waves.length) { current = waves.length - 1; }
        
        waves[current].begin();
    }
    
    public boolean tick() {
        if(current >= waves.length) { return false; }
        
        waves[current].tick();
        return waves[current].over;
    }
}
