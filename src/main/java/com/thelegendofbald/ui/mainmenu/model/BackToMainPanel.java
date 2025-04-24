package com.thelegendofbald.ui.mainmenu.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.math3.fraction.Fraction;

import com.thelegendofbald.ui.api.JButtonFactory;
import com.thelegendofbald.ui.api.Panels;
import com.thelegendofbald.ui.mainmenu.controller.SwitchToOtherPanel;
import com.thelegendofbald.ui.model.JButtonFactoryImpl;
import com.thelegendofbald.ui.model.TrasparentBackgroundButton;
import com.thelegendofbald.ui.view.GameWindow;

public class BackToMainPanel extends JPanel {

    private static final String PATH = "/images/buttons/back.png";
    private static final double IMAGE_PROPORTION = 0.025;

    private final Fraction SCREEN_PROPORTION;
    private final int PANEL_WIDTH;
    private final int PANEL_HEIGHT;

    private final JButtonFactory jbFactory = new JButtonFactoryImpl();
    private final JButton backButton;

    public BackToMainPanel(Dimension size) {
        SCREEN_PROPORTION = new Fraction(size.getWidth() / size.getHeight());
        PANEL_WIDTH = (int) size.getWidth();
        PANEL_HEIGHT = (int) (size.getHeight() * SCREEN_PROPORTION.getNumerator() * IMAGE_PROPORTION);

        this.setMaximumSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setOpaque(false);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        ImageIcon originalImage = new ImageIcon(this.getClass().getResource(PATH));
        Image imageResized = originalImage.getImage().getScaledInstance(
                (int) (size.getWidth() * SCREEN_PROPORTION.getDenominator() * IMAGE_PROPORTION),
                PANEL_HEIGHT,
                Image.SCALE_SMOOTH);

        backButton = jbFactory.createTrasparentButton(new ImageIcon(imageResized), new Dimension(PANEL_WIDTH, PANEL_HEIGHT), Optional.of(Color.WHITE));
        backButton.addActionListener(e -> {
                GameWindow parent = (GameWindow) SwingUtilities.getWindowAncestor(this);
                new SwitchToOtherPanel(parent, Panels.MAIN_MENU).actionPerformed(e);
            });

        this.add(backButton);

    }

}
