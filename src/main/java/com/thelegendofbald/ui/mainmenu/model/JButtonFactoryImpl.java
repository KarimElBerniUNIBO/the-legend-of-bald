package com.thelegendofbald.ui.mainmenu.model;

import java.awt.Dimension;

import javax.swing.JButton;

import com.thelegendofbald.ui.mainmenu.api.JButtonFactory;
import com.thelegendofbald.ui.mainmenu.api.SquareButton;

public class JButtonFactoryImpl implements JButtonFactory {

    @Override
    public JButton createSquareButton(String text, Dimension parentSize) {
        return new SquareButton(text, parentSize);
    }


}
