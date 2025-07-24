package com.thelegendofbald.view.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class ShopPanel extends JPanel {

    private List<String> items; // In futuro puoi usare una classe Weapon o Item

    public ShopPanel() {
        this.items = new ArrayList<>();
        // Esempio: aggiungi oggetti di test
        items.add("Spada di legno");
        items.add("Arco semplice");
        items.add("Pozione di cura");
        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.DARK_GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Negozio", 20, 40);

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        int y = 80;
        for (String item : items) {
            g.drawString(item, 40, y);
            y += 30;
        }
    }

    // In futuro: aggiungi metodi per comprare/vendere, mostrare dettagli, ecc.
}