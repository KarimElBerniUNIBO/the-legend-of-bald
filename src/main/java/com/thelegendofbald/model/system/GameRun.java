package com.thelegendofbald.model.system;

import com.thelegendofbald.model.system.Timer.TimeData;

/**
 * Represents a game run with a name and associated time data.
 * 
 * @param name The name of the game run.
 * @param timedata The time data associated with the game run.
 */
public record GameRun(String name, TimeData timedata) {
}
