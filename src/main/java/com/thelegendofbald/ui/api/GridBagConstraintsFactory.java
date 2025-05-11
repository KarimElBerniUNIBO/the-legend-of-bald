package com.thelegendofbald.ui.api;

import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * Factory for creating different types of GridBagConstraints.
 */
public interface GridBagConstraintsFactory {

    /**
     * Creates and returns a {@link GridBagConstraints} instance with default settings.
     * <p>
     * This method provides a convenient way to obtain a pre-configured {@code GridBagConstraints}
     * object, which can be customized further as needed before use in a layout.
     *
     * @return a {@code GridBagConstraints} object initialized with default values
     */
    GridBagConstraints createDefaultGridBagConstraint();

    /**
     * Creates and returns a {@link GridBagConstraints} instance with default settings and specified insets.
     * <p>
     * This method provides a convenient way to obtain a pre-configured {@code GridBagConstraints}
     * object, which can be customized further as needed before use in a layout.
     *
     * @param insets the insets to set for the GridBagConstraints
     * @return a {@code GridBagConstraints} object initialized with default values and specified insets
     */
    GridBagConstraints createDefaultGridBagConstraint(Insets insets);

    /**
     * Create layout constraints for vertical positioning.
     * 
     * @return GridBagConstraints configured for vertical layout
     */
    GridBagConstraints createVerticalGridBagConstraints();

    /**
     * Create layout constraints for vertical positioning with specified insets.
     * 
     * @param insets the insets to set for the GridBagConstraints
     * @return GridBagConstraints configured for vertical layout with specified insets
     */
    GridBagConstraints createVerticalGridBagConstraints(Insets insets);

    /**
     * Create layout constraints for horizontal positioning.
     * 
     * @return GridBagConstraints configured for horizontal layout
     */
    GridBagConstraints createHorizontalGridBagConstraints();

    /**
     * Create layout constraints for horizontal positioning with specified insets.
     * 
     * @param insets the insets to set for the GridBagConstraints
     * @return GridBagConstraints configured for horizontal layout with specified insets
     */
    GridBagConstraints createHorizontalGridBagConstraints(Insets insets);

    /**
     * Create layout constraints for both axes.
     * 
     * @return GridBagConstraints configured for both vertical and horizontal layout
     */
    GridBagConstraints createBothGridBagConstraints();

    /**
     * Create layout constraints for both axes with specified insets.
     * 
     * @param insets the insets to set for the GridBagConstraints
     * @return GridBagConstraints configured for both vertical and horizontal layout with specified insets
     */
    GridBagConstraints createBothGridBagConstraints(Insets insets);

}
