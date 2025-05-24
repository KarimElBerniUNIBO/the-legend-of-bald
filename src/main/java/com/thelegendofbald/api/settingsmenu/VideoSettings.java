package com.thelegendofbald.api.settingsmenu;

import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSlider;

import com.thelegendofbald.view.common.CustomCheckBox;
import com.thelegendofbald.view.common.CustomJSlider;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * The {@code VideoSettings} enum defines the available video-related settings
 * for the application, each associated with a display text and a corresponding
 * Swing {@link JComponent} for user interaction.
 * <p>
 * The settings include:
 * <ul>
 *   <li>{@link #FULLSCREEN} - A checkbox to toggle fullscreen mode.</li>
 *   <li>{@link #RESOLUTION} - A combo box to select the screen resolution.</li>
 *   <li>{@link #FPS} - A slider to adjust the frame rate per second.</li>
 * </ul>
 * Each enum constant implements the {@link SettingOption} interface, providing
 * methods to retrieve the display text and the UI component.
 */
public enum VideoSettings implements SettingOption {
    /**
     * A checkbox to toggle fullscreen mode.
     */
    FULLSCREEN("FULLSCREEN", new CustomCheckBox()),
    /**
     * A combo box to select the screen resolution.
     */
    RESOLUTION("RESOLUTION", new JComboBox<>(List.of(
            "900x600",
            "Altro").toArray())),
    /**
     * A slider to adjust the frame rate per second.
     */
    FPS("FRAMERATE PER SECOND", new CustomJSlider(JSlider.HORIZONTAL, 0, 144, 60));

    private final String text;
    private final JComponent jcomponent;

    VideoSettings(final String text, final JComponent jcomponent) {
        this.text = text;
        this.jcomponent = jcomponent;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "JComponent must be mutable for UI interaction; safe in enum context."
    )
    @Override
    public JComponent getJcomponent() {
        return this.jcomponent;
    }

}
