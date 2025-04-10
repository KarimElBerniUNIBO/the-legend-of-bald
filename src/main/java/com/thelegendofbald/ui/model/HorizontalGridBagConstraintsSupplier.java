package com.thelegendofbald.ui.model;

import java.awt.GridBagConstraints;
import java.util.function.Supplier;

public class HorizontalGridBagConstraintsSupplier implements Supplier<GridBagConstraints> {

    private class HorizontalGridBagConstraints extends GridBagConstraints {

        private HorizontalGridBagConstraints() {
            this.fill = GridBagConstraints.HORIZONTAL;
            this.weightx = 1.0;
            this.weighty = 1.0;
        }

    }

    @Override
    public GridBagConstraints get() {
        return new HorizontalGridBagConstraints();
    }

}
