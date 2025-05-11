package com.thelegendofbald.ui.settingsmenu.api;

import javax.swing.JComponent;

/**
 * Represents a configurable option in the settings menu UI.
 * Implementations of this interface provide the display text and the associated
 * Swing component for the option.
 *
 * <p>
 * Methods:
 * <ul>
 *   <li>{@link #getText()} - Returns the display text for the setting option.</li>
 *   <li>{@link #getJcomponent()} - Returns the Swing {@code JComponent} associated with this option.</li>
 *   <li>{@link #getSize()} - Returns the maximum number of setting options, as defined by {@code Settings.getMaxIndex()}.</li>
 * </ul>
 * </p>
 */
public interface SettingOption {

    /**
     * Returns the display text associated with this setting option.
     *
     * @return the text representation of the setting option
     */
    String getText();

    /**
     * Returns the Swing {@link JComponent} associated with this setting option.
     * This component can be used to display or interact with the setting in a user interface.
     *
     * @return the {@code JComponent} representing this setting option
     */
    JComponent getJcomponent();

    /**
     * Returns the total number of available setting options.
     *
     * @return the number of setting options as an integer
     */
    static int getSize() {
        return (int) Settings.getMaxIndex();
    }

}
