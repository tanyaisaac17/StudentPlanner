package com.studyplanner.gui;

import javax.swing.*;
import java.awt.*;

public class SideNavigationPanel extends JPanel {
    private JButton btnDashboard;
    private JButton btnSubjects;
    private JButton btnTasks;
    private JButton btnStudySession;
    private JButton btnSummary;
    private JButton btnExit;
    
    private static final Color NAV_BG = new Color(15, 14, 46);
    private static final Color NAV_BUTTON_BG = new Color(26, 15, 58);
    private static final Color ACTIVE_BTN = new Color(168, 85, 247);
    private static final Color TEXT_COLOR = new Color(226, 232, 240);
    
    private NavigationCallback callback;
    
    public interface NavigationCallback {
        void navigateTo(String section);
    }
    
    public SideNavigationPanel(NavigationCallback callback) {
        this.callback = callback;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(NAV_BG);
        setPreferredSize(new Dimension(220, 700));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(56, 189, 248)));
        
        add(Box.createVerticalStrut(20));
        
        JLabel titleLabel = new JLabel("STUDY PLANNER");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);
        
        add(Box.createVerticalStrut(30));
        
        btnDashboard = createNavButton("Dashboard", "DASHBOARD");
        btnSubjects = createNavButton("Subjects", "SUBJECTS");
        btnTasks = createNavButton("Tasks", "TASKS");
        btnStudySession = createNavButton("Study Session", "STUDY_SESSION");
        btnSummary = createNavButton("Summary", "SUMMARY");
        
        add(btnDashboard);
        add(Box.createVerticalStrut(10));
        add(btnSubjects);
        add(Box.createVerticalStrut(10));
        add(btnTasks);
        add(Box.createVerticalStrut(10));
        add(btnStudySession);
        add(Box.createVerticalStrut(10));
        add(btnSummary);
        
        add(Box.createVerticalGlue());
        
        btnExit = createNavButton("Exit", "EXIT");
        add(btnExit);
        add(Box.createVerticalStrut(20));
    }
    
    private JButton createNavButton(String text, String section) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(190, 45));
        btn.setPreferredSize(new Dimension(190, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(NAV_BUTTON_BG);
        btn.setForeground(TEXT_COLOR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setIcon(null);
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setVerticalAlignment(SwingConstants.CENTER);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addActionListener(e -> {
            if ("EXIT".equals(section)) {
                System.exit(0);
            } else {
                setActiveButton(btn);
                callback.navigateTo(section);
            }
        });
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(ACTIVE_BTN);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!btn.getBackground().equals(ACTIVE_BTN)) {
                    btn.setBackground(NAV_BUTTON_BG);
                }
            }
        });
        
        return btn;
    }
    
    private void setActiveButton(JButton activeBtn) {
        btnDashboard.setBackground(NAV_BUTTON_BG);
        btnSubjects.setBackground(NAV_BUTTON_BG);
        btnTasks.setBackground(NAV_BUTTON_BG);
        btnStudySession.setBackground(NAV_BUTTON_BG);
        btnSummary.setBackground(NAV_BUTTON_BG);
        
        activeBtn.setBackground(ACTIVE_BTN);
    }
}
