package com.thelegendofbald.ui.settingsmenu.model;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;

import com.thelegendofbald.ui.api.MenuView;
import com.thelegendofbald.ui.api.Resizable;
import com.thelegendofbald.ui.api.View;
import com.thelegendofbald.ui.controller.ResizeListener;
import com.thelegendofbald.ui.controller.TemplateButtonMouseListener;
import com.thelegendofbald.utils.ImageUtils;

/**
 * CustomCheckBox is a specialized JCheckBox component designed for use in a
 * custom settings menu UI.
 * It supports dynamic resizing and custom icons for checked and unchecked
 * states.
 * <p>
 * Features:
 * <ul>
 * <li>Uses custom icons for checked and unchecked states, loaded from
 * resources.</li>
 * <li>Automatically scales icons based on the component's height.</li>
 * <li>Implements {@link View}, {@link MenuView} and {@link Resizable} interfaces for
 * integration with the menu system and resizing behavior.</li>
 * <li>Handles resizing and updates its view accordingly.</li>
 * <li>Non-opaque and centered alignment for seamless UI integration.</li>
 * </ul>
 * </p>
 */
public class CustomCheckBox extends JCheckBox implements View, MenuView, Resizable {

    private static final double HEIGHT_PROPORTION = 0.85;

    private static final String STARTING_ICON_PATH = "images/";
    private static final String CHECKED_ICON_PATH = STARTING_ICON_PATH + "buttons/checkbox_checked.png";
    private static final String UNCHECKED_ICON_PATH = STARTING_ICON_PATH + "buttons/checkbox_unchecked.png";

    private final ImageIcon checkedIconOriginal = new ImageIcon(
            this.getClass().getClassLoader().getResource(CHECKED_ICON_PATH));
    private final ImageIcon uncheckedIconOriginal = new ImageIcon(
            this.getClass().getClassLoader().getResource(UNCHECKED_ICON_PATH));

    private boolean initialized;

    /**
     * Constructs a new {@code CustomCheckBox} with custom UI settings.
     * <p>
     * This constructor initializes the checkbox to be non-opaque and centers its content
     * both horizontally and vertically. It also adds a {@link ResizeListener} to handle
     * resize events and a {@link TemplateButtonMouseListener} to handle mouse interactions.
     */
    public CustomCheckBox() {
        super();
        this.setOpaque(false);
        this.setHorizontalAlignment(CENTER);
        this.setVerticalAlignment(CENTER);
        this.addComponentListener(new ResizeListener(this::onResize));
        this.addMouseListener(new TemplateButtonMouseListener() {
        });
    }

    /**
     * Called when the component is added to a container or made displayable.
     * <p>
     * Subclasses can override this method to perform additional initialization
     * when the checkbox is added to a parent container. If overridden, ensure
     * that {@code super.addNotify()} is called to preserve the default behavior.
     * </p>
     */
    @Override
    public void addNotify() {
        super.addNotify();
        if (!this.initialized) {
            this.initialized = true;
            SwingUtilities.invokeLater(this::initializeIcons);
        }
    }

    private void initializeIcons() {
        this.updateComponentsSize();
    }

    /**
     * Handles resize events for the checkbox.
     * <p>
     * Subclasses can override this method to provide custom behavior when the
     * checkbox is resized. If overridden, ensure that {@code super.onResize()} is
     * called to preserve the default behavior of updating the view.
     * </p>
     */
    @Override
    public void onResize() {
        this.updateView();
    }

    /**
     * Updates the size of the checkbox's icons based on the component's height.
     * <p>
     * Subclasses can override this method to provide custom logic for resizing
     * the icons. If overridden, ensure that {@code super.updateComponentsSize()} is
     * called to preserve the default behavior of scaling the icons.
     * </p>
     */
    @Override
    public void updateComponentsSize() {
        final int size = this.getHeight() > 1 ? (int) (this.getHeight() * HEIGHT_PROPORTION) : 1;
        this.setIcon(ImageUtils.scaleImageIcon(uncheckedIconOriginal, size, size));
        this.setSelectedIcon(ImageUtils.scaleImageIcon(checkedIconOriginal, size, size));
    }

    /**
     * Updates the view of the checkbox.
     * <p>
     * Subclasses can override this method to provide custom logic for updating
     * the checkbox's appearance. If overridden, ensure that {@code super.updateView()}
     * is called to preserve the default behavior of resizing and repainting the component.
     * </p>
     */
    @Override
    public void updateView() {
        this.updateComponentsSize();
        this.revalidate();
        this.repaint();
    }

    @Override
    public void addComponentsToPanel() {
    }

    /**
     * Sets the preferred size of the checkbox.
     * <p>
     * Subclasses can override this method to provide custom logic for handling
     * size changes. If overridden, ensure that {@code super.setPreferredSize(Dimension)}
     * is called to preserve the default behavior of updating the view.
     * </p>
     *
     * @param size the preferred size of the checkbox
     */
    @Override
    public void setPreferredSize(final Dimension size) {
        super.setPreferredSize(size);
        SwingUtilities.invokeLater(this::updateView);
    }

    /**
     * Gets the internal size of the checkbox.
     * <p>
     * Subclasses can override this method to provide custom logic for retrieving
     * the internal size. If overridden, ensure that {@code super.getInternalSize()}
     * is called to preserve the default behavior.
     * </p>
     *
     * @return the preferred size of the checkbox
     */
    @Override
    public Dimension getInternalSize() {
        return this.getPreferredSize();
    }

    /**
     * Sets the internal size of the checkbox.
     * <p>
     * Subclasses can override this method to provide custom logic for handling
     * size changes. If overridden, ensure that {@code super.setInternalSize(Dimension)}
     * is called to preserve the default behavior of updating the view.
     * </p>
     *
     * @param size the new internal size of the checkbox
     */
    @Override
    public void setInternalSize(final Dimension size) {
        this.setPreferredSize(size);
        SwingUtilities.invokeLater(this::updateView);
    }

}
