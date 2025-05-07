package com.thelegendofbald.ui.mainmenu.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.JPanel;

import org.apache.commons.math3.util.Pair;

import com.thelegendofbald.ui.api.AdapterPanel;
import com.thelegendofbald.ui.mainmenu.model.TitleLabel;
import com.thelegendofbald.ui.mainmenu.model.TitleLabelFactoryImpl;

/**
 * Represents the main panel of the main menu.
 * This panel serves as the container for menu components, including the title
 * and central elements.
 */
public class MainPanel extends AdapterPanel {

    private static final String TITLE_TEXT = "THE LEGEND OF BALD";
    private static final String TITLE_FONT_NAME = Font.SERIF;

    private final TitleLabelFactoryImpl tlFactory = new TitleLabelFactoryImpl();

    private TitleLabel titleLabel;
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

        this.setPreferredSize(size);
        this.setBackground(Color.BLACK);
        this.setLayout(new BorderLayout());
    }

    @Override
    protected void initializeComponents() {
        titleLabel = tlFactory.createTitleLabelWithProportion(
                TITLE_TEXT,
                this.getSize(),
                Optional.of(new Pair<>(1.0, 0.3)),
                Optional.empty(),
                Optional.of(TITLE_FONT_NAME));
        centerPanel = new CenterPanel(this.getSize());
        leftSidePanel = new SidePanel(this.getSize(), new Pair<>(3.5, 1.0));
        rightSidePanel = new SidePanel(this.getSize(), new Pair<>(3.5, 1.0));
    }

    private void updateSize(Dimension size) {
        Arrays.stream(this.getComponents()).forEach(component -> component.setPreferredSize(size));
        /*this.titleLabel.setPreferredSize(size);
        this.centerPanel.setPreferredSize(size);
        this.leftSidePanel.setPreferredSize(size);
        this.rightSidePanel.setPreferredSize(size);*/
    }

    @Override
    protected void addComponentsToPanel() {
        this.add(titleLabel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(leftSidePanel, BorderLayout.EAST);
        this.add(rightSidePanel, BorderLayout.WEST);
        this.updateSize(this.getSize());
    }

}
