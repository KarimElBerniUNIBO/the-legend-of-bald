package com.thelegendofbald.api.common;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.view.common.TextLabel;

/**
 * Factory interface for creating {@link TextLabel} instances with customizable properties.
 * <p>
 * Implementations of this interface are responsible for constructing {@code TitleLabel}
 * objects based on the provided parameters, allowing for flexible configuration of
 * label appearance and behavior.
 * </p>
 *
 * @author 
 */
public interface TextLabelFactory {

    /**
     * Creates a TextLabel with the specified text and size, allowing optional customization
     * of size proportion, foreground color, and font name.
     *
     * @param text           the text to display in the title label
     * @param size           the base dimension for the label
     * @param moltiplicator  an optional pair of doubles representing width and height multipliers for the size
     * @param fgColor        an optional foreground color for the label text
     * @param fontName       an optional font name to use for the label text
     * @return               a TextLabel instance configured with the provided parameters
     */
    TextLabel createTextLabelWithProportion(String text, Dimension size, Optional<Pair<Double, Double>> moltiplicator,
            Optional<Color> fgColor,
            Optional<String> fontName);

}
