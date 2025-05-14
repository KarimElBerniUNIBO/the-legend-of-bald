package com.thelegendofbald.view.common;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.api.common.TitleLabelFactory;

/**
 * Implementation of the {@link TitleLabelFactory} interface.
 * <p>
 * This factory is responsible for creating instances of {@link TitleLabel} with customizable
 * properties such as text, size, scaling multipliers, foreground color, and font name.
 * Default values are provided for the scaling multipliers, foreground color, and font name
 * if not specified.
 * </p>
 *
 * <p>
 * Default values:
 * <ul>
 *   <li>Scaling multipliers: (1.0, 1.0)</li>
 *   <li>Foreground color: {@link Color#WHITE}</li>
 *   <li>Font name: {@link Font#MONOSPACED}</li>
 * </ul>
 * </p>
 *
 * @see TitleLabel
 * @see TitleLabelFactory
 */
public final class TitleLabelFactoryImpl implements TitleLabelFactory {

    private static final Pair<Double, Double> DEFAULT_MOLTIPLICATOR = Pair.of(1.0, 1.0);
    private static final Color DEFAULT_FOREGROUND_COLOR = Color.WHITE;
    private static final String DEFAULT_FONT_NAME = Font.MONOSPACED;

    @Override
    public TitleLabel createTitleLabelWithProportion(final String text, final Dimension size,
            final Optional<Pair<Double, Double>> moltiplicator, final Optional<Color> fgColor, final Optional<String> fontName) {
        return new TitleLabel(text, size, moltiplicator.orElse(DEFAULT_MOLTIPLICATOR),
                fgColor.orElse(DEFAULT_FOREGROUND_COLOR), fontName.orElse(DEFAULT_FONT_NAME));
    }

}
