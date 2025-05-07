package com.thelegendofbald.ui.model;

import java.awt.GridBagConstraints;
import java.util.function.Supplier;

/**
 * A supplier that provides {@code GridBagConstraints} instances
 * configured for vertical filling.
 * <p>
 * This class ensures that components stretch vertically while distributing 
 * space evenly within the container.
 * </p>
 */
public final class VerticalGridBagConstraintsSupplier implements Supplier<GridBagConstraints> {

    @Override
    public GridBagConstraints get() {
        return new VerticalGridBagConstraints();
    }

    private final class VerticalGridBagConstraints extends GridBagConstraints {

        private VerticalGridBagConstraints() {
            this.fill = GridBagConstraints.VERTICAL;
            this.anchor = GridBagConstraints.CENTER;
            this.weighty = 1.0;
            this.weightx = 1.0;
        }

    }

}
