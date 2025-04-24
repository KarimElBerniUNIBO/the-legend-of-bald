package com.thelegendofbald.ui.mainmenu.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Optional;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.math3.fraction.Fraction;

import com.thelegendofbald.ui.mainmenu.model.TitleLabelFactoryImpl;

/**
 * Represents the main panel of the main menu.
 * This panel serves as the container for menu components, including the title
 * and central elements.
 */
public class MainPanel extends JPanel {

    private static final String TITLE_TEXT = "THE LEGEND OF BALD";
    private static final String TITLE_FONT_NAME = Font.SERIF;
    private final int FONT_WIDTH_PROPORTION;
    private final int FONT_HEIGHT_PROPORTION;
    private final Dimension FONT_PROPORTION;

    private final TitleLabelFactoryImpl tlFactory = new TitleLabelFactoryImpl();

    private final JLabel titleLabel;
    private final JPanel centerPanel;

    /**
     * Constructs the main menu panel with a specified size.
     *
     * @param size The preferred dimensions of the panel.
     */
    public MainPanel(final Dimension size) {
        Fraction proportion = new Fraction(size.getWidth() / size.getHeight());
        FONT_WIDTH_PROPORTION = proportion.getDenominator();
        FONT_HEIGHT_PROPORTION = proportion.getNumerator();
        FONT_PROPORTION = new Dimension(FONT_WIDTH_PROPORTION, FONT_HEIGHT_PROPORTION);

        this.setPreferredSize(size);
        this.setBackground(Color.BLACK);
        this.setLayout(new BorderLayout());

        titleLabel = tlFactory.createTitleLabelWithProportion(
                TITLE_TEXT,
                size,
                FONT_PROPORTION,
                Optional.empty(),
                Optional.of(TITLE_FONT_NAME));
        centerPanel = new CenterPanel(size);

        this.add(titleLabel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(new SidePanel(size), BorderLayout.EAST);
        this.add(new SidePanel(size), BorderLayout.WEST);
    }

}
