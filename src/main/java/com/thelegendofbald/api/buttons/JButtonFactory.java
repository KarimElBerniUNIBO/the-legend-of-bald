package com.thelegendofbald.api.buttons;

import java.awt.Color;
import java.util.Optional;

import javax.swing.ImageIcon;

import org.apache.commons.lang3.tuple.Pair;

import com.thelegendofbald.view.buttons.KeybindingButton;
import com.thelegendofbald.view.buttons.RectangleButton;
import com.thelegendofbald.view.buttons.RoundedButton;
import com.thelegendofbald.view.buttons.TrasparentBackgroundButton;

/**
 * Factory for creating template of buttons for User Interface.
 */
public interface JButtonFactory {

        /**
         * Creates a rectangular button with the specified properties.
         *
         * @param text          the text to display on the button
         * @param moltiplicator an optional pair of width and height multipliers to
         *                      scale the button size relative to the parent
         * @param bgColor       an optional background color for the button
         * @param fontName      an optional font name for the button text
         * @param fontColor     an optional color for the button text
         * @param fontType      an optional font style (e.g., Font.PLAIN, Font.BOLD) for
         *                      the button text
         * @return a new instance of {@link RectangleButton} configured with the
         *         specified properties
         */
        RectangleButton createRectangleButton(String text, 
                        Optional<Pair<Double, Double>> moltiplicator, Optional<Color> bgColor,
                        Optional<String> fontName,
                        Optional<Color> fontColor, Optional<Integer> fontType);

        /**
         * Creates a RectangleButton with the specified icon, parent size, and optional
         * customization parameters.
         *
         * @param icon          the ImageIcon to be displayed on the button
         * @param moltiplicator an Optional containing a Pair of Doubles to multiply the
         *                      button's width and height, or empty for default sizing
         * @param bgColor       an Optional containing the background Color of the
         *                      button, or empty for default color
         * @param fgColor       an Optional containing the foreground Color of the
         *                      button, or empty for default color
         * @return a new instance of RectangleButton configured with the provided
         *         parameters
         */
        RectangleButton createRectangleButton(ImageIcon icon, Optional<Pair<Double, Double>> moltiplicator,
                        Optional<Color> bgColor, Optional<Color> fgColor);

        /**
         * Creates a rounded button with customizable properties.
         *
         * @param text          the text to display on the button
         * @param moltiplicator an optional pair of width and height multipliers for
         *                      scaling the button size relative to the parent
         * @param arcProportion the proportion of the button's arc (corner roundness)
         * @param bgColor       an optional background color for the button
         * @param fontName      an optional font name for the button's text
         * @param fontColor     an optional color for the button's text
         * @param fontType      an optional font style (e.g., Font.PLAIN, Font.BOLD)
         * @return a new instance of RoundedButton with the specified properties
         */
        RoundedButton createRoundedButton(String text,
                        Optional<Pair<Double, Double>> moltiplicator,
                        double arcProportion, Optional<Color> bgColor, Optional<String> fontName,
                        Optional<Color> fontColor,
                        Optional<Integer> fontType);

        /**
         * Creates a rounded button with the specified icon, size, and optional
         * customization parameters.
         *
         * @param icon          the {@link ImageIcon} to display on the button
         * @param moltiplicator an {@link Optional} containing a {@link Pair} of width
         *                      and height multipliers for scaling the button size, or
         *                      empty for default scaling
         * @param arcProportion the proportion (as a double) of the button's arc for
         *                      rounded corners
         * @param bgColor       an {@link Optional} specifying the background
         *                      {@link Color} of the button, or empty for default color
         * @param fgColor       an {@link Optional} specifying the foreground
         *                      {@link Color} (e.g., text or icon color), or empty for
         *                      default color
         * @return a new instance of {@code RoundedButton} configured with the specified
         *         parameters
         */
        RoundedButton createRoundedButton(ImageIcon icon, Optional<Pair<Double, Double>> moltiplicator,
                        double arcProportion, Optional<Color> bgColor,
                        Optional<Color> fgColor);

        /**
         * Creates a transparent background button with the specified properties.
         *
         * @param text          the text to display on the button
         * @param moltiplicator an optional pair of doubles representing width and
         *                      height multipliers for the button size
         * @param fontName      an optional font name to use for the button text
         * @param fontColor     an optional color for the button text
         * @param fontType      an optional font style (e.g., Font.PLAIN, Font.BOLD) for
         *                      the button text
         * @return a new instance of {@link TrasparentBackgroundButton} configured with
         *         the provided parameters
         */
        TrasparentBackgroundButton createTrasparentButton(String text,
                        Optional<Pair<Double, Double>> moltiplicator, Optional<String> fontName,
                        Optional<Color> fontColor,
                        Optional<Integer> fontType);

        /**
         * Creates a transparent background button with the specified icon and parent
         * size.
         *
         * @param icon          the {@link ImageIcon} to be displayed on the button
         * @param moltiplicator an {@link Optional} containing a {@link Pair} of
         *                      {@link Double} values used as multipliers for sizing or
         *                      positioning; if empty, default values are used
         * @param fgColor       an {@link Optional} containing the foreground
         *                      {@link Color} for the button; if empty, the default
         *                      color is used
         * @return a new instance of {@link TrasparentBackgroundButton} configured with
         *         the provided parameters
         */
        TrasparentBackgroundButton createTrasparentButton(ImageIcon icon, Optional<Pair<Double, Double>> moltiplicator,
                        Optional<Color> fgColor);

        /**
         * Creates a {@link KeybindingButton} with the specified properties.
         *
         * @param text          the text to display on the button
         * @param moltiplicator an optional pair of multipliers (width, height) to scale
         *                      the button size relative to the parent
         * @param arcProportion the proportion of the button's arc (for rounded corners)
         * @param bgColor       an optional background color for the button
         * @param fontName      an optional font name for the button text
         * @param fontColor     an optional color for the button text
         * @param fontType      an optional font style (e.g., Font.PLAIN, Font.BOLD)
         * @return a configured {@link KeybindingButton} instance
         */
        KeybindingButton createKeybindingButton(String text,
                        Optional<Pair<Double, Double>> moltiplicator, double arcProportion, Optional<Color> bgColor,
                        Optional<String> fontName, Optional<Color> fontColor, Optional<Integer> fontType);

        /**
         * Creates a {@link KeybindingButton} with the specified icon, parent size,
         * optional size multipliers,
         * arc proportion for rounded corners, and optional background and foreground
         * colors.
         *
         * @param icon          the {@link ImageIcon} to display on the button
         * @param moltiplicator an {@link Optional} containing a {@link Pair} of doubles
         *                      to multiply the button's width and height, or empty if
         *                      not used
         * @param arcProportion the proportion (as a double) of the button's arc for
         *                      rounded corners
         * @param bgColor       an {@link Optional} containing the background
         *                      {@link Color}, or empty if default should be used
         * @param fgColor       an {@link Optional} containing the foreground
         *                      {@link Color}, or empty if default should be used
         * @return a new {@link KeybindingButton} instance configured with the specified
         *         parameters
         */
        KeybindingButton createKeybindingButton(ImageIcon icon, Optional<Pair<Double, Double>> moltiplicator,
                        double arcProportion, Optional<Color> bgColor,
                        Optional<Color> fgColor);

}
