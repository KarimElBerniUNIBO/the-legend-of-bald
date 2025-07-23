package com.thelegendofbald.api.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import com.thelegendofbald.life.LifeComponent;

public class LifePanel extends AdapterPanel {

    private LifeComponent lifeComponent;  

    public LifePanel(Dimension size, LifeComponent lifeComponent) {
        super(size);
        this.lifeComponent = lifeComponent;
    }

    @Override
    protected void initializeComponents() {
        super.initializeComponents(); // imposta listener resize e chiama updateView
        addComponentsToPanel(); // se serve aggiungere sotto-componenti
    }

    @Override
    public void addComponentsToPanel() {
        //TO-DO
    }

    @Override
    public void updateView() {
        repaint();
        
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        double perc = lifeComponent.getPercentage(); 
        int width = (int) (perc * getWidth());

        // sfondo grigio
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        // barra vita rossa
        g.setColor(Color.RED);
        g.fillRect(0, 0, width, getHeight());

        // bordo nero
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    @Override
    public void updateComponentsSize() {
        // Implement if needed, otherwise leave empty
    }
}
