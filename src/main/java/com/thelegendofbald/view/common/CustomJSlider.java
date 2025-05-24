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
import com.thelegendofbald.view.constraints.GridBagConstraintsFactoryImpl;

public class CustomJSlider extends AdapterPanel {

    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private final TextLabelFactory tlFactory = new TextLabelFactoryImpl();

    private final int orientation;
    private final int min;
    private final int max;
    private final int value;

    private Optional<JSlider> slider = Optional.empty();
    private Optional<TextLabel> text = Optional.empty();

    public CustomJSlider(int orientation, int min, int max, int value) {
        super(new Dimension(0, 0));
        this.orientation = orientation;
        this.min = min;
        this.max = max;
        this.value = value;
        this.setLayout(new GridBagLayout());
    }
    
    @Override
    protected void initializeComponents() {
        this.slider = Optional.of(new JSlider(this.orientation, this.min, this.max, this.value));
        this.text = Optional.of(tlFactory.createTextLabelWithProportion(String.valueOf(this.value), this.getSize(), Optional.empty(), Optional.of(Pair.of(3.0, 1.0)), Optional.empty(), Optional.empty()));
        this.slider.ifPresent(s -> {
            s.setMajorTickSpacing(10);
            s.setMinorTickSpacing(5);
            s.setPaintTicks(true);
            s.setSnapToTicks(true);
            s.addChangeListener(e -> {
                if (!s.getValueIsAdjusting()) {
                    this.text.ifPresent(t -> t.setText(String.valueOf(s.getValue())));
                }
            });
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
        this.slider.ifPresent(s -> this.add(s, this.gbc));

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

}
