package com.thelegendofbald.ui.api;

import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * Factory for creating different types of GridBagConstraints.
 */
public interface GridBagConstraintsFactory {

    GridBagConstraints createDefaultGridBagConstraint();
    GridBagConstraints createDefaultGridBagConstraint(Insets insets);

    /**
     * Create layout constraints for vertical positioning.
     * 
     * @return GridBagConstraints configured for vertical layout
     */
    GridBagConstraints createVerticalGridBagConstraints();
    GridBagConstraints createVerticalGridBagConstraints(Insets insets);

    /**
     * Create layout constraints for horizontal positioning.
     * 
     * @return GridBagConstraints configured for horizontal layout
     */
    GridBagConstraints createHorizontalGridBagConstraints();
    GridBagConstraints createHorizontalGridBagConstraints(Insets insets);

    /**
     * Create layout constraints for both axes.
     * 
     * @return GridBagConstraints configured for both vertical and horizontal layout
     */
    GridBagConstraints createBothGridBagConstraints();
    GridBagConstraints createBothGridBagConstraints(Insets insets);

}
