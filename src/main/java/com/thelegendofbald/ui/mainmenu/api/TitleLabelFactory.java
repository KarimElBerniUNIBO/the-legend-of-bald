package com.thelegendofbald.ui.mainmenu.api;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Optional;

import javax.swing.JLabel;

public interface TitleLabelFactory {

    JLabel createTitleLabelWithProportion(String text, Dimension size, Dimension proportion, Optional<Color> fgColor,
            Optional<String> fontName);

}