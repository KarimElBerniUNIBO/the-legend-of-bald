package com.thelegendofbald.ui.mainmenu.api;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JButton;

public interface InteractivePanel {

    List<JButton> getListOfButtons(Dimension size);

    void connectButtonsWithActionListeners();

    void addButtonsToPanel();

    void unselectAllButtons();

}