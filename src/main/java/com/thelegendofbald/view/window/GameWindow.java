package com.thelegendofbald.view.window;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.thelegendofbald.model.system.Game;
import com.thelegendofbald.view.panel.base.MenuPanel;
import com.thelegendofbald.view.panel.base.Panels;
import com.thelegendofbald.model.config.WindowMode;
import com.thelegendofbald.view.panel.game.GamePanel;

/**
 * La classe {@code GameWindow} rappresenta la finestra principale del gioco.
 * <p>
 * Estende {@link JFrame} e gestisce il contenitore principale per i vari pannelli di gioco
 * (menu, partita, impostazioni), occupandosi della navigazione tra di essi e della configurazione
 * delle proprietà della finestra (dimensioni, modalità schermo, icona).
 * </p>
 */
public final class GameWindow extends JFrame implements View, MainView {

    private static final long serialVersionUID = 1L;

    /** Titolo della finestra dell'applicazione. */
    private static final String TITLE = "The Legend of Bald";
    /** Larghezza predefinita della finestra in pixel. */
    private static final int DEFAULT_WINDOW_WIDTH = 1280;
    /** Altezza predefinita della finestra in pixel. */
    private static final int DEFAULT_WINDOW_HEIGHT = 704;

<<<<<<< HEAD
=======
    /**
     * Dimensione interna attuale della finestra.
     * Dichiarata volatile per garantire la visibilità delle modifiche tra i thread.
     */
>>>>>>> f2bbfff79374462da6c37903b0911f2b7b6949da
    private volatile Dimension internalSize = new Dimension(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);

    /** Il pannello attualmente visualizzato nella finestra. */
    private Panels currentPanel = Panels.MAIN_MENU;

    /**
     * Riferimento opzionale al pannello visualizzato precedentemente.
     * Utile per logiche di navigazione "indietro".
     */
    private transient Optional<Panels> lastPanel = Optional.empty();

    /**
     * Costruttore predefinito.
     * Inizializza la finestra e imposta le dimensioni predefinite per tutti i pannelli gestiti.
     */
    public GameWindow() {
        super();
        this.updatePanelsSize();
    }

<<<<<<< HEAD
=======
    /**
     * Aggiorna la dimensione preferita di tutti i pannelli registrati nell'enum {@link Panels}.
     * <p>
     * Questo metodo è sincronizzato per evitare race condition durante il ridimensionamento
     * concorrente della finestra.
     * </p>
     */
>>>>>>> f2bbfff79374462da6c37903b0911f2b7b6949da
    private synchronized void updatePanelsSize() {
        final Dimension size = this.getInternalSize();
        Arrays.stream(Panels.values())
                .map(Panels::getPanel)
                .forEach(panel -> panel.setPreferredSize(size));
        this.updateView();
    }

    /**
     * Configura le proprietà iniziali della finestra (titolo, icona, resize) e la rende visibile.
     * Imposta il pannello corrente come content pane.
     */
    @Override
    public void display() {
        this.setTitle(TITLE);
        this.setIconImage(new ImageIcon(this.getClass().getResource("/images/icon.png")).getImage());
        this.setResizable(true);
        this.setContentPane(currentPanel.getPanel());
        this.pack();
        this.setLocationByPlatform(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    /**
     * Cambia il pannello principale visualizzato nella finestra.
     * Memorizza il pannello corrente in {@code lastPanel} prima di effettuare lo scambio.
     *
     * @param panelEnum l'identificativo del nuovo pannello da visualizzare
     */
    @Override
    public void changeMainPanel(final Panels panelEnum) {
        lastPanel = Optional.of(currentPanel);
        currentPanel = panelEnum;
        this.updateView();
    }

    /**
     * Aggiorna la vista corrente, impostando il contenuto del frame e richiedendo il focus.
     * <p>
     * Se il pannello attivato è un {@link GamePanel} e non è in esecuzione, avvia il loop di gioco.
     * </p>
     */
    @Override
    public void updateView() {
        final MenuPanel panel = currentPanel.getPanel();
        this.setContentPane(panel);
        this.revalidate();
        this.repaint();
        panel.requestFocusInWindow();
        if (panel instanceof GamePanel gamePanel && !gamePanel.isRunning()) {
            gamePanel.startGame();
            gamePanel.requestFocusInWindow();
        }
    }

    /**
     * Restituisce una copia delle dimensioni interne correnti della finestra.
     *
     * @return un nuovo oggetto {@link Dimension} che rappresenta la larghezza e l'altezza
     */
    @Override
    public synchronized Dimension getInternalSize() {
        return (Dimension) internalSize.clone();
    }

    /**
     * Imposta le nuove dimensioni interne della finestra e aggiorna tutti i pannelli di conseguenza.
     *
     * @param size la nuova dimensione da applicare
     */
    @Override
    public synchronized void setInternalSize(final Dimension size) {
        internalSize = (Dimension) size.clone();
        this.updatePanelsSize();
    }

    /**
     * Restituisce, se presente, il pannello visualizzato prima di quello corrente.
     *
     * @return un {@link Optional} contenente l'ultimo pannello visitato, o vuoto se non esiste
     */
    @Override
    public Optional<Panels> getLastPanel() {
        return lastPanel;
    }

    /**
     * Restituisce l'identificativo del pannello attualmente visualizzato.
     *
     * @return l'enum {@link Panels} corrente
     */
    @Override
    public Panels getCurrentPanel() {
        return currentPanel;
    }

    /**
     * Imposta la modalità della finestra (schermo intero, finestra, finestra senza bordi).
     * La modifica richiede la chiusura e riapertura (dispose/setVisible) del frame.
     *
     * @param windowMode la modalità di visualizzazione desiderata
     */
    @Override
    public void setWindowMode(final WindowMode windowMode) {
        Optional.ofNullable(windowMode).ifPresent(mode -> {
            this.dispose();
            switch (mode) {
                case FULLSCREEN -> {
                    this.setUndecorated(true);
                    this.setExtendedState(MAXIMIZED_BOTH);
                }
                case WINDOWED_FULLSCREEN -> {
                    this.setUndecorated(false);
                    this.setExtendedState(MAXIMIZED_BOTH);
                }
                case WINDOW -> {
                    this.setUndecorated(false);
                    this.setExtendedState(NORMAL);
                    this.pack();
                }
            }
            this.setVisible(true);
            this.updateView();
        });
    }

    /**
     * Imposta il target FPS (Frames Per Second) per il pannello di gioco.
     *
     * @param fps il numero di frame al secondo desiderati
     */
    @Override
    public void setFPS(final int fps) {
        final Game game = (Game) Panels.GAME_MENU.getPanel();
        game.setFPS(fps);
    }

    /**
     * Abilita o disabilita la visualizzazione del contatore FPS a schermo.
     *
     * @param showFPS {@code true} per mostrare gli FPS, {@code false} per nasconderli
     */
    @Override
    public void toggleViewFps(final boolean showFPS) {
        final Game game = (Game) Panels.GAME_MENU.getPanel();
        game.setShowingFPS(showFPS);
    }

    /**
     * Abilita o disabilita la visualizzazione del timer di gioco a schermo.
     *
     * @param showTimer {@code true} per mostrare il timer, {@code false} per nasconderlo
     */
    @Override
    public void toggleViewTimer(final boolean showTimer) {
        final GamePanel game = (GamePanel) Panels.GAME_MENU.getPanel();
        game.setShowingTimer(showTimer);
    }
}
