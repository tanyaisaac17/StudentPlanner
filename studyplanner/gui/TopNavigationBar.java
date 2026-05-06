package com.studyplanner.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * TOP NAVIGATION BAR - Reusable component for all form screens
 * 
 * FEATURES:
 * - Back button (arrow icon) on left
 * - Title in center
 * - Consistent styling across all forms
 * - Automatically navigates to DASHBOARD when back is clicked
 */
public class TopNavigationBar extends JPanel implements ActionListener {
    
    private NavigationListener navigationListener;
    private JButton btnBack;
    private JLabel lblTitle;
    
    /**
     * CONSTRUCTOR
     * @param navigationListener To handle back button navigation
     * @param title Screen title to display
     */
    public TopNavigationBar(NavigationListener navigationListener, String title) {
        this.navigationListener = navigationListener;
        
        setLayout(new BorderLayout(15, 0));
        setBackground(UITheme.PANEL_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        
        // Back button on left
        btnBack = new JButton("← Back");
        btnBack.setFont(UITheme.FONT_NORMAL);
        btnBack.setBackground(UITheme.SECONDARY_BUTTON);
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setPreferredSize(new Dimension(100, 35));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(this);
        
        // Hover effect for back button
        btnBack.addMouseListener(new java.awt.event.MouseAdapter() {
            Color original = UITheme.SECONDARY_BUTTON;
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnBack.setBackground(new Color(100, 116, 139));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnBack.setBackground(original);
            }
        });
        
        // Title in center
        lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(UITheme.TEXT_COLOR);
        
        // Add components
        add(btnBack, BorderLayout.WEST);
        add(lblTitle, BorderLayout.CENTER);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBack) {
            navigationListener.navigateTo(MainFrame.DASHBOARD_SCREEN);
        }
    }
}
