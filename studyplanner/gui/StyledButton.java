package com.studyplanner.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StyledButton extends JButton {
    private static final int ARC = 16;

    private final Color baseColor;
    private final Color hoverColor;
    private boolean hovering = false;

    public StyledButton(String text, Color baseColor) {
        super(text);
        this.baseColor = baseColor;
        this.hoverColor = darken(baseColor, 0.92f);

        setFont(UITheme.FONT_BUTTON);
        setForeground(UITheme.BUTTON_TEXT);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setBorderPainted(false);

        setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        setPreferredSize(new Dimension(280, 48));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovering = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovering = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color fill = hovering ? hoverColor : baseColor;
        if (getModel().isArmed()) {
            fill = darken(fill, 0.90f);
        }

        g2.setColor(fill);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC, ARC);

        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(darken(baseColor, 0.80f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, ARC, ARC);
        g2.dispose();
    }

    private static Color darken(Color color, float factor) {
        int r = Math.max(0, Math.round(color.getRed() * factor));
        int g = Math.max(0, Math.round(color.getGreen() * factor));
        int b = Math.max(0, Math.round(color.getBlue() * factor));
        return new Color(r, g, b);
    }
}
