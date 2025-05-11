package com.thelegendofbald.ui.view;

import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.thelegendofbald.ui.api.Panels;
import com.thelegendofbald.ui.api.View;
import com.thelegendofbald.ui.api.MainView;
import com.thelegendofbald.ui.leaderboard.view.LeaderBoardPanel;
import com.thelegendofbald.ui.mainmenu.view.MainPanel;
import com.thelegendofbald.ui.settingsmenu.view.SettingsPanel;

public class GameWindow extends JFrame implements View, MainView {

    private static final String TITLE = "The Legend of Bald";
    private static Dimension internalSize = new Dimension(1280, 704);

    private final List<JPanel> panels = new LinkedList<>();

    public GameWindow() {
        this.panels.add(new MainPanel(internalSize));
        this.panels.add(new SettingsPanel(internalSize));
        this.panels.add(new LeaderBoardPanel(internalSize));
        this.panels.add(new GamePanel(internalSize, this)); 

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
    public Dimension getInternalSize() {
        return internalSize;
    }

    @Override
    public void setInternalSize(Dimension size) {
        GameWindow.internalSize = size;
    }

    @Override
    public void updateView() {
        SwingUtilities.invokeLater(() -> {
            this.revalidate();
            this.repaint();
        });
    }

}
