package com.thelegendofbald.ui.settingsmenu.view;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.thelegendofbald.ui.api.JButtonFactory;
import com.thelegendofbald.ui.model.JButtonFactoryImpl;
import com.thelegendofbald.ui.settingsmenu.api.Settings;
import com.thelegendofbald.ui.settingsmenu.api.SettingsEditor;
import com.thelegendofbald.ui.settingsmenu.api.SettingsEditorsManager;

class SettingsEditorPanel extends JPanel implements SettingsEditorsManager {

    private static final double WIDTH_PROPORTION = 0.7;
    private static final double HEIGHT_PROPORTION = 0.65;

    private static final String APPLY_BUTTON_TEXT = "Apply";
    private static final double APPLY_BUTTON_ARC_PROPORTION = 0.1;

    private final List<SettingsEditor> settingsEditors;
    private SettingsEditor actualSettingsEditor;

    private final JButtonFactory jbFactory = new JButtonFactoryImpl();
    private final JButton apply;

    private Dimension preferredSize;

    SettingsEditorPanel(Dimension size) {
        int width = (int) (size.getWidth() * WIDTH_PROPORTION);
        int height = (int) (size.getHeight() * HEIGHT_PROPORTION);
        this.preferredSize = new Dimension(width, height);
        this.settingsEditors = this.getSettingsEditors();
        this.actualSettingsEditor = this.settingsEditors.getFirst();

        this.apply = jbFactory.createRoundedButton(APPLY_BUTTON_TEXT, preferredSize, APPLY_BUTTON_ARC_PROPORTION,
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        this.setOpaque(false);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        this.add((JPanel) actualSettingsEditor);
        this.add(apply);
    }

    private List<SettingsEditor> getSettingsEditors() {
        Arrays.stream(Settings.values()).forEach(setting -> setting.setSettingsEditor(new SettingsEditorImpl(preferredSize, setting)));

        return Arrays.stream(Settings.values())
                .map(Settings::getSettingsEditor)
                .toList();
    }

    @Override
    public void changeSettingsEditorPanel(SettingsEditor settingsEditor) {
        if (!actualSettingsEditor.equals(settingsEditor)) {
            this.removeAll();
            this.add((JPanel) this.settingsEditors.get(this.settingsEditors.indexOf(settingsEditor)));
            this.actualSettingsEditor = settingsEditor;
            this.add(apply);
            this.revalidate();
            this.repaint();
        }
    }

    @Override
    public void setPreferredSize(Dimension size) {
        int width = (int) (size.getWidth() * WIDTH_PROPORTION);
        int height = (int) (size.getHeight() * HEIGHT_PROPORTION);
        this.preferredSize = new Dimension(width, height);
        this.settingsEditors.forEach(editor -> ((JPanel)editor).setPreferredSize(this.preferredSize));
        this.apply.setPreferredSize(this.preferredSize);
    }

}
