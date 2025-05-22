package com.thelegendofbald.characters;

import com.thelegendofbald.combat.Combatant;
import com.thelegendofbald.life.LifeComponent;

public abstract class Entity  implements Combatant {
    protected int x, y; // Posizione
    protected String name; // Nome dell'entità
    protected LifeComponent lifeComponent;

    public Entity(int x, int y,String name , LifeComponent lifeComponent) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.lifeComponent = lifeComponent;
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
<<<<<<< HEAD
    public int getHealth() { return health; }
    public void setHealth(int health) {
        this.health = health;
    }
=======
>>>>>>> 6c79dc4629d27007d900f192fa0ecdfbde35897c

    public LifeComponent getLifeComponent(){
        return this.lifeComponent;
    }

}
