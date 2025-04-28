package com.thelegendofbald.ui.api;

import java.awt.Dimension;


public interface View {

    void display();

    void changeMainPanel(Panels panelEnum);

    Dimension getSize();

    void setSize(Dimension size);

    void update();

}