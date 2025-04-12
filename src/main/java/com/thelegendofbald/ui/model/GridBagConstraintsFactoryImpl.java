package com.thelegendofbald.ui.model;

import java.awt.GridBagConstraints;
import java.util.function.Supplier;

import com.thelegendofbald.ui.api.GridBagConstraintsFactory;

/**
 * Implementation of {@code GridBagConstraintsFactory} that provides
 * pre-configured {@code GridBagConstraints} instances.
 *
 * This factory supplies constraints for different layout behaviors,
 * including vertical, horizontal, and both-directions filling.
 */
public final class GridBagConstraintsFactoryImpl implements GridBagConstraintsFactory {

    private final Supplier<GridBagConstraints> verticalGBCSupplier = new VerticalGridBagConstraintsSupplier();
    private final Supplier<GridBagConstraints> horizontalGBCSupplier = new HorizontalGridBagConstraintsSupplier();
    private final Supplier<GridBagConstraints> bothGBCSupplier = new BothGridBagConstraintsSupplier();

    /**
     * Creates a {@code GridBagConstraints} instance configured for vertical filling.
     *
     * @return A {@code GridBagConstraints} object with vertical fill properties.
     */
    @Override
    public GridBagConstraints createVerticalGridBagConstraints() {
        return verticalGBCSupplier.get();
    }

    /**
     * Creates a {@code GridBagConstraints} instance configured for horizontal filling.
     *
     * @return A {@code GridBagConstraints} object with horizontal fill properties.
     */
    @Override
    public GridBagConstraints createHorizontalGridBagConstraints() {
        return horizontalGBCSupplier.get();
    }

    /**
     * Creates a {@code GridBagConstraints} instance configured to fill both directions.
     *
     * @return A {@code GridBagConstraints} object with both horizontal and vertical fill properties.
     */
    @Override
    public GridBagConstraints createBothGridBagConstraints() {
        return bothGBCSupplier.get();
    }

}
