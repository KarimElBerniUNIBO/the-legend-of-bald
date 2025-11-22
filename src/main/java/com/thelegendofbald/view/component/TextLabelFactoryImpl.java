package com.thelegendofbald.view.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.view.factory.TextLabelFactory;

/**
 * Implementation of the {@link TextLabelFactory} interface.
 * <p>
 * This factory is responsible for creating instances of {@link TextLabel} with
 * customizable
 * properties such as text, size, scaling multipliers, foreground color, and
 * font name.
 * Default values are provided for the scaling multipliers, foreground color,
 * and font name
 * if not specified.
 * </p>
 *
 * <p>
 * Default values:
 * <ul>
 * <li>Scaling multipliers: (1.0, 1.0)</li>
 * <li>Foreground color: {@link Color#WHITE}</li>
 * <li>Font name: {@link Font#MONOSPACED}</li>
 * </ul>
 * </p>
 *
 * @see TextLabel
 * @see TextLabelFactory
 */
public final class TextLabelFactoryImpl implements TextLabelFactory {

    private static final Pair<Double, Double> DEFAULT_PROPORTION = Pair.of(1.0, 1.0);
    private static final Pair<Double, Double> DEFAULT_MOLTIPLICATOR = Pair.of(1.0, 1.0);
    private static final Color DEFAULT_FOREGROUND_COLOR = Color.WHITE;
    private static final String DEFAULT_FONT_NAME = Font.MONOSPACED;

    @Override
    public TextLabel createTextLabelWithProportion(final String text, final Dimension size,
            final Optional<Pair<Double, Double>> proportion, final Optional<Pair<Double, Double>> moltiplicator,
            final Optional<Color> fgColor, final Optional<String> fontName) {
        return new TextLabel(text, size, proportion.orElse(DEFAULT_PROPORTION),
                moltiplicator.orElse(DEFAULT_MOLTIPLICATOR),
                fgColor.orElse(DEFAULT_FOREGROUND_COLOR), fontName.orElse(DEFAULT_FONT_NAME));
    }

}
