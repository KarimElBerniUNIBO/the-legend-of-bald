package com.thelegendofbald.ui.settingsmenu.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.thelegendofbald.ui.api.JButtonFactory;
import com.thelegendofbald.ui.mainmenu.api.InteractivePanel;
import com.thelegendofbald.ui.model.JButtonFactoryImpl;
import com.thelegendofbald.ui.settingsmenu.api.Buttons;
import com.thelegendofbald.ui.settingsmenu.api.SettingsEditorsManager;
import com.thelegendofbald.ui.settingsmenu.controller.SwitchToOtherSettingsEditorPanel;

final class CategoriesPanel extends JPanel implements InteractivePanel {

    private static final int HEIGHT_PROPORTION = 10;
    private static final double WIDTH_BUTTONS_PADDING = 0.1;

    private final JButtonFactory jbFactory = new JButtonFactoryImpl();
    private final List<JButton> buttons;

    private final SettingsEditorsManager sem;

    CategoriesPanel(Dimension size, SettingsEditorsManager sem) {
        this.buttons = this.getListOfButtons(size);
        this.sem = sem;

        this.setMaximumSize(new Dimension((int) size.getWidth(), (int) size.getHeight() / HEIGHT_PROPORTION));
        this.setOpaque(false);
        this.setLayout(new FlowLayout(FlowLayout.CENTER, (int) (size.getWidth() * WIDTH_BUTTONS_PADDING), 0));

        this.connectButtonsWithActionListeners();
        this.addButtonsToPanel();
    }

    @Override
    public List<JButton> getListOfButtons(Dimension size) {
        return Stream.iterate(0, i -> i <= Buttons.getMaxIndex(), i -> i + 1)
                .map(i -> jbFactory.createTrasparentButton(Buttons.getIndex(i).getName(), size,
                        Optional.of(Font.MONOSPACED), Optional.of(Color.WHITE), Optional.empty()))
                .toList();
    }

    @Override
    public void connectButtonsWithActionListeners() {
        Stream.iterate(0, i -> i < this.buttons.size(), i -> i + 1).forEach(i -> this.buttons.get(i)
                .addActionListener(new SwitchToOtherSettingsEditorPanel(sem, Buttons.getIndex(i))));
    }

    @Override
    public void addButtonsToPanel() {
        buttons.forEach(this::add);
    }

}
