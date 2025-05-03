package com.thelegendofbald.ui.mainmenu.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Optional;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.math3.fraction.Fraction;

import com.thelegendofbald.ui.api.AdapterPanel;
import com.thelegendofbald.ui.mainmenu.model.TitleLabelFactoryImpl;

/**
 * Represents the main panel of the main menu.
 * This panel serves as the container for menu components, including the title
 * and central elements.
 */
public class MainPanel extends AdapterPanel {

    private static final String TITLE_TEXT = "THE LEGEND OF BALD";
    private static final String TITLE_FONT_NAME = Font.SERIF;
    private final int FONT_WIDTH_PROPORTION;
    private final int FONT_HEIGHT_PROPORTION;
    private final Dimension FONT_PROPORTION;

    private final TitleLabelFactoryImpl tlFactory = new TitleLabelFactoryImpl();

    private JLabel titleLabel;
    private JPanel centerPanel;
    private JPanel leftSidePanel;
    private JPanel rightSidePanel;

    /**
     * Constructs the main menu panel with a specified size.
     *
     * @param size The preferred dimensions of the panel.
     */
    public MainPanel(final Dimension size) {
        super(size);
        Fraction proportion = new Fraction(size.getWidth() / size.getHeight());
        FONT_WIDTH_PROPORTION = proportion.getDenominator();
        FONT_HEIGHT_PROPORTION = proportion.getNumerator();
        FONT_PROPORTION = new Dimension(FONT_WIDTH_PROPORTION, FONT_HEIGHT_PROPORTION);

        this.setPreferredSize(size);
        this.setBackground(Color.BLACK);
        this.setLayout(new BorderLayout());
        this.initializeComponents();
    }

    private void initializeComponents() {
        titleLabel = tlFactory.createTitleLabelWithProportion(
                TITLE_TEXT,
                this.getSize(),
                FONT_PROPORTION,
                Optional.empty(),
                Optional.of(TITLE_FONT_NAME));
        centerPanel = new CenterPanel(this.getSize());
        leftSidePanel = new SidePanel(this.getSize());
        rightSidePanel = new SidePanel(this.getSize());
    }

    public void updateSize(Dimension size) {
        this.titleLabel.setPreferredSize(size);
        this.centerPanel.setPreferredSize(size);
        this.leftSidePanel.setPreferredSize(size);
        this.rightSidePanel.setPreferredSize(size);
    }

    @Override
    public void addComponentsToPanel() {
        this.updateSize(this.getSize());

        this.add(titleLabel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(leftSidePanel, BorderLayout.EAST);
        this.add(rightSidePanel, BorderLayout.WEST);
        super.addComponentsToPanel();
    }

}
