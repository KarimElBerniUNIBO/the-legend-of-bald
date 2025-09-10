package com.thelegendofbald.api.common.animation;

/**
 * An interface for objects that support animation updates.
 * Classes implementing this interface must provide the logic
 * for updating their animation state.
 */
public interface Animatable {

    /**
     * Updates the animation state of the object.
     * This method should be called periodically to advance
     * the animation frames or states.
     */
    void updateAnimation();

}
