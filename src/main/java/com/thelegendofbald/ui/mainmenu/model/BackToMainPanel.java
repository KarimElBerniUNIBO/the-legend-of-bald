package com.thelegendofbald.ui.mainmenu.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import org.apache.commons.math3.fraction.Fraction;

import com.thelegendofbald.ui.api.AdapterPanel;
import com.thelegendofbald.ui.api.JButtonFactory;
import com.thelegendofbald.ui.api.Panels;
import com.thelegendofbald.ui.mainmenu.controller.SwitchToOtherPanel;
import com.thelegendofbald.ui.model.JButtonFactoryImpl;
import com.thelegendofbald.ui.view.GameWindow;
import com.thelegendofbald.utils.ImageUtils;

public class BackToMainPanel extends AdapterPanel {

    private static final String PATH = "/images/buttons/back.png";
    private static final double IMAGE_PROPORTION = 0.25;

    private final JButtonFactory jbFactory = new JButtonFactoryImpl();
    private Optional<JButton> backButton = Optional.empty();

    private Optional<ImageIcon> originalImage = Optional.empty();

    public BackToMainPanel(Dimension size) {
        super(size);
        this.setOpaque(false);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
    }

    @Override
    protected void initializeComponents() {
        this.originalImage = Optional.of(new ImageIcon(this.getClass().getResource(PATH)));
        var imageProportion = new Fraction(this.getWidth() / this.getHeight());

        this.originalImage.ifPresent(image -> this.backButton = Optional.of(jbFactory.createTrasparentButton(this.getImageResized(image), new Dimension(imageProportion.getNumerator(), imageProportion.getDenominator()), Optional.empty(), Optional.of(Color.WHITE))));
        this.backButton.ifPresent(button -> button.addActionListener(e -> {
            GameWindow parent = (GameWindow) SwingUtilities.getWindowAncestor(this);
            new SwitchToOtherPanel(parent, Panels.MAIN_MENU).actionPerformed(e);
        }));
    }

    private void updateSize() {
        if (this.getWidth() > 0 && this.getHeight() > 0) {
            this.originalImage.ifPresent(image -> backButton.ifPresent(button -> button.setIcon(this.getImageResized(image))));
        }
    }

    private ImageIcon getImageResized(ImageIcon image) {
        var panelWidth = this.getWidth();
        var panelHeight = this.getHeight();

        int originalWidth = image.getIconWidth();
        int originalHeight = image.getIconHeight();

        double aspectRatio = originalWidth / originalHeight;

        int newWidth = (int) ((panelHeight * IMAGE_PROPORTION) * aspectRatio);
        int newHeight = (int) (panelHeight * IMAGE_PROPORTION);

        if (newWidth > panelWidth) {
            newWidth = (int) panelWidth;
            newHeight = (int) (panelWidth / aspectRatio);
        }

        if (newWidth == 0) newWidth = 1;
        if (newHeight == 0) newHeight = 1;

        return ImageUtils.scaleImageIcon(image, newWidth, newHeight);
    }

    @Override
    protected void addComponentsToPanel() {
        this.updateSize();
        backButton.ifPresent(this::add);
    }

    @Override
    public void setPreferredSize(Dimension windowSize) {
        super.setPreferredSize(new Dimension((int) (windowSize.getWidth() * 0.1), (int) (windowSize.getHeight() * 0.2)));
        this.updateSize();
    }

}
