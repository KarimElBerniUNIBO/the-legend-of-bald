package com.thelegendofbald.model.config;

import com.thelegendofbald.view.panel.settings.SettingsEditor;

/**
 * Interface for managing the settings editor panels within the application.
 * Implementations of this interface are responsible for handling the logic
 * to switch or update the current {@link SettingsEditor} panel displayed to the user.
 */
public interface SettingsEditorsManager {

    /**
     * Switches the currently displayed settings editor panel to the specified {@link SettingsEditor}.
     *
     * @param settingsEditor the new settings editor panel to display
     */
    void changeSettingsEditorPanel(SettingsEditor settingsEditor);

}
