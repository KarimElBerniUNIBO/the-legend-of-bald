package com.thelegendofbald.ui.model;

import java.awt.GridBagConstraints;
import java.util.function.Supplier;

class VerticalGridBagConstraintsSupplier implements Supplier<GridBagConstraints> {

    private class VerticalGridBagConstraints extends GridBagConstraints {

        private VerticalGridBagConstraints() {
            this.fill = GridBagConstraints.VERTICAL;
            this.weighty = 1.0;
            this.weightx = 1.0;
        }

    }

    @Override
    public GridBagConstraints get() {
        return new VerticalGridBagConstraints();
    }

}