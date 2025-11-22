package com.thelegendofbald.view.component;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.JSlider;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.view.layout.GridBagConstraintsFactory;
import com.thelegendofbald.view.factory.TextLabelFactory;
import com.thelegendofbald.view.panel.base.AdapterPanel;
import com.thelegendofbald.model.system.SoundPlayer;
import com.thelegendofbald.view.layout.GridBagConstraintsFactoryImpl;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * CustomSlider is a specialized slider component that extends AdapterPanel.
 * It includes features such as tick marks, sound effects, and a text label
 * displaying the current value of the slider.
 */
public final class CustomSlider extends AdapterPanel {

    private static final double SLIDER_GBC_WEIGHT = 0.7;
    private static final double TEXT_GBC_WEIGHTX = 0.3;

    private static final long serialVersionUID = 1L;

    private static final int MINOR_TICK_SPACING = 5;
    private static final int MAJOR_TICK_SPACING = 2 * MINOR_TICK_SPACING;

    private final transient GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createBothGridBagConstraints();

    private final transient TextLabelFactory tlFactory = new TextLabelFactoryImpl();
    private final transient SoundPlayer tickSound = new SoundPlayer("/slider/tick.wav");

    private final JSlider slider;
    private int lastValue;
    private transient Optional<TextLabel> text = Optional.empty();

    /**
     * Constructor for CustomSlider.
     *
     * @param orientation The orientation of the slider (horizontal or vertical).
     * @param min        The minimum value of the slider.
     * @param max        The maximum value of the slider.
     * @param value      The initial value of the slider.
     */
    public CustomSlider(final int orientation, final int min, final int max, final int value) {
        super();
        this.slider = new JSlider(orientation, min, max, value);
        this.lastValue = this.slider.getValue();
        this.initialize();
    }

    private void initialize() {
        SwingUtilities.invokeLater(() -> {
            this.setLayout(new GridBagLayout());
        });
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
        this.gbc.weightx = SLIDER_GBC_WEIGHT;
        this.add(this.slider, this.gbc);

        this.gbc.gridx = 1;
        this.gbc.weightx = TEXT_GBC_WEIGHTX;
        this.text.ifPresent(t -> this.add(t, this.gbc));

        this.updateComponentsSize();
    }

    @Override
    public void setPreferredSize(final Dimension size) {
        super.setPreferredSize(size);
        SwingUtilities.invokeLater(this::updateView);
    }

    /**
     * Gets the GridBagConstraints for the slider component.
     *
     * @return The GridBagConstraints for the slider.
     */
    @SuppressFBWarnings(
        value = "EI",
        justification = "This method is designed to return the JSlider instance without throwing exceptions."
    )
    public JSlider getSlider() {
        return slider;
    }

    /**
     * Gets the text label associated with the slider.
     *
     * @return An Optional containing the TextLabel if it exists, otherwise an empty Optional.
     */
    public Optional<TextLabel> getText() {
        return text;
    }

    /**
     * Gets the current value of the slider.
     *
     * @return The current value of the slider.
     */
    public int getValue() {
        return slider.getValue();
    }

    /**
     * Gets the last value of the slider.
     *
     * @return The last value of the slider.
     */
    public int getLastValue() {
        return lastValue;
    }

    /**
     * Sets the last value of the slider.
     *
     * @param lastValue The last value to set for the slider.
     */
    public void setLastValue(final int lastValue) {
        this.lastValue = lastValue;
    }

}
