package com.thelegendofbald.ui.mainmenu.api;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Optional;

import org.apache.commons.math3.util.Pair;

import com.thelegendofbald.ui.mainmenu.model.TitleLabel;

public interface TitleLabelFactory {

    TitleLabel createTitleLabelWithProportion(String text, Dimension size, Optional<Pair<Double, Double>> moltiplicator, Optional<Color> fgColor,
            Optional<String> fontName);

}