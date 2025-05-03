package com.thelegendofbald.ui.mainmenu.model;

import java.awt.Color;
import java.awt.Dimension;
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
    private static final double IMAGE_PROPORTION = 0.05;

    private final JButtonFactory jbFactory = new JButtonFactoryImpl();
    private JButton backButton;

    private Optional<ImageIcon> originalImage = Optional.empty();

    public BackToMainPanel(Dimension size) {
        super(new Dimension((int) (size.getWidth() * IMAGE_PROPORTION), (int) (size.getHeight() * IMAGE_PROPORTION)));

        this.setOpaque(false);
        this.initializeComponents(size);

        backButton.addActionListener(e -> {
                GameWindow parent = (GameWindow) SwingUtilities.getWindowAncestor(this);
                new SwitchToOtherPanel(parent, Panels.MAIN_MENU).actionPerformed(e);
            });
    }

    private void initializeComponents(Dimension size) {
        int width = (int) (size.getWidth() * IMAGE_PROPORTION);
        int height = (int) (size.getHeight() * IMAGE_PROPORTION);
        Fraction imageProportion = new Fraction(width / height);
        this.originalImage = Optional.of(new ImageIcon(this.getClass().getResource(PATH)));

        this.originalImage.ifPresent(image -> {
            var imageResized = ImageUtils.scaleImageIcon(image, width, height);
            backButton = jbFactory.createTrasparentButton(imageResized, new Dimension(imageProportion.getNumerator(), imageProportion.getDenominator()), Optional.of(Color.WHITE));
        });
    }

    private void updateSize() {
        int panelWidth = (int) (this.getWidth());
        int panelHeight = (int) (this.getHeight());
        this.originalImage.ifPresent(image -> {
            int originalWidth = image.getIconWidth();
            int originalHeight = image.getIconHeight();
    
            double aspectRatio = (double) originalWidth / originalHeight;

            int newWidth = (int) (panelHeight * aspectRatio);
            int newHeight = panelHeight;
    
            if (newWidth > panelWidth) {
                newWidth = panelWidth;
                newHeight = (int) (panelWidth / aspectRatio);
            }

            var imageResized = ImageUtils.scaleImageIcon(image, newWidth, newHeight);
            backButton.setIcon(imageResized);
        });
    }

    @Override
    protected void addComponentsToPanel() {
        this.updateSize();
        this.add(backButton);
    }

}
