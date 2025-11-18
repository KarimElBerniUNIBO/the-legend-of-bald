package com.thelegendofbald.api.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.thelegendofbald.life.LifeComponent;

/**
 * A panel that visually represents an entity's health.
 * It now implements PropertyChangeListener to "listen" for
 * changes from the LifeComponent.
 */
public class LifePanel extends AdapterPanel implements PropertyChangeListener {

    private static final long serialVersionUID = 1L;

    private final LifeComponent lifeComponent;

    /**
     * Constructs a new LifePanel associated with a LifeComponent.
     * @param lifeComponent the LifeComponent to monitor
     */
    public LifePanel(final LifeComponent lifeComponent) {
        super();
        if (lifeComponent == null) {
            throw new IllegalArgumentException("LifeComponent cannot be null");
        }
        this.lifeComponent = lifeComponent;
        this.lifeComponent.addPropertyChangeListener(this);
    }

    /**
     * Initializes the panel's components.
     */
    @Override
    protected void initializeComponents() {
        super.initializeComponents(); 
        addComponentsToPanel();
    }

    /**
     * Adds sub-components to the panel.
     */
    @Override
    public void addComponentsToPanel() {
    }

     /**
     * Triggers a redraw of the panel.
     */
    @Override
    public void updateView() {
        repaint();
    }

    /**
     * Draws the health bar.
     * @param g The Graphics object used for drawing.
     */
     @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final double perc = lifeComponent.getPercentage(); 
        final int width = (int) (perc * getWidth());

        g.setColor(Color.GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.RED);
        g.fillRect(0, 0, width, getHeight());

        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    /**
     * Placeholder for updating component sizes.
     */
    @Override
    public void updateComponentsSize() {
    }

    /**
     * This method is called automatically by the LifeComponent
     * whenever its health changes (because we subscribed in the constructor).
     *
     * @param evt The event that describes the change.
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (LifeComponent.HEALTH_PROPERTY.equals(evt.getPropertyName())) {
            updateView();
        }
    }
}
