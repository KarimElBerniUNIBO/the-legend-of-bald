package com.thelegendofbald.ui.model;

import java.awt.GridBagConstraints;
import java.util.function.Supplier;

/**
 * A supplier that provides {@code GridBagConstraints} instances
 * configured for horizontal filling.
 * 
 * This class ensures that components stretch horizontally while distributing 
 * space evenly within the container.
 */
public final class HorizontalGridBagConstraintsSupplier implements Supplier<GridBagConstraints> {

    @Override
    public GridBagConstraints get() {
        return new HorizontalGridBagConstraints();
    }

    private final class HorizontalGridBagConstraints extends GridBagConstraints {

        private HorizontalGridBagConstraints() {
            this.fill = GridBagConstraints.HORIZONTAL;
            this.weightx = 1.0;
            this.weighty = 1.0;
        }

    }

}
