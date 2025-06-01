package com.thelegendofbald.api.settingsmenu;

import javax.swing.JComponent;

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
     * @throws IllegalStateException if the component type is unexpected
     */
    Object getValue() throws IllegalStateException;

    /**
     * Gets the Swing component associated with this setting option.
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
