package com.thelegendofbald.api.settingsmenu;

import java.util.Arrays;
import java.util.Optional;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import com.thelegendofbald.api.views.MainView;
import com.thelegendofbald.view.common.CustomCheckBox;
import com.thelegendofbald.view.common.CustomComboBox;
import com.thelegendofbald.view.common.CustomSlider;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Enum representing video settings in the application.
 * Each setting is associated with a specific UI component for user interaction.
 */
public enum VideoSettings implements SettingOption {
    /**
     * Represents the window mode setting.
     */
    WINDOW_MODE("WINDOW MODE", createWindowModeComboBox()),
    /**
     * Represents the show FPS setting.
     */
    SHOW_FPS("SHOW FPS", createShowFPSCheckBox()),
    /**
     * Represents the show timer setting.
     */
    SHOW_TIMER("SHOW TIMER", createShowTimerCheckBox());

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
     * Returns the {@link JComponent} associated with this video setting.
     * <b>Note:</b> The component should not be modified externally as it is
     * intended for UI purposes only.
     *
     * @return the JComponent for this video setting
     */
    @SuppressFBWarnings(
        value = "EI",
        justification = "This method is intended to return a UI component for display purposes only."
    )
    @Override
    public JComponent getJComponent() {
        return this.jcomponent;
    }

    private static JComboBox<WindowMode> createWindowModeComboBox() {
        final var comboBox = new CustomComboBox<>(Arrays.asList(WindowMode.values()));
        comboBox.setSelectedItem(WindowMode.WINDOW);
        comboBox.addActionListener(e -> {
            final var selectedMode = (WindowMode) comboBox.getSelectedItem();
            if (Optional.ofNullable(selectedMode).isPresent() && selectedMode != comboBox.getLastSelectedItem()) {
                final var window = (MainView) SwingUtilities.getWindowAncestor(comboBox);
                window.setWindowMode(selectedMode);
                comboBox.setLastSelectedItem(selectedMode);
            }
        });
        return comboBox;
    }

    private static CustomCheckBox createShowFPSCheckBox() {
        final var checkBox = new CustomCheckBox();
        checkBox.addActionListener(e -> {
            final boolean isSelected = checkBox.isSelected();
            final var window = (MainView) SwingUtilities.getWindowAncestor(checkBox);
            window.toggleViewFps(isSelected);
        });
        return checkBox;
    }

    private static CustomCheckBox createShowTimerCheckBox() {
        final var checkBox = new CustomCheckBox();
        checkBox.setSelected(true);
        checkBox.addActionListener(e -> {
            final boolean isSelected = checkBox.isSelected();
            final var window = (MainView) SwingUtilities.getWindowAncestor(checkBox);
            window.toggleViewTimer(isSelected);
        });
        return checkBox;
    }

}
