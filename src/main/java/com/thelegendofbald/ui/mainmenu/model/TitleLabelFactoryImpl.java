package com.thelegendofbald.ui.mainmenu.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Optional;

import org.apache.commons.math3.util.Pair;

import com.thelegendofbald.ui.mainmenu.api.TitleLabelFactory;

public class TitleLabelFactoryImpl implements TitleLabelFactory {

    private static final Pair<Double, Double> DEFAULT_MOLTIPLICATOR = new Pair<Double,Double>(1.0, 1.0);
    private static final Color DEFAULT_FOREGROUND_COLOR = Color.WHITE;
    private static final String DEFAULT_FONT_NAME = Font.MONOSPACED;

    @Override
    public TitleLabel createTitleLabelWithProportion(String text, Dimension size, Optional<Pair<Double, Double>> moltiplicator, Optional<Color> fgColor, Optional<String> fontName) {
        return new TitleLabel(text, size, moltiplicator.orElse(DEFAULT_MOLTIPLICATOR), fgColor.orElse(DEFAULT_FOREGROUND_COLOR), fontName.orElse(DEFAULT_FONT_NAME));
    }

}
