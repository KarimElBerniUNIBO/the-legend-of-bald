package com.thelegendofbald.api.interactable;

/**
 * Interface for objects that the player can actively interact with.
 *
 * This interface defines a contract for any game object that requires
 * player input (e.g., pressing a key) to trigger an action, such as
 * opening a chest or talking to an NPC.
 */
public interface Interactable {
    /**
     * Executes the interaction between the player and the object.
     * The specific action performed depends on the implementing class.
     */
    void interact();
}
