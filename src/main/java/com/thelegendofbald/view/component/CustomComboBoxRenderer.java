package com.thelegendofbald.view.component;

import java.awt.Component;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.view.factory.TextLabelFactory;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * CustomComboBoxRenderer is a specialized renderer for JComboBox that provides
 * custom rendering of items in the combo box.
 * <p>
 * It uses a TextLabelFactory to create text labels with specific proportions
 * and styles, ensuring that the items are displayed clearly and consistently.
 * The renderer also handles selection states to differentiate between selected
 * and unselected items visually.
 * </p>
 *
 * @see CustomComboBox
 */
public final class CustomComboBoxRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = 1L;

    private static final double HEIGHT_PROPORTION = 0.2;
    private static final Pair<Double, Double> TEXT_MULTIPLIER = Pair.of(1.0, 2.0);

    private final transient TextLabelFactory tlFactory = new TextLabelFactoryImpl();
    private final CustomComboBox<?> comboBox;

    /**
     * Constructs a CustomComboBoxRenderer with the specified combo box.
     *
     * @param comboBox the combo box to be rendered
     */
    @SuppressFBWarnings(
        value = "EI2", 
        justification = "This constructor is designed to initialize the renderer with a CustomComboBox instance."
    )
    public CustomComboBoxRenderer(final CustomComboBox<?> comboBox) {
        super();
        this.comboBox = comboBox;
    }

    @Override
    public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index,
            final boolean isSelected,
            final boolean cellHasFocus) {
        final var textLabel = tlFactory.createTextLabelWithProportion(value.toString(), comboBox.getSize(),
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
