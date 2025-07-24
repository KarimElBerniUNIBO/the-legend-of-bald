package com.thelegendofbald.view.common;

import java.awt.Color;
import java.awt.Component;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.api.common.TextLabelFactory;

public class CustomComboBoxRenderer extends DefaultListCellRenderer {

    private static final double HEIGHT_PROPORTION = 0.2;
    private static final Pair<Double, Double> TEXT_MULTIPLIER = Pair.of(1.0, 2.0);

    private final TextLabelFactory tlFactory = new TextLabelFactoryImpl();
    private final CustomComboBox<?> comboBox;

    public CustomComboBoxRenderer(CustomComboBox<?> comboBox) {
        super();
        this.comboBox = comboBox;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        var textLabel = tlFactory.createTextLabelWithProportion(value.toString(), comboBox.getSize(),
                Optional.empty(), Optional.of(TEXT_MULTIPLIER), Optional.of(list.getForeground()),
                Optional.of(list.getFont().getName()));
        
        textLabel.setBorder(BorderFactory.createEmptyBorder((int) (comboBox.getHeight() * HEIGHT_PROPORTION), 0,
                (int) (comboBox.getHeight() * HEIGHT_PROPORTION), 0));

        if (isSelected) {
            textLabel.setBackground(list.getSelectionBackground());
            textLabel.setForeground(list.getSelectionForeground());
        } else {
            textLabel.setBackground(list.getBackground());
            textLabel.setForeground(list.getForeground());
        }

        textLabel.setOpaque(true);

        return textLabel;
    }

}
