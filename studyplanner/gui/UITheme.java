package com.studyplanner.gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;
import java.awt.*;

public final class UITheme {
    private UITheme() {}

    // Cyberpunk palette (pink / purple / bluish) - single theme across the app
    public static final Color PRIMARY_BACKGROUND = Color.decode("#0F0E2E"); // deep navy
    public static final Color PANEL_BACKGROUND = Color.decode("#1A0F3A");   // dark purple
    public static final Color PANEL_BACKGROUND_2 = Color.decode("#2D1B69"); // medium purple

    public static final Color PRIMARY_BUTTON = Color.decode("#EC4899");   // pink
    public static final Color SECONDARY_BUTTON = Color.decode("#06B6D4"); // cyan
    public static final Color DANGER_BUTTON = Color.decode("#EF4444");    // red

    public static final Color TEXT_COLOR = Color.decode("#F5F3FF");       // near-white
    public static final Color TEXT_MUTED = Color.decode("#94A3B8");       // gray-blue
    public static final Color FIELD_BORDER = Color.decode("#0EA5E9");     // sky blue
    public static final Color BUTTON_TEXT = PRIMARY_BACKGROUND;
    public static final Color ACCENT_COLOR = Color.decode("#D946EF");     // magenta accent
    public static final Color COLOR_DARK_BG = PRIMARY_BACKGROUND;         // dark background
    public static final Color CARD_COLOR = PANEL_BACKGROUND;              // card background
    public static final Color SECONDARY_BACKGROUND = PANEL_BACKGROUND_2;  // secondary background

    public static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 22);
    public static final Font FONT_SECTION = new Font("Arial", Font.BOLD, 16);
    public static final Font FONT_NORMAL = new Font("Arial", Font.PLAIN, 14);
    public static final Font FONT_BUTTON = new Font("Arial", Font.BOLD, 14);

    public static void installSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Fall back to default LookAndFeel
        }

        // Tab colors (best-effort; some LookAndFeels may ignore some keys)
        UIManager.put("TabbedPane.background", PANEL_BACKGROUND);
        UIManager.put("TabbedPane.foreground", TEXT_COLOR);
        UIManager.put("TabbedPane.selected", PANEL_BACKGROUND_2);
        UIManager.put("TabbedPane.contentAreaColor", PANEL_BACKGROUND);
        UIManager.put("TabbedPane.shadow", PRIMARY_BACKGROUND);
        UIManager.put("TabbedPane.darkShadow", PRIMARY_BACKGROUND);
    }

    public static void configureFrame(JFrame frame, String title, Dimension size, Dimension minimumSize, int closeOperation) {
        frame.setTitle(title);
        frame.setDefaultCloseOperation(closeOperation);
        frame.setSize(size);
        frame.setMinimumSize(minimumSize);
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setResizable(true); // Enables minimize & maximize

        // App icon placeholder:
        // Add an icon file to your resources and set it like:
        // frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/app_icon.png")));
    }

    public static JPanel createRootPanel() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(PRIMARY_BACKGROUND);
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return root;
    }

    public static JPanel createCardPanel(LayoutManager layout) {
        JPanel card = new JPanel(layout);
        card.setBackground(PANEL_BACKGROUND);
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return card;
    }

    public static JPanel createTransparentPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setOpaque(false);
        return panel;
    }

    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(FONT_TITLE);
        label.setForeground(TEXT_COLOR);
        return label;
    }

    public static JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_SECTION);
        label.setForeground(TEXT_COLOR);
        return label;
    }

    public static JLabel createNormalLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_NORMAL);
        label.setForeground(TEXT_COLOR);
        return label;
    }

    public static StyledButton primaryButton(String text) {
        return new StyledButton(text, PRIMARY_BUTTON);
    }

    public static StyledButton secondaryButton(String text) {
        return new StyledButton(text, SECONDARY_BUTTON);
    }

    public static StyledButton dangerButton(String text) {
        return new StyledButton(text, DANGER_BUTTON);
    }

    public static void styleTextField(JTextField field) {
        field.setFont(FONT_NORMAL);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setBackground(PANEL_BACKGROUND_2);
        field.setBorder(createFieldBorder());
    }

    public static void styleTextArea(JTextArea area) {
        area.setFont(FONT_NORMAL);
        area.setForeground(TEXT_COLOR);
        area.setCaretColor(TEXT_COLOR);
        area.setBackground(PANEL_BACKGROUND_2);
        area.setBorder(createFieldBorder());
    }

    public static void styleTextComponent(JTextComponent component) {
        component.setFont(FONT_NORMAL);
        component.setForeground(TEXT_COLOR);
        component.setCaretColor(TEXT_COLOR);
        component.setBackground(PANEL_BACKGROUND_2);
        component.setBorder(createFieldBorder());
    }

    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(FONT_NORMAL);
        comboBox.setBackground(PANEL_BACKGROUND_2);
        comboBox.setForeground(TEXT_COLOR);
    }

    public static void styleSpinner(JSpinner spinner) {
        spinner.setFont(FONT_NORMAL);
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
            styleTextField(tf);
        }
    }

    public static void styleTabbedPane(JTabbedPane tabbedPane) {
        tabbedPane.setFont(FONT_NORMAL);
        tabbedPane.setBackground(PANEL_BACKGROUND);
        tabbedPane.setForeground(TEXT_COLOR);
        tabbedPane.setOpaque(true);
    }

    private static Border createFieldBorder() {
        // Underline-style border to avoid "boxy" fields
        Border underline = BorderFactory.createMatteBorder(0, 0, 2, 0, FIELD_BORDER);
        Border padding = BorderFactory.createEmptyBorder(8, 10, 8, 10);
        return BorderFactory.createCompoundBorder(underline, padding);
    }
}
