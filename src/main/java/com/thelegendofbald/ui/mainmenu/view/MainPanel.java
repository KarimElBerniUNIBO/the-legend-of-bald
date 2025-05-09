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

    /**
     * Constructs the main menu panel with a specified size.
     *
     * @param size The preferred dimensions of the panel.
     */
    public MainPanel(final Dimension size) {
        super(size);

        this.setPreferredSize(size);
        this.setOpaque(true);
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
    }


    @Override
    public void addComponentsToPanel() {
        this.add(titleLabel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.updateComponentsSize();
    }

    @Override
    public void updateComponentsSize() {
        Arrays.stream(this.getComponents()).forEach(component -> component.setPreferredSize(this.getSize()));
    }

}
