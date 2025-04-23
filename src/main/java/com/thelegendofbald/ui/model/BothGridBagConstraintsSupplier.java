package com.thelegendofbald.ui.model;

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

    private final class BothGridBagConstraints extends GridBagConstraints {

        private BothGridBagConstraints() {
            this.fill = GridBagConstraints.BOTH;
            this.anchor = GridBagConstraints.CENTER;
            this.weighty = 1.0;
            this.weightx = 1.0;
        }

    }

}
