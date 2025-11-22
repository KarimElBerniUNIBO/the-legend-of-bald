package com.thelegendofbald.view.layout;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.function.Supplier;

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

    @Override
    public GridBagConstraints createDefaultGridBagConstraint() {
        return new GridBagConstraints();
    }

    @Override
    public GridBagConstraints createDefaultGridBagConstraint(final Insets insets) {
        final var gbc = this.createDefaultGridBagConstraint();
        gbc.insets = insets;
        return gbc;
    }

    /**
     * Creates a {@code GridBagConstraints} instance configured for vertical filling.
     *
     * @return A {@code GridBagConstraints} object with vertical fill properties.
     */
    @Override
    public GridBagConstraints createVerticalGridBagConstraints() {
        return verticalGBCSupplier.get();
    }

    @Override
    public GridBagConstraints createVerticalGridBagConstraints(final Insets insets) {
        final GridBagConstraints gbc = verticalGBCSupplier.get();
        gbc.insets = insets;
        return gbc;
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

    @Override
    public GridBagConstraints createHorizontalGridBagConstraints(final Insets insets) {
        final GridBagConstraints gbc = horizontalGBCSupplier.get();
        gbc.insets = insets;
        return gbc;
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

    @Override
    public GridBagConstraints createBothGridBagConstraints(final Insets insets) {
        final GridBagConstraints gbc = bothGBCSupplier.get();
        gbc.insets = insets;
        return gbc;
    }

}
