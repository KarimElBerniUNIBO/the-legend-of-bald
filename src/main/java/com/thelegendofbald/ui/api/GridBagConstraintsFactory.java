package com.thelegendofbald.ui.api;

import java.awt.GridBagConstraints;

/**
 * Factory for creating different types of GridBagConstraints.
 */
public interface GridBagConstraintsFactory {

    /**
     * Create layout constraints for vertical positioning.
     * 
     * @return GridBagConstraints configured for vertical layout
     */
    GridBagConstraints createVerticalGridBagConstraints();

    /**
     * Create layout constraints for horizontal positioning.
     * 
     * @return GridBagConstraints configured for horizontal layout
     */
    GridBagConstraints createHorizontalGridBagConstraints();

    /**
     * Create layout constraints for both axes.
     * 
     * @return GridBagConstraints configured for both vertical and horizontal layout
     */
    GridBagConstraints createBothGridBagConstraints();

}
