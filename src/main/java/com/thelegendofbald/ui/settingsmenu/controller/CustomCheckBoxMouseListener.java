package com.thelegendofbald.ui.settingsmenu.controller;

import java.awt.event.MouseEvent;

import com.thelegendofbald.ui.controller.TemplateButtonMouseListener;
import com.thelegendofbald.ui.settingsmenu.model.CustomCheckBox;

public class CustomCheckBoxMouseListener extends TemplateButtonMouseListener {

    private final CustomCheckBox checkBox;

    public CustomCheckBoxMouseListener(CustomCheckBox checkBox) {
        super();
        this.checkBox = checkBox;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        if (checkBox.isSelected()) {
            checkBox.setIcon(checkBox.getUncheckedIcon());
        } else {
            checkBox.setIcon(checkBox.getCheckedIcon());
        }
    }

}
