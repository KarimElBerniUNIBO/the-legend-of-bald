package com.thelegendofbald.ui.settingsmenu.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.math3.util.Pair;

import com.thelegendofbald.ui.api.JButtonFactory;
import com.thelegendofbald.ui.mainmenu.api.InteractivePanel;
import com.thelegendofbald.ui.model.JButtonFactoryImpl;
import com.thelegendofbald.ui.model.TrasparentBackgroundButton;
import com.thelegendofbald.ui.settingsmenu.api.Settings;
import com.thelegendofbald.ui.settingsmenu.api.SettingsEditorsManager;
import com.thelegendofbald.ui.settingsmenu.controller.SwitchToOtherSettingsEditorPanel;

final class CategoriesPanel extends JPanel implements InteractivePanel {

    private static final double HEIGHT_PROPORTION = 0.1;
    private static final double WIDTH_BUTTONS_PADDING = 0.05;

    private final JButtonFactory jbFactory = new JButtonFactoryImpl();
    private final List<JButton> buttons = new LinkedList<>();

    private final SettingsEditorsManager sem;

    CategoriesPanel(Dimension size, SettingsEditorsManager sem) {
        this.sem = sem;
        this.setOpaque(false);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (this.buttons.isEmpty()) {
            this.buttons.addAll(this.getListOfButtons());
            this.linkJButtonsToButtonsEnum();
            this.connectButtonsWithActionListeners();
            SwingUtilities.invokeLater(() -> {
                this.setLayout(new FlowLayout(FlowLayout.CENTER, (int) (this.getWidth() * WIDTH_BUTTONS_PADDING), 0));
                ((TrasparentBackgroundButton) this.buttons.getFirst()).select();
                this.addButtonsToPanel();
                this.revalidate();
                this.repaint();
            });
        }
    }

    private void linkJButtonsToButtonsEnum() {
        this.buttons.stream().forEach(jb -> Settings.getSettingByIndex(this.buttons.indexOf(jb)).setLinkedButton(jb));
    }

    private List<JButton> getListOfButtons() {
        return Stream.iterate(0, i -> i <= Settings.getMaxIndex(), i -> i + 1)
                .map(i -> (JButton) jbFactory.createTrasparentButton(Settings.getSettingByIndex(i).getName(), this.getSize(), Optional.of(new Pair<>(1.0, 2.5)),
                        Optional.of(Font.MONOSPACED), Optional.of(Color.WHITE), Optional.empty()))
                .toList();
    }

    private void connectButtonsWithActionListeners() {
        Stream.iterate(0, i -> i < this.buttons.size(), i -> i + 1)
                .forEach(i -> this.buttons.get(i).addActionListener(new SwitchToOtherSettingsEditorPanel(sem, Settings.getSettingByIndex(i))));
    }

    private void addButtonsToPanel() {
        buttons.forEach(this::add);
    }

    @Override
    public void unselectAllButtons() {
        this.buttons.stream()
                .map(jbutton -> (TrasparentBackgroundButton) jbutton)
                .forEach(TrasparentBackgroundButton::unselect);
    }

    private void updateLayout() {
        var layout = (FlowLayout) this.getLayout();
        layout.setHgap((int) (this.getWidth() * WIDTH_BUTTONS_PADDING));
    }

    @Override
    public void setPreferredSize(Dimension size) {
        super.setPreferredSize(new Dimension((int) size.getWidth(), (int) (size.getHeight() * HEIGHT_PROPORTION)));
        SwingUtilities.invokeLater(this::updateLayout);
    }

}
