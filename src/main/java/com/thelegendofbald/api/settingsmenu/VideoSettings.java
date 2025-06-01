package com.thelegendofbald.api.settingsmenu;

import java.util.Arrays;
import java.util.Optional;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

import com.thelegendofbald.api.views.MainView;
import com.thelegendofbald.view.common.CustomCheckBox;
import com.thelegendofbald.view.common.CustomComboBox;
import com.thelegendofbald.view.common.CustomSlider;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public enum VideoSettings implements SettingOption {
    WINDOW_MODE("WINDOW MODE", createWindowModeComboBox()),
    FPS("FRAMERATE PER SECOND", createFPSSlider()),
    SHOW_FPS("SHOW FPS", createShowFPSCheckBox());

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

    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP", 
        justification = "The JComponent is used for UI purposes and should not be modified externally."
    )
    /**
     * Returns the {@link JComponent} associated with this video setting.
     *
     * @return the JComponent for this video setting
     */
    @Override
    public JComponent getJComponent() {
        return this.jcomponent;
    }

    private static JComboBox<WindowMode> createWindowModeComboBox() {
        var comboBox = new CustomComboBox<>(Arrays.asList(WindowMode.values()));
        comboBox.setSelectedItem(WindowMode.WINDOW);
        comboBox.addActionListener(e -> {
            var selectedMode = (WindowMode) comboBox.getSelectedItem();
            if (Optional.ofNullable(selectedMode).isPresent() && selectedMode != comboBox.getLastSelectedItem()) {
                var window = (MainView) SwingUtilities.getWindowAncestor(comboBox);
                window.setWindowMode(selectedMode);
                comboBox.setLastSelectedItem(selectedMode);
            }
        });
        return comboBox;
    }

    private static CustomSlider createFPSSlider() {
        var customSlider = new CustomSlider(JSlider.HORIZONTAL, 30, 144, 60);
        var slider = customSlider.getSlider();

        slider.addChangeListener(e -> {
            if (!slider.getValueIsAdjusting()
                    && customSlider.getLastValue() != customSlider.getValue()) {
                var window = (MainView) SwingUtilities.getWindowAncestor(customSlider);
                window.setFPS(slider.getValue());
                customSlider.setLastValue(slider.getValue());
            }
        });

        return customSlider;
    }

    private static CustomCheckBox createShowFPSCheckBox() {
        var checkBox = new CustomCheckBox();
        checkBox.addActionListener(e -> {
            boolean isSelected = checkBox.isSelected();
            var window = (MainView) SwingUtilities.getWindowAncestor(checkBox);
            window.toggleViewFps(isSelected);
        });
        return checkBox;
    }

}
