package com.thelegendofbald.view.buttons;

import java.awt.Color;
import java.awt.Font;
import java.util.Optional;

import javax.swing.ImageIcon;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.api.buttons.JButtonFactory;


/**
 * Implementation of the {@link JButtonFactory} interface that provides factory methods
 * for creating various types of custom buttons with configurable properties such as
 * text, icon, size, colors, font, and arc proportion.
 * <p>
 * This factory supplies default values for optional parameters, ensuring that buttons
 * are always created with valid and consistent appearance settings.
 * </p>
 * <ul>
 *   <li>{@code RectangleButton}: Standard rectangular button with customizable text or icon.</li>
 *   <li>{@code RoundedButton}: Button with rounded corners, customizable arc proportion, text or icon.</li>
 *   <li>{@code TrasparentBackgroundButton}: Button with a transparent background, customizable text or icon.</li>
 *   <li>{@code KeybindingButton}: Button designed for keybinding UI, with rounded corners and customizable appearance.</li>
 * </ul>
 * <p>
 * Default values used when options are not provided:
 * <ul>
 *   <li>Multiplicator: (1.0, 1.0)</li>
 *   <li>Background color: {@link Color#WHITE}</li>
 *   <li>Foreground color: {@link Color#BLACK}</li>
 *   <li>Font name: {@link Font#SANS_SERIF}</li>
 *   <li>Font type: {@link Font#BOLD}</li>
 * </ul>
 * </p>
 *
 * @see JButtonFactory
 * @see RectangleButton
 * @see RoundedButton
 * @see TrasparentBackgroundButton
 * @see KeybindingButton
 */
public final class JButtonFactoryImpl implements JButtonFactory {

        private static final Pair<Double, Double> DEFAULT_MOLTIPLICATOR = Pair.of(1.0, 1.0);
        private static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
        private static final Color DEFAULT_FOREGROUND_COLOR = Color.BLACK;
        private static final String DEFAULT_FONT_NAME = Font.SANS_SERIF;
        private static final int DEFAULT_FONT_TYPE = Font.BOLD;

        @Override
        public RectangleButton createRectangleButton(final String text,
                        final Optional<Pair<Double, Double>> moltiplicator, final Optional<Color> bgColor,
                        final Optional<String> fontName, final Optional<Color> fontColor, final Optional<Integer> fontType) {
                return new RectangleButton(text, moltiplicator.orElse(DEFAULT_MOLTIPLICATOR),
                                bgColor.orElse(DEFAULT_BACKGROUND_COLOR),
                                fontName.orElse(DEFAULT_FONT_NAME), fontColor.orElse(DEFAULT_FOREGROUND_COLOR),
                                fontType.orElse(DEFAULT_FONT_TYPE));
        }

        @Override
        public RectangleButton createRectangleButton(final ImageIcon icon,
                        final Optional<Pair<Double, Double>> moltiplicator, final Optional<Color> bgColor,
                        final Optional<Color> fgColor) {
                return new RectangleButton(icon, moltiplicator.orElse(DEFAULT_MOLTIPLICATOR),
                                bgColor.orElse(DEFAULT_BACKGROUND_COLOR),
                                fgColor.orElse(DEFAULT_FOREGROUND_COLOR));
        }

        @Override
        public RoundedButton createRoundedButton(final String text,
                        final Optional<Pair<Double, Double>> moltiplicator, final double arcProportion,
                        final Optional<Color> bgColor, final Optional<String> fontName, final Optional<Color> fontColor,
                        final Optional<Integer> fontType) {
                return new RoundedButton(text, moltiplicator.orElse(DEFAULT_MOLTIPLICATOR), arcProportion,
                                bgColor.orElse(DEFAULT_BACKGROUND_COLOR),
                                fontName.orElse(DEFAULT_FONT_NAME), fontColor.orElse(DEFAULT_FOREGROUND_COLOR),
                                fontType.orElse(DEFAULT_FONT_TYPE));
        }

        @Override
        public RoundedButton createRoundedButton(final ImageIcon icon, 
                        final Optional<Pair<Double, Double>> moltiplicator, final double arcProportion,
                        final Optional<Color> bgColor, final Optional<Color> fgColor) {
                return new RoundedButton(icon, moltiplicator.orElse(DEFAULT_MOLTIPLICATOR), arcProportion,
                                bgColor.orElse(DEFAULT_BACKGROUND_COLOR),
                                fgColor.orElse(DEFAULT_FOREGROUND_COLOR));
        }

        @Override
        public TrasparentBackgroundButton createTrasparentButton(final String text, 
                        final Optional<Pair<Double, Double>> moltiplicator, final Optional<String> fontName,
                        final Optional<Color> fontColor, final Optional<Integer> fontType) {
                return new TrasparentBackgroundButton(text, moltiplicator.orElse(DEFAULT_MOLTIPLICATOR),
                                DEFAULT_BACKGROUND_COLOR,
                                fontName.orElse(DEFAULT_FONT_NAME), fontColor.orElse(DEFAULT_FOREGROUND_COLOR),
                                fontType.orElse(DEFAULT_FONT_TYPE));
        }

        @Override
        public TrasparentBackgroundButton createTrasparentButton(final ImageIcon icon, 
                        final Optional<Pair<Double, Double>> moltiplicator, final Optional<Color> fgColor) {
                return new TrasparentBackgroundButton(icon, moltiplicator.orElse(DEFAULT_MOLTIPLICATOR),
                                DEFAULT_BACKGROUND_COLOR,
                                fgColor.orElse(DEFAULT_FOREGROUND_COLOR));
        }

        @Override
        public KeybindingButton createKeybindingButton(final String text, 
                        final Optional<Pair<Double, Double>> moltiplicator, final double arcProportion,
                        final Optional<Color> bgColor, final Optional<String> fontName, final Optional<Color> fontColor,
                        final Optional<Integer> fontType) {
                return new KeybindingButton(text, moltiplicator.orElse(DEFAULT_MOLTIPLICATOR),
                                arcProportion, bgColor.orElse(DEFAULT_BACKGROUND_COLOR),
                                fontName.orElse(DEFAULT_FONT_NAME), fontColor.orElse(DEFAULT_FOREGROUND_COLOR),
                                fontType.orElse(DEFAULT_FONT_TYPE));
        }

        @Override
        public KeybindingButton createKeybindingButton(final ImageIcon icon, 
                        final Optional<Pair<Double, Double>> moltiplicator, final double arcProportion,
                        final Optional<Color> bgColor, final Optional<Color> fgColor) {
                return new KeybindingButton(icon, moltiplicator.orElse(DEFAULT_MOLTIPLICATOR),
                                arcProportion, bgColor.orElse(DEFAULT_BACKGROUND_COLOR),
                                fgColor.orElse(DEFAULT_FOREGROUND_COLOR));
        }

}
