package com.thelegendofbald.view.main;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;

import javax.swing.JComponent;

import com.thelegendofbald.view.window.GameWindow;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thelegendofbald.view.panel.base.Panels;

/**
 * Test per GameWindow senza dipendere da MenuPanel/MenuView.
 */
class GameWindowTest {

    private static final int DEFAULT_W = 1280;
    private static final int DEFAULT_H = 704;

    private GameWindow window;

    @BeforeEach
    void setUp() {

        Assumptions.assumeFalse(GraphicsEnvironment.isHeadless(), "Richiede AWT non headless");
        this.window = new GameWindow();
    }

    @AfterEach
    void tearDown() {
        if (this.window != null) {
            this.window.dispose();
        }
    }

    @Test
    void internalSizeDefensiveCopy() {
        final Dimension d1 = window.getInternalSize();
        assertEquals(new Dimension(DEFAULT_W, DEFAULT_H), d1);

        d1.setSize(1, 1);
        final Dimension d2 = window.getInternalSize();
        assertEquals(new Dimension(DEFAULT_W, DEFAULT_H), d2);
    }

    @Test
    void setInternalSizePropagatesPreferredSize() {
        final int width = 800;
        final int height = 600;
        final Dimension input = new Dimension(width, height);

        window.setInternalSize(input);

        input.setSize(10, 10);

        assertEquals(new Dimension(width, height), window.getInternalSize(),
                "La dimensione interna non dovrebbe cambiare se modifico l'input esterno");

        assertNotNull(window.getContentPane(), "Content pane non deve essere null");

        assertEquals(new Dimension(width, height), window.getContentPane().getPreferredSize(),
                "La preferredSize del content deve essere quella originale (800x600)");
    }

    @Test
    void updateViewSetsContentPane() {
        window.updateView();
        final var content = window.getContentPane();
        assertNotNull(content, "Content pane non dovrebbe essere null");
        assertTrue(content instanceof JComponent, "Il content deve essere un componente Swing");
    }

    @Test
    void changeMainPanelUpdatesState() {
        assertEquals(Panels.MAIN_MENU, window.getCurrentPanel());
        assertTrue(window.getLastPanel().isEmpty(), "lastPanel inizialmente vuoto");

        window.changeMainPanel(Panels.MAIN_MENU);

        assertEquals(Panels.MAIN_MENU, window.getCurrentPanel());
        assertTrue(window.getLastPanel().isPresent(), "lastPanel dovrebbe essere valorizzato");
        assertEquals(Panels.MAIN_MENU, window.getLastPanel().get(), "lastPanel deve contenere il precedente current");
    }
}
