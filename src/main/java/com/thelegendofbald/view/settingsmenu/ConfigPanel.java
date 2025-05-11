package com.thelegendofbald.view.settingsmenu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Optional;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;

import org.apache.commons.math3.util.Pair;

import com.thelegendofbald.api.common.GridBagConstraintsFactory;
import com.thelegendofbald.api.panels.AdapterPanel;
import com.thelegendofbald.view.common.TitleLabel;
import com.thelegendofbald.view.common.TitleLabelFactoryImpl;
import com.thelegendofbald.view.contraints.GridBagConstraintsFactoryImpl;

/**
 * ConfigPanel is a custom Swing panel used for displaying a configuration option
 * with a title and associated value component. It extends AdapterPanel and arranges
 * its components using a GridBagLayout. The panel displays a title label and a
 * configurable value component side by side, with optional insets and a white border.
 * 
 * <p>
 * The title label is created using a TitleLabelFactoryImpl and is initialized
 * lazily when the panel is added to the component hierarchy. The value component
 * is provided externally via the constructor.
 * </p>
 *
 * @see AdapterPanel
 * @see TitleLabel
 * @see TitleLabelFactoryImpl
 * @see GridBagConstraintsFactory
 */
public final class ConfigPanel extends AdapterPanel {

    private static final int UP_DOWN_INSETS = 2;

    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = gbcFactory.createHorizontalGridBagConstraints();

    private final TitleLabelFactoryImpl tlFactory = new TitleLabelFactoryImpl();

    private Optional<TitleLabel> title = Optional.empty();
    private final String text;
    private final JComponent values;

    /**
     * Constructs a ConfigPanel with the specified label text and value component.
     *
     * <p>
     * The panel is initialized with a transparent background, a white matte border
     * on the top and bottom, and uses a GridBagLayout for arranging its components.
     * </p>
     *
     * @param text   the label or description for this configuration panel
     * @param values the component containing the configurable values or controls
     */
    public ConfigPanel(final String text, final JComponent values) {
        super(new Dimension(0, 0));
        this.text = text;
        this.values = values;

        this.setOpaque(false);
        this.setBorder(new MatteBorder(UP_DOWN_INSETS, 0, UP_DOWN_INSETS, 0, Color.WHITE));
        this.setLayout(new GridBagLayout());
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (this.title.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                this.initializeComponents();
                this.removeAll();
                this.addComponentsToPanel();
                this.revalidate();
                this.repaint();
            });
        }
    }

    @Override
    protected void initializeComponents() {
        this.title = Optional.of(tlFactory.createTitleLabelWithProportion(this.text, this.getSize(),
                Optional.of(new Pair<>(1.0, 1.0)), Optional.empty(), Optional.empty()));
    }

    @Override
    public void addComponentsToPanel() {
        this.updateComponentsSize();

        gbc.gridx = 0;
        this.title.ifPresent(t -> this.add(t, gbc));

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.NONE;
        this.add(values, gbc);
    }

    @Override
    public void setPreferredSize(final Dimension size) {
        super.setPreferredSize(size);
        SwingUtilities.invokeLater(this::updateComponentsSize);
    }

    @Override
    public void updateComponentsSize() {
        this.title.ifPresent(t -> t.setPreferredSize(this.getSize()));
        this.values.setPreferredSize(this.getSize());
    }

}
