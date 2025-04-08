package com.thelegendofbald.ui.model;

import javax.swing.*;

import com.thelegendofbald.ui.api.View;

import java.awt.*;

public class GameWindow extends JFrame implements View {

    private static final String TITLE = "The Legend of Bald";
    private static final Dimension size = new Dimension(900, 600);

    private final JPanel map;
    public GameWindow() {
        this.map = new GamePanel(size,"/images/examplemap.jpg");
        this.add(map);
    }

    @Override
    public void display() {
        this.setTitle(TITLE);
        this.setResizable(false);
        this.pack();
        this.setLocationByPlatform(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
