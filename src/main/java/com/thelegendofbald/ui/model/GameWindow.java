package com.thelegendofbald.ui.model;

import javax.swing.*;

import com.thelegendofbald.ui.api.View;

import java.awt.*;

public class GameWindow extends JFrame implements View {

    private static final String TITLE = "The Legend of Bald";
    private static final Dimension size = new Dimension(800, 600);

    private final JPanel gamePanel;

    public GameWindow() {
        this.gamePanel = new GamePanel(size);
        this.add(this.gamePanel);
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
