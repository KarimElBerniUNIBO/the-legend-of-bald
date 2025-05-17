package com.thelegendofbald.view.mainmenu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.JPanel;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.api.panels.AdapterPanel;
import com.thelegendofbald.view.common.TextLabel;
import com.thelegendofbald.view.common.TextLabelFactoryImpl;

/**
 * Represents the main panel of the main menu.
 * This panel serves as the container for menu components, including the title
 * and central elements.
 */
public final class MainPanel extends AdapterPanel {

    private static final long serialVersionUID = 5314111965396464683L;

    private static final String TITLE_TEXT = "THE LEGEND OF BALD";
    private static final String TITLE_FONT_NAME = Font.SERIF;

    private static final Pair<Double, Double> TITLE_PROPORTION = Pair.of(1.0, 0.3);

    private final TextLabelFactoryImpl tlFactory = new TextLabelFactoryImpl();

    private Optional<TextLabel> titleLabel = Optional.empty();
    private Optional<JPanel> centerPanel = Optional.empty();

    /**
     * Constructs the main menu panel with a specified size.
     *
     * @param size The preferred dimensions of the panel.
     */
    public MainPanel(final Dimension size) {
        super(size);
        this.setPreferredSize(size);
        this.setLayout(new BorderLayout());

    }

    @Override
    protected void initializeComponents() {
        titleLabel = Optional.of(tlFactory.createTextLabelWithProportion(
                TITLE_TEXT,
                this.getSize(),
                Optional.of(TITLE_PROPORTION),
                Optional.empty(),
                Optional.of(TITLE_FONT_NAME)));
        centerPanel = Optional.of(new CenterPanel(this.getSize()));
        super.initializeComponents();
    }


    @Override
    public void addComponentsToPanel() {
        titleLabel.ifPresent(label -> this.add(label, BorderLayout.NORTH));
        centerPanel.ifPresent(panel -> this.add(panel, BorderLayout.CENTER));
        this.updateComponentsSize();
    }

    @Override
    public void updateComponentsSize() {
        Arrays.stream(this.getComponents()).forEach(component -> component.setPreferredSize(this.getSize()));
    }

}
