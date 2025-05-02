package com.thelegendofbald.ui.settingsmenu.api;

import javax.swing.JComponent;

public interface SettingOption {

    String getText();

    JComponent getJcomponent();

    static int getSize() {
        return (int) Settings.getMaxIndex();
    }

}
