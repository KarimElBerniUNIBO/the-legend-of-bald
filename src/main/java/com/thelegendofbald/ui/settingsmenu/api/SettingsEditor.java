package com.thelegendofbald.ui.settingsmenu.api;

import java.util.List;

import com.thelegendofbald.ui.settingsmenu.model.ConfigPanel;

public interface SettingsEditor {

    void onResize();

    List<ConfigPanel> getConfigsPanels();

    void addComponentsToPanel();

}