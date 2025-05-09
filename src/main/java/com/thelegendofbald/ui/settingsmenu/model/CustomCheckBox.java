package com.thelegendofbald.ui.settingsmenu.model;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;

import com.thelegendofbald.ui.api.MenuView;
import com.thelegendofbald.ui.api.Resizable;
import com.thelegendofbald.ui.controller.ResizeListener;
import com.thelegendofbald.ui.controller.TemplateButtonMouseListener;
import com.thelegendofbald.utils.ImageUtils;

public class CustomCheckBox extends JCheckBox implements MenuView,Resizable {

    private static final String STARTING_ICON_PATH = "images/";
    private static final String CHECKED_ICON_PATH = STARTING_ICON_PATH + "buttons/checkbox_checked.png";
    private static final String UNCHECKED_ICON_PATH = STARTING_ICON_PATH + "buttons/checkbox_unchecked.png";

    private final ImageIcon checkedIconOriginal = new ImageIcon(this.getClass().getClassLoader().getResource(CHECKED_ICON_PATH));
    private final ImageIcon uncheckedIconOriginal = new ImageIcon(this.getClass().getClassLoader().getResource(UNCHECKED_ICON_PATH));

    private static final double HEIGHT_PROPORTION = 0.85;

    private boolean initialized = false;

    public CustomCheckBox() {
        super();
        this.setOpaque(false);
        this.setHorizontalAlignment(CENTER);
        this.setVerticalAlignment(CENTER);
        this.addComponentListener(new ResizeListener(this::onResize));
        this.addMouseListener(new TemplateButtonMouseListener(){});
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (!this.initialized) {
            this.initialized = true;
            //this.setPreferredSize(new Dimension((int) this.getParent().getHeight(), (int) this.getParent().getHeight()));
            SwingUtilities.invokeLater(this::initializeIcons);
        }
    }

    private void initializeIcons() {
        this.updateComponentsSize();
    }

    @Override
    public void onResize() {
        this.updateView();
    }

    @Override
    public void updateComponentsSize() {
        int size = this.getHeight() > 1 ? (int) (this.getHeight() * HEIGHT_PROPORTION) : 1;
        this.setIcon(ImageUtils.scaleImageIcon(uncheckedIconOriginal, size, size));
        this.setSelectedIcon(ImageUtils.scaleImageIcon(checkedIconOriginal, size, size));
    }

    @Override
    public void updateView() {
        this.updateComponentsSize();
        this.revalidate();
        this.repaint();
    }

    @Override
    public void addComponentsToPanel() {}

    @Override
    public void setPreferredSize(Dimension size) {
        super.setPreferredSize(size);
        SwingUtilities.invokeLater(this::updateView);
    }

}
