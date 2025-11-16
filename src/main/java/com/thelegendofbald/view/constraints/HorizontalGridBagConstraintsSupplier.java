package com.thelegendofbald.view.constraints;

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

    private static final class HorizontalGridBagConstraints extends GridBagConstraints {

        private static final long serialVersionUID = 1L;

        private HorizontalGridBagConstraints() {
            this.fill = HORIZONTAL;
            this.anchor = CENTER;
            this.weightx = 1.0;
            this.weighty = 1.0;
        }

    }

}
