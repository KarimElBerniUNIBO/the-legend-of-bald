package com.thelegendofbald.api.settingsmenu;

import javax.swing.JComponent;

/**
 * The {@code SettingOption} interface defines the contract for setting options
 * in the application settings menu.
 * Each setting option must provide a display text, a value, and a Swing component
 * for user interaction.
 */
public interface SettingOption {

    /**
     * Returns the display text associated with this setting option.
     *
     * @return the text representation of the setting option
     */
    String getText();

    /**
     * Returns the value of the setting option.
     * The type of the value may vary depending on the specific setting.
     * 
     * @return the value of the setting option, which can be of any type
     */
    Object getValue();

    /**
     * Gets the Swing component associated with this setting option.
     * <b>Note:</b> The component should not be modified externally as it is intended for UI purposes only.
     * 
     * @return the JComponent that represents the setting option
     */
    JComponent getJComponent();

    /**
     * Returns the total number of available setting options.
     *
     * @return the number of setting options as an integer
     */
    static int getSettingsCount() {
        return Settings.getMaxIndex();
    }

}
