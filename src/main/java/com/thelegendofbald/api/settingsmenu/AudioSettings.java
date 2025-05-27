package com.thelegendofbald.api.settingsmenu;

import javax.swing.JComponent;
import javax.swing.JSlider;

import com.thelegendofbald.view.common.CustomJSlider;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * The {@code AudioSettings} enum defines audio-related settings options for the
 * application,
 * such as master and music volume controls. Each enum constant represents a
 * specific audio
 * setting and is associated with a display text and a corresponding
 * {@link JComponent}
 * (specifically, a {@link JSlider}) for user interaction.
 *
 * <p>
 * Implements the {@link SettingOption} interface to provide standardized access
 * to
 * the setting's display text and UI component.
 *
 * <ul>
 * <li>{@link #MASTER} - Controls the master volume.</li>
 * <li>{@link #MUSIC} - Controls the music volume.</li>
 * </ul>
 */
public enum AudioSettings implements SettingOption {
    /**
     * Represents the master volume setting.
     */
    MASTER("MASTER VOLUME", createMasterSlider()),
    /**
     * Represents the music volume setting.
     */
    MUSIC("MUSIC VOLUME", createMusicSlider());

    private final String text;
    private final JComponent jcomponent;

    AudioSettings(final String text, final JComponent jcomponent) {
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

    private static CustomJSlider createMasterSlider() {
        var customSlider = new CustomJSlider(JSlider.HORIZONTAL, 0, 100, 50);
        var slider = customSlider.getSlider();

        slider.addChangeListener(e -> {
            if (!slider.getValueIsAdjusting()) {
                String value = String.valueOf(slider.getValue());
                var lastValue = customSlider.getLastValue();

                if (lastValue != slider.getValue()) {
                    System.out.println("Selected Master Volume: " + value);
                    customSlider.setLastValue(slider.getValue());
                }
            }
        });

        return customSlider;
    }

    private static CustomJSlider createMusicSlider() {
        var customSlider = new CustomJSlider(JSlider.HORIZONTAL, 0, 100, 50);
        var slider = customSlider.getSlider();

        slider.addChangeListener(e -> {
            if (!slider.getValueIsAdjusting()) {
                String value = String.valueOf(slider.getValue());
                var lastValue = customSlider.getLastValue();

                if (lastValue != slider.getValue()) {
                    System.out.println("Selected Music Volume: " + value);
                    customSlider.setLastValue(slider.getValue());
                }
            }
        });

        return customSlider;
    }

}
