package com.thelegendofbald.ui.view;

import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.thelegendofbald.ui.api.Panels;
import com.thelegendofbald.ui.api.View;
import com.thelegendofbald.ui.leaderboard.view.LeaderBoardPanel;
import com.thelegendofbald.ui.mainmenu.view.MainPanel;
import com.thelegendofbald.ui.settingsmenu.view.SettingsPanel;

public class GameWindow extends JFrame implements View {

    private static final String TITLE = "The Legend of Bald";
    private Dimension size = new Dimension(900, 600);

    private final List<JPanel> panels = new LinkedList<>();
    
    public GameWindow() {
        this.panels.add(new MainPanel(size));
        this.panels.add(new SettingsPanel(size));
        this.panels.add(new LeaderBoardPanel(size));
        this.panels.add(new GamePanel(size,"/images/examplemap.jpg"));

        this.setContentPane(this.panels.getFirst());
    }

    @Override
    public void display() {
        this.setTitle(TITLE);
        this.setResizable(true);
        this.pack();
        this.setLocationByPlatform(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void changeMainPanel(Panels panelEnum) {
        JPanel panel = this.panels.get(panelEnum.getIndex());
        this.setContentPane(panel);
        //this.pack();
        this.revalidate();
        this.repaint();
        panel.requestFocusInWindow();
    }

    @Override
    public Dimension getSize() {
        return size;
    }

    @Override
    public void setSize(Dimension size) {
        this.size = size;
    }

    @Override
    public void update() {
        SwingUtilities.invokeLater(() -> {
            this.revalidate();
            this.repaint();
        });
    }

}
