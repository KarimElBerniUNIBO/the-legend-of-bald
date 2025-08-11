package com.thelegendofbald.view.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.thelegendofbald.item.ShopItem;
import com.thelegendofbald.item.weapons.Axe;
import com.thelegendofbald.item.weapons.FireBall;
import com.thelegendofbald.item.weapons.Sword;
import com.thelegendofbald.model.combat.CombatManager;
import com.thelegendofbald.model.common.Wallet;

public class ShopPanel extends JPanel {

    private final List<ShopItem> items;
    private final Wallet wallet;
    private int selectedIndex = -1;
    private final JLabel goldLabel;

    public ShopPanel(CombatManager combatManager, Wallet wallet) {
        this.items = new ArrayList<>();
        this.wallet = wallet;

        // Aggiungi oggetti veri che implementano ShopItem
        items.add(new Sword(0, 0, 32, 32, combatManager));
        items.add(new Axe(0, 0, 32, 32, combatManager));
        items.add(new FireBall(0, 0, 32, 32, combatManager));

        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.DARK_GRAY);
        setLayout(null); // disabilita layout manager

        // Etichetta oro in basso a destra
        goldLabel = new JLabel("Oro: " + wallet.getCoins());
        goldLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
        goldLabel.setForeground(Color.YELLOW);
        Dimension size = goldLabel.getPreferredSize();
        goldLabel.setBounds(400 - size.width - 20, 270, size.width, size.height);
        add(goldLabel);

        // Aggiungi listener per click su oggetti
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int y = 80;
                for (int i = 0; i < items.size(); i++) {
                    Rectangle rect = new Rectangle(20, y - 25, 360, 35);
                    if (rect.contains(e.getPoint())) {
                        selectedIndex = i;
                        repaint();
                        System.out.println("Selezionato: " + items.get(i).getDisplayName());
                        break;
                    }
                    y += 50;
                }
            }
        });
    }

    public void updateGoldDisplay() {
        goldLabel.setText("Oro: " + wallet.getCoins());
        goldLabel.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 22));
        g2.drawString("Negozio", 20, 40);

        int y = 80;
        for (int i = 0; i < items.size(); i++) {
            ShopItem item = items.get(i);

            // Sfondo selezionato
            if (i == selectedIndex) {
                g2.setColor(new Color(70, 130, 180));
                g2.fillRoundRect(20, y - 25, 360, 40, 10, 10);
            }

            // Testo e immagine
            g2.setColor(Color.WHITE);
            if (item.getSprite() != null) {
                g2.drawImage(item.getSprite(), 30, y - 20, 24, 24, null);
            }

            g2.setFont(new Font("Arial", Font.PLAIN, 16));
            g2.drawString(item.getDisplayName() + " - " + item.getPrice() + "G", 60, y);

            y += 50;
        }

        // Descrizione oggetto selezionato
        if (selectedIndex >= 0) {
            ShopItem selectedItem = items.get(selectedIndex);
            g2.setColor(Color.LIGHT_GRAY);
            g2.setFont(new Font("Arial", Font.ITALIC, 14));
            g2.drawString(selectedItem.getDescription(), 20, getHeight() - 30);
        }
    }
}
