package com.thelegendofbald.ui.model;

import java.awt.Dimension;

import javax.swing.JButton;

import com.thelegendofbald.ui.api.JButtonFactory;
import com.thelegendofbald.ui.mainmenu.model.SquareButton;

public class JButtonFactoryImpl implements JButtonFactory {

    @Override
    public JButton createSquareButton(String text, Dimension parentSize) {
        return new SquareButton(text, parentSize);
    }


}
