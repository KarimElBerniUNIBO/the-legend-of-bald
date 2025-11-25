package com.thelegendofbald.view.panel.settings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.view.factory.JButtonFactory;
import com.thelegendofbald.view.panel.base.AdapterPanel;
import com.thelegendofbald.view.panel.base.InteractivePanel;
import com.thelegendofbald.model.config.Settings;
import com.thelegendofbald.model.config.SettingsEditorsManager;
import com.thelegendofbald.controller.navigation.SwitchToOtherSettingsEditorPanel;
import com.thelegendofbald.view.component.JButtonFactoryImpl;
import com.thelegendofbald.view.component.TrasparentBackgroundButton;

final class CategoriesPanel extends AdapterPanel implements InteractivePanel {

    private static final long serialVersionUID = 1L;

    private static final double HEIGHT_PROPORTION = 0.1;
    private static final double WIDTH_BUTTONS_PADDING = 0.05;

    private static final Pair<Double, Double> BUTTON_PROPORTION = Pair.of(1.0, 2.5);

    private final transient JButtonFactory jbFactory = new JButtonFactoryImpl();
    private final List<JButton> buttons;

    private final SettingsEditorsManager sem;

    CategoriesPanel(final SettingsEditorsManager sem) {
        super();
        this.sem = sem;
        this.buttons = this.getListOfButtons();
        ((TrasparentBackgroundButton) this.buttons.getFirst()).select();
        this.setOpaque(false);
        this.linkJButtonsToButtonsEnum();
        this.connectButtonsWithActionListeners();
    }

    @Override
    protected void initializeComponents() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, (int) (this.getWidth() * WIDTH_BUTTONS_PADDING), 0));
    }

    private void linkJButtonsToButtonsEnum() {
        this.buttons.stream().forEach(jb -> Settings.getSettingByIndex(this.buttons.indexOf(jb)).setLinkedButton(jb));
    }

    private List<JButton> getListOfButtons() {
        return Stream.iterate(0, i -> i <= Settings.getMaxIndex(), i -> i + 1)
            .map(i -> jbFactory.createTrasparentButton(Settings.getSettingByIndex(i).getName(),
                Optional.of(BUTTON_PROPORTION),
                Optional.of(Font.MONOSPACED), Optional.of(Color.WHITE), Optional.empty()))
            .map(JButton.class::cast)
            .toList();
    }
    /*
     * Suppresses the unchecked cast warning because the
     * buttonFactory.createTrasparentButton method
     * returns a TrasparentBackgroundButton, which is a subclass of JButton. The cast is
     * necessary to maintain compatibility with the List<JButton> type used in this
     * class.
     */

    private void connectButtonsWithActionListeners() {
        Stream.iterate(0, i -> i + 1).limit(this.buttons.size())
                .forEach(i -> this.buttons.get(i)
                        .addActionListener(new SwitchToOtherSettingsEditorPanel(sem, Settings.getSettingByIndex(i))));
    }

    @Override
    public void unselectAllButtons() {
        this.buttons.stream()
                .map(jbutton -> (TrasparentBackgroundButton) jbutton)
                .forEach(TrasparentBackgroundButton::unselect);
    }

    private void updateLayout() {
        final var layout = (FlowLayout) this.getLayout();
        layout.setHgap((int) (this.getWidth() * WIDTH_BUTTONS_PADDING));
    }

    @Override
    public void setPreferredSize(final Dimension size) {
        final var preferredSize = new Dimension((int) size.getWidth(), (int) (size.getHeight() * HEIGHT_PROPORTION));
        super.setPreferredSize(preferredSize);
        SwingUtilities.invokeLater(this::updateView);
    }

    @Override
    public void updateComponentsSize() {
        this.updateLayout();
    }

    @Override
    public void addComponentsToPanel() {
        this.updateComponentsSize();
        buttons.forEach(this::add);
    }

}
