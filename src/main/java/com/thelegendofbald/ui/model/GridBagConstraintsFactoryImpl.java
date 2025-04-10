package com.thelegendofbald.ui.model;

import java.awt.GridBagConstraints;
import java.util.function.Supplier;

import com.thelegendofbald.ui.api.GridBagConstraintsFactory;

public class GridBagConstraintsFactoryImpl implements GridBagConstraintsFactory {

    private final Supplier<GridBagConstraints> verticalGBCSupplier = new VerticalGridBagConstraintsSupplier();
    private final Supplier<GridBagConstraints> horizontalGBCSupplier = new HorizontalGridBagConstraintsSupplier();
    private final Supplier<GridBagConstraints> bothGBCSupplier = new BothGridBagConstraintsSupplier();

    @Override
    public GridBagConstraints createVerticalGridBagConstraints() {
        return verticalGBCSupplier.get();
    }

    @Override
    public GridBagConstraints createHorizontalGridBagConstraints() {
        return horizontalGBCSupplier.get();
    }

    @Override
    public GridBagConstraints createBothGridBagConstraints() {
        return bothGBCSupplier.get();
    }

}
