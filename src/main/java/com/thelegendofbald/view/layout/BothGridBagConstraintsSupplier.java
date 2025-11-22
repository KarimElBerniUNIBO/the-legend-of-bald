package com.thelegendofbald.view.layout;

import java.awt.GridBagConstraints;
import java.util.function.Supplier;

/**
 * A supplier class that provides instances of GridBagConstraints
 * with predefined settings for flexible layout behavior.
 */
public final class BothGridBagConstraintsSupplier implements Supplier<GridBagConstraints> {

    @Override
    public GridBagConstraints get() {
        return new BothGridBagConstraints();
    }

    private static final class BothGridBagConstraints extends GridBagConstraints {

        private static final long serialVersionUID = 1L;

        private BothGridBagConstraints() {
            this.fill = BOTH;
            this.anchor = CENTER;
            this.weighty = 1.0;
            this.weightx = 1.0;
        }

    }

}
