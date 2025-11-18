package com.thelegendofbald.api.panels;

import java.awt.Color;
import java.awt.Graphics;

// --- MODIFICA 1: Import necessari per "ascoltare" ---
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
// --- FINE MODIFICA ---

import com.thelegendofbald.life.LifeComponent;

/**
 * Un pannello che rappresenta visivamente la salute di un'entità.
 * Ora implementa PropertyChangeListener per "ascoltare"
 * i cambiamenti dal LifeComponent.
 */
// --- MODIFICA 2: Implementa l'interfaccia "listener" ---
public class LifePanel extends AdapterPanel implements PropertyChangeListener {

    private static final long serialVersionUID = 1L;

    private final LifeComponent lifeComponent;

    /**
     * Costruisce un nuovo LifePanel associato a un LifeComponent.
     * @param lifeComponent il LifeComponent da monitorare
     */
    public LifePanel(final LifeComponent lifeComponent) {
        super();
        if (lifeComponent == null) {
            throw new IllegalArgumentException("LifeComponent cannot be null");
        }
        this.lifeComponent = lifeComponent;

        // --- MODIFICA 3: Iscrivi questo pannello agli aggiornamenti! ---
        // Dice al LifeComponent: "Ehi, quando la tua vita cambia,
        // avvisa questo pannello chiamando il suo metodo propertyChange."
        this.lifeComponent.addPropertyChangeListener(this);
    }

    /**
     * Inizializza i componenti del pannello.
     */
    @Override
    protected void initializeComponents() {
        super.initializeComponents(); 
        addComponentsToPanel();
    }

    /**
     * Aggiunge sotto-componenti al pannello.
     */
    @Override
    public void addComponentsToPanel() {
        // Attualmente non necessario
    }

     /**
     * Attiva un ridisegno del pannello.
     */
    @Override
    public void updateView() {
        repaint();
    }

    /**
     * Disegna la barra della vita.
     * (Nessuna modifica qui, era già corretto)
     * @param g L'oggetto Graphics usato per disegnare.
     */
     @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final double perc = lifeComponent.getPercentage(); 
        final int width = (int) (perc * getWidth());

        // Sfondo grigio
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Barra rossa della vita
        g.setColor(Color.RED);
        g.fillRect(0, 0, width, getHeight());

        // Bordo nero
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    /**
     * Placeholder per aggiornare le dimensioni.
     */
    @Override
    public void updateComponentsSize() {
         // Non necessario
    }

    // --- MODIFICA 4: Metodo obbligatorio dell'interfaccia PropertyChangeListener ---
    /**
     * Questo metodo viene chiamato AUTOMATICAMENTE dal LifeComponent
     * ogni volta che la sua vita cambia (perché ci siamo iscritti nel costruttore).
     *
     * @param evt L'evento che descrive il cambiamento.
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        // Controlliamo che la proprietà cambiata sia "currentHealth"
        // (La stringa l'abbiamo definita in LifeComponent)
        if (LifeComponent.HEALTH_PROPERTY.equals(evt.getPropertyName())) {
            // La vita è cambiata! Diciamo a Swing di ridisegnare questo pannello.
        updateView(); // Chiama repaint()
        }
    }
}
