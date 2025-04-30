package com.thelegendofbald.ui.settingsmenu.model;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

import com.thelegendofbald.ui.controller.ResizeListener;
import com.thelegendofbald.ui.settingsmenu.controller.CustomCheckBoxMouseListener;
import com.thelegendofbald.utils.ImageUtils;

public class CustomCheckBox extends JCheckBox {

    private static final String STARTING_ICON_PATH = "images/";
    private static final String CHECKED_ICON_PATH = STARTING_ICON_PATH + "buttons/checkbox_checked.png";
    private static final String UNCHECKED_ICON_PATH = STARTING_ICON_PATH + "buttons/checkbox_unchecked.png";

    private final ImageIcon checkedIconOriginal = new ImageIcon(this.getClass().getClassLoader().getResource(CHECKED_ICON_PATH));
    private final ImageIcon uncheckedIconOriginal = new ImageIcon(this.getClass().getClassLoader().getResource(UNCHECKED_ICON_PATH));
    private ImageIcon checkIconResized;
    private ImageIcon uncheckIconResized;

    private static final double HEIGHT_PROPORTION = 0.85;

    public CustomCheckBox() {
        super();
        this.setSelected(false);
        this.setOpaque(false);
        this.setHorizontalAlignment(CENTER);
        this.setVerticalAlignment(CENTER);
        this.addComponentListener(new ResizeListener(this::onResize));
        this.addMouseListener(new CustomCheckBoxMouseListener(this));
    }

    public void onResize() {
        if (this.getHeight() > 0) {
            int size = (int) (this.getHeight() * HEIGHT_PROPORTION);
            
            checkIconResized = ImageUtils.scaleImageIcon(checkedIconOriginal, size, size);
            uncheckIconResized = ImageUtils.scaleImageIcon(uncheckedIconOriginal, size, size);

            this.setIcon(uncheckIconResized);

            this.revalidate();
            this.repaint();
        }
    }

    public ImageIcon getCheckedIcon() {
        return checkIconResized;
    }
    public ImageIcon getUncheckedIcon() {
        return uncheckIconResized;
    }

}
