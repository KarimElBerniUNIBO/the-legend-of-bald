package com.thelegendofbald.api.panels;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.thelegendofbald.api.views.MenuView;
import com.thelegendofbald.api.views.View;

/**
 * An abstract base class for menu panels in the UI, providing common sizing and
 * initialization logic.
 * <p>
 * BasePanel manages preferred, minimum, and maximum sizes, and ensures that
 * component initialization
 * occurs only once when the panel is added to a container. Subclasses must
 * implement methods for
 * initializing components and adding them to the panel.
 * </p>
 */
public abstract class BasePanel extends JPanel implements View, MenuView {

    private static final long serialVersionUID = 1L;

    private boolean initializedComponets;

    /**
     * Constructs a new {@code BasePanel}.
     */
    public BasePanel() {
        super();
        this.initialize();
    }

    private void initialize() {
        SwingUtilities.invokeLater(() -> {
            this.setBackground(Color.BLACK);
        });
    }

    /**
     * Called when the component is added to a container or made displayable.
     * <p>
     * Subclasses can override this method to perform additional initialization
     * when the panel is added to a parent container. If overridden, ensure
     * that {@code super.addNotify()} is called to preserve the default behavior.
     * </p>
     */
    @Override
    public void addNotify() {
        super.addNotify();
        if (!this.initializedComponets) {
            SwingUtilities.invokeLater(this::initializeComponents);
            this.initializedComponets = true;
        }
        SwingUtilities.invokeLater(this::updateView);
    }

    /**
     * Initializes the components of the panel.
     * <p>
     * Subclasses must implement this method to provide specific initialization
     * logic for their components. This method is called when the panel is added
     * to a container and should be used to set up the UI elements.
     * </p>
     */
    protected abstract void initializeComponents();

    /**
     * Updates the view by removing all components and re-adding them to the panel.
     * <p>
     * Subclasses can override this method to provide custom update logic. If overridden,
     * ensure that {@code super.updateView()} is called to maintain the default behavior
     * of clearing and revalidating the panel.
     * </p>
     */
    @Override
    public void updateView() {
        this.removeAll();
        this.addComponentsToPanel();
        this.revalidate();
        this.repaint();
    }

    @Override
    public abstract void addComponentsToPanel();

    /**
     * Checks if the panel has been initialized.
     * <p>
     * Subclasses can override this method to provide custom logic for determining
     * the initialization state. If overridden, ensure that the method remains
     * thread-safe and consistent with the panel's lifecycle.
     * </p>
     *
     * @return {@code true} if the panel is initialized; {@code false} otherwise.
     */
    public boolean isInitializedComponets() {
        return initializedComponets;
    }

}
