package com.thelegendofbald.controller.input;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.thelegendofbald.model.config.ControlsSettings;
import com.thelegendofbald.model.entity.Bald;
import com.thelegendofbald.model.item.Interactable;
import com.thelegendofbald.model.system.CombatManager;
import com.thelegendofbald.controller.level.LevelManager;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Handles user input and manages key bindings for the game.
 */
public class InputController {

    private static final double MOVE_SPEED = 2.0;
    private static final double DX_STEP = 1.0;
    private static final double DY_STEP = 1.0;
    private static final String MAP_1 = "map_1";

    private final Set<Integer> pressedKeys = new HashSet<>();
    private final Bald bald;
    private final CombatManager combatManager;
    private final LevelManager levelManager;
    private final Runnable toggleOptions;
    private final Runnable toggleInventory;

    /**
     * Constructs a new InputController.
     *
     * @param component       the component to bind keys to.
     * @param bald            the player character.
     * @param combatManager   the combat manager.
     * @param levelManager    the level manager.
     * @param toggleOptions   runnable to toggle options menu.
     * @param toggleInventory runnable to toggle inventory.
     */
    @SuppressFBWarnings(
        value = "EI2",
        justification = "InputController controls these specific instances."
    )
    public InputController(final JComponent component, final Bald bald, final CombatManager combatManager,
            final LevelManager levelManager, final Runnable toggleOptions, final Runnable toggleInventory) {
        this.bald = bald;
        this.combatManager = combatManager;
        this.levelManager = levelManager;
        this.toggleOptions = toggleOptions;
        this.toggleInventory = toggleInventory;
        setupKeyBindings(component);
    }

    /**
     * Refreshes the key bindings for the specified component.
     *
     * @param component the component to bind keys to.
     */
    public void refreshKeyBindings(final JComponent component) {
        pressedKeys.clear();
        setupKeyBindings(component);
    }

    private void setupKeyBindings(final JComponent component) {
        final InputMap im = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        final ActionMap am = component.getActionMap();
        im.clear();
        am.clear();


        bindKey(im, am, "pressed UP", ControlsSettings.UP.getKey(), true,
                () -> pressedKeys.add(ControlsSettings.UP.getKey()));
        bindKey(im, am, "pressed DOWN", ControlsSettings.DOWN.getKey(), true,
                () -> pressedKeys.add(ControlsSettings.DOWN.getKey()));
        bindKey(im, am, "pressed LEFT", ControlsSettings.LEFT.getKey(), true,
                () -> pressedKeys.add(ControlsSettings.LEFT.getKey()));
        bindKey(im, am, "pressed RIGHT", ControlsSettings.RIGHT.getKey(), true,
                () -> pressedKeys.add(ControlsSettings.RIGHT.getKey()));
        bindKey(im, am, "pressed ESCAPE", KeyEvent.VK_ESCAPE, true, toggleOptions);
        bindKey(im, am, "pressed SPACE", ControlsSettings.ATTACK.getKey(), true,
                combatManager::tryToAttack);
        bindKey(im, am, "pressed I", ControlsSettings.INVENTORY.getKey(), true,
                toggleInventory);
        bindKey(im, am, "interact", ControlsSettings.INTERACT.getKey(), true,
                this::interactWithItems);


        bindKey(im, am, "released UP", ControlsSettings.UP.getKey(), false,
                () -> pressedKeys.remove(ControlsSettings.UP.getKey()));
        bindKey(im, am, "released DOWN", ControlsSettings.DOWN.getKey(), false,
                () -> pressedKeys.remove(ControlsSettings.DOWN.getKey()));
        bindKey(im, am, "released LEFT", ControlsSettings.LEFT.getKey(), false,
                () -> pressedKeys.remove(ControlsSettings.LEFT.getKey()));
        bindKey(im, am, "released RIGHT", ControlsSettings.RIGHT.getKey(), false,
                () -> pressedKeys.remove(ControlsSettings.RIGHT.getKey()));
    }

    private void bindKey(final InputMap im, final ActionMap am, final String name, final int key,
            final boolean pressed, final Runnable action) {
        im.put(KeyStroke.getKeyStroke(key, 0, !pressed), name);
        am.put(name, new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                action.run();
            }
        });
    }

    /**
     * Handles player input and updates movement/actions.
     */
    public void handleInput() {
        if (bald.isImmobilized()) {
            bald.setSpeedX(0);
            bald.setSpeedY(0);
            return;
        }
        double dx = 0;
        double dy = 0;

        if (pressedKeys.contains(ControlsSettings.LEFT.getKey())) {
            dx -= DX_STEP;
        }
        if (pressedKeys.contains(ControlsSettings.RIGHT.getKey())) {
            dx += DX_STEP;
        }
        if (!MAP_1.equals(levelManager.getCurrentMapName())) {
            if (pressedKeys.contains(ControlsSettings.UP.getKey())) {
                dy -= DY_STEP;
            }
            if (pressedKeys.contains(ControlsSettings.DOWN.getKey())) {
                dy += DY_STEP;
            }
        }
        if (pressedKeys.contains(ControlsSettings.ATTACK.getKey())) {
            combatManager.tryToAttack();
        }

        final double magnitude = Math.hypot(dx, dy);
        if (magnitude > 0) {
            dx = dx / magnitude * MOVE_SPEED;
            dy = dy / magnitude * MOVE_SPEED;
        }

        bald.setFacingRight(dx > 0 || dx == 0 && bald.isFacingRight());
        bald.setSpeedX(dx);
        bald.setSpeedY(dy);
    }

    private void interactWithItems() {
        levelManager.getItemManager().getItems().stream()
                .filter(item -> bald.getBounds().intersects(item.getBounds()))
                .filter(item -> item instanceof Interactable)
                .map(item -> (Interactable) item)
                .findFirst()
                .ifPresent(Interactable::interact);
    }

    /**
     * Clears all currently pressed keys.
     */
    public void clearPressedKeys() {
        pressedKeys.clear();
    }
}
