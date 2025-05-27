package com.thelegendofbald.api.settingsmenu;

import java.util.Optional;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

import com.thelegendofbald.api.views.MainView;
import com.thelegendofbald.view.common.CustomCheckBox;
import com.thelegendofbald.view.common.CustomJSlider;

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

    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "JComponent must be mutable for UI interaction; safe in enum context."
    )
    @Override
    public JComponent getJcomponent() {
        return this.jcomponent;
    }

    private static JComboBox<WindowMode> createWindowModeComboBox() {
        var comboBox = new JComboBox<>(WindowMode.values());
        comboBox.setSelectedItem(WindowMode.WINDOW);
        comboBox.addActionListener(e -> {
            var selectedMode = (WindowMode) comboBox.getSelectedItem();
            if (Optional.ofNullable(selectedMode).isPresent()) {
                System.out.println("Selected Window Mode: " + selectedMode.getText());
                var window = (MainView) SwingUtilities.getWindowAncestor(comboBox);
                window.setWindowMode(selectedMode);
            }
        });
        return comboBox;
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
                    var window = (MainView) SwingUtilities.getWindowAncestor(slider);
                    window.setFPS(slider.getValue());
                }
            }
        });

        return customSlider;
    }

    private static CustomCheckBox createShowFPSCheckBox() {
        var checkBox = new CustomCheckBox();
        checkBox.addActionListener(e -> {
            boolean isSelected = checkBox.isSelected();
            System.out.println("Show FPS: " + isSelected);
            var window = (MainView) SwingUtilities.getWindowAncestor(checkBox);
            window.toggleViewFps(isSelected);
        });
        return checkBox;
    }

}
