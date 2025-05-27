package com.thelegendofbald.api.settingsmenu;

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
 *   <li>{@link #FPS} - A slider to adjust the frame rate per second.</li>
 * </ul>
 * Each enum constant implements the {@link SettingOption} interface, providing
 * methods to retrieve the display text and the UI component.
 */
public enum VideoSettings implements SettingOption {
    /**
     * A checkbox to toggle fullscreen mode.
     */
    FULLSCREEN("FULLSCREEN", createFullscreenCheckBox()),
    /**
     * A slider to adjust the frame rate per second.
     */
    FPS("FRAMERATE PER SECOND", createFPSSlider());

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

    private static CustomCheckBox createFullscreenCheckBox() {
        CustomCheckBox checkBox = new CustomCheckBox();
        checkBox.addActionListener(e -> {
            boolean isSelected = checkBox.isSelected();
            System.out.println("Fullscreen mode: " + isSelected);
        });

        return checkBox;
    }

    private static CustomJSlider createFPSSlider() {
        var customSlider = new CustomJSlider(JSlider.HORIZONTAL, 30, 144, 60);
        var slider = customSlider.getSlider();

        slider.addChangeListener(e -> {
            if (!slider.getValueIsAdjusting()) {
                String value = String.valueOf(slider.getValue());
                var lastValue = customSlider.getLastValue();

                if (lastValue != slider.getValue()) {
                    System.out.println("Selected FPS: " + value);
                    customSlider.setLastValue(slider.getValue());
                }
            }
        });

        return customSlider;
    }

}
