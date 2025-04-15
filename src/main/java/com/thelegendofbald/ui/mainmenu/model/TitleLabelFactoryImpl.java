package com.thelegendofbald.ui.mainmenu.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Optional;

import javax.swing.JLabel;

import com.thelegendofbald.ui.mainmenu.api.TitleLabelFactory;

public class TitleLabelFactoryImpl implements TitleLabelFactory {

    private static final Color DEFAULT_FOREGROUND_COLOR = Color.WHITE;
    private static final String DEFAULT_FONT_NAME = Font.MONOSPACED;

    @Override
    public JLabel createTitleLabelWithProportion(String text, Dimension size, Dimension proportion, Optional<Color> fgColor, Optional<String> fontName) {
        return new TitleLabel(text, size, fgColor.orElse(DEFAULT_FOREGROUND_COLOR), fontName.orElse(DEFAULT_FONT_NAME), proportion);
    }

}
