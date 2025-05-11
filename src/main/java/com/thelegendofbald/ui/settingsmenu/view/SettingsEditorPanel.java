package com.thelegendofbald.ui.settingsmenu.view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import com.thelegendofbald.ui.api.AdapterPanel;
import com.thelegendofbald.ui.api.GridBagConstraintsFactory;
import com.thelegendofbald.ui.api.JButtonFactory;
import com.thelegendofbald.ui.mainmenu.model.BackToMainPanel;
import com.thelegendofbald.ui.model.GridBagConstraintsFactoryImpl;
import com.thelegendofbald.ui.model.JButtonFactoryImpl;
import com.thelegendofbald.ui.settingsmenu.api.Settings;
import com.thelegendofbald.ui.settingsmenu.api.SettingsEditorsManager;
import com.thelegendofbald.ui.settingsmenu.model.SettingsEditor;

class SettingsEditorPanel extends AdapterPanel implements SettingsEditorsManager {

    private static final double WIDTH_PROPORTION = 0.7;
    private static final double HEIGHT_PROPORTION = 0.65;

    private static final double BOTTOM_INSETS = 0.05;
    private static final double SIDE_INSETS = 0.2;

    private static final double SETTINGS_EDITOR_WEIGHTY = 0.9;
    private static final double APPLY_BUTTON_WEIGHTY = 0.1;

    private static final String APPLY_BUTTON_TEXT = "Apply";
    private static final double APPLY_BUTTON_ARC_PROPORTION = 0.1;

    private final List<SettingsEditor> settingsEditors = new ArrayList<>();
    private Optional<SettingsEditor> actualSettingsEditor = Optional.empty();

    private final GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = this.gbcFactory.createBothGridBagConstraints();

    private final JButtonFactory jbFactory = new JButtonFactoryImpl();
    private Optional<JButton> apply = Optional.empty();

    SettingsEditorPanel(final Dimension size) {
        super(size);
        this.setOpaque(false);
        this.setLayout(new GridBagLayout());
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (this.settingsEditors.isEmpty()) {
            this.initializeComponents();
            this.addComponentsToPanel();
        }
    }

    private List<SettingsEditor> getSettingsEditors(final Dimension size) {
        Arrays.stream(Settings.values())
                .forEach(setting -> setting.setSettingsEditor(new SettingsEditor(size, setting)));

        return Arrays.stream(Settings.values())
                .map(Settings::getSettingsEditor)
                .toList();
    }

    @Override
    protected void initializeComponents() {
        final var preferredSize = this.calculatePreferredSize(this.getParent().getSize());
        this.settingsEditors.addAll(this.getSettingsEditors(preferredSize));
        this.actualSettingsEditor = Optional.of(this.settingsEditors.getFirst());
        this.apply = Optional.of(jbFactory.createRoundedButton(APPLY_BUTTON_TEXT, preferredSize, Optional.empty(),
                APPLY_BUTTON_ARC_PROPORTION,
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
    }

    private Dimension calculatePreferredSize(final Dimension parentSize) {
        final var width = (int) (parentSize.getWidth() * WIDTH_PROPORTION);
        final var height = (int) (parentSize.getHeight() * HEIGHT_PROPORTION);
        return new Dimension(width, height);
    }

    @Override
    public void addComponentsToPanel() {
        Optional.ofNullable(this.getParent())
                .map(Container::getSize)
                .ifPresent(parentSize -> {
                    this.updateComponentsSize();
                    this.gbc.gridx = 0;
                    this.gbc.gridy = 0;
                    this.gbc.weighty = SETTINGS_EDITOR_WEIGHTY;
                    this.actualSettingsEditor.ifPresent(se -> this.add(se, gbc));

                    this.gbc.gridy = 1;
                    this.gbc.weighty = APPLY_BUTTON_WEIGHTY;
                    this.apply.ifPresent(b -> this.add(b, gbc));

                    this.revalidate();
                    this.repaint();
                });
    }

    @Override
    public void changeSettingsEditorPanel(final SettingsEditor settingsEditor) {
        if (!actualSettingsEditor.map(se -> se.equals(settingsEditor)).orElse(false)) {
            this.removeAll();
            this.actualSettingsEditor = Optional.of(settingsEditor);
            this.addComponentsToPanel();
        }
    }

    @Override
    public void setPreferredSize(final Dimension size) {
        super.setPreferredSize(size);
        SwingUtilities.invokeLater(() -> {
            this.removeAll();
            this.addComponentsToPanel();
        });
    }

    @Override
    public void updateComponentsSize() {
        final var preferredSize = this.calculatePreferredSize(this.getSize());
        this.settingsEditors.forEach(editor -> ((SettingsEditor) editor).setPreferredSize(preferredSize));
        this.gbc.insets.set(0, (int) ((this.getWidth() * SIDE_INSETS) - (this.getWidth() * BackToMainPanel.WIDTH_PROPORTION)),
                (int) (this.getHeight() * BOTTOM_INSETS),
                (int) (this.getWidth() * SIDE_INSETS));
    }

}
