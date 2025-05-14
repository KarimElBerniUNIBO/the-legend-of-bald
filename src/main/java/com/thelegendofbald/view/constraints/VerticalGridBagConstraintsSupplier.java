package com.thelegendofbald.view.constraints;

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

    private static final long serialVersionUID = 1L;

    @Override
    public GridBagConstraints get() {
        return new VerticalGridBagConstraints();
    }

    private final class VerticalGridBagConstraints extends GridBagConstraints {

        private VerticalGridBagConstraints() {
            this.fill = VERTICAL;
            this.anchor = CENTER;
            this.weighty = 1.0;
            this.weightx = 1.0;
        }

    }

}
