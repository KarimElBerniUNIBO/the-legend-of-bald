package com.thelegendofbald.view.main;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;

import javax.swing.JComponent;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.thelegendofbald.api.panels.Panels;

/**
 * Test per GameWindow senza dipendere da MenuPanel/MenuView.
 */
class GameWindowTest {

    private static final int DEFAULT_W = 1280;
    private static final int DEFAULT_H = 704;

    private GameWindow window;

    @BeforeEach
    void setUp() {
        // Evita eccezioni AWT in ambienti headless.
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
    @DisplayName("getInternalSize() ritorna copia difensiva con i default")
    void getInternalSize_defensiveCopy_defaults() {
        Dimension d1 = window.getInternalSize();
        assertEquals(new Dimension(DEFAULT_W, DEFAULT_H), d1);

        // Modifico la copia: lo stato interno non deve cambiare
        d1.setSize(1, 1);
        Dimension d2 = window.getInternalSize();
        assertEquals(new Dimension(DEFAULT_W, DEFAULT_H), d2);
    }

    @Test
    @DisplayName("setInternalSize() fa copia difensiva e propaga la preferredSize al content")
    void setInternalSize_propagatesPreferredSize() {
        Dimension input = new Dimension(800, 600);
        window.setInternalSize(input);

        // Modifico l'oggetto passato: la size interna non deve cambiare
        input.setSize(10, 10);
        assertEquals(new Dimension(800, 600), window.getInternalSize());

        // Dopo updatePanelsSize() + updateView(), il content pane deve avere quella preferredSize
        JComponent content = (JComponent) window.getContentPane();
        assertNotNull(content, "Content pane non deve essere null");
        assertEquals(new Dimension(800, 600), content.getPreferredSize(),
                "La preferredSize del content deve essere aggiornata");
    }

    @Test
    @DisplayName("updateView() imposta un content pane non nullo e focus richiesto")
    void updateView_setsContentPane() {
        window.updateView(); // è già chiamato in costruttore ma lo richiamiamo
        var content = window.getContentPane();
        assertNotNull(content, "Content pane non dovrebbe essere null");
        assertTrue(content instanceof JComponent, "Il content deve essere un componente Swing");
    }

    @Test
    @DisplayName("changeMainPanel() aggiorna currentPanel e lastPanel")
    void changeMainPanel_updatesState() {
        assertEquals(Panels.MAIN_MENU, window.getCurrentPanel());
        assertTrue(window.getLastPanel().isEmpty(), "lastPanel inizialmente vuoto");

        // Cambiamo pannello (qui restiamo su MAIN_MENU per non dipendere da altri tipi)
        window.changeMainPanel(Panels.MAIN_MENU);

        assertEquals(Panels.MAIN_MENU, window.getCurrentPanel());
        assertTrue(window.getLastPanel().isPresent(), "lastPanel dovrebbe essere valorizzato");
        assertEquals(Panels.MAIN_MENU, window.getLastPanel().get(), "lastPanel deve contenere il precedente current");
    }
}
