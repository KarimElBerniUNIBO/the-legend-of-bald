package com.thelegendofbald.api.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import com.thelegendofbald.life.LifeComponent;

/**
 * A panel that visually represents an entity's health as a dynamic health bar.
 * This panel acts as a view that observes a {@link LifeComponent} (the model)
 * and updates its display based on the component's state.
 */
public class LifePanel extends AdapterPanel {

    private final LifeComponent lifeComponent;  

    /**
     * Constructs a new LifePanel.
     *
     * @param size The preferred size of the panel.
     * @param lifeComponent The {@link LifeComponent} instance this panel will display.
     */
    public LifePanel(Dimension size, LifeComponent lifeComponent) {
        super(size);
        this.lifeComponent = lifeComponent;
    }

    /**
     * Initializes the components of the panel.
     * This method is called during the panel's setup.
     */
    @Override
    protected void initializeComponents() {
        super.initializeComponents(); // Sets up resize listener and calls updateView
        addComponentsToPanel(); // Adds any sub-components if needed
    }

    /**
     * Placeholder method to add any sub-components to the panel.
     * Currently not implemented, as the panel draws its own content.
     */
    @Override
    public void addComponentsToPanel() {
        // This method can be implemented if sub-components (like labels) are needed.
    }

    /**
     * Triggers a redraw of the panel to reflect the current state of the health bar.
     * This method is called to update the view when the health value changes.
     */
    @Override
    public void updateView() {
        repaint();
    }

    /**
     * Paints the visual representation of the health bar.
     *
     * @param g The Graphics object used for drawing.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        double perc = lifeComponent.getPercentage(); 
        int width = (int) (perc * getWidth());

        // Draw the gray background
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw the red foreground health bar
        g.setColor(Color.RED);
        g.fillRect(0, 0, width, getHeight());

        // Draw the black border
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    /**
     * This method is a placeholder for updating component sizes on a resize event.
     * Currently not implemented as the health bar scales automatically with the panel.
     */
    @Override
    public void updateComponentsSize() {
        // Implement if needed, otherwise leave empty
    }
}