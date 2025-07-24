package com.thelegendofbald.view.common;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.JSlider;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.api.common.GridBagConstraintsFactory;
import com.thelegendofbald.api.common.TextLabelFactory;
import com.thelegendofbald.api.panels.AdapterPanel;
import com.thelegendofbald.model.sounds.SoundPlayer;
import com.thelegendofbald.view.constraints.GridBagConstraintsFactoryImpl;

public class CustomSlider extends AdapterPanel {

    private static final int MINOR_TICK_SPACING = 5;
    private static final int MAJOR_TICK_SPACING = 2 * MINOR_TICK_SPACING;

    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private final TextLabelFactory tlFactory = new TextLabelFactoryImpl();
    private final SoundPlayer tickSound = new SoundPlayer("/slider/tick.wav");

    private final JSlider slider;
    private int lastValue;
    private Optional<TextLabel> text = Optional.empty();

    public CustomSlider(int orientation, int min, int max, int value) {
        super(new Dimension(0, 0));
        this.slider = new JSlider(orientation, min, max, value);
        this.lastValue = this.slider.getValue();
        this.setLayout(new GridBagLayout());
    }

    @Override
    protected void initializeComponents() {
        this.text = Optional.of(tlFactory.createTextLabelWithProportion(String.valueOf(this.getValue()), this.getSize(),
                Optional.empty(), Optional.of(Pair.of(3.0, 1.0)), Optional.empty(), Optional.empty()));
        this.slider.setMajorTickSpacing(MAJOR_TICK_SPACING);
        this.slider.setMinorTickSpacing(MINOR_TICK_SPACING);
        this.slider.setPaintTicks(true);
        this.slider.setSnapToTicks(true);
        this.slider.addChangeListener(e -> {
            if (this.getValue() % MINOR_TICK_SPACING == 0 || this.getValue() == this.slider.getMaximum()) {
                this.tickSound.play();
                this.text.ifPresent(t -> t.setText(String.valueOf(this.getValue())));
            }
        });

        super.initializeComponents();
    }

    @Override
    public void updateComponentsSize() {
        Arrays.stream(this.getComponents())
                .forEach(c -> c.setPreferredSize(this.getSize()));
    }

    @Override
    public void addComponentsToPanel() {
        this.gbc.gridx = 0;
        this.gbc.weightx = 0.7;
        this.add(this.slider, this.gbc);

        this.gbc.gridx = 1;
        this.gbc.weightx = 0.3;
        this.text.ifPresent(t -> this.add(t, this.gbc));

        this.updateComponentsSize();
    }

    @Override
    public void setPreferredSize(Dimension size) {
        super.setPreferredSize(size);
        SwingUtilities.invokeLater(this::updateView);
    }

    public JSlider getSlider() {
        return slider;
    }

    public Optional<TextLabel> getText() {
        return text;
    }

    public int getValue() {
        return slider.getValue();
    }

    public int getLastValue() {
        return lastValue;
    }

    public void setLastValue(int lastValue) {
        this.lastValue = lastValue;
    }

}
