package com.thelegendofbald.life;

public class LifeComponent {

    private int maxHealth;
    private int currentHealth;

    public LifeComponent(int maxHealth){
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public void damageTaken(int damage){
        this.currentHealth= Math.max(0, this.currentHealth - damage);
    }

    public void heal(int amount){
        if(getCurrentHealth() + amount > this.maxHealth){
            setCurrentHealth(this.maxHealth);
        } else {
            this.currentHealth = getCurrentHealth() + amount;   
        }
    }

    public boolean isDead(){
        return this.currentHealth <= 0 ;
    }

    public double getPercentage(){
        return (double) currentHealth / maxHealth;
    }

}
