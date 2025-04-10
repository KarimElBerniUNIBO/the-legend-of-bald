package com.thelegendofbald.ui.model;

import java.awt.GridBagConstraints;
import java.util.function.Supplier;

public class BothGridBagConstraintsSupplier implements Supplier<GridBagConstraints> {

    private class BothGridBagConstraints extends GridBagConstraints {

        private BothGridBagConstraints() {
            this.fill = GridBagConstraints.BOTH;
            this.weighty = 1.0;
            this.weightx = 1.0;
        }

    }

    @Override
    public GridBagConstraints get() {
        return new BothGridBagConstraints();
    }

}
