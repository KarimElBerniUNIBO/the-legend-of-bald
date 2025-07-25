package com.thelegendofbald.view.settingsmenu;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.swing.SwingUtilities;

import com.thelegendofbald.api.common.GridBagConstraintsFactory;
import com.thelegendofbald.api.panels.AdapterPanel;
import com.thelegendofbald.api.settingsmenu.Settings;
import com.thelegendofbald.api.settingsmenu.SettingsEditorsManager;
import com.thelegendofbald.view.common.BackToPreviousPanel;
import com.thelegendofbald.view.constraints.GridBagConstraintsFactoryImpl;

final class SettingsEditorManagerPanel extends AdapterPanel implements SettingsEditorsManager {

    private static final long serialVersionUID = 1L;

    private static final double WIDTH_PROPORTION = 0.7;
    private static final double HEIGHT_PROPORTION = 0.65;

    private static final double BOTTOM_INSETS = 0.05;
    private static final double SIDE_INSETS = 0.2;

    private final List<SettingsEditor> settingsEditors = new ArrayList<>();
    private transient Optional<SettingsEditor> actualSettingsEditor = Optional.empty();

    private final transient GridBagConstraintsFactory gbcFactory = new GridBagConstraintsFactoryImpl();
    private final GridBagConstraints gbc = this.gbcFactory.createBothGridBagConstraints();

    SettingsEditorManagerPanel() {
        super();
        this.initialize();
    }

    private void initialize() {
        SwingUtilities.invokeLater(() -> {
            this.setOpaque(false);
            this.setLayout(new GridBagLayout());
        });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (this.settingsEditors.isEmpty()) {
            this.initializeComponents();
            this.addComponentsToPanel();
        }
    }

    private List<SettingsEditor> getSettingsEditors() {
        Arrays.stream(Settings.values())
                .forEach(setting -> setting.setSettingsEditor(new SettingsEditor(setting)));

        return Arrays.stream(Settings.values())
                .map(Settings::getSettingsEditor)
                .toList();
    }

    @Override
    protected void initializeComponents() {
        this.settingsEditors.addAll(this.getSettingsEditors());
        this.actualSettingsEditor = Optional.of(this.settingsEditors.getFirst());
        super.initializeComponents();
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
                    this.actualSettingsEditor.ifPresent(se -> this.add(se, gbc));
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
        this.settingsEditors.forEach(editor -> editor.setPreferredSize(preferredSize));
        this.gbc.insets.set(0,
                (int) ((this.getWidth() * SIDE_INSETS) - (this.getWidth() * BackToPreviousPanel.WIDTH_PROPORTION)),
                (int) (this.getHeight() * BOTTOM_INSETS),
                (int) (this.getWidth() * SIDE_INSETS));
    }

}
