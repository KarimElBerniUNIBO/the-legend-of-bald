package com.thelegendofbald.ui.mainmenu.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Represents the main panel of the main menu.
 * This panel serves as the container for menu components, including the title
 * and central elements.
 */
public class MenuPanel extends JPanel {

    private static final String TITLE_TEXT = "THE LEGEND OF BALD";
    private static final String TITLE_FONT_NAME = Font.SERIF;

    private final JLabel titleLabel;
    private final JPanel centerPanel;

    /**
     * Constructs the main menu panel with a specified size.
     *
     * @param size The preferred dimensions of the panel.
     */
    public MenuPanel(final Dimension size) {
        this.setPreferredSize(size);
        this.setBackground(Color.BLACK);
        this.setLayout(new BorderLayout());

        titleLabel = new TitleLabel(
                TITLE_TEXT,
                size,
                Color.WHITE,
                TITLE_FONT_NAME);
        centerPanel = new CenterPanel(size);

        this.add(titleLabel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(new SidePanel(size), BorderLayout.EAST);
        this.add(new SidePanel(size), BorderLayout.WEST);
    }

}
