package com.thelegendofbald.ui.api;

import java.awt.Dimension;


public interface View {

    void display();

    void changeMainPanel(Panels panelEnum);

    Dimension getInternalSize();

    void setInternalSize(Dimension size);

    void update();

}