package com.thelegendofbald.model.config;

import javax.swing.JComponent;
import javax.swing.JSlider;

import com.thelegendofbald.model.system.SoundManager;
import com.thelegendofbald.view.component.CustomCheckBox;
import com.thelegendofbald.view.component.CustomComboBox;
import com.thelegendofbald.view.component.CustomSlider;

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

    @Override
    public Object getValue() {
        switch (this.jcomponent) {
            case CustomComboBox<?> combobox -> {
                return combobox.getSelectedItem();
            }
            case CustomSlider slider -> {
                return slider.getValue();
            }
            case CustomCheckBox checkbox -> {
                return checkbox.isSelected();
            }
            default -> {
                throw new IllegalStateException("Unexpected component type: " + this.jcomponent.getClass().getName());
            }
        }
    }

    /**
     * Returns the {@link JComponent} associated with this audio setting.
     * <b>Note:</b> The component should not be modified externally as it is intended for UI purposes only.
     * 
     * @return the JComponent for this audio setting
     */
    @SuppressFBWarnings(
        value = "EI",
        justification = "This method is intended to return a UI component for display purposes only."
    )
    @Override
    public JComponent getJComponent() {
        return this.jcomponent;
    }

    private static CustomSlider createMasterSlider() {
        final var customSlider = new CustomSlider(JSlider.HORIZONTAL, 0, 100, 100);
        final var slider = customSlider.getSlider();

        slider.addChangeListener(e -> {
            if (slider.getValueIsAdjusting()
                    && customSlider.getLastValue() != customSlider.getValue()) {
                final int value = customSlider.getValue();
                final float volume = value / 100f;
                SoundManager.setMasterVolume(volume);
                customSlider.setLastValue(slider.getValue());
            }
        });

        return customSlider;
    }

    private static CustomSlider createMusicSlider() {
        final var customSlider = new CustomSlider(JSlider.HORIZONTAL, 0, 100, 50);
        final var slider = customSlider.getSlider();

        slider.addChangeListener(e -> {
            if (!slider.getValueIsAdjusting()) {
                final var value = slider.getValue();
                final var lastValue = customSlider.getLastValue();

                if (lastValue != value) {
                    customSlider.setLastValue(value);
                }
            }
        });

        return customSlider;
    }

}
